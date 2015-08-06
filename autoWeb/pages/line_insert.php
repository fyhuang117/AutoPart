<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/BandSub.class.php';
require_once '../model/Line.class.php';

if(trim($_POST['nam']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("名称不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

$_line = new Line();
$_line->nam = $_POST['nam'];

$_band_sub = new BandSub();
$_band_sub->id = intval($_POST['band_sub_id']);
$_band_sub->init();
$_line->band_sub = $_band_sub;

$_line->save();
?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
document.location.href = "../pages/line.php";
</script>