<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Order.class.php';

if(!isset($input_data->id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

$_order = new Order();
$_order->buy_user = $_user;
$order_list = $_order->getListByBuyUser();

$out_data = new stdClass();
$out_data->lis = array();

foreach ($order_list as $_order_){
	$obj = new stdClass();
	
	$obj->orderid = $_order_->id;
	$obj->num = $_order_->num;
	$obj->sta = $_order_->status;
	$obj->sel = $_order_->sel;
	$obj->buy = $_order_->buy;
	$obj->pri = $_order_->pri;
	$obj->par = $_order_->par;
	$obj->car = $_order_->car;
	$obj->c = $_order_->c;
	$obj->avi = $_order_->avi;
	$obj->qau = $_order_->qau;
	$obj->pay = $_order_->pay;
	$obj->del = $_order_->del;
	$obj->pof = $_order_->pof;
	if($_order_->pic1 != ''){
		$obj->pic1 = ORDER_PIC1_URL.$_order_->pic1;
	}else{
		$obj->pic1 = '';
	}
	
	if($_order_->pic2 != ''){
		$obj->pic2 = ORDER_PIC2_URL.$_order_->pic2;
	}else{
		$obj->pic2 = '';
	}
	
	if($_order_->pic3 != ''){
		$obj->pic3 = ORDER_PIC3_URL.$_order_->pic3;
	}else{
		$obj->pic3 = '';
	}
	
	$obj->update_time = $_order_->update_time;
	
	$out_data->lis[] = $obj;
}

out_data($out_data);
?>