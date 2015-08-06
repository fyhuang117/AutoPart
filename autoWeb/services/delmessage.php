<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/UserMessage.class.php';

if(!isset($input_data->id) || !isset($input_data->message_id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_message = new UserMessage();
$_message->id = intval($input_data->message_id);
$_message->init();

if($_user->id == 0 || $_message->id == 0 || $_message->user->id != $_user->id){
	send_error(404, '用户不存在');
}

$_message->del();
?>