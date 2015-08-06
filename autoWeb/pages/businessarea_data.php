<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/BusinessArea.class.php';

$_area = new BusinessArea();
$_area->parent_id = intval($_GET['parent_id']);
$area_list = $_area->getListByParent();
	?>
	<div class="ajaxTable" style=" margin-left:12px; border:1px solid #DEF4FE;"><a href="javascript:void(0);" class="ajaxClose">X</a>
    <table width="100%" border="0">
<tr>
            <th width="14%" align="center">编号</th>
            <th align="center">名称</th>
            <th width="14%" align="center">经度</th>
            <th width="14%" align="center">纬度</th>
            <th width="30%" align="center" class="border_ri_no"><a href="javascript:$('#parentNewId').val('<?php echo $_area->parent_id;?>');$('#myModal3').modal('show');" class="addNewLink" style="color:#F30;">新增</a></th>
          </tr>
              <?php 
    
          	foreach ($area_list as $_businessArea_){
          		echo '<tr class="bottbords">
				            <td  align="center">'.$_businessArea_->id.'</td>
				            <td  align="center"><a href="javascript:void(0);" class="ajaxLink" rel="../pages/businessarea_data.php?parent_id='.$_businessArea_->id.'">'.$_businessArea_->name.'</a></td>
							<td  align="center">'.$_businessArea_->lng.'</td>
							<td  align="center">'.$_businessArea_->lat.'</td>
				            <td  align="center" class="border_ri_no"><a href="#" class="a_css" onClick="edit('.$_businessArea_->id.');">编辑</a>&nbsp;&nbsp;<a href="../pages/businessarea_del.php?id='.$_businessArea_->id.'" class="a_css">删除</a></td>
				          </tr>';
          	}
          ?>
          </table></div>
	<?php 

?>
