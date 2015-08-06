<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/PartType.class.php';
require_once '../model/PartBand.class.php';

if(!isset($input_data->parttypeid)){
	send_error(400, '消息格式或参数不正确');
}

$_partType = new PartType();
$_partType->id = $input_data->parttypeid;
$_partType->init();

if($_partType->id == 0){
	send_error(404, '配件类型不存在');
}

$partBand_id_str = $_partType->ban;
$partBand_id_array = explode(',', $partBand_id_str);

$out_data = new stdClass();
$out_data->lis = array();

foreach ($partBand_id_array as $partBand_id){
	if($partBand_id != NULL && $partBand_id != '' && intval($partBand_id) > 0){
		
		$_partBand = new PartBand();
		$_partBand->id = intval($partBand_id);
		$_partBand->init();
		
		$obj = new stdClass();
		$obj->partbandid = $_partBand->id;
		$obj->nam = $_partBand->nam;
		if($_partBand->pic != ''){
			$obj->pic = PARTBAND_PIC_URL.$_partBand->pic;
		}else{
			$obj->pic = '';
		}
		
		$out_data->lis[] = $obj;
	}
}

out_data($out_data);


?>