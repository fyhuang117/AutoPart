<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/Car.class.php';
require_once '../model/Line.class.php';

$_car = new Car();
$_car->id = intval($_GET['id']);
$_car->init();


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
<form action="car_update.php" method="post" id="ad_update" enctype='multipart/form-data'>
<input type="hidden" name="id" value="<?php echo $_car->id;?>"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabls">
  <tr>
    <td align="left" width="80">车系</td>
    <td>
    <select name="line_id" class="form-control">
    	<?php 
    		$_line = new Line();
    		$line_list = $_line->getList();
    		foreach ($line_list as $_line_){
				if($_car->line->id == $_line_->id){
					echo '<option value="'.$_line_->id.'" selected>'.$_line_->nam.'</option>';
				}else{
					echo '<option value="'.$_line_->id.'">'.$_line_->nam.'</option>';
				}
    		}
    	?>
    </select>
    </td>
  </tr>
  <tr>
    <td align="left" width="50">名称</td>
    <td><input type="text" name="nam"  class="form-control" value="<?php echo $_car->nam;?>"/></td>
  </tr>
   <tr>
    <td align="left" width="50">排量</td>
    <td><input type="text" name="val"  class="form-control" value="<?php echo $_car->val;?>"></td>
  </tr>
    <tr>
    <td align="left" width="50">年份</td>
    <td><input type="text" name="yea"  class="form-control" value="<?php echo $_car->yea;?>"></td>
  </tr>

</table>
</form>
</body>