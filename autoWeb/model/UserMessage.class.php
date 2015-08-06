<?php
require_once '../common/DbConnection.class.php';
require_once '../model/User.class.php';

class UserMessage{
	public $id = 0;
	public $user = NULL;
	public $title = '';
	public $content = '';
	public $send_time = '';
	public $read_status = 0;
	
	public function init(){
		$db = DbConnection::getInstance();
	
		$sql = "select id,user_id,title,content,send_time,read_status from tb_user_message where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if(count($rs) > 0){
			$_user = new User();
			$_user->id = intval($rs[0]['user_id']);
			$_user->init();
			$this->user = $_user;
			
			$this->title = $rs[0]['title'];
			$this->content = $rs[0]['content'];
			$this->send_time = $rs[0]['send_time'];
			$this->read_status = intval($rs[0]['read_status']);
		}else{
			$this->id = 0;
			$this->user = new User();
		}
	}
	
	/**
	 * 获取列表
	 */
	public function getListByUser(){
		$db = DbConnection::getInstance();
	
		$message_list = array();
	
		$sql = "select id,title,content,send_time,read_status from tb_user_message where user_id = ? order by id desc limit 50";
		$para_array = array($this->user->id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_user_message = new UserMessage();
			$_user_message->id = intval($row['id']);
			$_user_message->title = $row['title'];
			$_user_message->content = $row['content'];
			$_user_message->send_time = $row['send_time'];
			$_user_message->read_status = intval($row['read_status']);
			
			$message_list[] = $_user_message;
		}
	
		return $message_list;
	}
	
	/**
	 * 查询未读数量
	 */
	public function getNoReadCountByUser(){
		$db = DbConnection::getInstance();

		$sql = "select count(0) c from tb_user_message where user_id = ? and read_status = ?";
		$para_array = array($this->user->id,0);
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_user_message(user_id,title,content,send_time,read_status) values(?,?,?,now(),0)";
		$para_array = array($this->user->id,$this->title,$this->content);
		$db->exec($sql, $para_array);
	}
	
	/**
	 * 删除
	 */
	public function read(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_user_message set read_status = ? where id = ?";
		$para_array = array(1,$this->id);
		$db->exec($sql, $para_array);
	}
	
	/**
	 * 删除
	 */
	public function del(){
		$db = DbConnection::getInstance();
		
		$sql = "delete from tb_user_message where id = ?";
		$para_array = array($this->id);
		$db->exec($sql, $para_array);
	}
}
?>