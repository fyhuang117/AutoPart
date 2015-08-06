<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../jpush/JPush.class.php';
require_once '../model/Back.class.php';

$_back = new Back();
$_back->id = intval($_GET['back_id']);
$_back->init();

if($_back->id > 0){
	$_back->success();
	
	$_back->order->user_coupon->backCoupon();
	
	//推送消息
	$_jpush = new JPush();
	$_jpush->buy_push($_back->order->buy_user->jpush_alias, '您申请的退款已成功,请登录APP查看');
	
}
?>
<meta charset="utf-8"/>
<script>
alert("退款成功");
document.location.href = "../pages/back.php";
</script>