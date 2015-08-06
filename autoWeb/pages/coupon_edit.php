<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/Coupon.class.php';

$_coupon = new Coupon();
$_coupon->id = intval($_GET['id']);
$_coupon->init();


?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>易配购后台维护系统</title>
<link href="css/basic.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/date.css"/>
<style>
table{ margin:0 auto; width:90%;}
td{ padding:4px 0;}
</style>
<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
<script src="js/date.js"></script>
</head>

<body>
<form action="coupon_update.php" method="post" id="ad_update" enctype='multipart/form-data'>
<input type="hidden" name="id" value="<?php echo $_coupon->id;?>"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabls">
  <tr>
                  <td align="left">名称</td>
                  <td><input type="text" name="name" class="form-control" value="<?php echo $_coupon->name;?>"></td>
                </tr>
            <tr>
                  <td align="left">额度</td>
                  <td><input type="text" name="price" class="form-control" value="<?php echo $_coupon->price;?>"></td>
                </tr>
               <tr>
                  <td align="left">最低消费</td>
                  <td><input type="text" name="need_min" class="form-control" value="<?php echo $_coupon->need_min;?>"></td>
                </tr>
            <tr>
                  <td align="left" width="120">获取方式</td>
                  <td><select name="get_type"  class="form-control">
                      <option value="<?php echo COUPON_GET_TYPE_SHARE;?>" <?php echo $_coupon->get_type==COUPON_GET_TYPE_SHARE?'selected':'';?>>被分享人</option>
                      <option value="<?php echo COUPON_GET_TYPE_REGISTER;?>" <?php echo $_coupon->get_type==COUPON_GET_TYPE_REGISTER?'selected':'';?>>注册</option>
                      <option value="<?php echo COUPON_GET_TYPE_FIRST_ORDER;?>" <?php echo $_coupon->get_type==COUPON_GET_TYPE_FIRST_ORDER?'selected':'';?>>首单</option>
                      <option value="<?php echo COUPON_GET_TYPE_SHARE_FIRST_ORDER;?>" <?php echo $_coupon->get_type==COUPON_GET_TYPE_SHARE_FIRST_ORDER?'selected':'';?>>被分享人首单,分享人领</option>
                      <option value="<?php echo COUPON_GET_TYPE_DATE;?>" <?php echo $_coupon->get_type==COUPON_GET_TYPE_DATE?'selected':'';?>>特定时间</option>
                    </select></td>
                </tr>
               <tr>
                  <td align="left">领取时间</td>
                  <td><input type="text" name="get_date" class="form-control datetSelect" value="<?php echo $_coupon->get_date!='0000-00-00'?$_coupon->get_date:'';?>"></td>
                </tr>
                 <tr>
                  <td align="left">领取数量</td>
                  <td><input type="text" name="get_count" class="form-control" value="<?php echo $_coupon->get_count;?>"></td>
                </tr>
            <tr>
                  <td align="left">开始时间</td>
                  <td><input type="text" name="begin_date" class="form-control datetSelect" value="<?php echo $_coupon->begin_date;?>"></td>
                </tr>
            <tr>
                  <td align="left">结束时间</td>
                  <td><input type="text" name="end_date" class="form-control datetSelect" value="<?php echo $_coupon->end_date;?>"></td>
                </tr>

</table>
</form>
</body>
<script src="js/date.js"></script>
<script>
$('.datetSelect').datetimepicker({
	lang:'ch',
	timepicker:false,
	format:'Y-m-d',
	formatDate:'Y-m-d'
});

    </script>