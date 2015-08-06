<?php
require_once '../common/DbConnection.class.php';

class System{
	public $app_version = '';
	public $service_file = '';
	public $service_tel = '';
	
	public function init(){
		$db = DbConnection::getInstance();
	
		$sql = "select app_version,service_file,service_tel from tb_system";
		$rs = $db->query($sql);
		if (count($rs) > 0){
		
			$this->app_version = $rs[0]['app_version'];
			$this->service_file = $rs[0]['service_file'];
			$this->service_tel = $rs[0]['service_tel'];
		}
	}
	
	public function update(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_system set app_version = ?,service_file = ?,service_tel = ?";
		$para_array = array($this->app_version,$this->service_file,$this->service_tel);
		$db->exec($sql, $para_array);
	}
}

?>