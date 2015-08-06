<?php

header("Content-type:text/html;charset=utf-8");
require_once '../common/constant.php';



$filename = $_GET['fn'];

$filename = BAND_PIC_PATH.$filename;
	
$filesize = filesize($filename); 
$icon = fread(fopen($filename, "r"), $filesize);  
echo $icon;

?>