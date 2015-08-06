<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/Ad.class.php';

$_ad = new Ad();
$_ad->id = intval($_GET['id']);
$_ad->init();


?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>易配购后台维护系统</title>
<link href="css/basic.css" rel="stylesheet">
<style>
table{ margin:0 auto; width:90%;}
td{ padding:6px 0;}
</style>
<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
<script type="text/javascript" src="js/js.js"></script>
</head>

<body>
<form action="ad_update.php" method="post" id="ad_update" enctype='multipart/form-data'>
<input type="hidden" name="id" value="<?php echo $_ad->id;?>"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabls">
  <tr>
    <td align="left" width="50">标题</td>
    <td><input type="text" name="tit" class="form-control" value="<?php echo $_ad->tit;?>"></td>
  </tr>
  <tr>
    <td align="left" width="50">图片</td>
    <td><input type="file" name="pic" class="form-control"></td>
  </tr>
  <tr>
    <td align="left" width="50">连接</td>
    <td><input type="text" name="url" class="form-control" value="<?php echo $_ad->url;?>"></td>
  </tr>
  
</table>
</form>
</body>