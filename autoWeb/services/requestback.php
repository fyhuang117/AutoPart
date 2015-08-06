<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Order.class.php';
require_once '../model/Back.class.php';

if(!isset($input_data->id) || !isset($input_data->order_id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_order = new Order();
$_order->id = intval($input_data->orderid);
$_order->init();

if($_user->id == 0 || $_order->id == 0 || $_order->buy_user->id != $_user->id){
	send_error(404, '资源找不到');
}

$_back = new Back();
$_back->order = $_order;
$_back->save();
?>