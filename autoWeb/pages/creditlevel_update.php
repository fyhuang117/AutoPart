<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/CreditLevel.class.php';

if(trim($_POST['name']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("名称不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}
if(trim($_POST['level']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("等级不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}
if(trim($_POST['point']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("积分不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

$_creditLevel = new CreditLevel();
$_creditLevel->id = intval($_POST['id']);
$_creditLevel->init();

$_creditLevel->name = $_POST['name'];
$_creditLevel->level = intval($_POST['level']);
$_creditLevel->point = intval($_POST['point']);

$_creditLevel->update();
?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
parent.document.location.href = "../pages/creditlevel.php";
</script>