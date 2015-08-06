<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/BusinessArea.class.php';

if(!isset($input_data->areaparentid)){
	send_error(400, '消息格式或参数不正确');
}

$_area = new BusinessArea();
$_area->parent_id = intval($input_data->areaparentid);
$area_list = $_area->getListByParent();

$out_data = new stdClass();
$out_data->lis = array();

foreach ($area_list as $_area_){
	$obj = new stdClass();
	$obj->areaid = $_area_->id;
	$obj->name = $_area_->name;
	$obj->firstword = $_area_->first_word;
	
	$out_data->lis[] = $obj;
}

out_data($out_data);


?>