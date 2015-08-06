<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/Line.class.php';
require_once '../model/Car.class.php';

if(!isset($input_data->lineid)){
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
$years = $_car->getYearListByLine();

$out_data = new stdClass();
$out_data->lis = array();

foreach ($years as $y){
	
	$obj = new stdClass();
	$obj->year = $y;
	$obj->car = array();
	
	$_car = new Car();
	$_car->line = $_line;
	$_car->yea = $y;
	$car_list = $_car->getListByLineAndYear();
	
	foreach ($car_list as $_car_){
		$car = new stdClass();
		$car->carid = $_car_->id;
		$car->nam = $_car_->nam;
		$car->vol = $_car_->val;
		if($_car_->pic != ''){
			$car->pic = CAR_PIC_URL.$_car_->pic;
		}else{
			$car->pic = '';
		}
		
	
		$obj->car[] = $car;
	}
	
	$out_data->lis[] = $obj;
}

out_data($out_data);


?>