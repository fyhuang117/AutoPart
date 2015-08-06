<?php
require_once '../common/DbConnection.class.php';
require_once '../model/User.class.php';

class Out{
	public $id = 0;
	public $user = NULL;
	public $bank_name = '';
	public $bank_address = '';
	public $bank_no = '';
	public $bank_user = '';
	public $price = 0;
	public $request_time = '';
	public $status = 0;
	
	/**
	 * 获取列表
	 */
	public function init(){
		$db = DbConnection::getInstance();
	
		$sql = "select user_id,bank_name,bank_address,bank_no,bank_user,price,request_time,status from tb_out where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if(count($rs) > 0){

			$_user = new User();
			$_user->id = intval($rs[0]['user_id']);
			$_user->init();
			$this->user = $_user;
	
			$this->bank_name = $rs[0]['bank_name'];
			$this->bank_address = $rs[0]['bank_address'];
			$this->bank_no = $rs[0]['bank_no'];
			$this->bank_user = $rs[0]['bank_user'];
			$this->price = floatval($rs[0]['price']);
			$this->request_time = $rs[0]['request_time'];
			$this->status = intval($rs[0]['status']);
		}else{
			$this->id = 0;
			$this->user = new User();
		} 
	}
	
	/**
	 * 获取列表
	 */
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$out_list = array();
	
		$sql = "select id,user_id,bank_name,bank_address,bank_no,bank_user,price,request_time,status from tb_out order by id desc limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_out = new Out();
			$_out->id = intval($row['id']);
	
			$_user = new User();
			$_user->id = intval($row['user_id']);
			$_user->init();
			$_out->user = $_user;
	
			$_out->bank_name = $row['bank_name'];
			$_out->bank_address = $row['bank_address'];
			$_out->bank_no = $row['bank_no'];
			$_out->bank_user = $row['bank_user'];
			$_out->price = floatval($row['price']);
			$_out->request_time = $row['request_time'];
			$_out->status = intval($row['status']);
				
			$out_list[] = $_out;
		}
	
		return $out_list;
	}
	
	/**
	 * 获取列表
	 */
	public function getListByPageAndKey($start,$limit){
		$db = DbConnection::getInstance();
	
		$out_list = array();
	
		$sql = "select id,user_id,bank_name,bank_address,bank_no,bank_user,price,request_time,status from tb_out where bank_no like ? order by id desc limit $start,$limit";
		$para_array = array('%'.$this->bank_no.'%');
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_out = new Out();
			$_out->id = intval($row['id']);
	
			$_user = new User();
			$_user->id = intval($row['user_id']);
			$_user->init();
			$_out->user = $_user;
	
			$_out->bank_name = $row['bank_name'];
			$_out->bank_address = $row['bank_address'];
			$_out->bank_no = $row['bank_no'];
			$_out->bank_user = $row['bank_user'];
			$_out->price = floatval($row['price']);
			$_out->request_time = $row['request_time'];
			$_out->status = intval($row['status']);
	
			$out_list[] = $_out;
		}
	
		return $out_list;
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_out";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByKey(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_out where bank_no like ?";
		$para_array = array('%'.$this->bank_no.'%');
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_out(user_id,bank_name,bank_address,bank_no,bank_user,price,request_time,status) values(?,?,?,?,?,?,now(),?)";
		$para_array = array($this->user->id,$this->bank_name,$this->bank_address,$this->bank_no,$this->bank_user,$this->price,0);
		
		$db->exec($sql, $para_array);
	}
	
	public function success(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_out set status = ? where id = ?";
		$para_array = array(1,$this->id);
		
		$db->exec($sql, $para_array);
	}
}
?>