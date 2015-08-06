<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Bidding.class.php';
require_once '../model/PartBand.class.php';

if(!isset($input_data->id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '资源不存在');
}

$parttype_name = '';
if(isset($input_data->parttype_name) && $input_data->parttype_name != ''){
	$parttype_name = $input_data->parttype_name;
}

$_bidding = new Bidding();
$_bidding->user = $_user;
$bidding_list = $_bidding->getListByUser($parttype_name);

$out_data = new stdClass();
$out_data->lis = array();

foreach ($bidding_list as $_bidding_){
	$obj = new stdClass();
	$obj->biddingid = $_bidding_->id;
	$obj->nam = $_bidding_->user->nam;
	$obj->business_area = $_bidding_->user->business_area->name;
	$obj->distance = get_distance($_bidding_->user->business_area->lat, $_bidding_->user->business_area->lng, $_bidding_->inquiry->user->business_area->lat, $_bidding_->inquiry->user->business_area->lng);
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
	
	$obj->par = $_bidding_->inquiry->parttype->nam;
	
	if($_bidding_->inquiry->car->id > 0){
		$obj->car = $_bidding_->inquiry->car->nam;
	}else{
		$obj->car = NO_CAR_MESSAGE;
	}
	
	$obj->begin_time = $_bidding_->inquiry->create_time;
	$obj->end_time = $_bidding_->inquiry->end_time;
	
	$out_data->lis[] = $obj;
}

out_data($out_data);


?>