<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Bank.class.php';

if(!isset($input_data->id) || !isset($input_data->bank_name) || !isset($input_data->bank_address) 
	|| !isset($input_data->bank_no) || !isset($input_data->bank_user) || !isset($input_data->bank_id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_bank = new Bank();
$_bank->id = intval($input_data->bank_id);
$_bank->init();

if($_user->id == 0 || $_bank->id == 0 || $_bank->user->id != $_user->id){
	send_error(404, '用户不存在');
}

$_bank->bank_name = $input_data->bank_name;
$_bank->bank_address = $input_data->bank_address;
$_bank->bank_no = $input_data->bank_no;
$_bank->bank_user = $input_data->bank_user;
$_bank->update();
?>