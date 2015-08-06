<?php
require_once '../common/constant.php';
require_once '../common/FileManager.class.php';
require_once '../common/verify_permission.php';
require_once '../model/PartType.class.php';

$_partType = new PartType();
$_partType->id = $_GET['id'];
$_partType->init();

$_file_manager = new FileManager();
if($_partType->pic != ''){
	$_file_manager->file_name = $_partType->pic;
	$_file_manager->delFile(PARTTYPE_PIC_PATH);
}

function del_child($_partType){
	$_partType_del = new PartType();
	$_partType_del->par = $_partType->id;
	$partType_list = $_partType_del->getListByPar();
	if(count($partType_list) > 0){
		foreach ($partType_list as $_partType_del_){
			del_child($_partType_del_);
		}
	}
	
	if($_partType->pic != ''){
		$_file_manager = new FileManager();
		
		$_file_manager->file_name = $_partType->pic;
		$_file_manager->delFile(PARTTYPE_PIC_PATH);
	}
	
	$_partType->del();
}

del_child($_partType);
?>
<meta charset="utf-8"/>
<script>
alert("删除成功");
document.location.href = "../pages/parttype.php";
</script>