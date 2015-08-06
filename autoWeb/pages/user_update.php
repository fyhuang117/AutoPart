<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/User.class.php';
require_once '../model/BusinessArea.class.php';

$_user = new User();
$_user->id = intval($_POST['id']);
$_user->init();

$_businessArea = new BusinessArea();
$_businessArea->id = intval($_POST['business_area_id']);
$_businessArea->init();
if($_businessArea->id > 0){
	$_user->business_area = $_businessArea;
	$_user->updateBusinessArea();	
}

?>
<meta charset="utf-8"/>
<script>
alert("保存成功");
<?php 
		if($_user->type == 0){
			echo 'parent.document.location.href = "../pages/buy.php";';
		}else{
			echo 'parent.document.location.href = "../pages/sell.php";';
		}
	?>
</script>