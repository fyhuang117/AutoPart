<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../jpush/JPush.class.php';
require_once '../model/User.class.php';
require_once '../model/Order.class.php';
require_once '../model/Wallet.class.php';
require_once '../model/Transaction.class.php';
require_once '../model/Share.class.php';

if(!isset($input_data->id) || !isset($input_data->orderid)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_order = new Order();
$_order->id = intval($input_data->orderid);
$_order->init();

if($_user->id == 0 || $_order->id == 0 || $_user->id != $_order->buy_user->id || $_order->status != ORDER_STATUS_SEND){
	send_error(404, '资源不存在');
}

$_order->receive();

$_wallet = new Wallet();
$_wallet->user = $_order->sell_user;
$_wallet->balance = $_order->pri;
$_wallet->inByUser();

$_tran = new Transaction();
$_tran->user = $_order->sell_user;
$_tran->price = $_order->pri;
$_tran->type = TRANSACTION_TYPE_IN;
$_tran->save();

//推送消息
$_jpush = new JPush();
$_jpush->sell_push($_order->sell_user->jpush_alias, '您的订单有买家已收货,请登录APP查看');

//如果买家是首单
if($_order->getCompleteCountByBuy() == 1){
	$_coupon = new Coupon();
	$coupon_list = $_coupon->getListByPage(0, $_coupon->getCount());
	foreach ($coupon_list as $_coupon_){
		if($_coupon_->get_type == COUPON_GET_TYPE_FIRST_ORDER && $_coupon_->status == 1){
			for($i = 0;$i < $_coupon_->get_count;++$i){
				$_user_coupon = new UserCoupon();
				$_user_coupon->user = $_order->buy_user;
				$_user_coupon->coupon = $_coupon_;
				$_user_coupon->save();
			}
		}
	}
	
	$_share = new Share();
	$_share->user = $_order->buy_user;
	$share_user = $_share->getShare();
	
	//是否有分享人
	if($share_user->id > 0){
		$_coupon = new Coupon();
		foreach ($coupon_list as $_coupon_){
			if($_coupon_->get_type == COUPON_GET_TYPE_SHARE_FIRST_ORDER && $_coupon_->status == 1){
				for($i = 0;$i < $_coupon_->get_count;++$i){
					$_user_coupon = new UserCoupon();
					$_user_coupon->user = $share_user;
					$_user_coupon->coupon = $_coupon_;
					$_user_coupon->save();
				}
			}
		}
	}
}


?>