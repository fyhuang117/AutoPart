<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Order.class.php';
require_once '../model/CreditLevel.class.php';

if(!isset($input_data->id) || !isset($input_data->orderid) || !isset($input_data->score) || !isset($input_data->comments) || !isset($input_data->match) || !isset($input_data->service) 
	|| !isset($input_data->speed)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_order = new Order();
$_order->id = intval($input_data->orderid);
$_order->init();

if($_user->id == 0 || $_order->id == 0 || $_user->id != $_order->buy_user->id){
	send_error(404, '资源不存在');
}

if($_order->buyer_score > 0){
	send_error(404, '资源不存在');
}

$_order->buyer_score = intval($input_data->score);
$_order->comments = $input_data->comments;
$_order->match = intval($input_data->match);
$_order->service = intval($input_data->service);
$_order->speed = intval($input_data->speed);
$_order->buyerScore();

++$_order->sell_user->order_score_count;
$_order->sell_user->sell_match += $_order->match;
$_order->sell_user->sell_service += $_order->service;
$_order->sell_user->sell_speed += $_order->speed;

$_order->sell_user->updateSellScore();

$user_now_point = $_order->sell_user->sell_point;
switch ($_order->buyer_score){
	case BUYER_SCORE_BAD:
		--$user_now_point;
		break;
	case BUYER_SCORE_GOOD:
		++$user_now_point;
		break;
}

if($user_now_point < 0){
	$user_now_point = 0;
}

$_creditLevel = new CreditLevel();
$_creditLevel->point = $user_now_point;
$user_now_level = $_creditLevel->getLevelByPoint();

$_order->sell_user->sell_level = $user_now_level;
$_order->sell_user->sell_point = $user_now_point;
$_order->sell_user->updateSellLevelAndPoint();

?>