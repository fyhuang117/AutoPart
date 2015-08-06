<?php
require_once '../common/DbConnection.class.php';

class Band{
	public $id = 0;
	public $nam = '';
	public $mad = '';
	public $pic = '';
	public $first_word = '';
	
	public function init(){
		$db = DbConnection::getInstance();

		$sql = "select nam,mad,pic,first_word from tb_band where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if (count($rs) > 0){
			
			$this->nam = $rs[0]['nam'];
			
			$this->mad = $rs[0]['mad'];
			$this->pic = $rs[0]['pic'];
			$this->first_word = $rs[0]['first_word'];
		}else{
			$this->id = 0;
		}
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();

		$sql = "select count(0) c from tb_band";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByNam(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_band where nam like ?";
		$para_array = array('%'.$this->nam.'%');
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	/**
	 * 获取列表
	 * @return multitype:Band
	 */
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$band_list = array();
	
		$sql = "select id,nam,mad,pic,first_word from tb_band order by nam limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_band = new Band();
	
			$_band->id = intval($row['id']);
			$_band->nam = $row['nam'];
			$_band->mad = $row['mad'];
			$_band->pic = $row['pic'];
			$_band->first_word = $row['first_word'];
			
			$band_list[] = $_band;
		}
	
		return $band_list;
	}
	
	/**
	 * 获取列表
	 * @return multitype:Band
	 */
	public function getListByPageAndNam($start,$limit){
		$db = DbConnection::getInstance();
	
		$band_list = array();
	
		$sql = "select id,nam,mad,pic,first_word from tb_band where nam like ? order by nam limit $start,$limit";
		$para_array = array('%'.$this->nam.'%');
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_band = new Band();
	
			$_band->id = intval($row['id']);
			$_band->nam = $row['nam'];
			$_band->mad = $row['mad'];
			$_band->pic = $row['pic'];
			$_band->first_word = $row['first_word'];
	
			$band_list[] = $_band;
		}
	
		return $band_list;
	}
	
	/**
	 * 获取列表
	 * @return multitype:Band
	 */
	public function getList(){
		$db = DbConnection::getInstance();
	
		$band_list = array();
	
		$sql = "select id,nam,mad,pic,first_word from tb_band order by nam";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_band = new Band();
	
			$_band->id = intval($row['id']);
			$_band->nam = $row['nam'];
			$_band->mad = $row['mad'];
			$_band->pic = $row['pic'];
			$_band->first_word = $row['first_word'];
	
			$band_list[] = $_band;
		}
	
		return $band_list;
	}
	
	public function save(){
		$db = DbConnection::getInstance();
	
		$sql = "insert into tb_band(nam,mad,pic,first_word) values(?,?,?,?)";
		$para_array = array($this->nam,$this->mad,$this->pic,$this->first_word);
	
		$db->exec($sql, $para_array);
	}
	
	public function update(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_band set nam = ?,mad = ?,pic = ?,first_word = ? where id = ?";
		$para_array = array($this->nam,$this->mad,$this->pic,$this->id);
	
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
	
		$sql = "delete from tb_band where id = ?";
		$para_array = array($this->id);
	
		$db->exec($sql, $para_array);
	}
}
?>