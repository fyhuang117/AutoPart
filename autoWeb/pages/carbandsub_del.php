<?php
require_once '../common/constant.php';
require_once '../common/FileManager.class.php';
require_once '../common/verify_permission.php';
require_once '../model/BandSub.class.php';

$_band_sub = new BandSub();
$_band_sub->id = intval($_GET['id']);
$_band_sub->init();


$_band_sub->del();
?>
<meta charset="utf-8"/>
<script>
alert("删除成功");
document.location.href = "../pages/carbandsub.php";
</script>