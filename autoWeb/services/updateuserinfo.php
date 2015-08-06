<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';

if(!isset($input_data->id) || !isset($input_data->nam) || !isset($input_data->adr) || !isset($input_data->ton) || !isset($input_data->lat) 
	|| !isset($input_data->lp1) || !isset($input_data->lp2) || !isset($input_data->pic) || !isset($input_data->license) || !isset($input_data->agent)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

if($_user->id == 0){
	send_error(404, '用户不存在');
}

if($input_data->nam != ''){
	$_user->nam = $input_data->nam;
}
if($input_data->adr != ''){
	$_user->adr = $input_data->adr;
}
if($input_data->ton != ''){
	$_user->ton = $input_data->ton;
}
if($input_data->lat != ''){
	$_user->lat = $input_data->lat;
}



if($input_data->lp1 != NULL && $input_data->lp1 != ''){
	if($_user->lp1 != NULL && $_user->lp1 != ''){
		unlink(USER_LP1_PATH.$_user->lp1);
	}
	$_user->lp1 = save_file(_hex2bin($input_data->lp1), USER_LP1_PATH, 'png');
}


if($input_data->lp2 != NULL && $input_data->lp2 != ''){
	if($_user->lp2 != NULL && $_user->lp2 != ''){
		unlink(USER_LP2_PATH.$_user->lp2);
	}
	$_user->lp2 = save_file(_hex2bin($input_data->lp2), USER_LP2_PATH, 'png');
}


if($input_data->pic != NULL && $input_data->pic != ''){
	if($_user->pic != NULL && $_user->pic != ''){
		unlink(USER_PIC_PATH.$_user->pic);
	}
	$_user->pic = save_file(_hex2bin($input_data->pic), USER_PIC_PATH, 'png');
}


if($input_data->license != NULL && $input_data->license != ''){
	if($_user->license != NULL && $_user->license != ''){
		unlink(USER_LICENSE_PATH.$_user->license);
	}
	$_user->license = save_file(_hex2bin($input_data->license), USER_LICENSE_PATH, 'png');
}


if($input_data->agent != NULL && $input_data->agent != ''){
	if($_user->agent != NULL && $_user->agent != ''){
		unlink(USER_AGENT_PATH.$_user->agent);
	}
	$_user->agent = save_file(_hex2bin($input_data->agent), USER_AGENT_PATH, 'png');
}

if($_user->enb == 1){
	$_user->updatePic();
}else{
	$_user->update();
}
?>