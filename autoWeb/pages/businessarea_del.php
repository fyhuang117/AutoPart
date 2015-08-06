<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/BusinessArea.class.php';

$_businessArea = new BusinessArea();
$_businessArea->id = intval($_GET['id']);
$_businessArea->init();

function del_child($_businessArea){
	$_area_del = new BusinessArea();
	$_area_del->parent_id = $_businessArea->id;
	$area_list = $_area_del->getListByParent();
	if(count($area_list) > 0){
		foreach ($area_list as $_area_del_){
			del_child($_area_del_);
		}
	}

	$_businessArea->del();
}

del_child($_businessArea);
?>
<meta charset="utf-8"/>
<script>
alert("删除成功");
document.location.href = "../pages/businessarea.php";
</script>