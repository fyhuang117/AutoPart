<?php
require_once '../common/constant.php';
require_once '../common/FileManager.class.php';
require_once '../common/verify_permission.php';
require_once '../model/Car.class.php';

$_car = new Car();
$_car->id = intval($_GET['id']);
$_car->init();

$_file_manager = new FileManager();
if($_car->pic != ''){
	$_file_manager->file_name = $_car->pic;
	$_file_manager->delFile(CAR_PIC_PATH);
}

$_car->del();
?>
<meta charset="utf-8"/>
<script>
alert("删除成功");
document.location.href = "../pages/car.php";
</script>