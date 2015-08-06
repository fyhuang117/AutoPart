<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Bill.class.php';

if(!isset($input_data->id) || !isset($input_data->tit) || !isset($input_data->dat)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

$_bill = new Bill();
$_bill->user = $_user;
$_bill->tit = $input_data->tit;
$_bill->dat = $input_data->dat;

$_bill->save();

?>