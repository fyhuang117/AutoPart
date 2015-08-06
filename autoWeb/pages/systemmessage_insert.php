<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/SystemMessage.class.php';
require_once '../model/User.class.php';
require_once '../model/UserMessage.class.php';

$_system_message = new SystemMessage();
$_system_message->title = $_POST['title'];
$_system_message->content = $_POST['content'];
$_system_message->target_type = intval($_POST['target_type']);
$_system_message->save();

$_jpush = new JPush();

if(intval($_POST['target_type']) == 0 || intval($_POST['target_type']) == 2){
	//发送给买家
	
	$_user_q = new User();
	$user_list = $_user_q->getBuyListByPage(0, $_user_q->getBuyCount());
	foreach ($user_list as $_user_){
		$_user_message = new UserMessage();
		$_user_message->user = $_user_;
		$_user_message->title = $_POST['title'];
		$_user_message->content = $_POST['content'];
		$_user_message->save();
	}
	
	//推送
	$_jpush->buy_push_all($_POST['content']);
}

if(intval($_POST['target_type']) == 1 || intval($_POST['target_type']) == 2){
	//发送给卖家
	
	$_user_q = new User();
	$user_list = $_user_q->getSellListByPage(0, $_user_q->getSellCount());
	foreach ($user_list as $_user_){
		$_user_message = new UserMessage();
		$_user_message->user = $_user_;
		$_user_message->title = $_POST['title'];
		$_user_message->content = $_POST['content'];
		$_user_message->save();
	}
	
	//推送
	$_jpush->sell_push_all($_POST['content']);
}
?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
document.location.href = "../pages/systemmessage.php";
</script>