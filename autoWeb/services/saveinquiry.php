<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Inquiry.class.php';
require_once '../model/Bidding.class.php';
require_once '../model/PartBand.class.php';
require_once '../model/PartType.class.php';
require_once '../model/BusinessArea.class.php';

if(!isset($input_data->id) || !isset($input_data->parttypeid) || !isset($input_data->partbandid) || !isset($input_data->carid) || !isset($input_data->c) 
	|| !isset($input_data->pic1) || !isset($input_data->pic2) || !isset($input_data->pic3) || !isset($input_data->mes) || !isset($input_data->aum) || !isset($input_data->parttype_quality)  || !isset($input_data->businessarea_id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_partType = new PartType();
$_partType->id = $input_data->parttypeid;
$_partType->init();

$_car = new Car();
$_car->id = intval($input_data->carid);
$_car->init();

$_area = new BusinessArea();
$_area->id = intval($input_data->businessarea_id);
$_area->init();

if($_user->id == 0 || $_partType->id == 0){
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

$_inquiry = new Inquiry();
$_inquiry->user = $_user;
$_inquiry->parttype = $_partType;
$_inquiry->partband_id = $input_data->partbandid;
$_inquiry->car = $_car;
$_inquiry->c = intval($input_data->c);

if($input_data->pic1 != NULL && $input_data->pic1 != ''){
	$_inquiry->pic1 = save_file(_hex2bin($input_data->pic1), INQUIRY_PIC1_PATH, 'png');
}else{
	$_inquiry->pic1 = '';
}

if($input_data->pic2 != NULL && $input_data->pic2 != ''){
	$_inquiry->pic2 = save_file(_hex2bin($input_data->pic2), INQUIRY_PIC2_PATH, 'png');
}else{
	$_inquiry->pic2 = '';
}

if($input_data->pic3 != NULL && $input_data->pic3 != ''){
	$_inquiry->pic3 = save_file(_hex2bin($input_data->pic3), INQUIRY_PIC3_PATH, 'png');
}else{
	$_inquiry->pic3 = '';
}

$_inquiry->mes = $input_data->mes;

if($input_data->aum != NULL && $input_data->aum != ''){
	$_inquiry->aum = save_file(_hex2bin($input_data->aum), INQUIRY_AUM_PATH, '');
}else{
	$_inquiry->aum = '';
}

$_inquiry->parttype_quality = intval($input_data->parttype_quality);

$_inquiry->area = $_user->business_area;

$_inquiry->save();

?>