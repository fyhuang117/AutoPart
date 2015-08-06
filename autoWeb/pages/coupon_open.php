<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/Coupon.class.php';

$_coupon = new Coupon();
$_coupon->id = intval($_GET['coupon_id']);
$_coupon->init();

$_coupon->open();

?>
<meta charset="utf-8"/>
<script>
alert("启用成功");
document.location.href = "../pages/coupon.php";
</script>
