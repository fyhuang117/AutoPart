<?php
require_once '../common/constant.php';
require_once '../common/FileManager.class.php';
require_once '../common/verify_permission.php';
require_once '../model/PartType.class.php';

if(trim($_POST['nam']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("名称不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

$_partType = new PartType();
$_partType->id = $_POST['id'];
$_partType->init();

$_partType->type = intval($_POST['type']);
$_partType->nam = $_POST['nam'];

/*
$_file_manager = new FileManager();
if(isset($_FILES["pic"]) && $_FILES["pic"]["name"] != ''){
	$_file_manager->file_name = $_partType->pic;
	$_file_manager->delFile(PARTTYPE_PIC_PATH);

	$_file_manager->upload($_FILES["pic"], '', PARTTYPE_PIC_PATH);
	$_partType->pic = $_file_manager->file_name;
}
*/

$_partType->par = intval($_POST['par']);

$ban = ',';
if(isset($_POST['band_id'])){
	foreach ($_POST['band_id'] as $band_id){
		$ban .= $band_id.',';
	}
}
$_partType->ban = $ban;

$cars = ',';
if(isset($_POST['car_id'])){
	foreach ($_POST['car_id'] as $car_id){
		$cars .= $car_id.',';
	}
}
$_partType->cars = $cars;

$h = 0;
if(isset($_POST['h'])){
	$h = 1;
}
$_partType->h = $h;

$_partType->is_select_car = intval($_POST['is_select_car']);
$_partType->is_select_quality = intval($_POST['is_select_quality']);

$_partType->update();
?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
parent.document.location.href = "../pages/parttype.php";
</script>