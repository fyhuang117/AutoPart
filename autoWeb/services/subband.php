<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/Band.class.php';
require_once '../model/BandSub.class.php';
require_once '../model/Line.class.php';

if(!isset($input_data->bandid)){
	send_error(400, '消息格式或参数不正确');
}

$_band = new Band();
$_band->id = intval($input_data->bandid);
$_band->init();

if($_band->id == 0){
	send_error(404, '品牌不存在');
}

$_band_sub = new BandSub();
$_band_sub->band = $_band;
$band_sub_list = $_band_sub->getListByBand();

$out_data = new stdClass();
$out_data->lis = array();

foreach ($band_sub_list as $_band_sub_){
	$obj = new stdClass();
	$obj->subbandid = $_band_sub_->id;
	$obj->name = $_band_sub_->name;
	
	$_line = new Line();
	$_line->band_sub = $_band_sub_;
	$line_list = $_line->getListByBand();
	
	$obj->line = array();
	
	foreach ($line_list as $_line_){
		$line = new stdClass();
		$line->lineid = $_line_->id;
		$line->nam = $_line_->nam;
	
		$obj->line[] = $line;
	}
	
	$out_data->lis[] = $obj;
}

out_data($out_data);


?>