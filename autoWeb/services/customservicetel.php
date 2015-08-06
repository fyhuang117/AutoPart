<?php
require_once '../common/constant.php';
require_once '../model/System.class.php';

$_system = new System();
$_system->init();

$out_data = new stdClass();
$out_data->tel = $_system->service_tel;

out_data($out_data);
?>