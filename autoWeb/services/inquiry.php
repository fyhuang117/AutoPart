<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Inquiry.class.php';
require_once '../model/Bidding.class.php';
require_once '../model/PartBand.class.php';

if(!isset($input_data->inquiryid)){
	send_error(400, '消息格式或参数不正确');
}

$_inquiry = new Inquiry();
$_inquiry->id = intval($input_data->inquiryid);
$_inquiry->init();

if($_inquiry->id == 0){
	send_error(404, '询价单不存在');
}

$out_data = new stdClass();
$out_data->par = $_inquiry->parttype->nam;

$band_array = array();
$band_id_array = explode(',', $_inquiry->partband_id);
foreach ($band_id_array as $band_id){
	if($band_id != NULL && $band_id != ''){
		$_partBand = new PartBand();
		$_partBand->id = $band_id;
		$_partBand->init();
			
		$band_array[] = $_partBand->nam;
	}
}
$out_data->ban = $band_array;

if($_inquiry->car->id > 0){
	$out_data->car = $_inquiry->car->nam;
}else{
	$out_data->car = NO_CAR_MESSAGE;
}

$out_data->c = $_inquiry->c;

if($_inquiry->pic1 != ''){
	$out_data->pic1 = INQUIRY_PIC1_URL.$_inquiry->pic1;
}else{
	$out_data->pic1 = '';
}

if($_inquiry->pic2 != ''){
	$out_data->pic2 = INQUIRY_PIC2_URL.$_inquiry->pic2;
}else{
	$out_data->pic2 = '';
}

if($_inquiry->pic3 != ''){
	$out_data->pic3 = INQUIRY_PIC3_URL.$_inquiry->pic3;
}else{
	$out_data->pic3 = '';
}

$out_data->mes = $_inquiry->mes;

if($_inquiry->aum != ''){
	$out_data->aum = INQUIRY_AUM_URL.$_inquiry->aum;
}else{
	$out_data->aum = '';
}

$out_data->parttype_quality = $_inquiry->parttype_quality;
$out_data->business_area = $_inquiry->area->name;


out_data($out_data);


?>