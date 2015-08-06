<?php
require_once '../common/DbConnection.class.php';
require_once '../model/User.class.php';

class Bank{
	public $id = 0;
	public $user = NULL;
	public $bank_name = '';
	public $bank_address = '';
	public $bank_no = '';
	public $bank_user = '';
	
	public function init(){
		$db = DbConnection::getInstance();

		$sql = "select user_id,bank_name,bank_address,bank_no,bank_user from tb_user_bank where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql);
		if(count($rs) > 0){
			$_user = new User();
			$_user->id = intval($rs[0]['user_id']);
			$_user->init();
			$this->user = $_user;
			
			$this->bank_name = $rs[0]['bank_name'];
			$this->bank_address = $rs[0]['bank_address'];
			$this->bank_no = $rs[0]['bank_no'];
			$this->bank_user = $rs[0]['bank_user'];
		}else{
			$this->id = 0;
			$this->user = NULL;
		}
	}
	
	/**
	 * 获取列表
	 */
	public function getListByUser(){
		$db = DbConnection::getInstance();
	
		$bank_list = array();
	
		$sql = "select id,bank_name,bank_address,bank_no,bank_user from tb_user_bank where user_id = ? order by id desc";
		$para_array = array($this->user->id);
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_bank = new Bank();
			$_bank->id = intval($row['id']);

			$_bank->bank_name = $row['bank_name'];
			$_bank->bank_address = $row['bank_address'];
			$_bank->bank_no = $row['bank_no'];
			$_bank->bank_user = $row['bank_user'];
				
			$bank_list[] = $_bank;
		}
	
		return $bank_list;
	}
	
	/**
	 * 查询相同卡号数量
	 */
	public function getCountByNo(){
		$db = DbConnection::getInstance();
	
		$out_list = array();
	
		$sql = "select count(0) c from tb_user_bank where user_id = ? and bank_no = ?";
		$para_array = array($this->user->id,$this->bank_no);
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_user_bank(user_id,bank_name,bank_address,bank_no,bank_user) values(?,?,?,?,?)";
		$para_array = array($this->user->id,$this->bank_name,$this->bank_address,$this->bank_no,$this->bank_user);
		
		$db->exec($sql, $para_array);
	}
	
	public function update(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_user_bank set bank_name = ?,bank_address = ?,bank_no = ?,bank_user = ? where id = ?";
		$para_array = array($this->bank_name,$this->bank_address,$this->bank_no,$this->bank_user,$this->id);
		
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
	
		$sql = "delete from tb_user_bank where id = ?";
		$para_array = array($this->id);
	
		$db->exec($sql, $para_array);
	}
}
?>