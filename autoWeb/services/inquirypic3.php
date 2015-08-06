<?php
require_once '../common/constant.php';

$filename = $_GET['fn'];

$filename = INQUIRY_PIC3_PATH.$filename;
	
$filesize = filesize($filename); 
$icon = fread(fopen($filename, "r"), $filesize);  
echo $icon;

?>