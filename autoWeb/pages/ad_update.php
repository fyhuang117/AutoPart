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

$_ad = new Ad();
$_ad->id = intval($_POST['id']);
$_ad->init();

$_ad->tit = $_POST['tit'];
$_ad->url = $_POST['url'];

$_file_manager = new FileManager();

if(isset($_FILES["pic"]) && $_FILES["pic"]["name"] != ''){
	$_file_manager->file_name = $_ad->pic;
	$_file_manager->delFile(AD_PIC_PATH);
	
	$_file_manager->upload($_FILES["pic"], '', AD_PIC_PATH);
	$_ad->pic = $_file_manager->file_name;
}

$_ad->update();
?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
parent.document.location.href = "../pages/ad.php";
</script>