<?php
require_once '../common/DbConnection.class.php';
require_once '../model/Band.class.php';

class BandSub{
	public $id = 0;
	public $band = NULL;
	public $name = '';
	
	public function init(){
		$db = DbConnection::getInstance();
	
		$sql = "select band_id,name from tb_band_sub where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if (count($rs) > 0){
				
			$_band = new Band();
			$_band->id = intval($rs[0]['band_id']);
			$_band->init();
			$this->band = $_band;
				
			$this->name = $rs[0]['name'];
		}else{
			$this->id = 0;
			$this->band = new Band();
		}
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
		
		$sql = "select count(0) c from tb_band_sub";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByNam(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_band_sub where name like ?";
		$para_array = array('%'.$this->name.'%');
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$band_sub_list = array();
	
		$sql = "select id,band_id,name from tb_band_sub order by name limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_band_sud = new BandSub();
	
			$_band_sud->id = intval($row['id']);
			
			$_band = new Band();
			$_band->id = intval($row['band_id']);
			$_band->init();
			$_band_sud->band = $_band;
			
			$_band_sud->name = $row['name'];
	
			$band_sub_list[] = $_band_sud;
		}
	
		return $band_sub_list;
	}
	
	public function getListByPageAndNam($start,$limit){
		$db = DbConnection::getInstance();
	
		$band_sub_list = array();
	
		$sql = "select id,band_id,name from tb_band_sub where name like ? order by name limit $start,$limit";
		$para_array = array('%'.$this->name.'%');
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_band_sud = new BandSub();
	
			$_band_sud->id = intval($row['id']);
			
			$_band = new Band();
			$_band->id = intval($row['band_id']);
			$_band->init();
			$_band_sud->band = $_band;
			
			$_band_sud->name = $row['name'];
	
			$band_sub_list[] = $_band_sud;
		}
	
		return $band_sub_list;
	}
	
	/**
	 * 根据品牌获取列表
	 */
	public function getListByBand(){
		$db = DbConnection::getInstance();
	
		$band_sub_list = array();
	
		$sql = "select id,name from tb_band_sub where band_id = ? order by name";
		$para_array = array($this->band->id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_band_sud = new BandSub();
				
			$_band_sud->id = intval($row['id']);
			$_band_sud->band = $this->band;
			$_band_sud->name = $row['name'];
	
			$band_sub_list[] = $_band_sud;
		}
	
		return $band_sub_list;
	}
	
	public function getList(){
		$db = DbConnection::getInstance();
	
		$band_sub_list = array();
	
		$sql = "select id,name from tb_band_sub order by name";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_band_sud = new BandSub();
	
			$_band_sud->id = intval($row['id']);
			$_band_sud->name = $row['name'];
	
			$band_sub_list[] = $_band_sud;
		}
	
		return $band_sub_list;
	}
	
	public function save(){
		$db = DbConnection::getInstance();

		$sql = "insert into tb_band_sub(band_id,name) values(?,?)";
		$para_array = array($this->band->id,$this->name);
	
		$db->exec($sql, $para_array);
	}
	
	public function update(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_band_sub set band_id = ?,name = ? where id = ?";
		$para_array = array($this->band->id,$this->name,$this->id);
	
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
	
		$sql = "delete from tb_band_sub where id = ?";
		$para_array = array($this->id);
	
		$db->exec($sql, $para_array);
	}
}
?>