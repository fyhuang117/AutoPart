<?php
require_once '../common/DbConnection.class.php';

class BusinessArea{
	public $id = 0;
	public $name = '';
	public $lat = 0;		//纬度
	public $lng = 0;		//经度
	public $parent_id = 0;
	public $first_word = '';

	public function init(){
		$db = DbConnection::getInstance();

		$sql = "select name,lat,lng,parent_id,first_word from tb_business_area where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if (count($rs) > 0){
			$this->name = $rs[0]['name'];
			$this->lat = floatval($rs[0]['lat']);
			$this->lng = floatval($rs[0]['lng']);
			$this->parent_id = intval($rs[0]['parent_id']);
			$this->first_word = $rs[0]['first_word'];
		}else{
			$this->id = 0;
		}
	}
	
	/**
	 * 获取列表
	 */
	public function getList(){
		$db = DbConnection::getInstance();
	
		$businessArea_list = array();
	
		$sql = "select id,name,lat,lng,parent_id,first_word from tb_business_area order by id";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_businessArea = new BusinessArea();
	
			$_businessArea->id = intval($row['id']);
			$_businessArea->name = $row['name'];
			$_businessArea->lat = floatval($row['lat']);
			$_businessArea->lng = floatval($row['lng']);
			$_businessArea->parent_id = intval($row['parent_id']);
			$_businessArea->first_word = $row['first_word'];
	
			$businessArea_list[] = $_businessArea;
		}
	
		return $businessArea_list;
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();

		$sql = "select count(0) c from tb_business_area where parent_id = 0";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByName(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_business_area where name like ? and parent_id = 0";
		$para_array = array('%'.$this->name.'%');
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$businessArea_list = array();
	
		$sql = "select id,name,lat,lng,parent_id,first_word from tb_business_area where parent_id = 0 order by id limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_businessArea = new BusinessArea();
	
			$_businessArea->id = intval($row['id']);
			$_businessArea->name = $row['name'];
			$_businessArea->lat = floatval($row['lat']);
			$_businessArea->lng = floatval($row['lng']);
			$_businessArea->parent_id = intval($row['parent_id']);
			$_businessArea->first_word = $row['first_word'];
			
			$businessArea_list[] = $_businessArea;
		}
	
		return $businessArea_list;
	}
	
	public function getListByParent(){
		$db = DbConnection::getInstance();
	
		$businessArea_list = array();
	
		$sql = "select id,name,lat,lng,parent_id,first_word from tb_business_area where parent_id = ? order by id";
		$para_array = array($this->parent_id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_businessArea = new BusinessArea();
	
			$_businessArea->id = intval($row['id']);
			$_businessArea->name = $row['name'];
			$_businessArea->lat = floatval($row['lat']);
			$_businessArea->lng = floatval($row['lng']);
			$_businessArea->parent_id = intval($row['parent_id']);
			$_businessArea->first_word = $row['first_word'];
				
			$businessArea_list[] = $_businessArea;
		}
	
		return $businessArea_list;
	}
	
	public function getListByPageAndName($start,$limit){
		$db = DbConnection::getInstance();
	
		$businessArea_list = array();
	
		$sql = "select id,name,lat,lng,parent_id,first_word from tb_business_area where name like ? and parent_id = 0 order by id limit $start,$limit";
		$para_array = array('%'.$this->name.'%');
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_businessArea = new BusinessArea();
	
			$_businessArea->id = intval($row['id']);
			$_businessArea->name = $row['name'];
			$_businessArea->lat = floatval($row['lat']);
			$_businessArea->lng = floatval($row['lng']);
			$_businessArea->parent_id = intval($row['parent_id']);
			$_businessArea->first_word = $row['first_word'];
	
			$businessArea_list[] = $_businessArea;
		}
	
		return $businessArea_list;
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_business_area(name,lat,lng,parent_id,first_word) values(?,?,?,?,?)";
		$para_array = array($this->name,$this->lat,$this->lng,$this->parent_id,$this->first_word);
		
		$db->exec($sql, $para_array);
		
		$this->id = $db->getLastInsertId('id');
	}
	
	public function update(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_business_area set name = ?,lat = ?,lng = ?,parent_id = ?,first_word = ? where id = ?";
		$para_array = array($this->name,$this->lat,$this->lng,$this->parent_id,$this->first_word,$this->id);
	
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
		
		$sql = "delete from tb_business_area where id = ?";
		$para_array = array($this->id);
		
		$db->exec($sql, $para_array);
	}
}
?>