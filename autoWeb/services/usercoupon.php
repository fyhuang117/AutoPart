<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/UserCoupon.class.php';

if(!isset($input_data->id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

$_user_coupon = new UserCoupon();
$_user_coupon->user = $_user;
$coupon_list = $_user_coupon->getListByUser();

$out_data = new stdClass();
$out_data->lis = array();
foreach ($coupon_list as $_coupon_){
	$obj = new stdClass();

	$obj->price = $_coupon_->coupon->price;
	$obj->need_min = $_coupon_->coupon->need_min;
	$obj->begin_date = $_coupon_->coupon->begin_date;
	$obj->end_date = $_coupon_->coupon->end_date;
	
	$now = date('Y-m-d');
	if($now >= $obj->begin_date && $now <= $obj->end_date){
		$obj->date_status = 1;
	}else{
		$obj->date_status = 0;
	}
	
	$obj->use_status = $_coupon_->use_status;

	$out_data->lis[] = $obj;
}

out_data($out_data);
?>