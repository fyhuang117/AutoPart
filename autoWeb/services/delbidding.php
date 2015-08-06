<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Bidding.class.php';

if(!isset($input_data->id) || !isset($input_data->biddingid)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_bidding = new Bidding();
$_bidding->id = intval($input_data->biddingid);
$_bidding->init();

if($_user->id == 0 || $_bidding->id == 0 || $_user->id != $_bidding->user->id || $_bidding->status != BIDDING_STATUS_NORMAL){
	send_error(404, '竞价单不存在');
}

$_bidding->del();
?>