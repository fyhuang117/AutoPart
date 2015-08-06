<?php 
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/Band.class.php';
require_once '../model/BandSub.class.php';

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

$_band_sub = new BandSub();
$datacount = 0;
$band_sub_list = array();
if($key == ''){
	$datacount = $_band_sub->getCount();
	$band_sub_list = $_band_sub->getListByPage($start, $limit);
}else{
	$_band_sub->name = $key;
	$datacount = $_band_sub->getCountByNam();
	$band_sub_list = $_band_sub->getListByPageAndNam($start, $limit);
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
</style>
    </head>
    <body>
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
              <p><a href="javascript:$('#myModal1').modal('show');" style=" float:right;" class="btn btn-primary">添加新子品牌</a>
            名称: <input  class="form-control" style="width:320px; display:inline-block;" name="key" placeholder="输入检索内容..." type="text" value="<?php echo $key;?>"> 
            <a class="btn btn-default" href="javascript:document.getElementById('search_form').submit();">搜索</a></p>
            </form>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table table-striped" id="ul_zy">
              <tr>
            <th width="14%" align="center">名称</th>
            <th width="14%" align="center">品牌</th>
            <th width="30%" align="center" class="border_ri_no"></th>
          </tr>
              <?php 
    
          	foreach ($band_sub_list as $_band_sub_){
          		echo '<tr class="bottbords">
				            <td  align="center">'.$_band_sub_->name.'</td>
							<td  align="center">'.$_band_sub_->band->nam.'</td>
				            <td align="center" class="border_ri_no"><a href="#" class="a_css" onClick="edit('.$_band_sub_->id.');">编辑</a>&nbsp;&nbsp;<a href="../pages/carbandsub_del.php?id='.$_band_sub_->id.'" class="a_css">删除</a></td>
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
          <form action="carbandsub_insert.php" method="post" id="ad_insert" enctype='multipart/form-data'>
        <div class="modal-body">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="modal-table">
            <tr>
                  <td align="left" width="120">品牌</td>
                  <td><select name="band_id" class="form-control">
                      <?php 
    		$_band = new Band();
    		$band_list = $_band->getList();
    		foreach ($band_list as $_band_){
    			echo '<option value="'.$_band_->id.'">'.$_band_->nam.'</option>';
    		}
    	?>
                    </select></td>
                </tr>
            <tr>
                  <td align="left">名称</td>
                  <td><input type="text" name="name" class="form-control"></td>
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
        <iframe id="frm" src="" frameborder="no" scrolling="no" style="width:100%; height:100px; overflow:hidden;"></iframe>
      </div>
          <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary" onClick="tj()">保存</button>
      </div>
        </div>
  </div>
    </div>
<?php include 'footer.php'?>
<script>
	function tj(){
		$("#frm").contents().find("#ad_update").submit();	
	}
	function edit(id){
		var h = "carbandsub_edit.php?id=" + id;
		$("#frm").attr("src",h);
		$("#myModa2").modal('show');
		//showEditDiv();
	}
    
    </script>


    </body>
</html>
