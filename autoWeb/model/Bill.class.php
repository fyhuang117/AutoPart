<?php
require_once '../common/DbConnection.class.php';
require_once '../model/User.class.php';

class Bill{
	public $id = 0;
	public $user = NULL;
	public $tit = '';
	public $dat = '';
	
	public function init(){
		$db = DbConnection::getInstance();

		$sql = "select user_id,tit,dat from tb_bill where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if (count($rs) > 0){
			
			$_user = new User();
			$_user->id = intval($rs[0]['user_id']);
			$_user->init();
			$this->user = $_user;
			
			$this->tit = $rs[0]['tit'];
			$this->dat = $rs[0]['dat'];
		}else{
			$this->id = 0;
			$this->user = new User();
		}
	}
	
	/**
	 * 根据用户获取列表
	 * @return multitype:Bill
	 */
	public function getListByUser(){
		$db = DbConnection::getInstance();
	
		$bill_list = array();
	
		$sql = "select id,tit,dat from tb_bill where user_id = ? order by id desc";
		$para_array = array($this->user->id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_bill = new Bill();
				
			$_bill->id = intval($row['id']);
			$_bill->user = $this->user;
			$_bill->tit = $row['tit'];
			$_bill->dat = $row['dat'];
	
			$bill_list[] = $_bill;
		}
	
		return $bill_list;
	}

	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_bill(user_id,tit,dat) values(?,?,?)";
		$para_array = array($this->user->id,$this->tit,$this->dat);
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
	
		$sql = "delete from tb_bill where id = ?";
		$para_array = array($this->id);
		$db->exec($sql, $para_array);
	}
}
?>