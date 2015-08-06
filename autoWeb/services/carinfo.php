<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/Car.class.php';

if(!isset($input_data->carid)){
	send_error(400, '消息格式或参数不正确');
}

$_car = new Car();
$_car->id = intval($input_data->carid);
$_car->init();

if($_car->id == 0){
	send_error(404, '汽车不存在');
}

$out_data = new stdClass();
$out_data->nam = $_car->nam;
$out_data->vol = $_car->val;
$out_data->yea = $_car->yea;
if($_car->pic != ''){
	$out_data->pic = CAR_PIC_URL.$_car->pic;
}else{
	$out_data->pic = '';
}


out_data($out_data);


?>