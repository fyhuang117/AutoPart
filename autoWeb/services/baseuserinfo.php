<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';

if(!isset($input_data->id) || !isset($input_data->nam) || !isset($input_data->adr) || !isset($input_data->ton) || !isset($input_data->lat)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

if($input_data->nam != ''){
	$_user->nam = $input_data->nam;
}
if($input_data->adr != ''){
	$_user->adr = $input_data->adr;
}
if($input_data->ton != ''){
	$_user->ton = $input_data->ton;
}
if($input_data->lat != ''){
	$_user->lat = $input_data->lat;
}


$_user->updateBase();
?>