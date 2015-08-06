<?php
require_once '../common/constant.php';
require_once '../model/System.class.php';

$_system = new System();
$_system->init();

$out_data = new stdClass();
if($_system->service_file != ''){
	$out_data->url = CUSTOMSERVICE_URL.$_system->service_file;
}else{
	$out_data->url = '';
}


out_data($out_data);
?>