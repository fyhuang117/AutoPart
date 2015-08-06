<?php
require_once '../common/DbConnection.class.php';
require_once '../model/BandSub.class.php';

class Line{
	public $id = 0;
	public $band_sub = NULL;
	public $nam = '';
	
	public function init(){
		$db = DbConnection::getInstance();
	
		$sql = "select band_sub_id,nam from tb_line where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if (count($rs) > 0){
				
			$_band_sub = new BandSub();
			$_band_sub->id = intval($rs[0]['band_sub_id']);
			$_band_sub->init();
			$this->band_sub = $_band_sub;
				
			$this->nam = $rs[0]['nam'];
		}else{
			$this->id = 0;
			$this->band_sub = new BandSub();
		}
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
		
		$sql = "select count(0) c from tb_line";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByNam(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_line where nam like ?";
		$para_array = array('%'.$this->nam.'%');
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$line_list = array();
	
		$sql = "select id,band_sub_id,nam from tb_line order by nam limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_line = new Line();
	
			$_line->id = intval($row['id']);
			
			$_band_sub = new BandSub();
			$_band_sub->id = intval($row['band_sub_id']);
			$_band_sub->init();
			$_line->band_sub = $_band_sub;
			
			$_line->nam = $row['nam'];
	
			$line_list[] = $_line;
		}
	
		return $line_list;
	}
	
	public function getListByPageAndNam($start,$limit){
		$db = DbConnection::getInstance();
	
		$line_list = array();
	
		$sql = "select id,band_sub_id,nam from tb_line where nam like ? order by nam limit $start,$limit";
		$para_array = array('%'.$this->nam.'%');
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_line = new Line();
	
			$_line->id = intval($row['id']);
				
			$_band_sub = new BandSub();
			$_band_sub->id = intval($row['band_sub_id']);
			$_band_sub->init();
			$_line->band_sub = $_band_sub;
				
			$_line->nam = $row['nam'];
	
			$line_list[] = $_line;
		}
	
		return $line_list;
	}
	
	/**
	 * 根据品牌获取列表
	 */
	public function getListByBand(){
		$db = DbConnection::getInstance();
	
		$line_list = array();
	
		$sql = "select id,nam from tb_line where band_sub_id = ? order by nam";
		$para_array = array($this->band_sub->id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_line = new Line();
				
			$_line->id = intval($row['id']);
			$_line->band_sub = $this->band_sub;
			$_line->nam = $row['nam'];
	
			$line_list[] = $_line;
		}
	
		return $line_list;
	}
	
	public function getList(){
		$db = DbConnection::getInstance();
	
		$line_list = array();
	
		$sql = "select id,nam from tb_line order by nam";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_line = new Line();
	
			$_line->id = intval($row['id']);
			$_line->nam = $row['nam'];
	
			$line_list[] = $_line;
		}
	
		return $line_list;
	}
	
	public function save(){
		$db = DbConnection::getInstance();

		$sql = "insert into tb_line(band_sub_id,nam) values(?,?)";
		$para_array = array($this->band_sub->id,$this->nam);
	
		$db->exec($sql, $para_array);
	}
	
	public function update(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_line set band_sub_id = ?,nam = ? where id = ?";
		$para_array = array($this->band_sub->id,$this->nam,$this->id);
	
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
	
		$sql = "delete from tb_line where id = ?";
		$para_array = array($this->id);
	
		$db->exec($sql, $para_array);
	}
}
?>