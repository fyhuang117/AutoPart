<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/PartType.class.php';

if(!isset($input_data->name)){
	send_error(400, '消息格式或参数不正确');
}

$_partType = new PartType();

$_partType->nam = $input_data->name;
$partType_list = $_partType->getListByNam();

$out_data = new stdClass();
$out_data->lis = array();

foreach ($partType_list as $_partType_){
	$obj = new stdClass();
	$obj->parttypeid = $_partType_->id;
	$obj->nam = $_partType_->nam;
	
	/*
	if($_partType_->pic != ''){
		$obj->pic = PARTTYPE_PIC_URL.$_partType_->pic;
	}else{
		$obj->pic = '';
	}

	$obj->is_select_car = $_partType_->is_select_car;
	$obj->is_select_quality = $_partType_->is_select_quality;
	*/

	$out_data->lis[] = $obj;
}

out_data($out_data);
?>