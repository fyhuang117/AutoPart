<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/CreditLevel.class.php';

$_creditLevel = new CreditLevel();
$_creditLevel->id = intval($_GET['id']);
$_creditLevel->init();

$_creditLevel->del();
?>
<meta charset="utf-8"/>
<script>
alert("删除成功");
document.location.href = "../pages/creditlevel.php";
</script>