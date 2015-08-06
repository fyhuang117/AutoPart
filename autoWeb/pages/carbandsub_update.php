<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/BandSub.class.php';
require_once '../model/Band.class.php';

if(trim($_POST['name']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("名称不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

$_band_sub = new BandSub();
$_band_sub->id = intval($_POST['id']);
$_band_sub->init();

$_band_sub->name = $_POST['name'];

$_band = new Band();
$_band->id = intval($_POST['band_id']);
$_band->init();
$_band_sub->band = $_band;

$_band_sub->update();
?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
parent.document.location.href = "../pages/carbandsub.php";
</script>