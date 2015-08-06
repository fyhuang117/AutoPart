<?php
require_once '../common/constant.php';
require_once '../common/FileManager.class.php';
require_once '../common/verify_permission.php';
require_once '../model/PartBand.class.php';

if(trim($_POST['nam']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("名称不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

if(trim($_POST['mad']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("产地不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

$_partBand = new PartBand();
$_partBand->id = intval($_POST['id']);
$_partBand->init();

$_partBand->nam = $_POST['nam'];
$_partBand->mad = $_POST['mad'];

$_file_manager = new FileManager();

if(isset($_FILES["pic"]) && $_FILES["pic"]["name"] != ''){
	$_file_manager->file_name = $_partBand->pic;
	$_file_manager->delFile(PARTBAND_PIC_PATH);
	
	$_file_manager->upload($_FILES["pic"], '', PARTBAND_PIC_PATH);
	$_partBand->pic = $_file_manager->file_name;
}

$_partBand->update();
?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
parent.document.location.href = "../pages/partsband.php";
</script>