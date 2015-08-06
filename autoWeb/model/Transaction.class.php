<?php
require_once '../common/DbConnection.class.php';
require_once '../model/User.class.php';

class Transaction{
	public $id = 0;
	public $user = NULL;
	public $type = 0;
	public $price = 0;
	public $transaction_time = '';
	
	/**
	 * 获取列表
	 */
	public function getListByUser(){
		$db = DbConnection::getInstance();
	
		$tran_list = array();
	
		$sql = "select id,type,price,transaction_time from tb_user_transaction where user_id = ? order by id desc limit 100";
		$para_array = array($this->user->id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_tran = new Transaction();
	
			$_tran->id = intval($row['id']);
			$_tran->price = floatval($row['price']);
			$_tran->type = intval($row['type']);
			$_tran->transaction_time = $row['transaction_time'];
	
			$tran_list[] = $_tran;
		}
	
		return $tran_list;
	}
	
	/**
	 * 获取收入列表
	 */
	public function getInListByUser(){
		$db = DbConnection::getInstance();
	
		$tran_list = array();
	
		$sql = "select id,type,price,transaction_time from tb_user_transaction where user_id = ? and type = ? order by id desc limit 100";
		$para_array = array($this->user->id,TRANSACTION_TYPE_IN);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_tran = new Transaction();
	
			$_tran->id = intval($row['id']);
			$_tran->price = floatval($row['price']);
			$_tran->type = intval($row['type']);
			$_tran->transaction_time = $row['transaction_time'];
	
			$tran_list[] = $_tran;
		}
	
		return $tran_list;
	}
	
	/**
	 * 获取提现列表
	 */
	public function getOutListByUser(){
		$db = DbConnection::getInstance();
	
		$tran_list = array();
	
		$sql = "select id,type,price,transaction_time from tb_user_transaction where user_id = ? and type = ? order by id desc limit 100";
		$para_array = array($this->user->id,TRANSACTION_TYPE_OUT);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_tran = new Transaction();
	
			$_tran->id = intval($row['id']);
			$_tran->price = floatval($row['price']);
			$_tran->type = intval($row['type']);
			$_tran->transaction_time = $row['transaction_time'];
	
			$tran_list[] = $_tran;
		}
	
		return $tran_list;
	}
	
	/**
	 * 累计收入
	 */
	public function getInByUser(){
		$db = DbConnection::getInstance();
	
		$sql = "select sum(price) price from tb_user_transaction where user_id = ? and type = ?";
		$para_array = array($this->user->id,TRANSACTION_TYPE_IN);
		$rs = $db->query($sql,$para_array);
		return floatval($rs[0]['price']);
	}
	
	/**
	 * 累计提现
	 */
	public function getOutByUser(){
		$db = DbConnection::getInstance();
	
		$sql = "select sum(price) price from tb_user_transaction where user_id = ? and type = ?";
		$para_array = array($this->user->id,TRANSACTION_TYPE_OUT);
		$rs = $db->query($sql,$para_array);
		return floatval($rs[0]['price']);
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_user_transaction(user_id,type,price,transaction_time) values(?,?,?,now())";
		$para_array = array($this->user->id,$this->type,$this->price);
		
		$db->exec($sql, $para_array);
	}
}
?>