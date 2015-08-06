<?php
require_once '../common/constant.php';
require_once '../common/FileManager.class.php';
require_once '../common/verify_permission.php';
require_once '../model/Line.class.php';

$_line = new Line();
$_line->id = intval($_GET['id']);
$_line->init();


$_line->del();
?>
<meta charset="utf-8"/>
<script>
alert("删除成功");
document.location.href = "../pages/line.php";
</script>