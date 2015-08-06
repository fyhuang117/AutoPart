<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Wallet.class.php';
require_once '../model/Transaction.class.php';
require_once '../model/Order.class.php';

if(!isset($input_data->id)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

$_wallet = new Wallet();
$_wallet->user = $_user;
$_wallet->initByUser();

$_order = new Order();
$_order->sell_user = $_user;

$_tran = new Transaction();
$_tran->user = $_user;

$out_data = new stdClass();

//账户余额
$out_data->balance = $_wallet->balance;
//即将到账
$out_data->pri = $_order->getPriBySell();
//累计收入
$out_data->in = $_tran->getInByUser();
//累计提现
$out_data->out = $_tran->getOutByUser();

out_data($out_data);
?>