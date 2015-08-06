<?php
require_once '../common/DbConnection.class.php';
require_once '../model/Order.class.php';

class Back{
	public $id = 0;
	public $order = NULL;
	public $request_time = '';
	public $status = 0;

	public function init(){
		$db = DbConnection::getInstance();

		$sql = "select id,order_id,request_time,status from tb_back where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if(count($rs) > 0){
			$_order = new Order();
			$_order->id = intval($rs[0]['order_id']);
			$_order->init();
			$this->order = $_order;
	
			$this->request_time = $rs[0]['request_time'];
			$this->status = intval($rs[0]['status']);
		}else{
			$this->id = 0;
			$this->order = new Order();
		}
	}
	
	/**
	 * 获取列表
	 */
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$back_list = array();
	
		$sql = "select id,order_id,request_time,status from tb_back order by id desc limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_back = new Back();
			$_back->id = intval($row['id']);
	
			$_order = new Order();
			$_order->id = intval($row['order_id']);
			$_order->init();
			$_back->order = $_order;

			$_back->request_time = $row['request_time'];
			$_back->status = intval($row['status']);
				
			$back_list[] = $_back;
		}
	
		return $back_list;
	}
	
	/**
	 * 获取列表
	 */
	public function getListByPageAndKey($order_num,$start,$limit){
		$db = DbConnection::getInstance();
	
		$back_list = array();
	
		$sql = "select b.id,b.order_id,b.request_time,b.status from tb_back b inner join tb_order o on b.order_id = o.id where o.num like ? order by b.id desc limit $start,$limit";
		$para_array = array('%'.$order_num.'%');
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_back = new Back();
			$_back->id = intval($row['id']);
	
			$_order = new Order();
			$_order->id = intval($row['order_id']);
			$_order->init();
			$_back->order = $_order;
	
			$_back->request_time = $row['request_time'];
			$_back->status = intval($row['status']);
	
			$back_list[] = $_back;
		}
	
		return $back_list;
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_back";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByKey($order_num){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_back b inner join tb_order o on b.order_id = o.id where o.num like ?";
		$para_array = array('%'.$order_num.'%');
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_back(order_id,request_time,status) values(?,now(),?)";
		$para_array = array($this->order->id,0);
		
		$db->exec($sql, $para_array);
	}
	
	public function success(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_back set status = ? where id = ?";
		$para_array = array(1,$this->id);
		
		$db->exec($sql, $para_array);
	}
}
?>