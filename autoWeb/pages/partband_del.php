<?php
require_once '../common/constant.php';
require_once '../common/FileManager.class.php';
require_once '../common/verify_permission.php';
require_once '../model/PartBand.class.php';

$_partBand = new PartBand();
$_partBand->id = intval($_GET['id']);
$_partBand->init();

$_file_manager = new FileManager();
if($_partBand->pic != ''){
	$_file_manager->file_name = $_partBand->pic;
	$_file_manager->delFile(PARTBAND_PIC_PATH);
}

$_partBand->del();
?>
<meta charset="utf-8"/>
<script>
alert("删除成功");
document.location.href = "../pages/partsband.php";
</script>