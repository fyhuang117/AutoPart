<?php
require_once '../common/constant.php';

class EasyMob{
	/**
	 * 开放注册模式
	 *
	 * @param $options['username'] 用户名        	
	 * @param $options['password'] 密码        	
	 */
	public function openRegister($options) {
		$url = EASYMOB_URL . "users";
		$result = $this->postCurl ( $url, $options, $head = 0 );
		return $result;
	}
	
	/**
	 * 获取Token
	 */
	public function getToken() {
		$option ['grant_type'] = "client_credentials";
		$option ['client_id'] = EASYMOB_CLIENT_ID;
		$option ['client_secret'] = EASYMOB_SECRET_KEY;
		$url = EASYMOB_URL . "token";
		$fp = @fopen ( "easemob.txt", 'r' );
		if ($fp) {
			$arr = unserialize ( fgets ( $fp ) );
			if ($arr ['expires_in'] < time ()) {
				$result = $this->postCurl ( $url, $option, $head = 0 );
				$result ['expires_in'] = $result ['expires_in'] + time ();
				@fwrite ( $fp, serialize ( $result ) );
				fclose ( $fp );
				return $result ['access_token'];
			}
			fclose ( $fp );
			return $arr ['access_token'];
		}
		$result = $this->postCurl ( $url, $option, $head = 0 );
		$result = json_decode($result);
		$result ['expires_in'] = $result ['expires_in'] + time ();
		$fp = @fopen ( "easemob.txt", 'w' );
		@fwrite ( $fp, serialize ( $result ) );
		fclose ( $fp );
		return $result ['access_token'];
	}
	
	/**
	 * CURL Post
	 */
	private function postCurl($url, $option, $header = 0, $type = 'POST') {
		$curl = curl_init (); // 启动一个CURL会话
		curl_setopt ( $curl, CURLOPT_URL, $url ); // 要访问的地址
		curl_setopt ( $curl, CURLOPT_SSL_VERIFYPEER, FALSE ); // 对认证证书来源的检查
		curl_setopt ( $curl, CURLOPT_SSL_VERIFYHOST, FALSE ); // 从证书中检查SSL加密算法是否存在
		curl_setopt ( $curl, CURLOPT_USERAGENT, 'Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)' ); // 模拟用户使用的浏览器
		if (! empty ( $option )) {
			$options = json_encode ( $option );
			curl_setopt ( $curl, CURLOPT_POSTFIELDS, $options ); // Post提交的数据包
		}
		curl_setopt ( $curl, CURLOPT_TIMEOUT, 30 ); // 设置超时限制防止死循环
		curl_setopt ( $curl, CURLOPT_HTTPHEADER, $header ); // 设置HTTP头
		curl_setopt ( $curl, CURLOPT_RETURNTRANSFER, 1 ); // 获取的信息以文件流的形式返回
		curl_setopt ( $curl, CURLOPT_CUSTOMREQUEST, $type );
		$result = curl_exec ( $curl ); // 执行操作
		//$res = object_array ( json_decode ( $result ) );
		//$res ['status'] = curl_getinfo ( $curl, CURLINFO_HTTP_CODE );
		//pre ( $res );
		curl_close ( $curl ); // 关闭CURL会话
		return $result;
	}
}
?>