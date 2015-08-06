<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Bank.class.php';

if(!isset($input_data->id) || !isset($input_data->bank_id)){
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

$_bank->del();
?>