<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Inquiry.class.php';
require_once '../model/Bidding.class.php';

if(!isset($input_data->id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

$_inquiry = new Inquiry();
$_inquiry->user = $_user;
$inquiry_list = $_inquiry->getListByUser();
$inquiry_count = count($inquiry_list);
$bidding_count = 0;


foreach ($inquiry_list as $_inquiry_){
	$_bidding = new Bidding();
	$_bidding->inquiry = $_inquiry_;
	$bidding_count += $_bidding->getNoReadCountByInquiry();
}

$out_data = new stdClass();
$out_data->c = $inquiry_count;
$out_data->nbc = $bidding_count;

out_data($out_data);


?>