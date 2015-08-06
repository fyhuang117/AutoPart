<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../jpush/JPush.class.php';
require_once '../model/User.class.php';
require_once '../model/Inquiry.class.php';
require_once '../model/Bidding.class.php';
require_once '../model/PartBand.class.php';

if(!isset($input_data->id) || !isset($input_data->inquiryid) || !isset($input_data->pri) || !isset($input_data->partbandid) || !isset($input_data->c) 
	|| !isset($input_data->avi) || !isset($input_data->qau) || !isset($input_data->pay) || !isset($input_data->del) || !isset($input_data->pof) 
	|| !isset($input_data->pic1) || !isset($input_data->pic2) || !isset($input_data->pic3) || !isset($input_data->mes) || !isset($input_data->aum) || !isset($input_data->parttype_quality)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_inquiry = new Inquiry();
$_inquiry->id = intval($input_data->inquiryid);
$_inquiry->init();

if($_user->id == 0 || $_inquiry->id == 0){
	send_error(404, '资源不存在');
}

$band_id_array = explode(',', $input_data->partbandid);
foreach ($band_id_array as $band_id){
	if($band_id != NULL && $band_id != ''){
		$_partBand = new PartBand();
		$_partBand->id = $band_id;
		$_partBand->init();
			
		if($_partBand->id == 0){
			send_error(404, '资源不存在');
		}
	}
}

$_bidding = new Bidding();
$_bidding->user = $_user;
$_bidding->inquiry = $_inquiry;
$_bidding->pri = $input_data->pri;
$_bidding->partband_id = $input_data->partbandid;
$_bidding->parttype_quality = intval($input_data->parttype_quality);
$_bidding->c = intval($input_data->c);
$_bidding->avi = intval($input_data->avi);
$_bidding->qau = $input_data->qau;
$_bidding->pay = intval($input_data->pay);
$_bidding->del = $input_data->del;
$_bidding->pof = intval($input_data->pof);

if($input_data->pic1 != NULL && $input_data->pic1 != ''){
	$_bidding->pic1 = save_file(_hex2bin($input_data->pic1), BIDDING_PIC1_PATH, 'png');
}else{
	$_bidding->pic1 = '';
}

if($input_data->pic2 != NULL && $input_data->pic2 != ''){
	$_bidding->pic2 = save_file(_hex2bin($input_data->pic2), BIDDING_PIC2_PATH, 'png');
}else{
	$_bidding->pic2 = '';
}

if($input_data->pic3 != NULL && $input_data->pic3 != ''){
	$_bidding->pic3 = save_file(_hex2bin($input_data->pic3), BIDDING_PIC3_PATH, 'png');
}else{
	$_bidding->pic3 = '';
}

$_bidding->mes = $input_data->mes;

if($input_data->aum != NULL && $input_data->aum != ''){
	$_bidding->aum = save_file(_hex2bin($input_data->aum), BIDDING_AUM_PATH, '');
}else{
	$_bidding->aum = '';
}

$_bidding->save();


//推送消息
$_jpush = new JPush();
$_jpush->buy_push($_bidding->inquiry->user->jpush_alias, '有经销商给您报价,请登录APP查看');

//send_sms($_inquiry->user->tel, '有经销商给您报价,请登录APP查看 [0了汽配]');

$out_data = new stdClass();
$out_data->biddingid = $_bidding->id;
out_data($out_data);

?>