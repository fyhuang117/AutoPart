<?php
require_once '../common/DbConnection.class.php';
require_once '../model/User.class.php';

class Share{
	public $id = 0;
	public $share_user = NULL;
	public $user = NULL;
	
	/**
	 * 获取列表
	 */
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$share_list = array();
	
		$sql = "select s.share_user_id,count(0) c from tb_share s inner join tb_user u on s.user_id = u.id where u.enb = 1 group by s.share_user_id order by c desc limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_obj = new stdClass();
			
			$_user = new User();
			$_user->id = intval($row['share_user_id']);
			$_user->init();
			$_obj->share_user = $_user;
	
			$_obj->share_count = intval($row['c']);
	
			$share_list[] = $_obj;
		}
	
		return $share_list;
	}
	
	public function getListByShare(){
		$db = DbConnection::getInstance();
	
		$share_list = array();
	
		$sql = "select s.user_id from tb_share s inner join tb_user u on s.user_id = u.id where s.share_user_id = ? and u.enb = 1 order by s.id desc";
		$para_array = array($this->share_user->id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_share = new Share();
			
			$_user = new User();
			$_user->id = intval($row['user_id']);
			$_user->init();
			$_share->user = $_user;

			$share_list[] = $_share;
		}
	
		return $share_list;
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_share s inner join tb_user u on s.user_id = u.id where u.enb = 1 group by s.share_user_id";
		$rs = $db->query($sql);
		if(count($rs) > 0){
			return intval($rs[0]['c']);
		}else{
			return 0;
		}
	}
	
	/**
	 * 查询分享人
	 */
	public function getShare(){
		$db = DbConnection::getInstance();

		$sql = "select share_user_id from tb_share where user_id = ?";
		$para_array = array($this->user->id);
		$rs = $db->query($sql,$para_array);
		if(count($rs) > 0){
			$_user = new User();
			$_user->id = intval($rs[0]['share_user_id']);
			$_user->init();
			
			return $_user;
		}else{
			return new User();
		}
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_share(share_user_id,user_id) values(?,?)";
		$para_array = array($this->share_user->id,$this->user->id);
		$db->exec($sql, $para_array);
	}
}
?>