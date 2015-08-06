<?php
require_once '../common/DbConnection.class.php';
require_once '../model/User.class.php';
require_once '../model/UserCoupon.class.php';

class Order{
	public $id = 0;
	public $num = '';
	public $sell_user = NULL;
	public $sel = '';
	public $buy_user = NULL;
	public $buy = '';
	public $pri = 0;
	public $par = '';
	public $ban = '';
	public $car = '';
	public $c = 0;
	public $parttype_quality = 0;
	public $avi = 0;
	public $qau = '';
	public $pay = 0;
	public $del = '';
	public $adr = '';
	public $pof = 0;
	public $bil = '';
	public $pic1 = '';
	public $pic2 = '';
	public $pic3 = '';
	public $create_time = '';
	public $status = 0;
	public $buyer_score = 0;
	public $comments = '';
	public $match = 0;
	public $service = 0;
	public $speed = 0;
	public $update_time = '';
	public $real_price = 0;
	public $user_coupon = NULL;
	public $is_freeze = 0;
	
	public function init(){
		$db = DbConnection::getInstance();
		
		$sql = "select num,sell_user_id,sel,buy_user_id,buy,pri,par,ban,car,c,parttype_quality,avi,qau,pay,del,adr,pof,bil,
				pic1,pic2,pic3,create_time,status,buyer_score,comments,matchs,service,speed,update_time,real_price,user_coupon_id from tb_order where id = ?";
		$para_array = array($this->id);
		$rs = $db->query($sql,$para_array);
		if (count($rs) > 0){
			$this->num = $rs[0]['num'];
			
			$_user = new User();
			$_user->id = intval($rs[0]['sell_user_id']);
			$_user->init();
			$this->sell_user = $_user;
			
			$this->sel = $rs[0]['sel'];
			
			$_user = new User();
			$_user->id = intval($rs[0]['buy_user_id']);
			$_user->init();
			$this->buy_user = $_user;
			
			$this->buy = $rs[0]['buy'];
			$this->pri = $rs[0]['pri'];
			$this->par = $rs[0]['par'];
			$this->ban = $rs[0]['ban'];
			$this->car = $rs[0]['car'];
			$this->c = intval($rs[0]['c']);
			$this->parttype_quality = intval($rs[0]['parttype_quality']);
			$this->avi = intval($rs[0]['avi']);
			$this->qau = $rs[0]['qau'];
			$this->pay = intval($rs[0]['pay']);
			$this->del = $rs[0]['del'];
			$this->adr = $rs[0]['adr'];
			$this->pof = $rs[0]['pof'];
			$this->bil = $rs[0]['bil'];
			$this->pic1 = $rs[0]['pic1'];
			$this->pic2 = $rs[0]['pic2'];
			$this->pic3 = $rs[0]['pic3'];
			$this->create_time = $rs[0]['create_time'];
			$this->status = intval($rs[0]['status']);
			$this->buyer_score = intval($rs[0]['buyer_score']);
			$this->comments = $rs[0]['comments'];
			$this->match = intval($rs[0]['matchs']);
			$this->service = intval($rs[0]['service']);
			$this->speed = intval($rs[0]['speed']);
			$this->update_time = $rs[0]['update_time'];
			$this->real_price = floatval($rs[0]['real_price']);
			
			$_user_coupon = new UserCoupon();
			$_user_coupon->id = intval($rs[0]['user_coupon_id']);
			$_user_coupon->init();
			$this->user_coupon = $_user_coupon;
		}else{
			$this->id = 0;
			$this->sell_user = new User();
			$this->buy_user = new User();
			$this->user_coupon = new UserCoupon();
		}
	}
	
	public function getCount(){
		$db = DbConnection::getInstance();
		
		$sql = "select count(0) c from tb_order";
		$rs = $db->query($sql);
		return intval($rs[0]['c']);
	}
	
	public function getCountByNum(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_order where num like ?";
		$para_array = array('%'.$this->num.'%');
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	/**
	 * 根据卖家获取未完成的订单
	 * @return number
	 */
	public function getNoCompleteCountBySell(){
		$db = DbConnection::getInstance();
	
		$sql = "select count(0) c from tb_order where sell_user_id = ? and status != ? and status != ? and is_freeze = 0";
		$para_array = array($this->sell_user->id,ORDER_STATUS_CANCEL,ORDER_STATUS_COMPLETE);
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	public function getListByPage($start,$limit){
		$db = DbConnection::getInstance();
	
		$order_list = array();
	
		$sql = "select id,num,sell_user_id,sel,buy_user_id,buy,pri,par,ban,car,c,parttype_quality,avi,qau,pay,del,adr,pof,bil,pic1,pic2,pic3,create_time,status,buyer_score,comments,matchs,service,speed,update_time,is_freeze from tb_order order by update_time desc limit $start,$limit";
		$rs = $db->query($sql);
		foreach ($rs as $row){
			$_order = new Order();
			$_order->id = intval($row['id']);
			$_order->num = $row['num'];
	
			$_user = new User();
			$_user->id = intval($row['sell_user_id']);
			$_user->init();
			$_order->sell_user = $_user;
	
			$_order->sel = $row['sel'];
	
			$_user = new User();
			$_user->id = intval($row['buy_user_id']);
			$_user->init();
			$_order->buy_user = $_user;
	
			$_order->buy = $row['buy'];
			$_order->pri = $row['pri'];
			$_order->par = $row['par'];
			$_order->ban = $row['ban'];
			$_order->car = $row['car'];
			$_order->c = intval($row['c']);
			$_order->parttype_quality = intval($row['parttype_quality']);
			$_order->avi = intval($row['avi']);
			$_order->qau = $row['qau'];
			$_order->pay = intval($row['pay']);
			$_order->del = $row['del'];
			$_order->adr = $row['adr'];
			$_order->pof = $row['pof'];
			$_order->bil = $row['bil'];
			$_order->pic1 = $row['pic1'];
			$_order->pic2 = $row['pic2'];
			$_order->pic3 = $row['pic3'];
			$_order->create_time = $row['create_time'];
			$_order->status = intval($row['status']);
			$_order->buyer_score = intval($row['buyer_score']);
			$_order->comments = $row['comments'];
			$_order->match = intval($row['matchs']);
			$_order->service = intval($row['service']);
			$_order->speed = intval($row['speed']);
			$_order->update_time = $row['update_time'];
			$_order->is_freeze = intval($row['is_freeze']);	
			
			$order_list[] = $_order;
		}
	
		return $order_list;
	}
	
	public function getListByPageAndNum($start,$limit){
		$db = DbConnection::getInstance();
	
		$order_list = array();
	
		$sql = "select id,num,sell_user_id,sel,buy_user_id,buy,pri,par,ban,car,c,parttype_quality,avi,qau,pay,del,adr,pof,bil,pic1,pic2,pic3,create_time,status,buyer_score,comments,matchs,service,speed,update_time from tb_order where num like ? order by update_time desc limit $start,$limit";
		$para_array = array('%'.$this->num.'%');
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_order = new Order();
			$_order->id = intval($row['id']);
			$_order->num = $row['num'];
	
			$_user = new User();
			$_user->id = intval($row['sell_user_id']);
			$_user->init();
			$_order->sell_user = $_user;
	
			$_order->sel = $row['sel'];
	
			$_user = new User();
			$_user->id = intval($row['buy_user_id']);
			$_user->init();
			$_order->buy_user = $_user;
	
			$_order->buy = $row['buy'];
			$_order->pri = $row['pri'];
			$_order->par = $row['par'];
			$_order->ban = $row['ban'];
			$_order->car = $row['car'];
			$_order->c = intval($row['c']);
			$_order->parttype_quality = intval($row['parttype_quality']);
			$_order->avi = intval($row['avi']);
			$_order->qau = $row['qau'];
			$_order->pay = intval($row['pay']);
			$_order->del = $row['del'];
			$_order->adr = $row['adr'];
			$_order->pof = $row['pof'];
			$_order->bil = $row['bil'];
			$_order->pic1 = $row['pic1'];
			$_order->pic2 = $row['pic2'];
			$_order->pic3 = $row['pic3'];
			$_order->create_time = $row['create_time'];
			$_order->status = intval($row['status']);
			$_order->buyer_score = intval($row['buyer_score']);
			$_order->comments = $row['comments'];
			$_order->match = intval($row['matchs']);
			$_order->service = intval($row['service']);
			$_order->speed = intval($row['speed']);
			$_order->update_time = $row['update_time'];
	
			$order_list[] = $_order;
		}
	
		return $order_list;
	}
	
	public function getListByBuyUser(){
		$db = DbConnection::getInstance();
		
		$order_list = array();
		
		$sql = "select id,num,sell_user_id,sel,buy_user_id,buy,pri,par,ban,car,c,parttype_quality,avi,qau,pay,del,adr,pof,bil,pic1,pic2,pic3,create_time,status,buyer_score,comments,matchs,service,speed,update_time from tb_order where buy_user_id = ? and is_freeze = 0 order by update_time desc";
		$para_array = array($this->buy_user->id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_order = new Order();
			$_order->id = intval($row['id']);
			$_order->num = $row['num'];
				
			$_user = new User();
			$_user->id = intval($row['sell_user_id']);
			$_user->init();
			$_order->sell_user = $_user;
				
			$_order->sel = $row['sel'];

			$_order->buy_user = $this->buy_user;
				
			$_order->buy = $row['buy'];
			$_order->pri = $row['pri'];
			$_order->par = $row['par'];
			$_order->ban = $row['ban'];
			$_order->car = $row['car'];
			$_order->c = intval($row['c']);
			$_order->parttype_quality = intval($row['parttype_quality']);
			$_order->avi = intval($row['avi']);
			$_order->qau = $row['qau'];
			$_order->pay = intval($row['pay']);
			$_order->del = $row['del'];
			$_order->adr = $row['adr'];
			$_order->pof = $row['pof'];
			$_order->bil = $row['bil'];
			$_order->pic1 = $row['pic1'];
			$_order->pic2 = $row['pic2'];
			$_order->pic3 = $row['pic3'];
			$_order->create_time = $row['create_time'];
			$_order->status = intval($row['status']);
			$_order->buyer_score = intval($row['buyer_score']);
			$_order->comments = $row['comments'];
			$_order->match = intval($row['matchs']);
			$_order->service = intval($row['service']);
			$_order->speed = intval($row['speed']);
			$_order->update_time = $row['update_time'];
			
			$order_list[] = $_order;
		}
		
		return $order_list;
	}
	
	public function getListByBuyUserAndStatusArray($status_array){
		$db = DbConnection::getInstance();
	
		$order_list = array();
	
		$sql = "select id,num,sell_user_id,sel,buy_user_id,buy,pri,par,ban,car,c,parttype_quality,avi,qau,pay,del,adr,pof,bil,pic1,pic2,pic3,create_time,status,buyer_score,comments,matchs,service,speed,update_time from tb_order where buy_user_id = ? and is_freeze = 0 and (";
		foreach ($status_array as $status){
			$sql .= ' status = '.$status.' or ';
		}
		$sql = substr($sql, 0,strlen($sql) - 4);
		$sql .= ') order by update_time desc';
		$para_array = array($this->buy_user->id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_order = new Order();
			$_order->id = intval($row['id']);
			$_order->num = $row['num'];
	
			$_user = new User();
			$_user->id = intval($row['sell_user_id']);
			$_user->init();
			$_order->sell_user = $_user;
	
			$_order->sel = $row['sel'];
	
			$_order->buy_user = $this->buy_user;
	
			$_order->buy = $row['buy'];
			$_order->pri = $row['pri'];
			$_order->par = $row['par'];
			$_order->ban = $row['ban'];
			$_order->car = $row['car'];
			$_order->c = intval($row['c']);
			$_order->parttype_quality = intval($row['parttype_quality']);
			$_order->avi = intval($row['avi']);
			$_order->qau = $row['qau'];
			$_order->pay = intval($row['pay']);
			$_order->del = $row['del'];
			$_order->adr = $row['adr'];
			$_order->pof = $row['pof'];
			$_order->bil = $row['bil'];
			$_order->pic1 = $row['pic1'];
			$_order->pic2 = $row['pic2'];
			$_order->pic3 = $row['pic3'];
			$_order->create_time = $row['create_time'];
			$_order->status = intval($row['status']);
			$_order->buyer_score = intval($row['buyer_score']);
			$_order->comments = $row['comments'];
			$_order->match = intval($row['matchs']);
			$_order->service = intval($row['service']);
			$_order->speed = intval($row['speed']);
			$_order->update_time = $row['update_time'];
	
			$order_list[] = $_order;
		}
	
		return $order_list;
	}
	
	public function getListBySellUserAndStatusArray($status_array){
		$db = DbConnection::getInstance();
	
		$order_list = array();
	
		$sql = "select id,num,sell_user_id,sel,buy_user_id,buy,pri,par,ban,car,c,parttype_quality,avi,qau,pay,del,adr,pof,bil,pic1,pic2,pic3,create_time,status,buyer_score,comments,matchs,service,speed,update_time from tb_order where sell_user_id = ? and is_freeze = 0 and (";
		foreach ($status_array as $status){
			$sql .= ' status = '.$status.' or ';
		}
		$sql = substr($sql, 0,strlen($sql) - 4);
		$sql .= ') order by update_time desc';
		$para_array = array($this->sell_user->id);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_order = new Order();
			$_order->id = intval($row['id']);
			$_order->num = $row['num'];
	
			$_user = new User();
			$_user->id = intval($row['buy_user_id']);
			$_user->init();
			$_order->buy_user = $_user;
	
			$_order->sel = $row['sel'];
	
			$_order->sell_user = $this->sell_user;
	
			$_order->buy = $row['buy'];
			$_order->pri = $row['pri'];
			$_order->par = $row['par'];
			$_order->ban = $row['ban'];
			$_order->car = $row['car'];
			$_order->c = intval($row['c']);
			$_order->parttype_quality = intval($row['parttype_quality']);
			$_order->avi = intval($row['avi']);
			$_order->qau = $row['qau'];
			$_order->pay = intval($row['pay']);
			$_order->del = $row['del'];
			$_order->adr = $row['adr'];
			$_order->pof = $row['pof'];
			$_order->bil = $row['bil'];
			$_order->pic1 = $row['pic1'];
			$_order->pic2 = $row['pic2'];
			$_order->pic3 = $row['pic3'];
			$_order->create_time = $row['create_time'];
			$_order->status = intval($row['status']);
			$_order->buyer_score = intval($row['buyer_score']);
			$_order->comments = $row['comments'];
			$_order->match = intval($row['matchs']);
			$_order->service = intval($row['service']);
			$_order->speed = intval($row['speed']);
			$_order->update_time = $row['update_time'];
	
			$order_list[] = $_order;
		}
	
		return $order_list;
	}
	
	public function getListByBuyUserAndStatus(){
		$db = DbConnection::getInstance();
	
		$order_list = array();
	
		$sql = "select id,num,sell_user_id,sel,buy_user_id,buy,pri,par,ban,car,c,parttype_quality,avi,qau,pay,del,adr,pof,bil,pic1,pic2,pic3,create_time,status,buyer_score,comments,matchs,service,speed,update_time from tb_order where buy_user_id = ? and is_freeze = 0 and status = ? order by update_time desc";
		$para_array = array($this->buy_user->id,$this->status);
		$rs = $db->query($sql,$para_array);
		foreach ($rs as $row){
			$_order = new Order();
			$_order->id = intval($row['id']);
			$_order->num = $row['num'];
	
			$_user = new User();
			$_user->id = intval($row['sell_user_id']);
			$_user->init();
			$_order->sell_user = $_user;
	
			$_order->sel = $row['sel'];
	
			$_order->buy_user = $this->buy_user;
	
			$_order->buy = $row['buy'];
			$_order->pri = $row['pri'];
			$_order->par = $row['par'];
			$_order->ban = $row['ban'];
			$_order->car = $row['car'];
			$_order->c = intval($row['c']);
			$_order->parttype_quality = intval($row['parttype_quality']);
			$_order->avi = intval($row['avi']);
			$_order->qau = $row['qau'];
			$_order->pay = intval($row['pay']);
			$_order->del = $row['del'];
			$_order->adr = $row['adr'];
			$_order->pof = $row['pof'];
			$_order->bil = $row['bil'];
			$_order->pic1 = $row['pic1'];
			$_order->pic2 = $row['pic2'];
			$_order->pic3 = $row['pic3'];
			$_order->create_time = $row['create_time'];
			$_order->status = intval($row['status']);
			$_order->buyer_score = intval($row['buyer_score']);
			$_order->comments = $row['comments'];
			$_order->match = intval($row['matchs']);
			$_order->service = intval($row['service']);
			$_order->speed = intval($row['speed']);
			$_order->update_time = $row['update_time'];
				
			$order_list[] = $_order;
		}
	
		return $order_list;
	}
	
	public function save(){
		$db = DbConnection::getInstance();
		
		$sql = "insert into tb_order(num,sell_user_id,sel,buy_user_id,buy,pri,par,ban,car,c,parttype_quality,avi,qau,pay,del,adr,pof,bil,pic1,pic2,pic3,create_time,status,update_time) 
					values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,now())";
		$para_array = array($this->createNum(),$this->sell_user->id,$this->sel,$this->buy_user->id,$this->buy,$this->pri,$this->par,$this->ban,$this->car,$this->c,$this->parttype_quality,$this->avi,$this->qau,$this->pay,$this->del,$this->adr,$this->pof,
				$this->bil,$this->pic1,$this->pic2,$this->pic3,ORDER_STATUS_NOPAY);
		
		$db->exec($sql, $para_array);
		
		$this->id = $db->getLastInsertId('id');
	}
	
	private function createNum(){
		return time();
	}

	/**
	 * 取消订单
	 */
	public function cancel(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_order set status = ?,update_time = now() where id = ? and status = ?";
		$para_array = array(ORDER_STATUS_CANCEL,$this->id,ORDER_STATUS_NOPAY);
		
		$db->exec($sql, $para_array);
	}
	
	/**
	 * 付款
	 */
	public function pay(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_order set status = ?,update_time = now(),real_price = ?,user_coupon_id = ? where id = ? and status = ?";
		$para_array = array(ORDER_STATUS_NOSEND,$this->real_price,$this->user_coupon->id,$this->id,ORDER_STATUS_NOPAY);
	
		$db->exec($sql, $para_array);
	}
	
	/**
	 * 发货
	 */
	public function send(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_order set status = ?,update_time = now() where id = ? and status = ?";
		$para_array = array(ORDER_STATUS_SEND,$this->id,ORDER_STATUS_NOSEND);
	
		$db->exec($sql, $para_array);
	}
	
	/**
	 * 收货
	 */
	public function receive(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_order set status = ?,update_time = now() where id = ? and status = ?";
		$para_array = array(ORDER_STATUS_SCORE,$this->id,ORDER_STATUS_SEND);
	
		$db->exec($sql, $para_array);
	}
	
	public function update(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_order set pri = ?,c = ? where id = ? and status = ?";
		$para_array = array($this->pri,$this->c,$this->id,ORDER_STATUS_NOPAY);
		
		$db->exec($sql, $para_array);
	}
	
	/**
	 * 买家评价
	 */
	public function buyerScore(){
		$db = DbConnection::getInstance();
		
		$sql = "update tb_order set buyer_score = ?,comments = ?,matchs = ?,service = ?,speed = ?,status = ?,update_time = now() where id = ? and status = ?";
		$para_array = array($this->buyer_score,$this->comments,$this->match,$this->service,$this->speed,ORDER_STATUS_COMPLETE,$this->id,ORDER_STATUS_SCORE);
		
		$db->exec($sql, $para_array);
	}
	
	/**
	 * 查询即将到账
	 */
	public function getPriBySell(){
		$db = DbConnection::getInstance();
		
		$sql = "select sum(pri) pri from tb_order where sell_user_id = ? and status = ?";
		$para_array = array($this->sell_user->id,ORDER_STATUS_SEND);
		$rs = $db->query($sql,$para_array);
		return floatval($rs[0]['pri']);
	}
	
	/**
	 * 查询买家已完成订单数量
	 * @return number
	 */
	public function getCompleteCountByBuy(){
		$db = DbConnection::getInstance();

		$sql = "select count(0) c from tb_order where buy_user_id = ? and (status = ? or status = ?)";
		$para_array = array($this->buy_user->id,ORDER_STATUS_SCORE,ORDER_STATUS_COMPLETE);
		$rs = $db->query($sql,$para_array);
		return intval($rs[0]['c']);
	}
	
	/**
	 * 冻结
	 */
	public function freeze(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_order set is_freeze = 1 where id = ?";
		$para_array = array($this->id);
	
		$db->exec($sql, $para_array);
	}
	
	/**
	 * 解冻
	 */
	public function unfreeze(){
		$db = DbConnection::getInstance();
	
		$sql = "update tb_order set is_freeze = 0 where id = ?";
		$para_array = array($this->id);
	
		$db->exec($sql, $para_array);
	}
}
?>