<?php
require_once '../common/DbConnection.class.php';

class Ad{
	public $id = 0;
	public $tit = '';
	public $pic = '';
	public $url = '';

	public function init(){
		$db = DbConnection::getInstance();

		$sql = "select tit,url,pic from tb_ad where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if (count($rs) > 0){
			
			$this->tit = $rs[0]['tit'];
			
			$this->url = $rs[0]['url'];
			$this->pic = $rs[0]['pic'];
		}else{
			$this->id = 0;
		}
	}
	
	/**
	 * 获取列表
	 */
	public function getList(){
		$db = DbConnection::getInstance();
	
		$ad_list = array();
	
		$sql = "select id,tit,url,pic from tb_ad order by id";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_ad = new Ad();
	
			$_ad->id = intval($row['id']);
			$_ad->tit = $row['tit'];
			$_ad->url = $row['url'];
			$_ad->pic = $row['pic'];
	
			$ad_list[] = $_ad;
		}
	
		return $ad_list;
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();

		$sql = "select count(0) c from tb_ad";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByTit(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_ad where tit like ?";
		$para_array = array('%'.$this->tit.'%');
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$ad_list = array();
	
		$sql = "select id,tit,url,pic from tb_ad order by id limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_ad = new Ad();
	
			$_ad->id = intval($row['id']);
			$_ad->tit = $row['tit'];
			$_ad->url = $row['url'];
			$_ad->pic = $row['pic'];
	
			$ad_list[] = $_ad;
		}
	
		return $ad_list;
	}
	
	public function getListByTitAndPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$ad_list = array();
	
		$sql = "select id,tit,url,pic from tb_ad where tit like ? order by id limit $start,$limit";
		$para_array = array('%'.$this->tit.'%');
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_ad = new Ad();
	
			$_ad->id = intval($row['id']);
			$_ad->tit = $row['tit'];
			$_ad->url = $row['url'];
			$_ad->pic = $row['pic'];
	
			$ad_list[] = $_ad;
		}
	
		return $ad_list;
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_ad(tit,pic,url) values(?,?,?)";
		$para_array = array($this->tit,$this->pic,$this->url);
		
		$db->exec($sql, $para_array);
	}
	
	public function update(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_ad set tit = ?,pic = ?,url = ? where id = ?";
		$para_array = array($this->tit,$this->pic,$this->url,$this->id);
	
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
		
		$sql = "delete from tb_ad where id = ?";
		$para_array = array($this->id);
		
		$db->exec($sql, $para_array);
	}
}
?>