<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Order.class.php';

if(!isset($input_data->id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

$_order = new Order();
$_order->sell_user = $_user;
$order_count = $_order->getNoCompleteCountBySell();

$out_data = new stdClass();
$out_data->count = $order_count;

out_data($out_data);

?>