<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/Order.class.php';

$_order = new Order();
$_order->id = intval($_GET['id']);
$_order->init();

$_order->unfreeze();
?>
<meta charset="utf-8"/>
<script>
alert("解冻成功");
parent.document.location.href = "../pages/order.php";
</script>