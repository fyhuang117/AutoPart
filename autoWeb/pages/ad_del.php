<?php
require_once '../common/constant.php';
require_once '../common/FileManager.class.php';
require_once '../common/verify_permission.php';
require_once '../model/Ad.class.php';

$_ad = new Ad();
$_ad->id = intval($_GET['id']);
$_ad->init();

$_file_manager = new FileManager();
if($_ad->pic != ''){
	$_file_manager->file_name = $_ad->pic;
	$_file_manager->delFile(AD_PIC_PATH);
}

$_ad->del();
?>
<meta charset="utf-8"/>
<script>
alert("删除成功");
document.location.href = "../pages/ad.php";
</script>