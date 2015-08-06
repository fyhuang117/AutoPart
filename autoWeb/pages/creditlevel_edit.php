<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/CreditLevel.class.php';

$_creditLevel = new CreditLevel();
$_creditLevel->id = intval($_GET['id']);
$_creditLevel->init();


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
<form action="creditlevel_update.php" method="post" id="ad_update" enctype='multipart/form-data'>
<input type="hidden" name="id" value="<?php echo $_creditLevel->id;?>"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabls">
  <tr>
    <td align="left" width="80">等级</td>
    <td><input type="text" name="level"  class="form-control" value="<?php echo $_creditLevel->level;?>"></td>
  </tr>
  <tr>
    <td align="left">名称</td>
    <td><input type="text" name="name"  class="form-control" value="<?php echo $_creditLevel->name;?>"></td>
  </tr>
  <tr>
    <td align="left">积分</td>
    <td><input type="text" name="point"  class="form-control" value="<?php echo $_creditLevel->point;?>"></td>
  </tr>
</table>
</form>
</body>