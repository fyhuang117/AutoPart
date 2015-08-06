<?php
require_once '../common/constant.php';

$filename = $_GET['fn'];

$filename = USER_LICENSE_PATH.$filename;
	
$filesize = filesize($filename); 
$icon = fread(fopen($filename, "r"), $filesize);  
echo $icon;

?>