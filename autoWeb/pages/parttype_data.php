<?php
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/PartType.class.php';
require_once '../model/PartBand.class.php';
require_once '../model/Car.class.php';

$_partType = new PartType();
$_partType->par = $_GET['parent_id'];
$partType_list = $_partType->getListByParent();

	?>
	<div class="ajaxTable" style=" margin-left:12px; border:1px solid #DEF4FE;"><a href="javascript:void(0);" class="ajaxClose">X</a>
    <table width="100%" border="0">
<tr>
          	<th width="10%" align="center">类型</th>
            <th align="center">名称</th>
            <th width="10%" align="center">上级分类</th>
            <th width="10%" align="center">对应品牌</th>
            <th width="6%" align="center">是否热门</th>
            <th width="6%" align="center">需要选车型</th>
            <th width="6%" align="center">需要选品质</th>
            <th width="10%" align="center" class="border_ri_no"> <a href="javascript:$('#parentNewId').val('<?php echo $_partType->par;?>');$('#myModal3').modal('show');" class="addNewLink" style="color:#F30;">新增</a></th>
          </tr>
          <?php 
    
          	foreach ($partType_list as $_partType_){
          		echo '<tr class="bottbords">
							<td align="center">';
          		switch ($_partType_->type){
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
				            <td align="center"><a href="javascript:void(0);" class="ajaxLink" rel="../pages/parttype_data.php?parent_id='.$_partType_->id.'">'.$_partType_->nam.'</a></td>
				            <td align="center">';
				
				if($_partType_->par > 0){
					$_parent_type = new PartType();
					$_parent_type->id = $_partType_->par;
					$_parent_type->init();
					
					echo $_parent_type->nam;
				}
				
				echo '</td><td align="center">';
				
				$partBand_str = $_partType_->ban;
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
				
				if($_partType_->h == 1){
					echo '是';
				}else{
					echo '否';
				}
				
				echo '</td><td align="center">';
				
				if($_partType_->is_select_car == 1){
					echo '是';
				}else{
					echo '否';
				}
				
				echo '</td><td align="center">';
				
				if($_partType_->is_select_quality == 1){
					echo '是';
				}else{
					echo '否';
				}
				
				echo '</td>
				            <td align="center" class="border_ri_no"><a href="#" class="a_css" onClick="edit('.$_partType_->id.');">编辑</a>&nbsp;&nbsp;<a href="../pages/parttype_del.php?id='.$_partType_->id.'" class="a_css">删除</a></td>
				          </tr>';
          	}
          ?>
          </table></div>
	<?php 

?>
