<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/BusinessArea.class.php';

if(trim($_POST['name']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("名称不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

if(trim($_POST['first_word']) == ''){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("首字母不能为空");';
	echo 'history.back();';
	echo '</script>';
	die();
}

$_businessArea = new BusinessArea();
$_businessArea->name = $_POST['name'];
$_businessArea->lat = floatval($_POST['lat']);
$_businessArea->lng = floatval($_POST['lng']);
$_businessArea->parent_id = intval($_POST['parent_id']);
$_businessArea->first_word = $_POST['first_word'];

$_businessArea->save();

echo '<tr class="bottbords">
				            <td width="14%" align="center">'.$_businessArea->id.'</td>
				            <td  width="14%" align="center"><a href="javascript:void(0);" class="ajaxLink" rel="../pages/businessarea_data.php?parent_id='.$_businessArea->id.'">'.$_businessArea->name.'</a></td>
							<td  width="14%" align="center">'.$_businessArea->lng.'</td>
							<td  width="14%" align="center">'.$_businessArea->lat.'</td>
				            <td width="30%" align="center" class="border_ri_no"><a href="#" class="a_css" onClick="edit('.$_businessArea->id.');">编辑</a>&nbsp;&nbsp;<a href="../pages/businessarea_del.php?id='.$_businessArea->id.'" class="a_css">删除</a></td>
				          </tr>';
?>
