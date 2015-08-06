<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/BandSub.class.php';
require_once '../model/Band.class.php';

$_band_sub = new BandSub();
$_band_sub->id = intval($_GET['id']);
$_band_sub->init();


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
<form action="carbandsub_update.php" method="post" id="ad_update" enctype='multipart/form-data'>
<input type="hidden" name="id" value="<?php echo $_band_sub->id;?>"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabls">
  <tr>
    <td align="left" width="120">品牌</td>
    <td>
    <select name="band_id" class="form-control">
    	<?php 
    		$_band = new Band();
    		$band_list = $_band->getList();
    		foreach ($band_list as $_band_){
				if($_band_sub->band->id == $_band_->id){
					echo '<option value="'.$_band_->id.'" selected>'.$_band_->nam.'</option>';
				}else{
					echo '<option value="'.$_band_->id.'">'.$_band_->nam.'</option>';
				}
    		}
    	?>
    </select>
    </td>
  </tr>
   <tr>
    <td align="left" width="50">名称</td>
    <td><input type="text" name="name"class="form-control" value="<?php echo $_band_sub->name;?>"></td>
  </tr>
  
</table>
</form>
</body>