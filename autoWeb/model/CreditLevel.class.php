<?php
require_once '../common/DbConnection.class.php';

class CreditLevel{
	public $id = 0;
	public $name = '';
	public $point = 0;
	public $level = 0;

	public function init(){
		$db = DbConnection::getInstance();

		$sql = "select name,point,level from tb_credit_level where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if (count($rs) > 0){
			$this->name = $rs[0]['name'];
			$this->point = intval($rs[0]['point']);
			$this->level = intval($rs[0]['level']);
		}else{
			$this->id = 0;
		}
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();

		$sql = "select count(0) c from tb_credit_level";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByName(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_credit_level where name like ?";
		$para_array = array('%'.$this->name.'%');
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$creditLevel_list = array();
	
		$sql = "select id,name,point,level from tb_credit_level order by level limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_creditLevel = new CreditLevel();
	
			$_creditLevel->id = intval($row['id']);
			$_creditLevel->name = $row['name'];
			$_creditLevel->point = intval($row['point']);
			$_creditLevel->level = intval($row['level']);
			
			$creditLevel_list[] = $_creditLevel;
		}
	
		return $creditLevel_list;
	}
	
	public function getListByPageAndName($start,$limit){
		$db = DbConnection::getInstance();
	
		$creditLevel_list = array();
	
		$sql = "select id,name,point,level from tb_credit_level where name like ? order by level limit $start,$limit";
		$para_array = array('%'.$this->name.'%');
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_creditLevel = new CreditLevel();
	
			$_creditLevel->id = intval($row['id']);
			$_creditLevel->name = $row['name'];
			$_creditLevel->point = intval($row['point']);
			$_creditLevel->level = intval($row['level']);
			
			$creditLevel_list[] = $_creditLevel;
		}
	
		return $creditLevel_list;
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_credit_level(name,point,level) values(?,?,?)";
		$para_array = array($this->name,$this->point,$this->level);
		
		$db->exec($sql, $para_array);
	}
	
	public function update(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_credit_level set name = ?,point = ?,level = ? where id = ?";
		$para_array = array($this->name,$this->point,$this->level,$this->id);
	
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
		
		$sql = "delete from tb_credit_level where id = ?";
		$para_array = array($this->id);
		
		$db->exec($sql, $para_array);
	}
	
	/**
	 * 根据积分获取等级
	 * @return number
	 */
	public function getLevelByPoint(){
		$db = DbConnection::getInstance();
		
		$sql = "select level from tb_credit_level where point >= ? order by level limit 1";
		$para_array = array($this->point);
		$rs = $db->query($sql,$para_array);
		if(count($rs) > 0){
			return intval($rs[0]['level']);
		}
		return 1;
	}
}
?>