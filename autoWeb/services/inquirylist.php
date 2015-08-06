<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Inquiry.class.php';
require_once '../model/Bidding.class.php';
require_once '../model/PartBand.class.php';

/*
if(!isset($input_data->id)){
	send_error(400, '消息格式或参数不正确');
}
*/

$car_id = 0;
$part_type = 0;
$part_band = 0;

if(isset($input_data->car_id)){
	$car_id = intval($input_data->car_id);
}

if(isset($input_data->part_type)){
	$part_type = intval($input_data->part_type);
}

if(isset($input_data->part_band)){
	$part_band = intval($input_data->part_band);
}

$_inquiry = new Inquiry();
$_inquiry->status = INQUIRY_STATUS_NORMAL;

if(isset($input_data->id)){
	$_user = new User();
	$_user->id = id_decode($input_data->id);
	$_user->init();

	if($_user->id == 0){
		send_error(404, '资源不存在');
	}
	
	$inquiry_list = $_inquiry->getListBySellAndPageAndStatus($_user->id,0,$_inquiry->getCount(),$car_id,$part_type,$part_band);
}else{
	$inquiry_list = $_inquiry->getListByPageAndStatus(0,$_inquiry->getCount(),$car_id,$part_type,$part_band);
}

$out_data = new stdClass();
$out_data->lis = array();

foreach ($inquiry_list as $_inquiry_){
	$obj = new stdClass();
	$obj->inquiryid = $_inquiry_->id;
	$obj->par = $_inquiry_->parttype->nam;
	
	if($_inquiry_->car->id > 0){
		$obj->car = $_inquiry_->car->nam;
	}else{
		$obj->car = NO_CAR_MESSAGE;
	}
	
	$obj->c = $_inquiry_->c;
	
	$band_array = array();
	$band_id_array = explode(',', $_inquiry_->partband_id);
	foreach ($band_id_array as $band_id){
		if($band_id != NULL && $band_id != ''){
			$_partBand = new PartBand();
			$_partBand->id = $band_id;
			$_partBand->init();
			
			$band_array[] = $_partBand->nam;
		}
	}
	$obj->ban = $band_array;
	
	if($_inquiry_->pic1 != ''){
		$obj->pic1 = INQUIRY_PIC1_URL.$_inquiry_->pic1;
	}else{
		$obj->pic1 = '';
	}
	
	if($_inquiry_->pic2 != ''){
		$obj->pic2 = INQUIRY_PIC2_URL.$_inquiry_->pic2;
	}else{
		$obj->pic2 = '';
	}
	
	if($_inquiry_->pic3 != ''){
		$obj->pic3 = INQUIRY_PIC3_URL.$_inquiry_->pic3;
	}else{
		$obj->pic3 = '';
	}
	
	$_bidding = new Bidding();
	$_bidding->inquiry = $_inquiry_;
	$obj->bc = $_bidding->getCountByInquiry();
	$obj->min = $_bidding->getMinPriByInquiry();
	
	$obj->begin_time = $_inquiry_->create_time;
	$obj->end_time = $_inquiry_->end_time;
	
	$obj->parttype_quality = $_inquiry_->parttype_quality;
	$obj->business_area = $_inquiry_->user->business_area->name;
	
	$out_data->lis[] = $obj;
}

out_data($out_data);


?>