<?php
require_once '../common/DbConnection.class.php';
require_once '../model/User.class.php';

class Wallet{
	public $id = 0;
	public $user = NULL;
	public $balance = 0;

	public function initByUser(){
		$db = DbConnection::getInstance();

		$sql = "select id,balance from tb_user_wallet where user_id = ?";
		$para_array = array($this->user->id);
		$rs = $db->query($sql,$para_array);
		if (count($rs) > 0){
			$this->id = $rs[0]['id'];
			$this->balance = floatval($rs[0]['balance']);
		}else{
			$this->id = 0;
		}
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_user_wallet(user_id,balance) values(?,?)";
		$para_array = array($this->user->id,0);
		
		$db->exec($sql, $para_array);
	}
	
	public function inByUser(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_user_wallet set balance = balance + ? where user_id = ?";
		$para_array = array($this->balance,$this->user->id);
	
		$db->exec($sql, $para_array);
	}
	
	public function outByUser(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_user_wallet set balance = balance - ? where user_id = ?";
		$para_array = array($this->balance,$this->user->id);
	
		$db->exec($sql, $para_array);
	}
}
?>