<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/PartType.class.php';
require_once '../model/Car.class.php';

if(!isset($input_data->parttypeid)){
	send_error(400, '消息格式或参数不正确');
}

$_partType = new PartType();
$_partType->id = $input_data->parttypeid;
$_partType->init();

if($_partType->id == 0){
	send_error(404, '配件分类不存在');
}

$out_data = new stdClass();
$out_data->lis = array();

$car_id_array = explode(',', $_partType->cars);
foreach ($car_id_array as $car_id){
	if($car_id != NULL && $car_id != ''){
		$_car = new Car();
		$_car->id = $car_id;
		$_car->init();
			
		$obj = new stdClass();
		$obj->carid = $_car->id;
		$obj->nam = $_car->nam;
		$obj->vol = $_car->val;
		$obj->yea = $_car->yea;
		if($_car->pic != ''){
			$obj->pic = CAR_PIC_URL.$_car->pic;
		}else{
			$obj->pic = '';
		}
		
		
		$out_data->lis[] = $obj;
	}
}

out_data($out_data);


?>