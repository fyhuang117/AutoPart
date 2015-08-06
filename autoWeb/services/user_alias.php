<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';

if(!isset($input_data->id) || !isset($input_data->alias)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

$_user->jpush_alias = $input_data->alias;
$_user->updateAlias();
?>