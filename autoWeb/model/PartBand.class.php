<?php
require_once '../common/DbConnection.class.php';

class PartBand{
	public $id = 0;
	public $nam = '';
	public $mad = '';
	public $pic = '';

	public function init(){
		$db = DbConnection::getInstance();

		$sql = "select nam,mad,pic from tb_partband where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if (count($rs) > 0){
			
			$this->nam = $rs[0]['nam'];
			
			$this->mad = $rs[0]['mad'];
			$this->pic = $rs[0]['pic'];
		}else{
			$this->id = 0;
		}
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
		
		$sql = "select count(0) c from tb_partband";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByNam(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_partband where nam like ?";
		$para_array = array('%'.$this->nam.'%');
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getList(){
		$db = DbConnection::getInstance();
	
		$partBand_list = array();
	
		$sql = "select id,nam,mad,pic from tb_partband order by id desc";
		$rs = $db->query($sql);
		foreach($rs as $row){
			$_partBand = new PartBand();
	
			$_partBand->id = intval($row['id']);
			$_partBand->nam = $row['nam'];
			$_partBand->mad = $row['mad'];
			$_partBand->pic = $row['pic'];
				
			$partBand_list[] = $_partBand;
		}
		return $partBand_list;
	}
	
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$partBand_list = array();
		
		$sql = "select id,nam,mad,pic from tb_partband order by id desc limit $start,$limit";
		$rs = $db->query($sql);
		foreach($rs as $row){
			$_partBand = new PartBand();

			$_partBand->id = intval($row['id']);
			$_partBand->nam = $row['nam'];
			$_partBand->mad = $row['mad'];
			$_partBand->pic = $row['pic'];
			
			$partBand_list[] = $_partBand;
		}
		return $partBand_list;
	}
	
	public function getListByPageAndNam($start,$limit){
		$db = DbConnection::getInstance();
	
		$partBand_list = array();
	
		$sql = "select id,nam,mad,pic from tb_partband where nam like ? order by id desc limit $start,$limit";
		$para_array = array('%'.$this->nam.'%');
		$rs = $db->query($sql,$para_array);
		foreach($rs as $row){
			$_partBand = new PartBand();
	
			$_partBand->id = intval($row['id']);
			$_partBand->nam = $row['nam'];
			$_partBand->mad = $row['mad'];
			$_partBand->pic = $row['pic'];
				
			$partBand_list[] = $_partBand;
		}
		return $partBand_list;
	}
	
	public function save(){
		$db = DbConnection::getInstance();
	
		$sql = "insert into tb_partband(nam,mad,pic) values(?,?,?)";
		$para_array = array($this->nam,$this->mad,$this->pic);
	
		$db->exec($sql, $para_array);
	}
	
	public function update(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_partband set nam = ?,mad = ?,pic = ? where id = ?";
		$para_array = array($this->nam,$this->mad,$this->pic,$this->id);
	
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
	
		$sql = "delete from tb_partband where id = ?";
		$para_array = array($this->id);
	
		$db->exec($sql, $para_array);
	}
}
?>