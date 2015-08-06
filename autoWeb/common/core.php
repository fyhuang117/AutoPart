<?php
	date_default_timezone_set ( 'PRC' );

	$input_data = NULL;
	if(isset($_GET['j'])){
		$input_data = json_decode($_GET['j']);
	}else{
		$input_data = json_decode(file_get_contents('php://input'));
	}
	
	if(!strstr($_SERVER['PHP_SELF'], '/pages/') && !isset($_GET['fn'])){
		if(empty($input_data)){
			send_error(400, '消息格式或参数不正确');
		}
	}
	
	_writeLog('request:'.json_encode($input_data));
	
	function out_data($data){
		$return_data = json_encode($data);
		
		_writeLog('response:'.$return_data);
		
		echo $return_data;
	}
	
	function _writeLog($str){
		if(IS_DEBUG){
			$f = fopen(LOG_PATH,'a');
			fwrite($f,date('Y-m-d H:i:s',time())."\r\n".$str."\r\n");
			fclose($f);
		}
	}
	
	function send_error($response_code,$message){
		$messgae_obj = new stdClass();
		$messgae_obj->err = $message;
		header(json_encode($messgae_obj), true, $response_code);
		die();
	}
	
	//id加密
	function id_encode($id){
		return bin2hex(encrypt($id,ID_ENCODE_KEY));
	}
	
	//id解密
	function id_decode($id){
		return intval(decrypt(_hex2bin($id), ID_ENCODE_KEY));
	}
	
	//环信id加密
	function easymob_id_encode($id){
		return bin2hex(encrypt($id,ID_ENCODE_KEY));
	}
	
	//环信id解密
	function easymob_id_decode($id){
		return intval(decrypt(_hex2bin($id), ID_ENCODE_KEY));
	}
	
	function encrypt($str, $key) {
		$block = mcrypt_get_block_size(MCRYPT_RIJNDAEL_128,MCRYPT_MODE_ECB);
		$pad = $block - (strlen($str) % $block);
		$str .= str_repeat(chr($pad),$pad);
		return mcrypt_encrypt ( MCRYPT_RIJNDAEL_128, $key, $str, MCRYPT_MODE_ECB );
	}
	
	function decrypt($str, $key) {
		$str = mcrypt_decrypt ( MCRYPT_RIJNDAEL_128, $key, $str, MCRYPT_MODE_ECB );
		$dec_s = strlen ( $str );
		$padding = ord ( $str [$dec_s - 1] );
		return substr ( $str, 0, - $padding );
	}
	
	function _hex2bin($hexdata) {
		$bindata = '';
		for($i = 0; $i < strlen ( $hexdata ); $i += 2) {
			$bindata .= chr ( hexdec ( substr ( $hexdata, $i, 2 ) ) );
		}
		return $bindata;
	}
	
	function save_file($file_data,$save_path,$file_ext){
		$folder_name = date('Ymd');
		if(!is_dir($save_path.$folder_name)){
			mkdir($save_path.$folder_name);
		}
		
		if($file_ext != ''){
			$filename = $folder_name.'/'.time().'.'.$file_ext;
		}else{
			$filename = $folder_name.'/'.time();
		}
		
		$file = fopen($save_path.$filename, "w");
		fwrite($file,$file_data);
		fclose($file);
		
		return $filename;
	}
	
	
	define('EARTH_RADIUS',6378.137); //地球半径
	
	function rad($d)
	{
		return $d * pi() / 180.0;
	}
	
	function get_distance($lat1, $lng1, $lat2, $lng2)
	{
		$radLat1 = rad($lat1);
		$radLat2 = rad($lat2);
		$a = $radLat1 - $radLat2;
		$b = rad($lng1) - rad($lng2);
	
		$s = 2 * asin(sqrt(pow(sin($a/2),2) +
				cos($radLat1) * cos($radLat2) * pow(sin($b/2),2)));
		$s = $s * EARTH_RADIUS;
		$s = round($s * 10000) / 10000;
		return $s;
	}
	
	require_once '../model/Inquiry.class.php';
	$_inquiry = new Inquiry();
	$_inquiry->clear_time_out();
	
	require_once '../common/class_curl.php';
	
	function send_sms($phone,$content){
		$curl = new CURL();
		$data = array('PhoneNum'=>$phone,'CodeNum'=>$content.' [哦了汽配]');
		$result = $curl->vpost('http://211.149.185.240:5555/Default.aspx', $data);
	}

	require_once '../model/Coupon.class.php';
	require_once '../model/UserCoupon.class.php';
	
	//特定时间发放优惠券
	$_coupon = new Coupon();
	$coupon_list = $_coupon->getListByPage(0, $_coupon->getCount());
	$now = date('Y-m-d');
	foreach ($coupon_list as $_coupon_){
		if($_coupon_->get_type == COUPON_GET_TYPE_DATE && $_coupon_->get_date == $now && $_coupon_->status == 1){
			for($i = 0;$i < $_coupon_->get_count;++$i){
				$_user_cou = new User();
				$buy_user_list = $_user_cou->getBuyListByPage(0, $_user_cou->getBuyCount());
				foreach ($buy_user_list as $_buy_user_){
					$_user_coupon = new UserCoupon();
					$_user_coupon->user = $_buy_user_;
					$_user_coupon->coupon = $_coupon_;
					$_user_coupon->save();
				}
			}
		}
	}

		
	//推送模块
	require_once '../jpush/JPush.class.php';
?>