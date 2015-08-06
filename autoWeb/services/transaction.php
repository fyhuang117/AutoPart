<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Transaction.class.php';

if(!isset($input_data->id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

$_tran = new Transaction();
$_tran->user = $_user;
$tran_list = $_tran->getListByUser();

$out_data = new stdClass();
$out_data->lis = array();
foreach ($tran_list as $_tran_){
	$obj = new stdClass();

	$obj->type = $_tran_->type;
	$obj->price = $_tran_->price;
	$obj->time = $_tran_->transaction_time;

	$out_data->lis[] = $obj;
}

out_data($out_data);
?>