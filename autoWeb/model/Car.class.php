<?php
require_once '../common/DbConnection.class.php';
require_once '../model/Line.class.php';

class Car{
	public $id = 0;
	public $line = NULL;
	public $nam = '';
	public $val = '';
	public $yea = '';
	public $pic = '';
	
	public function init(){
		$db = DbConnection::getInstance();

		$sql = "select line_id,nam,val,yea,pic from tb_car where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if (count($rs) > 0){
			
			$_line = new Line();
			$_line->id = intval($rs[0]['line_id']);
			$_line->init();
			$this->line = $_line;
			
			$this->nam = $rs[0]['nam'];
			$this->val = $rs[0]['val'];
			$this->yea = $rs[0]['yea'];
			$this->pic = $rs[0]['pic'];
		}else{
			$this->id = 0;
			$this->line = new Line();
		}
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
		
		$sql = "select count(0) c from tb_car";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByNam(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_car where nam like ?";
		$para_array = array('%'.$this->nam.'%');
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getList(){
		$db = DbConnection::getInstance();
	
		$car_list = array();
	
		$sql = "select id,line_id,nam,val,yea,pic from tb_car order by nam";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_car = new Car();
	
			$_car->id = intval($row['id']);
				
			$_line = new Line();
			$_line->id = intval($row['line_id']);
			$_line->init();
			$_car->line = $_line;
	
			$_car->yea = $row['yea'];
			$_car->nam = $row['nam'];
			$_car->val = $row['val'];
			$_car->pic = $row['pic'];
	
			$car_list[] = $_car;
		}
	
		return $car_list;
	}
	
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$car_list = array();
	
		$sql = "select id,line_id,nam,val,yea,pic from tb_car order by nam limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_car = new Car();
	
			$_car->id = intval($row['id']);
			
			$_line = new Line();
			$_line->id = intval($row['line_id']);
			$_line->init();
			$_car->line = $_line;
		
			$_car->yea = $row['yea'];
			$_car->nam = $row['nam'];
			$_car->val = $row['val'];
			$_car->pic = $row['pic'];
	
			$car_list[] = $_car;
		}
	
		return $car_list;
	}
	
	public function getListByPageAndNam($start,$limit){
		$db = DbConnection::getInstance();
	
		$car_list = array();
	
		$sql = "select id,line_id,nam,val,yea,pic from tb_car where nam like ? order by nam limit $start,$limit";
		$para_array = array('%'.$this->nam.'%');
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_car = new Car();
	
			$_car->id = intval($row['id']);
				
			$_line = new Line();
			$_line->id = intval($row['line_id']);
			$_line->init();
			$_car->line = $_line;
	
			$_car->yea = $row['yea'];
			$_car->nam = $row['nam'];
			$_car->val = $row['val'];
			$_car->pic = $row['pic'];
	
			$car_list[] = $_car;
		}
	
		return $car_list;
	}
	
	/**
	 * 根据车系和年份获取列表
	 */
	public function getListByLineAndYear(){
		$db = DbConnection::getInstance();
	
		$car_list = array();
	
		$sql = "select id,nam,val,pic from tb_car where line_id = ? and yea = ? order by nam";
		$para_array = array($this->line->id,$this->yea);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_car = new Car();
	
			$_car->id = intval($row['id']);
			$_car->line = $this->line;
			$_car->yea = $this->yea;
			$_car->nam = $row['nam'];
			$_car->val = $row['val'];
			$_car->pic = $row['pic'];
	
			$car_list[] = $_car;
		}
	
		return $car_list;
	}

	/**
	 * 获取车系对应的年份
	 */
	public function getYearListByLine(){
		$db = DbConnection::getInstance();
		
		$years = array();
		
		$sql = "select distinct(yea) yea from tb_car where line_id = ?";
		$para_array = array($this->line->id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$years[] = $row['yea'];
		}
		
		return $years;
	}
	
	public function save(){
		$db = DbConnection::getInstance();
	
		$sql = "insert into tb_car(line_id,nam,val,yea,pic) values(?,?,?,?,?)";
		$para_array = array($this->line->id,$this->nam,$this->val,$this->yea,$this->pic);
	
		$db->exec($sql, $para_array);
	}
	
	public function update(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_car set line_id = ?,nam = ?,val = ?,yea = ?,pic = ? where id = ?";
		$para_array = array($this->line->id,$this->nam,$this->val,$this->yea,$this->pic,$this->id);
	
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
	
		$sql = "delete from tb_car where id = ?";
		$para_array = array($this->id);
	
		$db->exec($sql, $para_array);
	}
}
?>