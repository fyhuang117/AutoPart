<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Bill.class.php';

if(!isset($input_data->id) || !isset($input_data->billid)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

$_bill = new Bill();
$_bill->id = intval($input_data->billid);
$_bill->init();

if($_bill->user->id == $_user->id){
	$_bill->del();
}

?>