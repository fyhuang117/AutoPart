<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/Ad.class.php';

$_ad = new Ad();
$ad_list = $_ad->getList();

$out_data = new stdClass();
$out_data->lis = array();

foreach ($ad_list as $_ad_){

	$obj = new stdClass();
	$obj->adid = $_ad_->id;
	$obj->tit = $_ad_->tit;
	if($_ad_->pic != ''){
		$obj->pic = AD_PIC_URL.$_ad_->pic;
	}else{
		$obj->pic = '';
	}
	$obj->url = $_ad_->url;
		
	$out_data->lis[] = $obj;
}

out_data($out_data);


?>