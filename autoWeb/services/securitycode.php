<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../common/class_curl.php';
require_once '../model/User.class.php';


if(!isset($input_data->tel) || !isset($input_data->type)){
	send_error(400, '消息格式或参数不正确');
}

$tel = $input_data->tel;		//手机号码

$_user = new User();
$_user->tel = $tel;
$_user->type = intval($input_data->type);
$_user->save_sec();

$sec = $_user->sec;

//发送短信
/*
$pwd = strtoupper(md5('SDK-BBX-010-21726'.'78@07ea@'));
$inter_url = 'http://sdk2.entinfo.cn:8061/mdsmssend.ashx?sn=SDK-BBX-010-21726&pwd='.$pwd.'&mobile='.$tel.'&content='.$sec.'&ext=&stime=&rrid=&msgfmt=';
$result = file_get_contents($inter_url);
*/
send_sms($tel, '验证码:'.$sec.',请勿向任何人提供您收到的验证码');

$out_data = new stdClass();
$out_data->result = 1;		
out_data($out_data);

?>