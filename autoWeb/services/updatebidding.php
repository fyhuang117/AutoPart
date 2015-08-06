<?php
require_once '../common/constant.php';
require_once '../common/core.php';
require_once '../model/User.class.php';
require_once '../model/Bidding.class.php';
require_once '../model/PartBand.class.php';

if(!isset($input_data->id) || !isset($input_data->biddingid) || !isset($input_data->pri) || !isset($input_data->partbandid) || !isset($input_data->c) 
	|| !isset($input_data->avi) || !isset($input_data->qau) || !isset($input_data->pay) || !isset($input_data->del) || !isset($input_data->pof) 
	|| !isset($input_data->pic1) || !isset($input_data->pic2) || !isset($input_data->pic3) || !isset($input_data->mes) || !isset($input_data->aum)){
	send_error(400, '消息格式或参数不正确');
}

$_user = new User();
$_user->id = id_decode($input_data->id);
$_user->init();

$_bidding = new Bidding();
$_bidding->id = intval($input_data->biddingid);
$_bidding->init();

if($_user->id == 0 || $_bidding->id == 0 || $_user->id != $_bidding->user->id || $_bidding->status != BIDDING_STATUS_NORMAL){
	send_error(404, '资源不存在');
}

$band_id_array = explode(',', $input_data->partbandid);
foreach ($band_id_array as $band_id){
	if($band_id != NULL && $band_id != ''){
		$_partBand = new PartBand();
		$_partBand->id = $band_id;
		$_partBand->init();
			
		if($_partBand->id == 0){
			send_error(404, '资源不存在');
		}
	}
}

if($input_data->pri != ''){
	$_bidding->pri = $input_data->pri;
}
if($input_data->partbandid != ''){
	$_bidding->partband_id = $input_data->partbandid;
}
if($input_data->c != ''){
	$_bidding->c = intval($input_data->c);
}
if($input_data->avi != ''){
	$_bidding->avi = intval($input_data->avi);
}
if($input_data->qau != ''){
	$_bidding->qau = $input_data->qau;
}
if($input_data->pay != ''){
	$_bidding->pay = intval($input_data->pay);
}
if($input_data->del != ''){
	$_bidding->del = $input_data->del;
}
if($input_data->pof != ''){
	$_bidding->pof = intval($input_data->pof);
}



if($input_data->pic1 != NULL && $input_data->pic1 != ''){
	if($_bidding->pic1 != NULL && $_bidding->pic1 != ''){
		unlink(BIDDING_PIC1_PATH.$_bidding->pic1);
	}
	$_bidding->pic1 = save_file(_hex2bin($input_data->pic1), BIDDING_PIC1_PATH, 'png');
}


if($input_data->pic2 != NULL && $input_data->pic2 != ''){
	if($_bidding->pic2 != NULL && $_bidding->pic2 != ''){
		unlink(BIDDING_PIC2_PATH.$_bidding->pic2);
	}
	$_bidding->pic2 = save_file(_hex2bin($input_data->pic2), BIDDING_PIC2_PATH, 'png');
}


if($input_data->pic3 != NULL && $input_data->pic3 != ''){
	if($_bidding->pic3 != NULL && $_bidding->pic3 != ''){
		unlink(BIDDING_PIC3_PATH.$_bidding->pic3);
	}
	$_bidding->pic3 = save_file(_hex2bin($input_data->pic3), BIDDING_PIC3_PATH, 'png');
}

if($input_data->mes != ''){
	$_bidding->mes = $input_data->mes;
}



if($input_data->aum != NULL && $input_data->aum != ''){
	if($_bidding->aum != NULL && $_bidding->aum != ''){
		unlink(BIDDING_AUM_PATH.$_bidding->aum);
	}
	$_bidding->aum = save_file(_hex2bin($input_data->aum), BIDDING_AUM_PATH, '');
}

$_bidding->update();
?>