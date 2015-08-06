<?php
class CURL {
	public $cookie_file; // 设置Cookie文件保存路径及文件名 
	public $loginurl; //登陆地地址
	public $actionstr; //登陆参数
	

	function random_cookie_file() {
		$this->cookie_file = dirname ( __FILE__ ) . "/cookies/cookie_" . date ( 'YmdGis' ) . floor ( microtime () * 100000000 ) . "_" . mt_rand ( 100, 999 ) . ".txt";
		return $this->cookie_file;
	}
	
	function init($cookie=null) {
		if( $cookie == null ){
			$this->cookie_file = $this->random_cookie_file();
		}else{
			$this->cookie_file = $cookie;
		}
		/*
		if (! file_exists ( $this->cookie_file )) { // 检测Cookie是否存在    
//			str = $this->vget ( 'jroam' ); // 获取登录随机值    
//			preg_match ( "/name=\"formhash\" value=\"(.*?)\"/is", $str, $hash ); // 提取登录随机值   
			$this->vlogin ( $this->loginurl, $this->actionstr ); // 登录获取Cookie    
		}
		*/
	}
	
	function vlogin($url, $data) { // 模拟登录获取Cookie函数
		$curl = curl_init ( $url ); // 启动一个CURL会话
		curl_setopt ( $curl, CURLOPT_URL, $url ); // 要访问的地址                
		curl_setopt ( $curl, CURLOPT_SSL_VERIFYPEER, 0 ); // 对认证证书来源的检查    
		curl_setopt ( $curl, CURLOPT_SSL_VERIFYHOST, 2 ); // 从证书中检查SSL加密算法是否存在    
		if(isset($_SERVER ['HTTP_USER_AGENT'])){
			curl_setopt ( $curl, CURLOPT_USERAGENT, $_SERVER ['HTTP_USER_AGENT'] ); // 模拟用户使用的浏览器    
		}else{
			curl_setopt ( $curl, CURLOPT_USERAGENT, 'Mozilla/5.0 (Windows NT 6.1; rv:12.0) Gecko/20100101 Firefox/12.0' ); // 模拟用户使用的浏览器    
		}    
		curl_setopt ( $curl, CURLOPT_FOLLOWLOCATION, 1 ); // 使用自动跳转    
		curl_setopt ( $curl, CURLOPT_AUTOREFERER, 1 ); // 自动设置Referer    
		curl_setopt ( $curl, CURLOPT_POST, 1 ); // 发送一个常规的Post请求    
		curl_setopt ( $curl, CURLOPT_POSTFIELDS, $data ); // Post提交的数据包    
		//curl_setopt ( $curl, CURLOPT_COOKIEJAR, $this->cookie_file ); // 存放Cookie信息的文件名称    
		//curl_setopt ( $curl, CURLOPT_COOKIEFILE, $this->cookie_file ); // 读取上面所储存的Cookie信息    
		curl_setopt ( $curl, CURLOPT_TIMEOUT, 30 ); // 设置超时限制防止死循环    
		curl_setopt ( $curl, CURLOPT_HEADER, 0 ); // 显示返回的Header区域内容    
		curl_setopt ( $curl, CURLOPT_RETURNTRANSFER, 1 ); // 获取的信息以文件流的形式返回    
		$tmpInfo = curl_exec ( $curl ); // 执行操作    
		if (curl_errno ( $curl )) {
			echo 'Errno' . curl_error ( $curl );
		}
		curl_close ( $curl ); // 关闭CURL会话    
		return $tmpInfo; // 返回数据    
	}
	
	function vget($url,$head = NULL) { // 模拟获取内容函数    
		$curl = curl_init (); // 启动一个CURL会话    
		curl_setopt ( $curl, CURLOPT_URL, $url ); // 要访问的地址                
		curl_setopt ( $curl, CURLOPT_SSL_VERIFYPEER, 1 ); // 对认证证书来源的检查    
		curl_setopt ( $curl, CURLOPT_SSL_VERIFYHOST, 2 ); // 从证书中检查SSL加密算法是否存在    
		//curl_setopt($curl, CURLOPT_CAINFO,'e:/php/wall/wall/cacert.pem');//证书地址
		//curl_setopt($curl, CURLOPT_CAPATH,'e:/php/wall/wall/cacert.pem');//证书地址
		if(isset($_SERVER ['HTTP_USER_AGENT'])){
			curl_setopt ( $curl, CURLOPT_USERAGENT, $_SERVER ['HTTP_USER_AGENT'] ); // 模拟用户使用的浏览器    
		}else{
			curl_setopt ( $curl, CURLOPT_USERAGENT, 'Mozilla/5.0 (Windows NT 6.1; rv:12.0) Gecko/20100101 Firefox/12.0' ); // 模拟用户使用的浏览器    
		}
		curl_setopt ( $curl, CURLOPT_FOLLOWLOCATION, 1 ); // 使用自动跳转    
		//curl_setopt ( $curl, CURLOPT_AUTOREFERER, 1 ); // 自动设置Referer    
		curl_setopt ( $curl, CURLOPT_HTTPGET, 1 ); // 发送一个常规的Post请求    
		curl_setopt ( $curl, CURLOPT_COOKIEJAR, $this->cookie_file ); // 存放Cookie信息的文件名称    
		curl_setopt ( $curl, CURLOPT_COOKIEFILE, $this->cookie_file ); // 读取上面所储存的Cookie信息    
		curl_setopt ( $curl, CURLOPT_TIMEOUT, 300 ); // 设置超时限制防止死循环    
		curl_setopt ( $curl, CURLOPT_HEADER, 0 ); // 显示返回的Header区域内容    
		curl_setopt ( $curl, CURLOPT_RETURNTRANSFER, 1 ); // 获取的信息以文件流的形式返回    
		if(!empty($head)){
			curl_setopt($curl,CURLOPT_HTTPHEADER,$head);
		}
		$tmpInfo = curl_exec ( $curl ); // 执行操作    
		if (curl_errno ( $curl )) {
			_writeLog('Errno' . curl_error ( $curl ));
		}
		curl_close ( $curl ); // 关闭CURL会话    
		return $tmpInfo; // 返回数据    
	}
	
	function vpost($url, $data,$head = NULL) { // 模拟提交数据函数    
		$curl = curl_init (); // 启动一个CURL会话    
		curl_setopt ( $curl, CURLOPT_URL, $url ); // 要访问的地址                
		curl_setopt ( $curl, CURLOPT_SSL_VERIFYPEER, 0 ); // 对认证证书来源的检查    
		curl_setopt ( $curl, CURLOPT_SSL_VERIFYHOST, 2 ); // 从证书中检查SSL加密算法是否存在    
		if(isset($_SERVER ['HTTP_USER_AGENT'])){
			curl_setopt ( $curl, CURLOPT_USERAGENT, $_SERVER ['HTTP_USER_AGENT'] ); // 模拟用户使用的浏览器    
		}else{
			curl_setopt ( $curl, CURLOPT_USERAGENT, 'Mozilla/5.0 (Windows NT 6.1; rv:12.0) Gecko/20100101 Firefox/12.0' ); // 模拟用户使用的浏览器    
		}
		curl_setopt ( $curl, CURLOPT_FOLLOWLOCATION, 1 ); // 使用自动跳转    
		curl_setopt ( $curl, CURLOPT_AUTOREFERER, 1 ); // 自动设置Referer    
		curl_setopt ( $curl, CURLOPT_POST, 1 ); // 发送一个常规的Post请求    
		curl_setopt ( $curl, CURLOPT_POSTFIELDS, $data ); // Post提交的数据包    
		//curl_setopt ( $curl, CURLOPT_COOKIEJAR, $this->cookie_file ); // 存放Cookie信息的文件名称    
		//curl_setopt ( $curl, CURLOPT_COOKIEFILE, $this->cookie_file ); // 读取上面所储存的Cookie信息    
		curl_setopt ( $curl, CURLOPT_TIMEOUT, 300 ); // 设置超时限制防止死循环    
		curl_setopt ( $curl, CURLOPT_HEADER, 0 ); // 显示返回的Header区域内容    
		curl_setopt ( $curl, CURLOPT_RETURNTRANSFER, 1 ); // 获取的信息以文件流的形式返回    
		if(!empty($head)){
			curl_setopt($curl,CURLOPT_HTTPHEADER,$head);
		}
		$tmpInfo = curl_exec ( $curl ); // 执行操作    
		if (curl_errno ( $curl )) {
			echo 'Errno' . curl_error ( $curl );
		}
		curl_close ( $curl ); // 关键CURL会话    
		return $tmpInfo; // 返回数据    
	}
	function delcookie($cookie_file) { // 删除Cookie函数    
		@unlink ( $cookie_file ); // 执行删除    
	}
	
	function curl_download($remote, $local) {
		$curl = curl_init ( $remote );
		$fp = fopen ( $local, "w" );
		
		//curl_setopt($curl, CURLOPT_COOKIE, "AC_DateCookie=2012/3/5 - 2012/3/11");
		curl_setopt ( $curl, CURLOPT_FILE, $fp );
		curl_setopt ( $curl, CURLOPT_COOKIEFILE, $this->cookie_file ); // 读取上面所储存的Cookie信息
		curl_setopt ( $curl, CURLOPT_HEADER, 0 );
		
		curl_exec ( $curl );
		curl_close ( $curl );
		fclose ( $fp );
	}
}

	//判断ip地址
	function verify_ip($ip){
		$curl = new CURL();
		
		$result = $curl->vget('http://www.ip138.com/ips138.asp?ip='.$ip.'&action=2');
		$result = iconv("GB2312","UTF-8",$result);
		if(strpos($result, '福建') && strpos($result, '龙岩')){
			return FALSE;
		}
		return TRUE;
	}