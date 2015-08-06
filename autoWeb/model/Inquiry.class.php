<?php
require_once '../common/DbConnection.class.php';
require_once '../model/User.class.php';
require_once '../model/PartType.class.php';
require_once '../model/Car.class.php';
require_once '../model/BusinessArea.class.php';

class Inquiry{
	public $id = 0;
	public $user = NULL;
	public $parttype = NULL;
	public $partband_id = '';
	public $car = NULL;
	public $c = 0;
	public $pic1 = '';
	public $pic2 = '';
	public $pic3 = '';
	public $mes = '';
	public $aum = '';
	public $create_time = '';
	public $end_time = '';
	public $status = 0;
	public $parttype_quality = 0;
	public $area = NULL;
	
	public function init(){
		$db = DbConnection::getInstance();
		
		$inquiry_list = array();
		
		$sql = "select id,user_id,parttype_id,partband_id,car_id,c,pic1,pic2,pic3,mes,aum,create_time,end_time,status,parttype_quality,area_id from tb_inquiry where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if(count($rs) > 0){
			
			$_user = new User();
			$_user->id = intval($rs[0]['user_id']);
			$_user->init();
			$this->user = $_user;
				
			$_partType = new PartType();
			$_partType->id = $rs[0]['parttype_id'];
			$_partType->init();
			$this->parttype = $_partType;
				
			$this->partband_id = $rs[0]['partband_id'];
				
			$_car = new Car();
			$_car->id = intval($rs[0]['car_id']);
			$_car->init();
			$this->car = $_car;
				
			$this->c = intval($rs[0]['c']);
			$this->pic1 = $rs[0]['pic1'];
			$this->pic2 = $rs[0]['pic2'];
			$this->pic3 = $rs[0]['pic3'];
			$this->mes = $rs[0]['mes'];
			$this->aum = $rs[0]['aum'];
			$this->create_time = $rs[0]['create_time'];
			$this->end_time = $rs[0]['end_time'];
			$this->status = intval($rs[0]['status']);
			$this->parttype_quality = intval($rs[0]['parttype_quality']);
			
			$_area = new BusinessArea();
			$_area->id = intval($rs[0]['area_id']);
			$_area->init();
			$this->area = $_area;
		
		}else{
			$this->id = 0;
			$this->user = new User();
			$this->parttype = new PartType();
			$this->car = new Car();
			$this->area = new BusinessArea();
		}
	}
	
	/**
	 * 根据用户获取数量
	 * @return number
	 */
	public function getCountByUser(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_inquiry where user_id = ? and status = ?";
		$para_array = array($this->user->id,INQUIRY_STATUS_NORMAL);
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
		
		$sql = "select count(0) c from tb_inquiry";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}

	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$inquiry_list = array();
	
		$sql = "select id,user_id,parttype_id,partband_id,car_id,c,pic1,pic2,pic3,mes,aum,create_time,end_time,status,parttype_quality,area_id from tb_inquiry order by id desc limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_inquiry = new Inquiry();
	
			$_inquiry->id = intval($row['id']);
			
			$_user = new User();
			$_user->id = intval($row['user_id']);
			$_user->init();
			$_inquiry->user = $_user;
				
			$_partType = new PartType();
			$_partType->id = $row['parttype_id'];
			$_partType->init();
			$_inquiry->parttype = $_partType;
				
			$_inquiry->partband_id = $row['partband_id'];
				
			$_car = new Car();
			$_car->id = intval($row['car_id']);
			$_car->init();
			$_inquiry->car = $_car;
				
			$_inquiry->c = intval($row['c']);
			$_inquiry->pic1 = $row['pic1'];
			$_inquiry->pic2 = $row['pic2'];
			$_inquiry->pic3 = $row['pic3'];
			$_inquiry->mes = $row['mes'];
			$_inquiry->aum = $row['aum'];
			$_inquiry->create_time = $row['create_time'];
			$_inquiry->end_time = $row['end_time'];
			$_inquiry->status = intval($row['status']);
			
			$_inquiry->parttype_quality = intval($row['parttype_quality']);
			
			$_area = new BusinessArea();
			$_area->id = intval($row['area_id']);
			$_area->init();
			$_inquiry->area = $_area;
			
			
			$inquiry_list[] = $_inquiry;
		}
	
		return $inquiry_list;
	}
	
	public function getListBySellAndPageAndStatus($sell_user_id,$start,$limit,$car_id = 0,$part_type = 0,$part_band = 0){
		$db = DbConnection::getInstance();
	
		$inquiry_list = array();
	
		$sql = "select id,user_id,parttype_id,partband_id,car_id,c,pic1,pic2,pic3,mes,aum,create_time,end_time,status,parttype_quality,area_id from tb_inquiry 
				where status = ? and id not in (select inquiry_id from tb_bidding where user_id = ?) ";
		if($car_id > 0){
			$sql .= "and car_id = $car_id ";
		}
		if($part_type > 0){
			$sql .= "and parttype_id = '$part_type' ";
		}
		if($part_band > 0){
			$sql .= "and partband_id like '%,$part_band,%' ";
		}
		$sql .= "order by id desc limit $start,$limit";
		$para_array = array($this->status,$sell_user_id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_inquiry = new Inquiry();
	
			$_inquiry->id = intval($row['id']);
	
			$_user = new User();
			$_user->id = intval($row['user_id']);
			$_user->init();
			$_inquiry->user = $_user;
	
			$_partType = new PartType();
			$_partType->id = $row['parttype_id'];
			$_partType->init();
			$_inquiry->parttype = $_partType;
	
			$_inquiry->partband_id = $row['partband_id'];
	
			$_car = new Car();
			$_car->id = intval($row['car_id']);
			$_car->init();
			$_inquiry->car = $_car;
	
			$_inquiry->c = intval($row['c']);
			$_inquiry->pic1 = $row['pic1'];
			$_inquiry->pic2 = $row['pic2'];
			$_inquiry->pic3 = $row['pic3'];
			$_inquiry->mes = $row['mes'];
			$_inquiry->aum = $row['aum'];
			$_inquiry->create_time = $row['create_time'];
			$_inquiry->end_time = $row['end_time'];
			$_inquiry->status = intval($row['status']);
	
			$_inquiry->parttype_quality = intval($row['parttype_quality']);
	
			$_area = new BusinessArea();
			$_area->id = intval($row['area_id']);
			$_area->init();
			$_inquiry->area = $_area;
	
	
			$inquiry_list[] = $_inquiry;
		}
	
		return $inquiry_list;
	}
	
	public function getListByPageAndStatus($start,$limit,$car_id = 0,$part_type = 0,$part_band = 0){
		$db = DbConnection::getInstance();
	
		$inquiry_list = array();
	
		$sql = "select id,user_id,parttype_id,partband_id,car_id,c,pic1,pic2,pic3,mes,aum,create_time,end_time,status,parttype_quality,area_id 
				from tb_inquiry where status = ? ";
		if($car_id > 0){
			$sql .= "and car_id = $car_id ";
		}
		if($part_type > 0){
			$sql .= "and parttype_id = '$part_type' ";
		}
		if($part_band > 0){
			$sql .= "and partband_id like '%,$part_band,%' ";
		}
		$sql .= "order by id desc limit $start,$limit";
		$para_array = array($this->status);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_inquiry = new Inquiry();
	
			$_inquiry->id = intval($row['id']);
				
			$_user = new User();
			$_user->id = intval($row['user_id']);
			$_user->init();
			$_inquiry->user = $_user;
	
			$_partType = new PartType();
			$_partType->id = $row['parttype_id'];
			$_partType->init();
			$_inquiry->parttype = $_partType;
	
			$_inquiry->partband_id = $row['partband_id'];
	
			$_car = new Car();
			$_car->id = intval($row['car_id']);
			$_car->init();
			$_inquiry->car = $_car;
	
			$_inquiry->c = intval($row['c']);
			$_inquiry->pic1 = $row['pic1'];
			$_inquiry->pic2 = $row['pic2'];
			$_inquiry->pic3 = $row['pic3'];
			$_inquiry->mes = $row['mes'];
			$_inquiry->aum = $row['aum'];
			$_inquiry->create_time = $row['create_time'];
			$_inquiry->end_time = $row['end_time'];
			$_inquiry->status = intval($row['status']);
				
			$_inquiry->parttype_quality = intval($row['parttype_quality']);
				
			$_area = new BusinessArea();
			$_area->id = intval($row['area_id']);
			$_area->init();
			$_inquiry->area = $_area;
				
				
			$inquiry_list[] = $_inquiry;
		}
	
		return $inquiry_list;
	}
	
	/**
	 * 根据用户获取列表
	 */
	public function getListByUser(){
		$db = DbConnection::getInstance();
	
		$inquiry_list = array();
	
		$sql = "select id,user_id,parttype_id,partband_id,car_id,c,pic1,pic2,pic3,mes,aum,create_time,end_time,status,parttype_quality,area_id from tb_inquiry where user_id = ? and status = ? order by id desc";
		$para_array = array($this->user->id,INQUIRY_STATUS_NORMAL);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_inquiry = new Inquiry();
				
			$_inquiry->id = intval($row['id']);
			$_inquiry->user = $this->user;
			
			$_partType = new PartType();
			$_partType->id = $row['parttype_id'];
			$_partType->init();
			$_inquiry->parttype = $_partType;
			
			$_inquiry->partband_id = $row['partband_id'];
			
			$_car = new Car();
			$_car->id = intval($row['car_id']);
			$_car->init();
			$_inquiry->car = $_car;
			
			$_inquiry->c = intval($row['c']);
			$_inquiry->pic1 = $row['pic1'];
			$_inquiry->pic2 = $row['pic2'];
			$_inquiry->pic3 = $row['pic3'];
			$_inquiry->mes = $row['mes'];
			$_inquiry->aum = $row['aum'];
			$_inquiry->create_time = $row['create_time'];
			$_inquiry->end_time = $row['end_time'];
			$_inquiry->status = intval($row['status']);
	
			$_inquiry->parttype_quality = intval($row['parttype_quality']);
				
			$_area = new BusinessArea();
			$_area->id = intval($row['area_id']);
			$_area->init();
			$_inquiry->area = $_area;
			
			$inquiry_list[] = $_inquiry;
		}
	
		return $inquiry_list;
	}
	
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_inquiry(user_id,parttype_id,partband_id,car_id,c,pic1,pic2,pic3,mes,aum,create_time,end_time,status,parttype_quality,area_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		$create_time = date('Y-m-d H:i:s',time());
		$end_time = date('Y-m-d H:i:s',time() + 60 * 60 * 3);
		$para_array = array($this->user->id,$this->parttype->id,$this->partband_id,$this->car->id,$this->c,$this->pic1,$this->pic2,$this->pic3,$this->mes,$this->aum,$create_time,$end_time,INQUIRY_STATUS_NORMAL,$this->parttype_quality,$this->area->id);
		$db->exec($sql, $para_array);
	}
	
	public function update(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_inquiry set parttype_id = ?,partband_id = ?,car_id = ?,c = ?,pic1 = ?,pic2 = ?,pic3 = ?,mes = ?,aum = ?,parttype_quality = ?,area_id = ? where id = ? and status = ?";
		$para_array = array($this->parttype->id,$this->partband_id,$this->car->id,$this->c,$this->pic1,$this->pic2,$this->pic3,$this->mes,$this->aum,$this->parttype_quality,$this->area->id,$this->id,INQUIRY_STATUS_NORMAL);
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_inquiry set status = ? where id = ? and status = ?";
		$para_array = array(INQUIRY_STATUS_DEL,$this->id,INQUIRY_STATUS_NORMAL);
		$db->exec($sql, $para_array);
	}
	
	public function complete(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_inquiry set status = ? where id = ? and status = ?";
		$para_array = array(INQUIRY_STATUS_COMPLETE,$this->id,INQUIRY_STATUS_NORMAL);
		$db->exec($sql, $para_array);
	}
	
	public function clear_time_out(){
		$db = DbConnection::getInstance();
		
		/*
		$sql = "update tb_inquiry set status = ? where end_time < ? and status = ?";
		$para_array = array(INQUIRY_STATUS_DEL,date('Y-m-d H:i:s',time()),INQUIRY_STATUS_NORMAL);
		$db->exec($sql, $para_array);
		*/
		
		$now_time = date('Y-m-d H:i:s',time());
		
		$sql = "select id from tb_inquiry where end_time < ? and status = ?";
		$para_array = array($now_time,INQUIRY_STATUS_NORMAL);
		$rs = $db->query($sql, $para_array);
		
		foreach($rs as $row){
			$inquiry_id = intval($row['id']);
			$sql = "delete from tb_bidding where inquiry_id = ?";
			$para_array = array($inquiry_id);
			$db->exec($sql, $para_array);
		}
		
		$sql = "delete from tb_inquiry where end_time < ? and status = ?";
		$para_array = array($now_time,INQUIRY_STATUS_NORMAL);
		$db->exec($sql, $para_array);
	}
}
?>