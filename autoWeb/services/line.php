<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/BandSub.class.php';
require_once '../model/Line.class.php';

if(!isset($input_data->subbandid)){
	send_error(400, '消息格式或参数不正确');
}

$_band_sub = new BandSub();
$_band_sub->id = intval($input_data->subbandid);
$_band_sub->init();

if($_band_sub->id == 0){
	send_error(404, '子品牌不存在');
}

$_line = new Line();
$_line->band_sub = $_band_sub;
$line_list = $_line->getListByBand();

$out_data = new stdClass();
$out_data->lis = array();

foreach ($line_list as $_line_){
	$obj = new stdClass();
	$obj->lineid = $_line_->id;
	$obj->nam = $_line_->nam;
	
	$out_data->lis[] = $obj;
}

out_data($out_data);


?>