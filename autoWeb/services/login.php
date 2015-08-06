<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';

if(!isset($input_data->tel) || !isset($input_data->sec) || !isset($input_data->type)){
	send_error(400, '消息格式或参数不正确');
}

$tel = $input_data->tel;		//手机号码
$sec = $input_data->sec;		//验证码

$_user = new User();
$_user->tel = $tel;
$_user->sec = $sec;
$_user->type = intval($input_data->type);

$_user->login();

if($_user->id == 0){
	//登录失败
	send_error(401, '验证码错误');
}else{
	$out_data = new stdClass();
	$out_data->id = id_encode($_user->id);
	
	if($_user->nam != NULL && $_user->nam != ''){
		//不需要补充信息
		$out_data->need = 0;
	}else{
		$out_data->need = 1;
		
	}
	
	
	
	out_data($out_data);
}



?>