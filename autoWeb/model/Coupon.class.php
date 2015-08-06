<?php
require_once '../common/DbConnection.class.php';

class Coupon{
	public $id = 0;
	public $name = '';
	public $price = 0;
	public $need_min = 0;
	public $get_type = 0;
	public $get_date = '';
	public $get_count = 0;
	public $begin_date = '';
	public $end_date = '';
	public $status = 0;
	
	public function init(){
		$db = DbConnection::getInstance();
	
		$sql = "select name,price,need_min,get_type,get_date,get_count,begin_date,end_date,status from tb_coupon where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if(count($rs) > 0){
			$this->name = $rs[0]['name'];
			$this->price = intval($rs[0]['price']);
			$this->need_min = intval($rs[0]['need_min']);
			$this->get_type = intval($rs[0]['get_type']);
			$this->get_date = $rs[0]['get_date'];
			$this->get_count = intval($rs[0]['get_count']);
			$this->begin_date = $rs[0]['begin_date'];
			$this->end_date = $rs[0]['end_date'];
			$this->status = intval($rs[0]['status']);
		}else{
			$this->id = 0;
		}
	}
	
	/**
	 * 获取列表
	 */
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$coupon_list = array();
	
		$sql = "select id,name,price,need_min,get_type,get_date,get_count,begin_date,end_date,status from tb_coupon order by id desc limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_coupon = new Coupon();
			$_coupon->id = intval($row['id']);
	
			$_coupon->name = $row['name'];
			$_coupon->price = intval($row['price']);
			$_coupon->need_min = intval($row['need_min']);
			$_coupon->get_type = intval($row['get_type']);
			$_coupon->get_date = $row['get_date'];
			$_coupon->get_count = intval($row['get_count']);
			$_coupon->begin_date = $row['begin_date'];
			$_coupon->end_date = $row['end_date'];
			$_coupon->status = intval($row['status']);
			
			$coupon_list[] = $_coupon;
		}
	
		return $coupon_list;
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_coupon";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_coupon(name,price,need_min,get_type,get_date,get_count,begin_date,end_date,status) values(?,?,?,?,?,?,?,?,?)";
		$para_array = array($this->name,$this->price,$this->need_min,$this->get_type,$this->get_date,$this->get_count,$this->begin_date,$this->end_date,1);
		$db->exec($sql, $para_array);
	}
	
	public function update(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_coupon set name = ?,price = ?,need_min = ?,get_type = ?,get_date = ?,get_count = ?,begin_date = ?,end_date = ? where id = ?";
		$para_array = array($this->name,$this->price,$this->need_min,$this->get_type,$this->get_date,$this->get_count,$this->begin_date,$this->end_date,$this->id);
		$db->exec($sql, $para_array);
	}
	
	public function close(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_coupon set status = ? where id = ?";
		$para_array = array(0,$this->id);
		$db->exec($sql, $para_array);
	}
	
	public function open(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_coupon set status = ? where id = ?";
		$para_array = array(1,$this->id);
		$db->exec($sql, $para_array);
	}
}
?> 