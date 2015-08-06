<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/BusinessArea.class.php';

if(trim($_POST['name']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("名称不能为空");';
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

$_businessArea = new BusinessArea();
$_businessArea->name = $_POST['name'];
$_businessArea->lat = floatval($_POST['lat']);
$_businessArea->lng = floatval($_POST['lng']);
$_businessArea->parent_id = intval($_POST['parent_id']);
$_businessArea->first_word = $_POST['first_word'];

$_businessArea->save();
?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
document.location.href = "../pages/businessarea.php";
</script>