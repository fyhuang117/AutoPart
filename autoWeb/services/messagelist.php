<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/UserMessage.class.php';

if(!isset($input_data->id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

$_message = new UserMessage();
$_message->user = $_user;
$message_list = $_message->getListByUser();

$out_data = new stdClass();
$out_data->lis = array();
foreach ($message_list as $_message_){
	$obj = new stdClass();

	$obj->message_id = $_message_->id;
	$obj->title = $_message_->title;
	$obj->content = $_message_->content;
	$obj->time = $_message_->send_time;
	$obj->read_status = $_message_->read_status;

	$_message_->read();
	
	$out_data->lis[] = $obj;
}

out_data($out_data);
?>