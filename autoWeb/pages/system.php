<?php 
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/System.class.php';

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

$_system = new System();
$_system->init();

/*
$datacount = 0;
if($key == ''){
	$datacount = $_car->getCount();
	$car_list = $_car->getListByPage($start, $limit);
}else{
	$_car->nam = $key;
	$datacount = $_car->getCountByNam();
	$car_list = $_car->getListByPageAndNam($start, $limit);
}
*/
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
     
      
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table table-striped" id="ul_zy">
          <tr>
          	<th width="14%" align="center">应用版本</th>
            <th width="14%" align="center">应用协议</th>
            <th width="14%" align="center">客服电话</th>
		<th width="14%" align="center"></th>
          </tr>
          <?php 
    
          		echo '<tr class="bottbords">
							<td align="center">'.$_system->app_version.'</td>
				            <td align="center"><a href="'.CUSTOMSERVICE_URL.$_system->service_file.'" target="_blank">查看</a></td>
							<td align="center">'.$_system->service_tel.'</td>
				            <td align="center" class="border_ri_no"><a href="#" class="a_css" onClick="edit();">编辑</a></td>
				          </tr>';
          	
          ?>
        </table>
      
      
      
        
      </div>
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
        <iframe id="frm" src="" frameborder="no" scrolling="no" style="width:100%; height:130px; overflow:hidden;"></iframe>
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
		var h = "system_edit.php?id=" + id;
		$("#frm").attr("src",h);
		$("#myModa2").modal('show');
		//showEditDiv();
	}
    
    </script>


</body>
</html>