<?php
require_once '../common/DbConnection.class.php';

class SystemMessage{
	public $id = 0;
	public $target_type = 0;
	public $title = '';
	public $content = '';
	public $send_time = '';

	/**
	 * 获取列表
	 */
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$message_list = array();
	
		$sql = "select id,target_type,title,content,send_time from tb_system_message order by id desc limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_message = new SystemMessage();
			$_message->id = intval($row['id']);
			$_message->target_type = intval($row['target_type']);
			$_message->title = $row['title'];
			$_message->content = $row['content'];
			$_message->send_time = $row['send_time'];
			
			$message_list[] = $_message;
		}
	
		return $message_list;
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_system_message";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function save(){
		$db = DbConnection::getInstance();
	
		$sql = "insert into tb_system_message(target_type,title,content,send_time) values(?,?,?,now())";
		$para_array = array($this->target_type,$this->title,$this->content);
	
		$db->exec($sql, $para_array);
	}
}
?>