<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Order.class.php';

if(!isset($input_data->id) || !isset($input_data->orderid) || !isset($input_data->pri) || !isset($input_data->c)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_order = new Order();
$_order->id = intval($input_data->orderid);
$_order->init();

if($_user->id == 0 || $_order->id == 0 || $_user->id != $_order->sell_user->id){
	send_error(404, '资源不存在');
}

if($input_data->pri != ''){
	$_order->pri = $input_data->pri;
}
if($input_data->c != ''){
	$_order->c = intval($input_data->c);
}

$_order->update();
?>