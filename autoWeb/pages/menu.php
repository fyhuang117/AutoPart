
<!--<ul class="nav nav-sidebar">
  <li class="custom-sidebar-parent active"><a href="../pages/businessarea.php"><span class="custom-sidebar-icon">+</span> 商圈</a></li>
  <li class="custom-sidebar-child"><a href="">2</a></li>
  <li class="custom-sidebar-child"><a href="">3</a></li>
  <li class="custom-sidebar-child"><a href="">4</a></li>
</ul>
<ul class="nav nav-sidebar">
  <li class="custom-sidebar-parent"><a href="../pages/parttype.php"><span class="custom-sidebar-icon">+</span> 配件</a></li>
  <li class="custom-sidebar-child"><a href="">2</a></li>
  <li class="custom-sidebar-child"><a href="">3</a></li>
  <li class="custom-sidebar-child"><a href="">4</a></li>
</ul>-->
<ul class="nav nav-sidebar">
	<li <?php if(strstr($_SERVER['PHP_SELF'],'businessarea.php')) echo 'class="active"'?>><a href="../pages/businessarea.php" >- 商圈</a></li>
    <li <?php if(strstr($_SERVER['PHP_SELF'],'parttype.php')) echo 'class="active"'?>><a href="../pages/parttype.php">- 配件</a></li>
  <li <?php if(strstr($_SERVER['PHP_SELF'],'partsband.php')) echo 'class="active"'?>><a href="../pages/partsband.php">- 配件品牌</a></li>
  <li <?php if(strstr($_SERVER['PHP_SELF'],'buy.php')) echo 'class="active"'?>><a href="../pages/buy.php">- 买家</a></li>
  <li <?php if(strstr($_SERVER['PHP_SELF'],'sell.php')) echo 'class="active"'?>><a href="../pages/sell.php">- 卖家</a></li>
  <li <?php if(strstr($_SERVER['PHP_SELF'],'coupon.php')) echo 'class="active"'?>><a href="../pages/coupon.php">- 优惠券管理</a></li>
   <li <?php if(strstr($_SERVER['PHP_SELF'],'cash.php')) echo 'class="active"'?>><a href="../pages/cash.php">- 提现申请</a></li>
   <li <?php if(strstr($_SERVER['PHP_SELF'],'back.php')) echo 'class="active"'?>><a href="../pages/back.php">- 退款申请</a></li>
  <li <?php if(strstr($_SERVER['PHP_SELF'],'ad.php')) echo 'class="active"'?>><a href="../pages/ad.php">- 广告</a></li>
  <li <?php if(strstr($_SERVER['PHP_SELF'],'carband.php')) echo 'class="active"'?>><a href="../pages/carband.php">- 汽车品牌</a></li>
  <li <?php if(strstr($_SERVER['PHP_SELF'],'carbandsub.php')) echo 'class="active"'?>><a href="../pages/carbandsub.php">- 子品牌</a></li>
  <li <?php if(strstr($_SERVER['PHP_SELF'],'line.php')) echo 'class="active"'?>><a href="../pages/line.php">- 车系</a></li>
  <li <?php if(strstr($_SERVER['PHP_SELF'],'car.php')) echo 'class="active"'?>><a href="../pages/car.php">- 年款</a></li>
  <!-- <li <?php if(strstr($_SERVER['PHP_SELF'],'inquiry.php')) echo 'class="active"'?>><a href="../pages/inquiry.php">- 询价单</a></li> -->
  <li <?php if(strstr($_SERVER['PHP_SELF'],'order.php')) echo 'class="active"'?>><a href="../pages/order.php">- 订单</a></li>
  <li <?php if(strstr($_SERVER['PHP_SELF'],'share.php')) echo 'class="active"'?>><a href="../pages/share.php">- 分享统计</a></li>
  <li <?php if(strstr($_SERVER['PHP_SELF'],'systemmessage.php')) echo 'class="active"'?>><a href="../pages/systemmessage.php">- 系统通知</a></li>
  <li <?php if(strstr($_SERVER['PHP_SELF'],'creditlevel.php')) echo 'class="active"'?>><a href="../pages/creditlevel.php">- 信用等级</a></li>
  <!-- <li <?php if(strstr($_SERVER['PHP_SELF'],'system.php')) echo 'class="active"'?>><a href="../pages/system.php">- 系统信息</a></li> -->
</ul>
