<?php
require_once '../common/DbConnection.class.php';
require_once '../model/User.class.php';
require_once '../model/Coupon.class.php';

class UserCoupon{
	public $id = 0;
	public $user = NULL;
	public $coupon = NULL;
	public $get_time = '';
	public $use_status = 0;
	
	public function init(){
		$db = DbConnection::getInstance();

		$sql = "select user_id,coupon_id,get_time,use_status from tb_user_coupon where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if(count($rs) > 0){
			$_user = new User();
			$_user->id = intval($rs[0]['user_id']);
			$_user->init();
			$this->user = $_user;
			
			$_coupon = new Coupon();
			$_coupon->id = intval($rs[0]['coupon_id']);
			$_coupon->init();
			$this->coupon = $_coupon;
				
			$this->get_time = $rs[0]['get_time'];
			$this->use_status = intval($rs[0]['use_status']);
		}else{
			$this->id = 0;
			$this->user = new User();
			$this->coupon = new Coupon();
		}
	}
	
	/**
	 * 查询用户优惠券信息
	 */
	public function getListByUser(){
		$db = DbConnection::getInstance();
	
		$coupon_list = array();
	
		$sql = "select id,coupon_id,get_time,use_status from tb_user_coupon where user_id = ? order by id desc";
		$para_array = array($this->user->id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_user_coupon = new UserCoupon();
			$_user_coupon->id = intval($row['id']);
	
			$_coupon = new Coupon();
			$_coupon->id = intval($row['coupon_id']);
			$_coupon->init();
			$_user_coupon->coupon = $_coupon;
			
			$_user_coupon->get_time = $row['get_time'];
			$_user_coupon->use_status = intval($row['use_status']);
				
			$coupon_list[] = $_user_coupon;
		}
	
		return $coupon_list;
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_user_coupon(user_id,coupon_id,get_time,use_status) values(?,?,now(),?)";
		$para_array = array($this->user->id,$this->coupon->id,0);
		$db->exec($sql, $para_array);
	}
	
	public function useCoupon(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_user_coupon set use_status = ? where id = ?";
		$para_array = array(1,$this->id);
		$db->exec($sql, $para_array);
	}
	
	public function backCoupon(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_user_coupon set use_status = ? where id = ?";
		$para_array = array(0,$this->id);
		$db->exec($sql, $para_array);
	}
}
?>