<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Bill.class.php';

if(!isset($input_data->id)){
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
$bill_list = $_bill->getListByUser();

$out_data = new stdClass();
$out_data->lis = array();

foreach ($bill_list as $_bill_){
	$obj = new stdClass();
	$obj->billid = $_bill_->id;
	$obj->tit = $_bill_->tit;
	$obj->dat = $_bill_->dat;
	
	$out_data->lis[] = $obj;
}

out_data($out_data);


?>