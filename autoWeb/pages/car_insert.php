<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../common/FileManager.class.php';
require_once '../model/Car.class.php';
require_once '../model/Line.class.php';

if(trim($_POST['nam']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("名称不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

if(trim($_POST['val']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("排量不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

if(trim($_POST['yea']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("年份不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

$_car = new Car();
$_car->nam = $_POST['nam'];
$_car->val = $_POST['val'];
$_car->yea = $_POST['yea'];

$_line = new Line();
$_line->id = intval($_POST['line_id']);
$_line->init();
$_car->line = $_line;

$_file_manager = new FileManager();
$_file_manager->upload($_FILES["pic"], '', CAR_PIC_PATH);
$_car->pic = $_file_manager->file_name;

$_car->save();
?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
document.location.href = "../pages/car.php";
</script>