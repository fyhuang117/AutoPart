<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../jpush/JPush.class.php';
require_once '../model/Out.class.php';
require_once '../model/Wallet.class.php';
require_once '../model/Transaction.class.php';

$_out = new Out();
$_out->id = intval($_GET['out_id']);
$_out->init();

if($_out->id > 0){
	$_out->success();
	
	$_wallet = new Wallet();
	$_wallet->user = $_out->user;
	$_wallet->balance = $_out->price;
	$_wallet->outByUser();
	
	$_tran = new Transaction();
	$_tran->user = $_out->user;
	$_tran->price = $_out->price;
	$_tran->type = TRANSACTION_TYPE_OUT;
	$_tran->save();
	
	//推送消息
	$_jpush = new JPush();
	$_jpush->sell_push($_out->user->jpush_alias, '您申请的提现已成功,请登录APP查看');
}
?>
<meta charset="utf-8"/>
<script>
alert("提现成功");
document.location.href = "../pages/cash.php";
</script>