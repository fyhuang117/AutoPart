<?php
require_once '../common/constant.php';
require_once '../common/FileManager.class.php';
require_once '../common/verify_permission.php';
require_once '../model/Band.class.php';

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

if(trim($_POST['first_word']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("首字母不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}


if(!isset($_FILES["pic"]) || $_FILES["pic"]["name"] == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("请上传文件");';
	echo 'history.back();';
	echo '</script>';
	die();
}



$_band = new Band();
$_band->nam = $_POST['nam'];
$_band->mad = $_POST['mad'];

$_file_manager = new FileManager();
$_file_manager->upload($_FILES["pic"], '', BAND_PIC_PATH);
$_band->pic = $_file_manager->file_name;


$_band->first_word = $_POST['first_word'];

$_band->save();
?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
document.location.href = "../pages/carband.php";
</script>