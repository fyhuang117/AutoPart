<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../jpush/JPush.class.php';
require_once '../model/User.class.php';
require_once '../model/Order.class.php';
require_once '../model/UserCoupon.class.php';

if(!isset($input_data->id) || !isset($input_data->orderid) || !isset($input_data->real_price) || !isset($input_data->user_coupon_id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_order = new Order();
$_order->id = intval($input_data->orderid);
$_order->init();

if($_user->id == 0 || $_order->id == 0 || $_user->id != $_order->buy_user->id || $_order->status != ORDER_STATUS_NOPAY){
	send_error(404, '资源不存在');
}

$_order->real_price = floatval($input_data->real_price);

$_user_coupon = new UserCoupon();
if(intval($input_data->user_coupon_id) > 0){
	//使用优惠券
	$_user_coupon->id = intval($input_data->user_coupon_id);
	$_user_coupon->init();
	
	if($_user_coupon->user->id != $_user->id){
		send_error(404, '资源不存在');
	}
}
$_order->user_coupon = $_user_coupon;

$_order->pay();

//推送消息
$_jpush = new JPush();
$_jpush->sell_push($_order->sell_user->jpush_alias, '您的订单有买家已付款,请尽快发货,登录APP查看');


//send_sms($_order->sell_user->tel, '您竞价的'.$_order->num.'订单已成交,请尽快发货.买家电话 '.$_order->buy_user->tel.' [0了汽配]');
?>