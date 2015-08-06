<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Out.class.php';
require_once '../model/Wallet.class.php';

if(!isset($input_data->id) || !isset($input_data->bank_name) || !isset($input_data->bank_address) 
	|| !isset($input_data->bank_no) || !isset($input_data->bank_user) || !isset($input_data->price)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0 || floatval($input_data->price) == 0){
	send_error(404, '提现数额大于余额');
}

$_wallet = new Wallet();
$_wallet->user = $_user;
$_wallet->initByUser();

if(floatval($input_data->price) > $_wallet->balance){
	send_error(404, '提现数额大于余额');
}

$_out = new Out();
$_out->user = $_user;
$_out->bank_name = $input_data->bank_name;
$_out->bank_address = $input_data->bank_address;
$_out->bank_no = $input_data->bank_no;
$_out->bank_user = $input_data->bank_user;
$_out->price = floatval($input_data->price);
$_out->save();
?>