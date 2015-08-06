<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';

if(!isset($input_data->id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

$out_data = new stdClass();
$out_data->nam = $_user->nam;
$out_data->adr = $_user->adr;
$out_data->ton = $_user->ton;
$out_data->lat = $_user->lat;

if($_user->lp1 != ''){
	$out_data->lp1 = LP1_URL.$_user->lp1;
}else{
	$out_data->lp1 = '';
}

if($_user->lp2 != ''){
	$out_data->lp2 = LP2_URL.$_user->lp2;
}else{
	$out_data->lp2 = '';
}

if($_user->pic != ''){
	$out_data->pic = PIC_URL.$_user->pic;
}else{
	$out_data->pic = '';
}

if($_user->license != ''){
	$out_data->license = LICENSE_URL.$_user->license;
}else{
	$out_data->license = '';
}

if($_user->agent != ''){
	$out_data->agent = AGENT_URL.$_user->agent;
}else{
	$out_data->agent = '';
}

$out_data->enb = $_user->enb;

$out_data->sell_level = $_user->sell_level;
$out_data->sell_score = array(
		$_user->order_score_count>0?round($_user->sell_match/$_user->order_score_count,1):0,
		$_user->order_score_count>0?round($_user->sell_service/$_user->order_score_count,1):0,
		$_user->order_score_count>0?round($_user->sell_speed/$_user->order_score_count,1):0
);

$out_data->share_id = bin2hex(encrypt($input_data->id, 'erahsshare'));

out_data($out_data);


?>