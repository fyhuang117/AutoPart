<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../jpush/JPush.class.php';
require_once '../model/User.class.php';
require_once '../model/Order.class.php';

if(!isset($input_data->id) || !isset($input_data->orderid)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_order = new Order();
$_order->id = intval($input_data->orderid);
$_order->init();

if($_user->id == 0 || $_order->id == 0 || $_user->id != $_order->sell_user->id || $_order->status != ORDER_STATUS_NOSEND){
	send_error(404, '资源不存在');
}

$_order->send();

//推送消息
$_jpush = new JPush();
$_jpush->buy_push($_order->buy_user->jpush_alias, '您的订单有卖家已发货,请等待收货,登录APP查看');
?>