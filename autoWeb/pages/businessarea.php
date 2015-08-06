<?php 
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/BusinessArea.class.php';

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

$_businessArea = new BusinessArea();
$datacount = 0;
$businessArea_list = array();
if($key == ''){
	$datacount = $_businessArea->getCount();
	$businessArea_list = $_businessArea->getListByPage($start, $limit);
}else{
	$_businessArea->name = $key;
	$datacount = $_businessArea->getCountByName();
	$businessArea_list = $_businessArea->getListByPageAndName($start, $limit);
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
              <p><a href="javascript:$('#myModal1').modal('show');" style=" float:right; margin-bottom:6px;" class="btn btn-primary">添加新商圈</a>
            <!-- <input class="form-control" style="width:320px; display:inline-block;" name="key" placeholder="输入检索内容..." type="text" value="<?php //echo $key;?>">
            <select name="type"  class="form-control" style="width:240px; display:inline-block;">
                  <option value="0">检索条件</option>
                  <option value="1">名称</option>
                  <option value="2">经度</option>
                  <option value="3">纬度</option>
                </select>
            <a class="btn btn-default" href="javascript:document.getElementById('search_form').submit();">搜索</a> --></p>
            </form>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table table-striped ajaxTable" id="ul_zy">
              <tr>
            <td width="14%" align="center">编号</td>
            <td width="14%" align="center">名称</td>
            <td width="14%" align="center">经度</td>
            <td width="14%" align="center">纬度</td>
            <td width="30%" align="center" class="border_ri_no"></td>
          </tr>
              <?php 
    
          	foreach ($businessArea_list as $_businessArea_){
          		echo '<tr class="bottbords">
				            <td width="14%" align="center">'.$_businessArea_->id.'</td>
				            <td  width="14%" align="center"><a href="javascript:void(0);" class="ajaxLink" rel="../pages/businessarea_data.php?parent_id='.$_businessArea_->id.'">'.$_businessArea_->name.'</a></td>
							<td  width="14%" align="center">'.$_businessArea_->lng.'</td>
							<td  width="14%" align="center">'.$_businessArea_->lat.'</td>
				            <td width="30%" align="center" class="border_ri_no"><a href="#" class="a_css" onClick="edit('.$_businessArea_->id.');">编辑</a>&nbsp;&nbsp;<a href="../pages/businessarea_del.php?id='.$_businessArea_->id.'" class="a_css">删除</a></td>
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
        <h4 class="modal-title" id="myModalLabel">添加新商圈</h4>
      </div>
          <form action="businessarea_insert.php" method="post" id="ad_insert" enctype='multipart/form-data'>
        <div class="modal-body">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="modal-table">
            <tr>
                  <td align="left" width="100">名称</td>
                  <td><input type="text" name="name" class="form-control"></td>
                </tr>
            <tr>
                  <td align="left">经度</td>
                  <td><input type="text" name="lng" class="form-control"></td>
                </tr>
            <tr>
                  <td align="left">纬度</td>
                  <td><input type="text" name="lat"  class="form-control"></td>
                </tr>
            <tr>
                  <td align="left">首字母</td>
                  <td><input type="text" name="first_word"  class="form-control"></td>
                </tr>
            <tr> </tr>
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
        <iframe id="frm" src="" frameborder="no" scrolling="no" style="width:100%; height:230px; overflow:hidden;"></iframe>
      </div>
          <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary" onClick="tj()">保存</button>
      </div>
        </div>
  </div>
    </div>
<div class="modal fade" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
    <div class="modal-content">
          <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">添加新商圈</h4>
      </div>
          <form action="businessarea_insert.php" method="post" id="pj_insert" enctype='multipart/form-data'>
        <div class="modal-body">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="modal-table">
            <tr>
                  <td align="left" width="100">名称</td>
                  <td><input type="text" name="name" class="form-control"></td>
                </tr>
            <tr>
                  <td align="left">经度</td>
                  <td><input type="text" name="lng" class="form-control"></td>
                </tr>
            <tr>
                  <td align="left">纬度</td>
                  <td><input type="text" name="lat"  class="form-control"></td>
                </tr>
            <tr>
                  <td align="left">首字母</td>
                  <td><input type="text" name="first_word"  class="form-control"></td>
                </tr>
            <tr> </tr>
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
<script>
	function tj(){
		$("#frm").contents().find("#ad_update").submit();	
	}
	function edit(id){
		var h = "businessarea_edit.php?id=" + id;
		$("#frm").attr("src",h);
		$("#myModa2").modal('show');
		//showEditDiv();
	}
    
	
	var _target=null;
	$(function(){
		$(".ajaxTable").on("click",".ajaxLink",function(){
			var o = $(this).parent().parent();
			$.get($(this).attr("rel"),function(data){
				
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
                url: "businessarea_insert_js.php",
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
