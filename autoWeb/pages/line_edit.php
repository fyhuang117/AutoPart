<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/BandSub.class.php';
require_once '../model/Line.class.php';

$_line = new Line();
$_line->id = intval($_GET['id']);
$_line->init();


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
<form action="line_update.php" method="post" id="ad_update" enctype='multipart/form-data'>
<input type="hidden" name="id" value="<?php echo $_line->id;?>"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabls">
  <tr>
    <td align="left" width="120">子品牌</td>
    <td>
    <select name="band_sub_id" class="form-control">
    	<?php 
    		$_band_sub = new BandSub();
    		$band_sub_list = $_band_sub->getList();
    		foreach ($band_sub_list as $_band_sub_){
				if($_line->band_sub->id == $_band_sub_->id){
					echo '<option value="'.$_band_sub_->id.'" selected>'.$_band_sub_->name.'</option>';
				}else{
					echo '<option value="'.$_band_sub_->id.'">'.$_band_sub_->name.'</option>';
				}
    		}
    	?>
    </select>
    </td>
  </tr>
   <tr>
    <td align="left" width="50">名称</td>
    <td><input type="text" name="nam" class="form-control" value="<?php echo $_line->nam;?>"></td>
  </tr>
 
</table>
</form>
</body>