<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/User.class.php';
require_once '../model/BusinessArea.class.php';

$_user = new User();
$_user->id = intval($_GET['id']);
$_user->init();

$_businessArea = new BusinessArea();
$businessArea_list = $_businessArea->getList();

?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>易配购后台维护系统</title>
<link href="css/basic.css" rel="stylesheet">
<style>
table{ margin:0 auto; width:90%;}
</style>
<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
<script type="text/javascript" src="js/js.js"></script>
</head>

<body>
<form action="user_update.php" method="post" id="ad_update" enctype='multipart/form-data'>
<input type="hidden" name="id" value="<?php echo $_user->id;?>"/>
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="left" width="120">所属商圈</td>
    <td>
    	<select name="business_area_id" class="form-control">
    		<?php 
    			foreach ($businessArea_list as $_businessArea_){
					if($_user->business_area->id == $_businessArea_->id){
						echo '<option value="'.$_businessArea_->id.'" selected>'.$_businessArea_->name.'</option>';
					}else{
						echo '<option value="'.$_businessArea_->id.'">'.$_businessArea_->name.'</option>';
					}
    			}
    		?>
    	</select>
    </td>
  </tr>
  <tr>
   

  </tr>
</table>
</form>
</body>