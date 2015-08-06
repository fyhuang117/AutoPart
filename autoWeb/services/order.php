<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Order.class.php';

if(!isset($input_data->id) || !isset($input_data->orderid)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_order = new Order();
$_order->id = intval($input_data->orderid);
$_order->init();

if($_user->id == 0 || $_order->id == 0 || ($_user->id != $_order->buy_user->id && $_user->id != $_order->sell_user->id)){
	send_error(404, '资源不存在');
}

$out_data = new stdClass();
$out_data->num = $_order->num;
$out_data->sta = $_order->status;
$out_data->sell_id = $_order->sell_user->id;
$out_data->sel = $_order->sel;
$out_data->sell_tel = $_order->sell_user->tel;
$out_data->sell_address = $_order->sell_user->adr;
if($_order->sell_user->pic != ''){
	$out_data->sell_pic = PIC_URL.$_order->sell_user->pic;
}else{
	$out_data->sell_pic = '';
}

$out_data->buy = $_order->buy; 
$out_data->pri = $_order->pri;
$out_data->par = $_order->par;
$out_data->ban = $_order->ban;
$out_data->car = $_order->car;
$out_data->c = $_order->c;
$out_data->avi = $_order->avi;
$out_data->qau = $_order->qau;
$out_data->pay = $_order->pay;
$out_data->del = $_order->del;
$out_data->adr = $_order->adr;
$out_data->pof = $_order->pof;
$out_data->bil = $_order->bil;

if($_order->pic1 != ''){
	$out_data->pic1 = ORDER_PIC1_URL.$_order->pic1;
}else{
	$out_data->pic1 = '';
}

if($_order->pic2 != ''){
	$out_data->pic2 = ORDER_PIC2_URL.$_order->pic2;
}else{
	$out_data->pic2 = '';
}

if($_order->pic3 != ''){
	$out_data->pic3 = ORDER_PIC3_URL.$_order->pic3;
}else{
	$out_data->pic3 = '';
}

$out_data->comments = $_order->comments;
$out_data->begin_time = $_order->create_time;
$out_data->update_time = $_order->update_time;

out_data($out_data);
?>