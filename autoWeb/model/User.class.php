<?php
require_once '../common/DbConnection.class.php';
require_once '../model/EasyMob.class.php';
require_once '../model/BusinessArea.class.php';
require_once '../model/Coupon.class.php';
require_once '../model/UserCoupon.class.php';
require_once '../model/Wallet.class.php';

class User{
	public $id = 0;
	public $tel = '';
	public $sec = '';
	public $nam = '';
	public $adr = '';
	public $ton = '';
	public $lat = '';
	public $lp1 = '';
	public $lp2 = '';
	public $pic = '';
	public $enb = 0;
	public $business_area = NULL;
	public $sell_level = 0;
	public $sell_point = 0;
	public $order_score_count = 0;
	public $sell_match = 0;
	public $sell_service = 0;
	public $sell_speed = 0;
	public $license = '';
	public $agent = '';
	public $type = 0;
	public $status = 0;
	public $jpush_alias = '';
	
	public function init(){
		$db = DbConnection::getInstance();
		
		$sql = "select tel,nam,adr,ton,lat,lp1,lp2,pic,enb,business_area_id,sell_level,sell_point,order_score_count,sell_match,sell_service,sell_speed,license,agent,type,status,jpush_alias from tb_user where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if(count($rs) > 0){
			$this->tel = $rs[0]['tel'];
			$this->nam = $rs[0]['nam'];
			$this->adr = $rs[0]['adr'];
			$this->ton = $rs[0]['ton'];
			$this->lat = $rs[0]['lat'];
			$this->lp1 = $rs[0]['lp1'];
			$this->lp2 = $rs[0]['lp2'];
			$this->pic = $rs[0]['pic'];
			$this->enb = intval($rs[0]['enb']);
			
			$_businessArea = new BusinessArea();
			$_businessArea->id = intval($rs[0]['business_area_id']);
			$_businessArea->init();
			$this->business_area = $_businessArea;
			
			$this->sell_level = intval($rs[0]['sell_level']);
			$this->sell_point = intval($rs[0]['sell_point']);
			$this->order_score_count = intval($rs[0]['order_score_count']);
			$this->sell_match = intval($rs[0]['sell_match']);
			$this->sell_service = intval($rs[0]['sell_service']);
			$this->sell_speed = intval($rs[0]['sell_speed']);
			
			$this->license = $rs[0]['license'];
			$this->agent = $rs[0]['agent'];
			$this->type = intval($rs[0]['type']);
			$this->status = intval($rs[0]['status']);
			$this->jpush_alias = $rs[0]['jpush_alias'];
		}else{
			$this->id = 0;
			$this->business_area = new BusinessArea();
		}
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
		
		$sql = "select count(0) c from tb_user where type = ?";
		$para_array = array($this->type);
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getBuyCount(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_user where type = 0";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getSellCount(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_user where type = 1";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByNam($key,$val){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_user where $key like ? and type = ?";
		$para_array = array('%'.$val.'%',$this->type);
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getCountByTel(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_user where tel = ? and type = ?";
		$para_array = array($this->tel,$this->type);
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
		
		$user_list = array();
		
		$sql = "select id,tel,nam,adr,ton,lat,lp1,lp2,pic,enb,business_area_id,sell_level,sell_point,order_score_count,sell_match,sell_service,sell_speed,license,agent,type,status from tb_user where type = ? order by id desc,enb desc limit $start,$limit";
		$para_array = array($this->type);
		$rs = $db->query($sql,$para_array);
		foreach($rs as $row){
			$_user = new User();
			
			$_user->id = intval($row['id']);
			$_user->tel = $row['tel'];
			$_user->nam = $row['nam'];
			$_user->adr = $row['adr'];
			$_user->ton = $row['ton'];
			$_user->lat = $row['lat'];
			$_user->lp1 = $row['lp1'];
			$_user->lp2 = $row['lp2'];
			$_user->pic = $row['pic'];
			$_user->enb = intval($row['enb']);
			
			$_businessArea = new BusinessArea();
			$_businessArea->id = intval($row['business_area_id']);
			$_businessArea->init();
			$_user->business_area = $_businessArea;
			
			$_user->sell_level = intval($row['sell_level']);
			$_user->sell_point = intval($row['sell_point']);
			$_user->order_score_count = intval($row['order_score_count']);
			$_user->sell_match = intval($row['sell_match']);
			$_user->sell_service = intval($row['sell_service']);
			$_user->sell_speed = intval($row['sell_speed']);
			
			$_user->license = $row['license'];
			$_user->agent = $row['agent'];
			$_user->type = intval($row['type']);
			$_user->status = intval($row['status']);
			
			$user_list[] = $_user;
		}
		
		return $user_list;
	}
	
	/**
	 * 查询全部买家
	 */
	public function getBuyListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$user_list = array();
	
		$sql = "select id,tel,nam,adr,ton,lat,lp1,lp2,pic,enb,business_area_id,sell_level,sell_point,order_score_count,sell_match,sell_service,sell_speed,license,agent,type,status from tb_user where type = 0 order by id desc limit $start,$limit";
		$rs = $db->query($sql);
		foreach($rs as $row){
			$_user = new User();
				
			$_user->id = intval($row['id']);
			$_user->tel = $row['tel'];
			$_user->nam = $row['nam'];
			$_user->adr = $row['adr'];
			$_user->ton = $row['ton'];
			$_user->lat = $row['lat'];
			$_user->lp1 = $row['lp1'];
			$_user->lp2 = $row['lp2'];
			$_user->pic = $row['pic'];
			$_user->enb = intval($row['enb']);
				
			$_businessArea = new BusinessArea();
			$_businessArea->id = intval($row['business_area_id']);
			$_businessArea->init();
			$_user->business_area = $_businessArea;
				
			$_user->sell_level = intval($row['sell_level']);
			$_user->sell_point = intval($row['sell_point']);
			$_user->order_score_count = intval($row['order_score_count']);
			$_user->sell_match = intval($row['sell_match']);
			$_user->sell_service = intval($row['sell_service']);
			$_user->sell_speed = intval($row['sell_speed']);
				
			$_user->license = $row['license'];
			$_user->agent = $row['agent'];
			$_user->type = intval($row['type']);
			$_user->status = intval($row['status']);
				
			$user_list[] = $_user;
		}
	
		return $user_list;
	}
	
	/**
	 * 查询全部卖家
	 */
	public function getSellListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$user_list = array();
	
		$sql = "select id,tel,nam,adr,ton,lat,lp1,lp2,pic,enb,business_area_id,sell_level,sell_point,order_score_count,sell_match,sell_service,sell_speed,license,agent,type,status from tb_user where type = 1 order by id desc limit $start,$limit";
		$rs = $db->query($sql);
		foreach($rs as $row){
			$_user = new User();
	
			$_user->id = intval($row['id']);
			$_user->tel = $row['tel'];
			$_user->nam = $row['nam'];
			$_user->adr = $row['adr'];
			$_user->ton = $row['ton'];
			$_user->lat = $row['lat'];
			$_user->lp1 = $row['lp1'];
			$_user->lp2 = $row['lp2'];
			$_user->pic = $row['pic'];
			$_user->enb = intval($row['enb']);
	
			$_businessArea = new BusinessArea();
			$_businessArea->id = intval($row['business_area_id']);
			$_businessArea->init();
			$_user->business_area = $_businessArea;
	
			$_user->sell_level = intval($row['sell_level']);
			$_user->sell_point = intval($row['sell_point']);
			$_user->order_score_count = intval($row['order_score_count']);
			$_user->sell_match = intval($row['sell_match']);
			$_user->sell_service = intval($row['sell_service']);
			$_user->sell_speed = intval($row['sell_speed']);
	
			$_user->license = $row['license'];
			$_user->agent = $row['agent'];
			$_user->type = intval($row['type']);
			$_user->status = intval($row['status']);
	
			$user_list[] = $_user;
		}
	
		return $user_list;
	}
	
	public function getListByNamAndPage($key,$val,$start,$limit){
		$db = DbConnection::getInstance();
	
		$user_list = array();
	
		$sql = "select id,tel,nam,adr,ton,lat,lp1,lp2,pic,enb,business_area_id,sell_level,sell_point,order_score_count,sell_match,sell_service,sell_speed,license,agent,type,status from tb_user where type = ? and $key like ? order by id desc,enb desc limit $start,$limit";
		$para_array = array($this->type,'%'.$val.'%');
		$rs = $db->query($sql,$para_array);
		foreach($rs as $row){
			$_user = new User();
				
			$_user->id = intval($row['id']);
			$_user->tel = $row['tel'];
			$_user->nam = $row['nam'];
			$_user->adr = $row['adr'];
			$_user->ton = $row['ton'];
			$_user->lat = $row['lat'];
			$_user->lp1 = $row['lp1'];
			$_user->lp2 = $row['lp2'];
			$_user->pic = $row['pic'];
			$_user->enb = intval($row['enb']);
			
			$_businessArea = new BusinessArea();
			$_businessArea->id = intval($row['business_area_id']);
			$_businessArea->init();
			$_user->business_area = $_businessArea;
				
			$_user->sell_level = intval($row['sell_level']);
			$_user->sell_point = intval($row['sell_point']);
			$_user->order_score_count = intval($row['order_score_count']);
			$_user->sell_match = intval($row['sell_match']);
			$_user->sell_service = intval($row['sell_service']);
			$_user->sell_speed = intval($row['sell_speed']);
			
			$_user->license = $row['license'];
			$_user->agent = $row['agent'];
			$_user->type = intval($row['type']);
			$_user->status = intval($row['status']);
			
			$user_list[] = $_user;
		}
	
		return $user_list;
	}
	
	/**
	 * 生成登录用验证吗
	 */
	public function save_sec(){
		$db = DbConnection::getInstance();
		
		$sec = rand(100000,999999);
		
		$sql = "select count(0) c,sec from tb_user where tel = ? and type = ?";
		$para_array = array($this->tel,$this->type);
		$rs = $db->query($sql,$para_array);
		if($rs[0]['c'] > 0){
			//存在用户
			$sql = "update tb_user set sec = ? where tel = ? and type = ?";
			$para_array = array($sec,$this->tel,$this->type);
			$db->exec($sql, $para_array);
			
			if($rs[0]['sec'] == ''){
				if($this->type == 0){
					$_coupon = new Coupon();
					$coupon_list = $_coupon->getListByPage(0, $_coupon->getCount());
					foreach ($coupon_list as $_coupon_){
						if($_coupon_->get_type == COUPON_GET_TYPE_REGISTER && $_coupon_->status == 1){
							for($i = 0;$i < $_coupon_->get_count;++$i){
								$_user_coupon = new UserCoupon();
								$_user_coupon->user = $this;
								$_user_coupon->coupon = $_coupon_;
								$_user_coupon->save();
							}
						}
					}
				}
			}
		}else{
			//不存在用户
			$sql = "insert into tb_user(tel,sec,type) values(?,?,?)";
			$para_array = array($this->tel,$sec,$this->type);
			$db->exec($sql, $para_array);
			
			$this->id = $db->getLastInsertId('id');
			
			$_wallet = new Wallet();
			$_wallet->user = $this;
			$_wallet->save();
			
			//如果是买家
			if($this->type == 0){
					$_coupon = new Coupon();
					$coupon_list = $_coupon->getListByPage(0, $_coupon->getCount());
					foreach ($coupon_list as $_coupon_){
						if($_coupon_->get_type == COUPON_GET_TYPE_REGISTER && $_coupon_->status == 1){
							for($i = 0;$i < $_coupon_->get_count;++$i){
								$_user_coupon = new UserCoupon();
								$_user_coupon->user = $this;
								$_user_coupon->coupon = $_coupon_;
								$_user_coupon->save();
							}
						}
					}
			}
			
			/*
			$_easyMob = new EasyMob();
			$easymob_id = easymob_id_encode($db->getLastInsertId('id'));
			$options = array('username' => $easymob_id,'password' => md5($easymob_id));
			$result = $_easyMob->openRegister($options);
			_writeLog('easymob_register:'.$result);
			*/
		}
		
		$this->sec = $sec;
	}
	
	/**
	 * 登录
	 */
	public function login(){
		$db = DbConnection::getInstance();
		
		$sql = "select id,nam,adr from tb_user where tel = ? and sec = ? and type = ? and status = 1";
		$para_array = array($this->tel,$this->sec,$this->type);
		$rs = $db->query($sql,$para_array);
		if(count($rs) > 0){
			$this->id = intval($rs[0]['id']);
			$this->nam = $rs[0]['nam'];
			$this->adr = $rs[0]['adr'];
		}else{
			$this->id = 0;
		}
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_user(tel,sec,type) values(?,?,?)";
		$para_array = array($this->tel,'',$this->type);
		$db->exec($sql, $para_array);
			
		$this->id = $db->getLastInsertId('id');
		
		$_wallet = new Wallet();
		$_wallet->user = $this;
		$_wallet->save();
	}
	
	/**
	 * 更新基本信息
	 */
	public function updateBase(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_user set nam = ?,adr = ?,ton = ?,lat = ? where id = ?";
		$para_array = array($this->nam,$this->adr,$this->ton,$this->lat,$this->id);
		$db->exec($sql,$para_array);
	}
	
	/**
	 * 更新信息
	 */
	public function update(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_user set nam = ?,adr = ?,ton = ?,lat = ?,lp1 = ?,lp2 = ?,pic = ?,license = ?,agent = ?,enb = ? where id = ? and enb != ?";
		$para_array = array($this->nam,$this->adr,$this->ton,$this->lat,$this->lp1,$this->lp2,$this->pic,$this->license,$this->agent,0,$this->id,1);
		$db->exec($sql,$para_array);
	}
	
	/**
	 * 更新头像
	 */
	public function updatePic(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_user set pic = ? where id = ?";
		$para_array = array($this->pic,$this->id);
		$db->exec($sql,$para_array);
	}
	
	/**
	 * 更新商圈
	 */
	public function updateBusinessArea(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_user set business_area_id = ? where id = ?";
		$para_array = array($this->business_area->id,$this->id);
		$db->exec($sql,$para_array);
	}
	
	/**
	 * 审核通过
	 */
	public function audit(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_user set enb = ? where id = ?";
		$para_array = array(1,$this->id);
		$db->exec($sql,$para_array);
	}
	
	/**
	 * 审核不通过
	 */
	public function refuse(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_user set enb = ? where id = ?";
		$para_array = array(2,$this->id);
		$db->exec($sql,$para_array);
	}
	
	/**
	 * 更新卖家积分和等级
	 */
	public function updateSellLevelAndPoint(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_user set sell_point = ?,sell_level = ? where id = ?";
		$para_array = array($this->sell_point,$this->sell_level,$this->id);
		$db->exec($sql,$para_array);
	}
	
	/**
	 * 更新订单评分
	 */
	public function updateSellScore(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_user set order_score_count = ?,sell_match = ?,sell_service = ?,sell_speed = ? where id = ?";
		$para_array = array($this->order_score_count,$this->sell_match,$this->sell_service,$this->sell_speed,$this->id);
		$db->exec($sql,$para_array);
	}
	
	/**
	 * 禁用
	 */
	public function close(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_user set status = 0 where id = ?";
		$para_array = array($this->id);
		$db->exec($sql,$para_array);
	}
	
	/**
	 * 启用
	 */
	public function open(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_user set status = 1 where id = ?";
		$para_array = array($this->id);
		$db->exec($sql,$para_array);
	}
	
	/**
	 * 申请审核
	 */
	public function requestAudit(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_user set enb = 3 where id = ?";
		$para_array = array($this->id);
		$db->exec($sql,$para_array);
	}
	
	/**
	 * 修改激光推送别名
	 */
	public function updateAlias(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_user set jpush_alias = ? where id = ?";
		$para_array = array($this->jpush_alias,$this->id);
		$db->exec($sql,$para_array);
	}
}
?>