<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/User.class.php';
require_once '../model/Share.class.php';
require_once '../model/Coupon.class.php';
require_once '../model/UserCoupon.class.php';

$_user = new User();
$_user->id = intval($_GET['id']);
$_user->init();

$_user->audit();

//推送
$_jpush = new JPush();
$push_message = '您的账户已经通过审核,请登录APP查看';
if($_user->type == 0){
	$_jpush->buy_push($_user->jpush_alias, $push_message);
}else{
	$_jpush->sell_push($_user->jpush_alias, $push_message);
}

//如果是买家
if($_user->type == 0){
	
	$_share = new Share();
	$_share->user = $_user;
	$share_user = $_share->getShare();
	
	//是否有分享人
	if($share_user->id > 0){
		$_coupon = new Coupon();
		$coupon_list = $_coupon->getListByPage(0, $_coupon->getCount());
		foreach ($coupon_list as $_coupon_){
			if($_coupon_->get_type == COUPON_GET_TYPE_SHARE && $_coupon_->status == 1){
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
<meta charset="utf-8"/>
<script>
alert("通过成功");
<?php 
	if($_user->type == 0){
		echo 'parent.document.location.href = "../pages/buy.php";';
	}else{
		echo 'parent.document.location.href = "../pages/sell.php";';
	}
?>
</script>