<?php
require_once '../common/constant.php';
require_once '../common/FileManager.class.php';
require_once '../common/verify_permission.php';
require_once '../model/PartType.class.php';

if(trim($_POST['nam']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("名称不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

$_partType = new PartType();
$_partType->type = intval($_POST['type']);
$_partType->nam = $_POST['nam'];

//$_file_manager = new FileManager();
//$_file_manager->upload($_FILES["pic"], '', PARTTYPE_PIC_PATH);
//$_partType->pic = $_file_manager->file_name;

$_partType->par = $_POST['parent_id'];

$ban = ',';
if(isset($_POST['band_id'])){
	foreach ($_POST['band_id'] as $band_id){
		$ban .= $band_id.',';
	}
}
$_partType->ban = $ban;

$cars = ',';
if(isset($_POST['car_id'])){
	foreach ($_POST['car_id'] as $car_id){
		$cars .= $car_id.',';
	}
}
$_partType->cars = $cars;

$h = 0;
if(isset($_POST['h'])){
	$h = 1;
}
$_partType->h = $h;

$_partType->is_select_car = intval($_POST['is_select_car']);
$_partType->is_select_quality = intval($_POST['is_select_quality']);

$_partType->save();

echo '<tr class="bottbords">
							<td align="center">';
switch ($_partType->type){
	case PART_TYPE_N:
		echo '配件';
		break;
	case PART_TYPE_V:
		echo '易损品';
		break;
	case PART_TYPE_C:
		echo '消耗品';
		break;
}
echo '</td>
				            <td align="center"><a href="javascript:void(0);" class="ajaxLink" rel="../pages/parttype_data.php?parent_id='.$_partType->id.'">'.$_partType->nam.'</a></td>
							<td align="center">';
if($_partType->pic != ''){
	echo '<img src="'.PARTTYPE_PIC_URL.$_partType->pic.'" height="50" width="50"/>';
}
	
echo '</td>
				          <td align="center">';

$partBand_str = $_partType->ban;
$partBand_id_array = explode(',', $partBand_str);
foreach ($partBand_id_array as $partBand_id){
	if($partBand_id != NULL && $partBand_id != '' && intval($partBand_id) > 0){
		$_partBand = new PartBand();
		$_partBand->id = intval($partBand_id);
		$_partBand->init();

		echo $_partBand->nam.' ';
	}
}

echo '</td><td align="center">';

if($_partType->h == 1){
	echo '是';
}else{
	echo '否';
}

echo '</td><td align="center">';

if($_partType->is_select_car == 1){
	echo '是';
}else{
	echo '否';
}

echo '</td><td align="center">';

if($_partType->is_select_quality == 1){
	echo '是';
}else{
	echo '否';
}

echo '</td>
				            <td align="center" class="border_ri_no"><a href="#" class="a_css" onClick="edit('.$_partType->id.');">编辑</a>&nbsp;&nbsp;<a href="../pages/parttype_del.php?id='.$_partType->id.'" class="a_css">删除</a></td>
				          </tr>';
?>
