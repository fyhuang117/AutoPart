<?php
require_once '../common/constant.php';
require_once '../common/core.php';

class DbConnection{
	
	private $db = NULL;
	private static $instance = NULL;
	
	private function __construct($host = DATABASE_HOST,$db_name = DATABASE_NAME,$user = DATABASE_USER,$pwd = DATABASE_PASSWORD,$charset = 'UTF8'){
		try {
			//$this->db = new PDO('mysql:host='.$host.';dbname='.$db_name, $user, $pwd, array(PDO::ATTR_PERSISTENT => true));	//长连接
			$this->db = new PDO('mysql:host='.$host.';dbname='.$db_name, $user, $pwd);	//短连接
			$this->db->exec('SET NAMES '.$charset);
		} catch (PDOException $e) {
			_writeLog('db init error:'.$e->getMessage());
			die();
		}
	}
	
	private function __clone() {}
	
	public static function getInstance()
	{
		if(! (self::$instance instanceof self) ) {
			self::$instance = new self();
		}
		return self::$instance;
	}
	
	/**
	 * 查询
	 * @param string $sql
	 * @param array $para_array
	 * @return multitype:
	 */
	public function query($sql,$para_array = array()){
		try{
			$stmt = $this->db->prepare($sql);
			$i = 1;
			foreach ($para_array as $k=>$v){
				$stmt->bindValue($i++,htmlspecialchars($v));
			}
			if($stmt->execute()){
				return $stmt->fetchAll(PDO::FETCH_ASSOC);
			}else{
				foreach ($stmt->errorInfo() as $error){
					_writeLog('db exec error:'.$error);
				}
				return array();
			}
		}catch (PDOException $e) {
			_writeLog('db query error:'.$e->getMessage());
			die();
		}	
	}
	
	/**
	 * 执行
	 * @param string $sql
	 * @param array $para_array
	 * @return boolean
	 */
	public function exec($sql,$para_array){
		try{
			$stmt = $this->db->prepare($sql);
			$i = 1;
			foreach ($para_array as $k=>$v){
				$stmt->bindValue($i++,htmlspecialchars($v));
			}
			if($stmt->execute()){
				return TRUE;
			}else{
				foreach ($stmt->errorInfo() as $error){
					_writeLog('db exec error:'.$error);
				}
				return FALSE;
			}
		}catch (PDOException $e) {
			_writeLog('db exec error:'.$e->getMessage());
			die();
		}
	}
	
	/**
	 * 获得最后一次新增的id
	 * @param string $name
	 * @return number
	 */
	public function getLastInsertId($name){
		try{
			return intval($this->db->lastInsertId($name));
		}catch (PDOException $e) {
			_writeLog('db getlastinsertid error:'.$e->getMessage());
			die();
		}
	}
	
	/**
	 * 事务开始
	 */
	public function beginTransaction(){
		try{
			$this->db->beginTransaction();
		}catch (PDOException $e) {
			_writeLog('db beginTransaction error:'.$e->getMessage());
			die();
		}
	}
	
	/**
	 * 事务提交
	 */
	public function endTransaction(){
		try{
			$this->db->commit();
		}catch (PDOException $e) {
			_writeLog('db endTransaction error:'.$e->getMessage());
			die();
		}
	}
}
?>