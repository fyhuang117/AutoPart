<?php
require_once '../common/constant.php';
require_once '../common/FileManager.class.php';
require_once '../common/verify_permission.php';
require_once '../model/Band.class.php';

$_band = new Band();
$_band->id = intval($_GET['id']);
$_band->init();

$_file_manager = new FileManager();
if($_band->pic != ''){
	$_file_manager->file_name = $_band->pic;
	$_file_manager->delFile(BAND_PIC_PATH);
}

$_band->del();
?>
<meta charset="utf-8"/>
<script>
alert("删除成功");
document.location.href = "../pages/carband.php";
</script>