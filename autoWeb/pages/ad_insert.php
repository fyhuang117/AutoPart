<?php
require_once '../common/constant.php';
require_once '../common/FileManager.class.php';
require_once '../common/verify_permission.php';
require_once '../model/Ad.class.php';

if(trim($_POST['tit']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("标题不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

if(trim($_POST['url']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("连接不能为空");';
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

$_ad = new Ad();
$_ad->tit = $_POST['tit'];
$_ad->url = $_POST['url'];

$_file_manager = new FileManager();
$_file_manager->upload($_FILES["pic"], '', AD_PIC_PATH);
$_ad->pic = $_file_manager->file_name;

$_ad->save();
?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
document.location.href = "../pages/ad.php";
</script>