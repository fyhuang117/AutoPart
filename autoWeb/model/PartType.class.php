<?php
require_once '../common/DbConnection.class.php';

class PartType{
	
	public $id = 0;
	public $type = 0;
	public $nam = '';
	public $pic = '';
	public $par = 0;
	public $ban = '';
	public $h = 0;
	public $cars = '';
	public $is_select_car = 0;
	public $is_select_quality = 0;
	
	public function init(){
		$db = DbConnection::getInstance();

		$sql = "select nam,type,pic,par,ban,h,cars,is_select_car,is_select_quality from tb_parttype where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if(count($rs) > 0){
			$this->nam = $rs[0]['nam'];
			$this->type = intval($rs[0]['type']);
			$this->pic = $rs[0]['pic'];
			$this->par = $rs[0]['par'];
			$this->ban = $rs[0]['ban'];
			$this->h = intval($rs[0]['h']);
			$this->cars = $rs[0]['cars'];
			$this->is_select_car = intval($rs[0]['is_select_car']);
			$this->is_select_quality = intval($rs[0]['is_select_quality']);
		}else{
			$this->id = 0;
		}
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
		
		$sql = "select count(0) c from tb_parttype where par = 0";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByNam(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_parttype where nam like ?";
		$para_array = array('%'.$this->nam.'%');
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getList(){
		$db = DbConnection::getInstance();
	
		$partType_list = array();
	
		$sql = "select id,nam,type,pic,par,ban,h,cars,is_select_car,is_select_quality from tb_parttype order by id desc";
		$rs = $db->query($sql);
		foreach($rs as $row){
			$_partType = new PartType();
			$_partType->id = $row['id'];
				
			$_partType->nam = $row['nam'];
			$_partType->type = intval($row['type']);
			$_partType->pic = $row['pic'];
			$_partType->par = $row['par'];
			$_partType->ban = $row['ban'];
			$_partType->h = intval($row['h']);
			$_partType->cars = $row['cars'];
			$_partType->is_select_car = intval($row['is_select_car']);
			$_partType->is_select_quality = intval($row['is_select_quality']);	
			
			$partType_list[] = $_partType;
		}
		return $partType_list;
	}
	
	public function getListByParent(){
		$db = DbConnection::getInstance();
	
		$partType_list = array();
	
		$sql = "select id,nam,type,pic,par,ban,h,cars,is_select_car,is_select_quality from tb_parttype where par = ? order by id desc";
		$para_array = array($this->par);
		$rs = $db->query($sql,$para_array);
		foreach($rs as $row){
			$_partType = new PartType();
			$_partType->id = $row['id'];
	
			$_partType->nam = $row['nam'];
			$_partType->type = intval($row['type']);
			$_partType->pic = $row['pic'];
			$_partType->par = $row['par'];
			$_partType->ban = $row['ban'];
			$_partType->h = intval($row['h']);
			$_partType->cars = $row['cars'];
			$_partType->is_select_car = intval($row['is_select_car']);
			$_partType->is_select_quality = intval($row['is_select_quality']);
				
			$partType_list[] = $_partType;
		}
		return $partType_list;
	}
	
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$partType_list = array();
		
		$sql = "select id,nam,type,pic,par,ban,h,cars,is_select_car,is_select_quality from tb_parttype where par = 0 order by id desc limit $start,$limit";
		$rs = $db->query($sql);
		foreach($rs as $row){
			$_partType = new PartType();
			$_partType->id = $row['id'];
			
			$_partType->nam = $row['nam'];
			$_partType->type = intval($row['type']);
			$_partType->pic = $row['pic'];
			$_partType->par = $row['par'];
			$_partType->ban = $row['ban'];
			$_partType->h = intval($row['h']);
			$_partType->cars = $row['cars'];
			$_partType->is_select_car = intval($row['is_select_car']);
			$_partType->is_select_quality = intval($row['is_select_quality']);
			
			$partType_list[] = $_partType;
		}
		return $partType_list;
	}
	
	public function getListByPageAndNam($start,$limit){
		$db = DbConnection::getInstance();
	
		$partType_list = array();
	
		$sql = "select id,nam,type,pic,par,ban,h,cars,is_select_car,is_select_quality from tb_parttype where nam like ? order by id desc limit $start,$limit";
		$para_array = array('%'.$this->nam.'%');
		$rs = $db->query($sql,$para_array);
		foreach($rs as $row){
			$_partType = new PartType();
			$_partType->id = $row['id'];
				
			$_partType->nam = $row['nam'];
			$_partType->type = intval($row['type']);
			$_partType->pic = $row['pic'];
			$_partType->par = $row['par'];
			$_partType->ban = $row['ban'];
			$_partType->h = intval($row['h']);
			$_partType->cars = $row['cars'];
			$_partType->is_select_car = intval($row['is_select_car']);	
			$_partType->is_select_quality = intval($row['is_select_quality']);
			
			$partType_list[] = $_partType;
		}
		return $partType_list;
	}
	
	/**
	 * 根据上级和类型获取列表
	 * @return multitype:PartType
	 */
	public function getListByParAndType(){
		$db = DbConnection::getInstance();
	
		$partType_list = array();
	
		$sql = "select id,nam,pic,ban,h,cars,is_select_car,is_select_quality from tb_parttype where type = ? and par = ? order by nam";
		$para_array = array($this->type,$this->par);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_partType = new PartType();
	
			$_partType->id = $row['id'];
			$_partType->type = $this->type;
			$_partType->par = $this->par;
			$_partType->nam = $row['nam'];
			$_partType->pic = $row['pic'];
			$_partType->ban = $row['ban'];
			$_partType->h = intval($row['h']);
			$_partType->cars = $row['cars'];
			$_partType->is_select_car = intval($row['is_select_car']);
			$_partType->is_select_quality = intval($row['is_select_quality']);
	
			$partType_list[] = $_partType;
		}
	
		return $partType_list;
	}
	
	/**
	 * 根据上级获取列表
	 * @return multitype:PartType
	 */
	public function getListByPar(){
		$db = DbConnection::getInstance();
	
		$partType_list = array();
	
		$sql = "select id,nam,pic,ban,h,cars,is_select_car,is_select_quality from tb_parttype where par = ? order by nam";
		$para_array = array($this->par);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_partType = new PartType();
	
			$_partType->id = $row['id'];
			$_partType->type = $this->type;
			$_partType->par = $this->par;
			$_partType->nam = $row['nam'];
			$_partType->pic = $row['pic'];
			$_partType->ban = $row['ban'];
			$_partType->h = intval($row['h']);
			$_partType->cars = $row['cars'];
			$_partType->is_select_car = intval($row['is_select_car']);
			$_partType->is_select_quality = intval($row['is_select_quality']);
	
			$partType_list[] = $_partType;
		}
	
		return $partType_list;
	}
	
	public function save(){
		$db = DbConnection::getInstance();
	
		$sql = "insert into tb_parttype(type,nam,pic,par,ban,h,cars,is_select_car,is_select_quality) values(?,?,?,?,?,?,?,?,?)";
		$para_array = array($this->type,$this->nam,$this->pic,$this->par,$this->ban,$this->h,$this->cars,$this->is_select_car,$this->is_select_quality);
	
		$db->exec($sql, $para_array);
		
		$this->id = $db->getLastInsertId('id');
	}
	
	public function update(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_parttype set type = ?,nam = ?,pic = ?,par = ?,ban = ?,h = ?,cars = ?,is_select_car = ?,is_select_quality = ? where id = ?";
		$para_array = array($this->type,$this->nam,$this->pic,$this->par,$this->ban,$this->h,$this->cars,$this->is_select_car,$this->is_select_quality,$this->id);
	
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
	
		$sql = "delete from tb_parttype where id = ?";
		$para_array = array($this->id);
	
		$db->exec($sql, $para_array);
	}
	
	/**
	 * 根据名称查询配件,只返回最底层
	 */
	public function getListByNam(){
		$db = DbConnection::getInstance();
	
		$partType_list = array();
	
		$sql_sub = "select count(0) c from tb_parttype where par = ?";
		
		$sql = "select id,nam,type,pic,par,ban,h,cars,is_select_car,is_select_quality from tb_parttype where nam like ? order by id desc";
		$para_array = array('%'.$this->nam.'%');
		$rs = $db->query($sql,$para_array);
		foreach($rs as $row){
			$_partType = new PartType();
			$_partType->id = $row['id'];
	
			//查询是否有下层
			$para_array = array($_partType->id);
			$rs_sub = $db->query($sql_sub,$para_array);
			if(count($rs_sub) == 0 || intval($rs_sub[0]['c']) == 0){
				//没有下层
				
				$_partType->nam = $row['nam'];
				$_partType->type = intval($row['type']);
				$_partType->pic = $row['pic'];
				$_partType->par = $row['par'];
				$_partType->ban = $row['ban'];
				$_partType->h = intval($row['h']);
				$_partType->cars = $row['cars'];
				$_partType->is_select_car = intval($row['is_select_car']);
				$_partType->is_select_quality = intval($row['is_select_quality']);
				
				$partType_list[] = $_partType;
			}
		}
		return $partType_list;
	}
}
?>