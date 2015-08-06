<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Inquiry.class.php';

if(!isset($input_data->id) || !isset($input_data->inquiryid)){
	send_error(400, '消息格式或参数不正确');
}

$_inquiry = new Inquiry();
$_inquiry->id = intval($input_data->inquiryid);
$_inquiry->init();

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_inquiry->id == 0 || $_user->id == 0 || $_user->id != $_inquiry->user->id || $_inquiry->status != INQUIRY_STATUS_NORMAL){
	send_error(404, '资源不存在');
}

$_inquiry->del();

?>