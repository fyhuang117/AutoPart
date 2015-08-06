<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/Line.class.php';
require_once '../model/Car.class.php';

if(!isset($input_data->lineid) || !isset($input_data->yea)){
	send_error(400, '消息格式或参数不正确');
}

$_line = new Line();
$_line->id = intval($input_data->lineid);
$_line->init();

if($_line->id == 0){
	send_error(404, '车系不存在');
}

$_car = new Car();
$_car->line = $_line;
$_car->yea = $input_data->yea;
$car_list = $_car->getListByLineAndYear();

$out_data = new stdClass();
$out_data->lis = array();

foreach ($car_list as $_car_){
	$obj = new stdClass();
	$obj->carid = $_car_->id;
	$obj->nam = $_car_->nam;
	$obj->vol = $_car_->val;
	$obj->yea = $_car_->yea;
	if($_car_->pic != ''){
		$obj->pic = CAR_PIC_URL.$_car_->pic;
	}else{
		$obj->pic = '';
	}
	
	$out_data->lis[] = $obj;
}

out_data($out_data);


?>