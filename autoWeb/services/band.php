<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/Band.class.php';

$_band = new Band();
$band_list = $_band->getList();

$out_data = new stdClass();
$out_data->lis = array();

foreach ($band_list as $_band_){

	$obj = new stdClass();
	$obj->bandid = $_band_->id;
	$obj->nam = $_band_->nam;
	if($_band_->pic != ''){
		$obj->pic = BAND_PIC_URL.$_band_->pic;
	}else{
		$obj->pic = '';
	}
	$obj->first = $_band_->first_word;
		
	$out_data->lis[] = $obj;
}

out_data($out_data);


?>