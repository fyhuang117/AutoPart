<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Bidding.class.php';
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

if($_user->id == 0 || $_bidding->id == 0 || ($_user->id != $_bidding->inquiry->user->id && $_user->id != $_bidding->user->id)){
	send_error(404, '资源不存在');
}

$out_data = new stdClass();
$out_data->sell_id = $_bidding->user->id;
$out_data->nam = $_bidding->user->nam;
$out_data->sell_tel = $_bidding->user->tel;
$out_data->sell_address = $_bidding->user->adr;
if($_bidding->user->pic != ''){
	$out_data->sell_pic = PIC_URL.$_bidding->user->pic;
}else{
	$out_data->sell_pic = '';
}

$out_data->business_area = $_bidding->user->business_area->name;
$out_data->distance = get_distance($_bidding->user->business_area->lat, $_bidding->user->business_area->lng, $_bidding->inquiry->user->business_area->lat, $_bidding->inquiry->user->business_area->lng);
$out_data->sell_level = $_bidding->user->sell_level;
$out_data->sell_score = array(
		$_bidding->user->order_score_count > 0?round($_bidding->user->sell_match/$_bidding->user->order_score_count,1):0,
		$_bidding->user->order_score_count > 0?round($_bidding->user->sell_service/$_bidding->user->order_score_count,1):0,
		$_bidding->user->order_score_count > 0?round($_bidding->user->sell_speed/$_bidding->user->order_score_count,1):0
);
$out_data->pri = $_bidding->pri;

$band_array = array();
$band_id_array = explode(',', $_bidding->partband_id);
foreach ($band_id_array as $band_id){
	if($band_id != NULL && $band_id != ''){
		$_partBand = new PartBand();
		$_partBand->id = $band_id;
		$_partBand->init();
			
		$band_array[] = $_partBand->nam;
	}
}
$out_data->ban = $band_array;

if($_bidding->inquiry->car->id > 0){
	$out_data->car = $_bidding->inquiry->car->nam;
}else{
	$out_data->car = NO_CAR_MESSAGE;
}

$out_data->c = $_bidding->c;
$out_data->parttype_quality = $_bidding->parttype_quality;
$out_data->avi = $_bidding->avi;
$out_data->qau = $_bidding->qau;
$out_data->pay = $_bidding->pay;
$out_data->del = $_bidding->del;
$out_data->pof = $_bidding->pof;

if($_bidding->pic1 != ''){
	$out_data->pic1 = BIDDING_PIC1_URL.$_bidding->pic1;
}else{
	$out_data->pic1 = '';
}

if($_bidding->pic2 != ''){
	$out_data->pic2 = BIDDING_PIC2_URL.$_bidding->pic2;
}else{
	$out_data->pic2 = '';
}

if($_bidding->pic3 != ''){
	$out_data->pic3 = BIDDING_PIC3_URL.$_bidding->pic3;
}else{
	$out_data->pic3 = '';
}

$out_data->mes = $_bidding->mes;

if($_bidding->aum != ''){
	$out_data->aum = BIDDING_AUM_URL.$_bidding->aum;
}else{
	$out_data->aum = '';
}

$out_data->parttypeid = $_bidding->inquiry->parttype->id;

out_data($out_data);


?>