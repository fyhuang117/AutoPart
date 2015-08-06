<?php
require_once '../common/DbConnection.class.php';
require_once '../common/class_curl.php';

class JPush{
	
	private $curl = NULL;
	
	private $register_url = 'https://device.jpush.cn/v3/devices/';
	private $push_url = 'https://api.jpush.cn/v3/push';
	
	private $sell_appKey = '19371b8c45091341f23ac032';
	private $sell_masterSecret = '3e178a3b5fc02de23d8abf1f';
	
	private $buy_appKey = 'd0b734bdb1eff6e7fdf501ec';
	private $buy_masterSecret = '80400ab8a3e8c2d3ffa104c0';
	
	function __construct(){
		$this->curl = new CURL();
	}
	
	/**
	 * 注册用户别名
	 */
	function register_alias($device_id,$alias){
		$obj = new stdClass();
		$obj->tags = '';
		$obj->alias = $alias;
		
		$result = $this->curl->vpost($this->register_url.$device_id, json_encode($obj),$this->head);
		_writeLog('register_alisa_device:',$device_id);
		_writeLog('register_alisa_result:',$result);
	}
	
	/**
	 * 推送消息
	 */
	function sell_push($alias,$message){
		$obj = new stdClass();
		$obj->platform = 'all';
		
		$obj->audience = new stdClass();
		$obj->audience->alias = array();
		$obj->audience->alias[] = $alias;
		
		$obj->notification = new stdClass();
		
		$obj->notification->android = new stdClass();
		$obj->notification->android->alert = $message;
		
		$obj->notification->ios = new stdClass();
		$obj->notification->ios->alert = $message;
		$obj->notification->ios->sound = 'sound.caf';
		
		$head = array("Authorization: Basic ".base64_encode($this->sell_appKey.':'.$this->sell_masterSecret),
						"Accept: application/json");
		
		$result = $this->curl->vpost($this->push_url, json_encode($obj),$head);
		_writeLog('push_obj:'.json_encode($obj));
		_writeLog('push_alias:'.$alias);
		_writeLog('push_message:'.$message);
		_writeLog('push_result:'.$result);
		
	}
	
	/**
	 * 推送消息给全部卖家
	 */
	function sell_push_all($message){
		$obj = new stdClass();
		$obj->platform = 'all';
		$obj->audience = 'all';
	
		$obj->notification = new stdClass();
	
		$obj->notification->android = new stdClass();
		$obj->notification->android->alert = $message;
	
		$obj->notification->ios = new stdClass();
		$obj->notification->ios->alert = $message;
		$obj->notification->ios->sound = 'sound.caf';
	
		$head = array("Authorization: Basic ".base64_encode($this->sell_appKey.':'.$this->sell_masterSecret),
							"Accept: application/json");
	
		$result = $this->curl->vpost($this->push_url, json_encode($obj),$head);
		_writeLog('push_obj:'.json_encode($obj));
		_writeLog('push_alias: all');
		_writeLog('push_message:'.$message);
		_writeLog('push_result:'.$result);
	
	}
	
	/**
	 * 推送消息
	 */
	function buy_push($alias,$message){
		$obj = new stdClass();
		$obj->platform = 'all';
	
		$obj->audience = new stdClass();
		$obj->audience->alias = array();
		$obj->audience->alias[] = $alias;
	
		$obj->notification = new stdClass();
	
		$obj->notification->android = new stdClass();
		$obj->notification->android->alert = $message;
	
		$obj->notification->ios = new stdClass();
		$obj->notification->ios->alert = $message;
		$obj->notification->ios->sound = 'sound.caf';
	
		$head = array("Authorization: Basic ".base64_encode($this->buy_appKey.':'.$this->buy_masterSecret),
						"Accept: application/json");
		
		$result = $this->curl->vpost($this->push_url, json_encode($obj),$head);
		_writeLog('push_obj:'.json_encode($obj));
		_writeLog('push_alias:'.$alias);
		_writeLog('push_message:'.$message);
		_writeLog('push_result:'.$result);
		
	}
	
	/**
	 * 推送消息给全部买家
	 */
	function buy_push_all($message){
		$obj = new stdClass();
		$obj->platform = 'all';
		$obj->audience = 'all';
	
		$obj->notification = new stdClass();
	
		$obj->notification->android = new stdClass();
		$obj->notification->android->alert = $message;
	
		$obj->notification->ios = new stdClass();
		$obj->notification->ios->alert = $message;
		$obj->notification->ios->sound = 'sound.caf';
	
		$head = array("Authorization: Basic ".base64_encode($this->buy_appKey.':'.$this->buy_masterSecret),
							"Accept: application/json");
	
		$result = $this->curl->vpost($this->push_url, json_encode($obj),$head);
		_writeLog('push_obj:'.json_encode($obj));
		_writeLog('push_alias: all');
		_writeLog('push_message:'.$message);
		_writeLog('push_result:'.$result);
	
	}
}
?>