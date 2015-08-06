<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Inquiry.class.php';
require_once '../model/Bidding.class.php';
require_once '../model/PartBand.class.php';

if(!isset($input_data->id) || !isset($input_data->inquiryid)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_inquiry = new Inquiry();
$_inquiry->id = intval($input_data->inquiryid);
$_inquiry->init();

if($_user->id == 0 || $_inquiry->id == 0 || $_user->id != $_inquiry->user->id){
	send_error(404, '资源不存在');
}

$avi = 2;
$business_area = 0;

if(isset($input_data->avi)){
	$avi = intval($input_data->avi);
}
if(isset($input_data->business_area)){
	$business_area = intval($input_data->business_area);
}

$sort_type = SORT_TYPE_DEFAULT;
if(isset($input_data->sort_type)){
	$sort_type = intval($input_data->sort_type);
}

$_bidding = new Bidding();
$_bidding->inquiry = $_inquiry;
$bidding_list = $_bidding->getListByInquiry($avi,$business_area,$sort_type);

$out_data = new stdClass();
$out_data->lis = array();

foreach ($bidding_list as $_bidding_){
	$obj = new stdClass();
	$obj->biddingid = $_bidding_->id;
	$obj->nam = $_bidding_->user->nam;
	if($_bidding_->user->pic != ''){
		$obj->sell_pic = PIC_URL.$_bidding_->user->pic;
	}else{
		$obj->sell_pic = '';
	}
	$obj->business_area = $_bidding_->user->business_area->name;
	$obj->sell_level = $_bidding_->user->sell_level;
	$obj->sell_score = array(
			$_bidding_->user->order_score_count > 0?round($_bidding_->user->sell_match/$_bidding_->user->order_score_count,1):0,
			$_bidding_->user->order_score_count > 0?round($_bidding_->user->sell_service/$_bidding_->user->order_score_count,1):0,
			$_bidding_->user->order_score_count > 0?round($_bidding_->user->sell_speed/$_bidding_->user->order_score_count,1):0
	);
	$obj->pri = $_bidding_->pri;
	$obj->avi = $_bidding_->avi;
	$obj->qau = $_bidding_->qau;
	$obj->pay = $_bidding_->pay;
	$obj->del = $_bidding_->del;
	$obj->pof = $_bidding_->pof;
	
	$band_array = array();
	$band_id_array = explode(',', $_bidding_->partband_id);
	foreach ($band_id_array as $band_id){
		if($band_id != NULL && $band_id != ''){
			$_partBand = new PartBand();
			$_partBand->id = $band_id;
			$_partBand->init();
			
			$band_array[] = $_partBand->nam;
		}
	}
	$obj->ban = $band_array;
	
	if($_bidding_->pic1 != ''){
		$obj->pic1 = BIDDING_PIC1_URL.$_bidding_->pic1;
	}else{
		$obj->pic1 = '';
	}
	
	if($_bidding_->pic2 != ''){
		$obj->pic2 = BIDDING_PIC2_URL.$_bidding_->pic2;
	}else{
		$obj->pic2 = '';
	}
	
	if($_bidding_->pic3 != ''){
		$obj->pic3 = BIDDING_PIC3_URL.$_bidding_->pic3;
	}else{
		$obj->pic3 = '';
	}
	
	
	$out_data->lis[] = $obj;
}

out_data($out_data);


?>