<?php 
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/PartType.class.php';
require_once '../model/PartBand.class.php';
require_once '../model/Car.class.php';

$page = 1;

if(isset($_POST['page'])){
	$page = intval($_POST['page']);
}

$start = ($page - 1) * PAGE_COUNT;
$limit = PAGE_COUNT;

$key = '';
if(isset($_POST['key'])){
	$key = $_POST['key'];
}

$_partType = new PartType();
$datacount = 0;
$partType_list = array();
if($key == ''){
	$datacount = $_partType->getCount();
	$partType_list = $_partType->getListByPage($start, $limit);
}else{
	$_partType->nam = $key;
	$datacount = $_partType->getCountByNam();
	$partType_list = $_partType->getListByPageAndNam($start, $limit);
}

?>
<!DOCTYPE html>
<html lang="utf-8">
    <head>
    <?php include 'head.php'?>
    <style>
.modal-table td {
	padding: 6px 12px;
}
.ajaxTable {
	position: relative;
	margin-bottom:12px;
}
.ajaxTable table {
	background-color: #FFF;
}
.ajaxTable table th {
	background-color: #DEF4FE;
	padding:4px 0;
}
.ajaxTable table td {
	border-bottom: 1px solid #F1F1F1;
	padding:4px 0;
}
.ajaxTable .ajaxClose {
	position: absolute;
	z-index: 999;
	top: 0;
	left: 0;
	background-color: #F30;
	color: #FFF;
	padding: 0 4px;
}
.pp_checkbox{ overflow:hidden; *display:inline-block; margin:0; padding:0px;}
.pp_checkbox li{ float:left; list-style:none; margin:0 12px 0 0;}
.pp_checkbox input{ vertical-align:middle; margin-right:4px;}
</style>
    </head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
    <div class="navbar-header"> <a class="navbar-brand" href="#">易配购后台维护系统</a> </div>
  </div>
    </nav>
<div class="container-fluid">
      <div class="row">
    <div class="col-sm-3 col-md-2 sidebar">
          <?php include 'menu.php'?>
        </div>
    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main"> <br>
          <div class="table-responsive">
        <form action="" method="post" id="search_form">
              <p><a href="javascript:$('#myModal1').modal('show');" style=" float:right; margin-bottom:6px;" class="btn btn-primary">添加新配件类型</a>
            名称: <input  class="form-control" style="width:320px; display:inline-block;" name="key" placeholder="输入检索内容..." type="text" value="<?php echo $key;?>">
              <a class="btn btn-default" href="javascript:document.getElementById('search_form').submit();">搜索</a></p>
            </form>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table table-striped ajaxTable" id="ul_zy">
              <tr>
            <th width="10%" align="center">类型</th>
            <th width="10%" align="center">名称</th>
            <th width="10%" align="center">对应品牌</th>
            <th width="6%" align="center">是否热门</th>
            <th width="6%" align="center">需要选车型</th>
            <th width="6%" align="center">需要选品质</th>
            <th width="10%" align="center" class="border_ri_no"></th>
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
              <form action='' method='post' id='_page'>
            <input type='hidden' name='page' id='_page_v' value='1'/>
            <input type='hidden' name='key' id='_page_key' value='<?php echo $key;?>'/>
          </form>
            </table>
        <?php 
 			require_once '../common/page.php';
 		?>
      </div>
        </div>
  </div>
    </div>
<div class="modal fade" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
    <div class="modal-content">
          <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">添加新配件类型</h4>
      </div>
          <form action="parttype_insert.php" method="post" id="ad_insert" enctype='multipart/form-data'>
        <div class="modal-body">
              <table width="100%" border="0" cellspacing="0" cellpadding="0"  class="modal-table">
            <tr>
                  <td align="left" width="120">类型</td>
                  <td><select name="type"  class="form-control">
                      <option value="<?php echo PART_TYPE_N;?>">配件</option>
                      <option value="<?php echo PART_TYPE_V;?>">易损品</option>
                      <option value="<?php echo PART_TYPE_C;?>">消耗品</option>
                    </select></td>
                </tr>
            <tr>
                  <td align="left">名称</td>
                  <td><input type="text" name="nam" class="form-control"></td>
                </tr>
            
                <input type="hidden" name="par" value="0"/>
            <tr>
                  <td align="left" width="50" valign="top">对应品牌</td>
                  <td>
                  
                  
                  <button type="button" class="btn btn-info btn-xs" onClick="$('#selectBandNew').modal('show');">选择品牌</button>
				  <div id="bandListDiv" style="display:none;">
                  	
                  </div>
				
                </td>
                </tr>
            <!-- 
          <tr>
            <td align="left" width="50">对应车型</td>
            <td>
                <?php 
                    /*
                    $_car = new Car();
                    $car_list = $_car->getList();
                    foreach ($car_list as $_car_){
                        echo '<input type="checkbox" name="car_id[]" value="'.$_car_->id.'"/>'.$_car_->nam;
                    }
                    */
                ?>
            </td>
          </tr>
           -->
            <tr>
                  <td align="left" width="50">是否选车型</td>
                  <td><select name="is_select_car" class="form-control">
                      <option value="0">否</option>
                      <option value="1">是</option>
                    </select></td>
                </tr>
            <tr>
                  <td align="left" width="50">是否选品质</td>
                  <td><select name="is_select_quality" class="form-control">
                      <option value="0">否</option>
                      <option value="1">是</option>
                    </select></td>
                </tr>
            <tr>
                  <td align="left" width="50">热门</td>
                  <td><input type="checkbox" name="h" value="1"/>
                是 </td>
                </tr>
          </table>
            </div>
        <div class="modal-footer">
       	  <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
              <button type="submit" class="btn btn-primary">保存</button>
            </div>
      </form>
        </div>
  </div>
    </div>
<div class="modal fade" id="myModa2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
    <div class="modal-content">
          <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">编辑</h4>
      </div>
          <div class="modal-body">
        <iframe id="frm" src="" frameborder="no" scrolling="no" style="width:100%; height:320px; overflow:hidden;"></iframe>
      </div>
          <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary" onClick="tj()">保存</button>
      </div>
        </div>
  </div>
    </div>
    
<div class="modal fade" id="selectBandNew" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
    <div class="modal-content">
          <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">选择品牌</h4>
      </div>
          <div class="modal-body">
        <iframe id="bandIframe" src="band.php" frameborder="no" scrolling="no" style="width:100%; height:320px; overflow:hidden;"></iframe>
      </div>
          <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary" onClick="spp1()">保存</button>
      </div>
        </div>
  </div>
    </div>  
 
<div class="modal fade" id="selectBandNew2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
    <div class="modal-content">
          <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">选择品牌</h4>
      </div>
          <div class="modal-body">
        <iframe id="bandIframe2" src="band.php" frameborder="no" scrolling="no" style="width:100%; height:320px; overflow:hidden;"></iframe>
      </div>
          <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary" onClick="spp2()">保存</button>
      </div>
        </div>
  </div>
    </div>  
 
    
<div class="modal fade" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
    <div class="modal-content">
          <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">添加新配件类型</h4>
      </div>
          <form action="parttype_insert.php" method="post" id="pj_insert" enctype='multipart/form-data'>
        <div class="modal-body">
              <table width="100%" border="0" cellspacing="0" cellpadding="0"  class="modal-table">
            <tr>
                  <td align="left" width="120">类型</td>
                  <td><select name="type"  class="form-control">
                      <option value="<?php echo PART_TYPE_N;?>">配件</option>
                      <option value="<?php echo PART_TYPE_V;?>">易损品</option>
                      <option value="<?php echo PART_TYPE_C;?>">消耗品</option>
                    </select></td>
                </tr>
            <tr>
                  <td align="left">名称</td>
                  <td><input type="text" name="nam" class="form-control"></td>
                </tr>
           
            <tr>
                  <td align="left" width="50">对应品牌</td>
                  <td><?php 
                    $_partBand = new PartBand();
                    $partBand_list = $_partBand->getList();
                    foreach ($partBand_list as $_partBand_){
                        echo '<input type="checkbox" name="band_id[]" value="'.$_partBand_->id.'"/>'.$_partBand_->nam;
                    }
                ?></td>
                </tr>
            <!-- 
          <tr>
            <td align="left" width="50">对应车型</td>
            <td>
                <?php 
                    /*
                    $_car = new Car();
                    $car_list = $_car->getList();
                    foreach ($car_list as $_car_){
                        echo '<input type="checkbox" name="car_id[]" value="'.$_car_->id.'"/>'.$_car_->nam;
                    }
                    */
                ?>
            </td>
          </tr>
           -->
            <tr>
                  <td align="left" width="50">是否选车型</td>
                  <td><select name="is_select_car" class="form-control">
                      <option value="0">否</option>
                      <option value="1">是</option>
                    </select></td>
                </tr>
            <tr>
                  <td align="left" width="50">是否选品质</td>
                  <td><select name="is_select_quality" class="form-control">
                      <option value="0">否</option>
                      <option value="1">是</option>
                    </select></td>
                </tr>
            <tr>
                  <td align="left" width="50">热门</td>
                  <td><input type="checkbox" name="h" value="1"/>
                是 </td>
                </tr>
          </table>
            </div>
        <div class="modal-footer">
        	<input type="hidden" name="parent_id" id="parentNewId">
       	  <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
              <button type="button" class="btn btn-primary" data-dismiss="modal" id="addNew">保存</button>

            </div>
      </form>
        </div>
  </div>
    </div>
<?php include 'footer.php'?>
<script src="js/jquery.form.js"></script>
<script>
	function selectBand(){
		$("#selectBandNew2").modal('show');
	}
	function spp1(){
		//alert($("#bandIframe").contents().find("#bandDiv").html());
		var html = '';
		$("#bandIframe").contents().find("input").each(function(index, element) {
            if ($(this).is(':checked')) { 
				html += '<input type="checkbox" name="band_id[]" value="'+ $(this).val() +'" checked="checked" />';
			} 
        });
		$("#bandListDiv").html(html);
		$("#selectBandNew").modal('hide');
	}
	
	function spp2(){
		var html = '';
		$("#bandIframe2").contents().find("input").each(function(index, element) {
            if ($(this).is(':checked')) { 
				html += '<input type="checkbox" name="band_id[]" value="'+ $(this).val() +'" checked="checked"   />';
			} 
        });
		$("#frm").contents().find("#bandListDiv").html(html);
		$("#selectBandNew2").modal('hide');
	}
	function tj(){
		$("#frm").contents().find("#ad_update").submit();	
	}
	function edit(id){
		var h = "parttype_edit.php?id=" + id;
		$("#frm").attr("src",h);
		$("#myModa2").modal('show');
		//showEditDiv();
	}
	
	var _target=null;
    $(function(){
		$(".ajaxTable").on("click",".ajaxLink",function(){
			var o = $(this).parent().parent();
			$.get($(this).attr("rel"),function(data){
				//alert(data);
				if($.trim(data) != "0"){
					var html = '<tr class="ajaxTr"><td colspan="'+ o.find("td").length +'">'+ data +'</td></tr>';
					o.nextAll(".ajaxTr").remove();
					$(html).insertAfter(o);
				}else{
					alert("无下级数据");	
				}
			});
			/*var o = $(this).parent().parent();
			
			var html = '<tr><td colspan="'+ o.find("td").length +'"><div class="ajaxTable"><a href="javascript:void(0);" class="ajaxClose">X</a><table width="100%"border="0"><tr><th scope="col">1</th><th scope="col">2</th><th scope="col">3</th><th scope="col">4</th><th scope="col">5</th><th scope="col">6</th></tr><tr><td>&nbsp;</td><td><a href="javascript:void(0)"rel=""class="ajaxLink">name</a></td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr></table></div></td></tr>';
			
			$(html).insertAfter(o);*/
		});
		
		$(".ajaxTable").on("click",".ajaxClose",function(){
			$(this).parent().parent().parent().remove();
		});
		
		$(".ajaxTable").on("click",".addNewLink",function(){
			_target = $(this);
		});
		
		$("#addNew").click(function (){
            var options = {
                url: "parttype_insert_js.php",
                type: 'post',
                dataType: 'text',
                data: $("#pj_insert").serialize(),
                success: function (data) {
                    if (data.length > 0)
						//alert(target.parent().html());
                       $(data).insertAfter(_target.parent().parent());
                }
            };
            $.ajax(options);
        });
	});
    </script>
</body>
</html>