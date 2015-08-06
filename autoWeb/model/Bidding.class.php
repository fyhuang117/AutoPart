<?php
require_once '../common/DbConnection.class.php';
require_once '../model/User.class.php';
require_once '../model/PartType.class.php';
require_once '../model/Car.class.php';
require_once '../model/Inquiry.class.php';

class Bidding{
	public $id = 0;
	public $user = NULL;
	public $inquiry = NULL;
	public $pri = 0;
	public $partband_id = '';
	public $parttype_quality = 0;
	public $c = 0;
	public $avi = 0;
	public $qau = '';
	public $pay = 0;
	public $del = '';
	public $pof = 0;
	public $pic1 = '';
	public $pic2 = '';
	public $pic3 = '';
	public $mes = '';
	public $aum = '';
	public $create_time = '';
	public $read_status = 0;
	public $status = 0;
	public $distance = 0;
	
	public function init(){
		$db = DbConnection::getInstance();

		$sql = "select user_id,inquiry_id,pri,partband_id,parttype_quality,c,avi,qau,pay,del,pof,pic1,pic2,pic3,mes,aum,create_time,read_status,status from tb_bidding where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if (count($rs) > 0){

			$_user = new User();
			$_user->id = intval($rs[0]['user_id']);
			$_user->init();
			$this->user = $_user;
			
			$_inquiry = new Inquiry();
			$_inquiry->id = intval($rs[0]['inquiry_id']);
			$_inquiry->init();
			$this->inquiry = $_inquiry;
	
			$this->pri = $rs[0]['pri'];
	
			$this->partband_id = $rs[0]['partband_id'];
			$this->parttype_quality = intval($rs[0]['parttype_quality']);
			
			$this->c = intval($rs[0]['c']);
			$this->avi = intval($rs[0]['avi']);
			$this->qau = $rs[0]['qau'];
			$this->pay = intval($rs[0]['pay']);
			$this->del = $rs[0]['del'];
			$this->pof = $rs[0]['pof'];
			$this->pic1 = $rs[0]['pic1'];
			$this->pic2 = $rs[0]['pic2'];
			$this->pic3 = $rs[0]['pic3'];
			$this->mes = $rs[0]['mes'];
			$this->aum = $rs[0]['aum'];
			$this->create_time = $rs[0]['create_time'];
			$this->read_status = intval($rs[0]['read_status']);
			$this->status = intval($rs[0]['status']);
		}else{
			$this->id = 0;
			$this->user = new User();
			$this->inquiry = new Inquiry();
		}
	}
	
	/**
	 * 根据询价单获取未读数量
	 * @return number
	 */
	public function getNoReadCountByInquiry(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_bidding where inquiry_id = ? and read_status = ? and status = ?";
		$para_array = array($this->inquiry->id,BIDDING_READ_STATUS_NO,BIDDING_STATUS_NORMAL);
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	/**
	 * 根据询价单获取数量
	 * @return number
	 */
	public function getCountByInquiry(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_bidding where inquiry_id = ? and status = ?";
		$para_array = array($this->inquiry->id,BIDDING_STATUS_NORMAL);
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	

	/**
	 * 根据询价单获取最低报价
	 * @return number
	 */
	public function getMinPriByInquiry(){
		$db = DbConnection::getInstance();
	
		$minPri = 0;
		
		$sql = "select min(pri) pri from tb_bidding where inquiry_id = ? and status = ?";
		$para_array = array($this->inquiry->id,BIDDING_STATUS_NORMAL);
		$rs = $db->query($sql,$para_array);
		if(count($rs) > 0){
			$minPri = $rs[0]['pri'];
		}
		return $minPri;
	}
	
	
	/**
	 * 根据询价单获取列表
	 */
	public function getListByInquiry($avi = 2,$business_area = 0,$sort_type = SORT_TYPE_DEFAULT){
		$db = DbConnection::getInstance();
	
		$bidding_list = array();
	
		$sql = "select b.id,b.user_id,b.pri,b.partband_id,b.parttype_quality,b.c,b.avi,b.qau,b.pay,b.del,b.pof,b.pic1,b.pic2,b.pic3,b.mes,b.aum,b.create_time,b.read_status,b.status from tb_bidding b 
				inner join tb_user u on b.user_id = u.id 
				where b.inquiry_id = ? and b.status = ? ";
		if($avi < 2){
			$sql .= "and b.avi = $avi ";
		}
		if($business_area > 0){
			$sql .= "and u.business_area_id = $business_area ";
		}
		switch ($sort_type){
			case SORT_TYPE_DEFAULT:
				$sql .= "order by b.id desc";
				break;
			case SORT_TYPE_DISTANCE:
				$sql .= "order by b.distance";
				break;
			case SORT_TYPE_PRICE_ASC:
				$sql .= "order by b.pri";
				break;
			case SORT_TYPE_PRICE_DESC:
				$sql .= "order by b.pri desc";
				break;
			case SORT_TYPE_USER_LEVEL:
				$sql .= "order by u.sell_level desc";
				break;
			case SORT_TYPE_USER_SCORE:
				$sql .= "order by (u.sell_match + u.sell_service + u.sell_speed) / 3 desc";
				break;
		}
		$para_array = array($this->inquiry->id,BIDDING_STATUS_NORMAL);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_bidding = new Bidding();
	
			$_bidding->id = intval($row['id']);
			$_user = new User();
			$_user->id = intval($row['user_id']);
			$_user->init();
			$_bidding->user = $_user;
				
			$_bidding->pri = $row['pri'];
				
			$_bidding->partband_id = $row['partband_id'];
			$_bidding->parttype_quality = intval($row['parttype_quality']);
				
			$_bidding->c = intval($row['c']);
			$_bidding->avi = intval($row['avi']);
			$_bidding->qau = $row['qau'];
			$_bidding->pay = intval($row['pay']);
			$_bidding->del = $row['del'];
			$_bidding->pof = $row['pof'];
			$_bidding->pic1 = $row['pic1'];
			$_bidding->pic2 = $row['pic2'];
			$_bidding->pic3 = $row['pic3'];
			$_bidding->mes = $row['mes'];
			$_bidding->aum = $row['aum'];
			$_bidding->create_time = $row['create_time'];
			$_bidding->read_status = intval($row['read_status']);
			$_bidding->status = intval($row['status']);
	
			$bidding_list[] = $_bidding;
		}
	
		return $bidding_list;
	}
	
	/**
	 * 根据用户获取列表
	 */
	public function getListByUser($parttype_name = ''){
		$db = DbConnection::getInstance();
	
		$bidding_list = array();
	
		$sql = "select b.id,b.user_id,b.inquiry_id,b.pri,b.partband_id,b.parttype_quality,b.c,b.avi,b.qau,b.pay,b.del,b.pof,b.pic1,b.pic2,b.pic3,b.mes,b.aum,b.create_time,b.read_status,b.status 
				from tb_bidding b inner join tb_inquiry i on b.inquiry_id = i.id 
				inner join tb_parttype p on i.parttype_id = p.id 
				where b.user_id = ? and b.status = ? ";
		if($parttype_name != ''){
			$sql .= "and p.nam like '%$parttype_name%' ";
		}
		$sql .= "order by b.id desc";
		$para_array = array($this->user->id,BIDDING_STATUS_NORMAL);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_bidding = new Bidding();
	
			$_bidding->id = intval($row['id']);
			$_bidding->user = $this->user;
	
			$_inquiry = new Inquiry();
			$_inquiry->id = intval($row['inquiry_id']);
			$_inquiry->init();
			$_bidding->inquiry = $_inquiry;
			
			$_bidding->pri = $row['pri'];
	
			$_bidding->partband_id = $row['partband_id'];
			$_bidding->parttype_quality = intval($row['parttype_quality']);
	
			$_bidding->c = intval($row['c']);
			$_bidding->avi = intval($row['avi']);
			$_bidding->qau = $row['qau'];
			$_bidding->pay = intval($row['pay']);
			$_bidding->del = $row['del'];
			$_bidding->pof = $row['pof'];
			$_bidding->pic1 = $row['pic1'];
			$_bidding->pic2 = $row['pic2'];
			$_bidding->pic3 = $row['pic3'];
			$_bidding->mes = $row['mes'];
			$_bidding->aum = $row['aum'];
			$_bidding->create_time = $row['create_time'];
			$_bidding->read_status = intval($row['read_status']);
			$_bidding->status = intval($row['status']);
	
			$bidding_list[] = $_bidding;
		}
	
		return $bidding_list;
	}
	
	/**
	 * 根据用户获取数量
	 */
	public function getCountByUser(){
		$db = DbConnection::getInstance();

		$sql = "select count(0) c from tb_bidding where user_id = ? and status = ? order by id desc";
		$para_array = array($this->user->id,BIDDING_STATUS_NORMAL);
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		$sql = "insert into tb_bidding(user_id,inquiry_id,pri,partband_id,parttype_quality,c,avi,qau,pay,del,pof,pic1,pic2,pic3,mes,aum,create_time,read_status,status,distance) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?)";
		$para_array = array($this->user->id,$this->inquiry->id,$this->pri,$this->partband_id,$this->parttype_quality,$this->c,$this->avi,$this->qau,$this->pay,$this->del,$this->pof,$this->pic1,$this->pic2,$this->pic3,$this->mes,$this->aum,BIDDING_READ_STATUS_NO,BIDDING_STATUS_NORMAL,$this->distance);
		
		$db->exec($sql, $para_array);
		
		$this->id = $db->getLastInsertId('id');
	}
	
	public function update(){
		$db = DbConnection::getInstance();
		$sql = "update tb_bidding set pri = ?,partband_id = ?,parttype_quality = ?,c = ?,avi = ?,qau = ?,pay = ?,del = ?,pof = ?,pic1 = ?,pic2 = ?,pic3 = ?,mes = ?,aum = ? where id = ? and status = ?";
		$para_array = array($this->pri,$this->partband_id,$this->parttype_quality,$this->c,$this->avi,$this->qau,$this->pay,$this->del,$this->pof,$this->pic1,$this->pic2,$this->pic3,$this->mes,$this->aum,$this->id,BIDDING_STATUS_NORMAL);
	
		$db->exec($sql, $para_array);
	}
	
	public function del(){
		$db = DbConnection::getInstance();
		$sql = "update tb_bidding set status = ? where id = ? and status = ?";
		$para_array = array(BIDDING_STATUS_DEL,$this->id,BIDDING_STATUS_NORMAL);
	
		$db->exec($sql, $para_array);
	}
	
	public function order(){
		$db = DbConnection::getInstance();
		$sql = "update tb_bidding set status = ? where id = ? and status = ?";
		$para_array = array(BIDDING_STATUS_ORDER,$this->id,BIDDING_STATUS_NORMAL);
		
		$db->exec($sql, $para_array);
	}
	
	public function end(){
		$db = DbConnection::getInstance();
		$sql = "update tb_bidding set status = ? where id = ? and status = ?";
		$para_array = array(BIDDING_STATUS_END,$this->id,BIDDING_STATUS_NORMAL);
		
		$db->exec($sql, $para_array);
	}
}
?>