<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../common/FileManager.class.php';
require_once '../model/System.class.php';

if(trim($_POST['app_version']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("版本不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

if(trim($_POST['service_tel']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("电话不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

$_system = new System();
$_system->init();

$_system->app_version = $_POST['app_version'];
$_system->service_tel = $_POST['service_tel'];

$_file_manager = new FileManager();

if(isset($_FILES["pic"]) && $_FILES["pic"]["name"] != ''){
	$_file_manager->file_name = $_system->service_file;
	$_file_manager->delFile(CUSTOMSERVICE_PATH);
	
	$_file_manager->upload($_FILES["pic"], '', CUSTOMSERVICE_PATH);
	$_system->service_file = $_file_manager->file_name;
}

$_system->update();
?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
parent.document.location.href = "../pages/system.php";
</script>