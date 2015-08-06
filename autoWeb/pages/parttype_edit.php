<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/PartType.class.php';
require_once '../model/PartBand.class.php';
require_once '../model/Car.class.php';

$_partType = new PartType();
$_partType->id = $_GET['id'];
$_partType->init();
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
<form action="parttype_update.php" method="post" id="ad_update" enctype='multipart/form-data'>
<input type="hidden" name="id" value="<?php echo $_partType->id;?>"/>
<input type="hidden" name="par" value="<?php echo $_partType->par;?>"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabls">
  <tr>
    <td align="left" width="100">类型</td>
    <td><select name="type" class="form-control">
    	<option value="<?php echo PART_TYPE_N;?>" <?php echo $_partType->type == PART_TYPE_N?'selected':''?>>配件</option>
    	<option value="<?php echo PART_TYPE_V;?>" <?php echo $_partType->type == PART_TYPE_V?'selected':''?>>易损品</option>
    	<option value="<?php echo PART_TYPE_C;?>" <?php echo $_partType->type == PART_TYPE_C?'selected':''?>>消耗品</option>
    </select></td>
  </tr>
  <tr>
    <td align="left">名称</td>
    <td><input type="text" name="nam"  class="form-control" value="<?php echo $_partType->nam;?>"></td>
  </tr>
  <tr>
    <td align="left">对应品牌</td>
    <td>
    	 <button type="button" class="btn btn-info btn-xs" onClick="window.parent.selectBand()">选择品牌</button>
				  <div id="bandListDiv" style="display:none;">
                  	
                  </div>
    </td>
  </tr>
  <tr>
    <td align="left">是否选车型</td>
    <td>
    	<select name="is_select_car"  class="form-control">
    		<option value="0" <?php echo $_partType->is_select_car==0?'selected':'';?>>否</option>
    		<option value="1" <?php echo $_partType->is_select_car==1?'selected':'';?>>是</option>
    	</select>
    </td>
  </tr>
  <tr>
    <td align="left">是否选品质</td>
    <td>
    	<select name="is_select_quality"  class="form-control">
    		<option value="0" <?php echo $_partType->is_select_quality==0?'selected':'';?>>否</option>
    		<option value="1" <?php echo $_partType->is_select_quality==1?'selected':'';?>>是</option>
    	</select>
    </td>
  </tr>
  <!-- 
  <tr>
    <td align="left">对应车型</td>
    <td>
    	<?php 
    	/*
    		$_car = new Car();
    		$car_list = $_car->getList();
    		foreach ($car_list as $_car_){
				if(strstr($_partType->cars,','.$_car_->id.',')){
					echo '<input type="checkbox" name="car_id[]" value="'.$_car_->id.'" checked/>'.$_car_->nam;
				}else{
					echo '<input type="checkbox" name="car_id[]" value="'.$_car_->id.'"/>'.$_car_->nam;
				}
    			
    		}
    	*/
    	?>
    </td>
  </tr>
   -->
  <tr>
    <td align="left">热门</td>
    <td>
    	<input type="checkbox" name="h" value="1" <?php echo $_partType->h == 1?'checked':''?>/>是
    </td>
  </tr>
  
</table>
</form>
</body>