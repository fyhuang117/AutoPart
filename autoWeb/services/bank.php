<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Bank.class.php';

if(!isset($input_data->id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

$_bank = new Bank();
$_bank->user = $_user;
$bank_list = $_bank->getListByUser();

$out_data = new stdClass();
$out_data->lis = array();
foreach ($bank_list as $_bank_){
	$obj = new stdClass();

	$obj->name = $_bank_->bank_name;
	$obj->address = $_bank_->bank_address;
	$obj->no = $_bank_->bank_no;
	$obj->user = $_bank_->bank_user;

	$out_data->lis[] = $obj;
}

out_data($out_data);
?>