<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/System.class.php';

if(!isset($input_data->type)){
	send_error(400, '消息格式或参数不正确');
}

$out_data = new stdClass();

if(intval($input_data->type) == 0){
	$out_data->version_num = VERSION_NUM_BUY;
	$out_data->version_str = VERSION_STR_BUY;
	$out_data->download = VERSION_URL_BUY_ANDROID;
}else{
	$out_data->version_num = VERSION_NUM_SELL;
	$out_data->version_str = VERSION_STR_SELL;
	$out_data->download = VERSION_URL_SELL_ANDROID;
}

out_data($out_data);

/*
if(!isset($input_data->app) || !isset($input_data->dat)){
	send_error(400, '消息格式或参数不正确');
}

$_system = new System();
$_system->init();

$out_data = new stdClass();
$out_data->app = $_system->app_version;			//服务端应用版本
$out_data->dat = DAT_VERSION;				//服务断数据版本

$client_app = intval($input_data->app);		//客户端应用版本
$client_dat = intval($input_data->dat);		//客户端数据版本

if($client_app >= $_system->app_version && $client_dat >= DAT_VERSION){
	$out_data->sta = VERSION_STATUS_NO_UPDATE;		//更新状态码
}else{
	if(IS_UPDATE_NOW){
		$out_data->sta = VERSION_STATUS_APP_UPDATE_NOW;
	}else{
		if($client_app < $_system->app_version && $client_dat < DAT_VERSION){
			$out_data->sta = VERSION_STATUS_APP_DAT_UPDATE;
		}else{
			if($client_app < $_system->app_version){
				$out_data->sta = VERSION_STATUS_APP_UPDATE;
			}else{
				$out_data->sta = VERSION_STATUS_DAT_UPDATE;
			}
		}
	}
}

$out_data->g_dat = UPDATE_URL;			//下载地址

out_data($out_data);
*/
?>