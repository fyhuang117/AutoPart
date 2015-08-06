<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../jpush/JPush.class.php';
require_once '../model/User.class.php';
require_once '../model/Bidding.class.php';
require_once '../model/Order.class.php';
require_once '../model/PartBand.class.php';

if(!isset($input_data->id) || !isset($input_data->biddingid)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_bidding = new Bidding();
$_bidding->id = intval($input_data->biddingid);
$_bidding->init();

if($_user->id == 0 || $_bidding->id == 0 || $_user->id != $_bidding->inquiry->user->id){
	send_error(404, '资源不存在');
}

$_order = new Order();
$_order->sell_user = $_bidding->user;
$_order->sel = $_bidding->user->nam.','.$_bidding->user->tel.','.$_bidding->user->adr;
$_order->buy_user = $_bidding->inquiry->user;
$_order->buy = $_bidding->inquiry->user->nam.','.$_bidding->inquiry->user->tel.','.$_bidding->inquiry->user->adr;
$_order->pri = $_bidding->pri;
$_order->par = $_bidding->inquiry->parttype->nam;

$band_nam = '';
$band_id_array = explode(',', $_bidding->partband_id);
foreach ($band_id_array as $band_id){
	if($band_id != NULL && $band_id != ''){
		$_partBand = new PartBand();
		$_partBand->id = $band_id;
		$_partBand->init();
			
		if($_partBand->id > 0){
			$band_nam .= $_partBand->nam.',';
		}
	}
}
$band_nam = substr($band_nam, 0,strlen($band_nam) - 1);
$_order->ban = $band_nam;

if($_bidding->inquiry->car->id > 0){
	$_order->car = $_bidding->inquiry->car->nam;
}else{
	$_order->car = NO_CAR_MESSAGE;
}

$_order->c = $_bidding->c;
$_order->avi = $_bidding->avi;
$_order->qau = $_bidding->qau;
$_order->pay = $_bidding->pay;
$_order->del = $_bidding->del;
$_order->adr = $_bidding->user->adr;
$_order->pof = $_bidding->pof;
$_order->bil = '';

$_order->pic1 = $_bidding->pic1;
if($_bidding->pic1 != NULL && $_bidding->pic1 != ''){
	if(is_file(BIDDING_PIC1_PATH.$_bidding->pic1)){
		$path = explode('/', $_bidding->pic1);
		if(!is_dir(ORDER_PIC1_PATH.$path[0])){
			mkdir(ORDER_PIC1_PATH.$path[0]);
		}
		copy(BIDDING_PIC1_PATH.$_bidding->pic1, ORDER_PIC1_PATH.$_bidding->pic1);
	}
}

$_order->pic2 = $_bidding->pic2;
if($_bidding->pic2 != NULL && $_bidding->pic2 != ''){
	if(is_file(BIDDING_PIC2_PATH.$_bidding->pic2)){
		$path = explode('/', $_bidding->pic2);
		if(!is_dir(ORDER_PIC2_PATH.$path[0])){
			mkdir(ORDER_PIC2_PATH.$path[0]);
		}
		copy(BIDDING_PIC2_PATH.$_bidding->pic2, ORDER_PIC2_PATH.$_bidding->pic2);
	}
}

$_order->pic3 = $_bidding->pic3;
if($_bidding->pic3 != NULL && $_bidding->pic3 != ''){
	if(is_file(BIDDING_PIC3_PATH.$_bidding->pic3)){
		$path = explode('/', $_bidding->pic3);
		if(!is_dir(ORDER_PIC3_PATH.$path[0])){
			mkdir(ORDER_PIC3_PATH.$path[0]);
		}
		copy(BIDDING_PIC3_PATH.$_bidding->pic3, ORDER_PIC3_PATH.$_bidding->pic3);
	}
}

$_order->save();

$_bidding->order();

$bidding_list = $_bidding->getListByInquiry();

foreach ($bidding_list as $_bidding_){
	if($_bidding_->status == BIDDING_STATUS_NORMAL){
		$_bidding_->end();
	}
}

$_bidding->inquiry->complete();

//推送消息
$_jpush = new JPush();
$_jpush->sell_push($_bidding->user->jpush_alias, '您的竞价单已确认为订单,请登录APP查看');

send_sms($_bidding->user->tel, '您的竞价单已确认为订单,请登录APP查看');

$out_data = new stdClass();
$out_data->orderid = $_order->id;
out_data($out_data);
?>