<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/System.class.php';

$_system = new System();
$_system->init();
?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>易配购后台维护系统</title>
<link href="css/basic.css" rel="stylesheet">
<style>
table{ margin:0 auto; width:90%;}
td{ padding:4px 0;}
</style>
<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
<script type="text/javascript" src="js/js.js"></script>
</head>

<body>
<form action="system_update.php" method="post" id="ad_update" enctype='multipart/form-data'>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabls">
   <tr>
    <td align="left" width="120">应用版本</td>
    <td><input type="text" name="app_version"  class="form-control" value="<?php echo $_system->app_version;?>"></td>
  </tr>
  <tr>
    <td align="left">应用协议</td>
    <td><input type="file" name="pic"  class="form-control"></td>
  </tr>
    <tr>
    <td align="left">客服电话</td>
    <td><input type="text" name="service_tel"  class="form-control" value="<?php echo $_system->service_tel;?>"></td>
  </tr>

</table>
</form>
</body>