<?php 
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/SystemMessage.class.php';

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

$_system_message = new SystemMessage();
$datacount = 0;
$message_list = array();
if($key == ''){
	$datacount = $_system_message->getCount();
	$message_list = $_system_message->getListByPage($start, $limit);
}else{
	/*
	$_partType->nam = $key;
	$datacount = $_partType->getCountByNam();
	$partType_list = $_partType->getListByPageAndNam($start, $limit);
	*/
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
<link rel="stylesheet" type="text/css" href="css/date.css"/>

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
              <a href="javascript:$('#myModal1').modal('show');" style=" float:right;" class="btn btn-primary">添加通知</a>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table table-striped" id="ul_zy">
              <tr>
            <th align="center">标题</th>
            <th align="center">内容</th>
            <th align="center">目标</th>
            <th align="center">发送时间</th>
          </tr>
          <?php 
          	foreach ($message_list as $_message_){
          		echo '<tr>
			            <td align="center">'.$_message_->title.'</td>
			            <td align="center">'.$_message_->content.'</td>
			            <td align="center">';
          		switch ($_message_->target_type){
          			case 0:
          				echo '买家';
          				break;
          			case 1:
          				echo '卖家';
          				break;
          			case 2:
          				echo '全部';
          				break;
          		}
			    echo '</td>
			            <td align="center">'.$_message_->send_time.'</td>	            
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
        <h4 class="modal-title" id="myModalLabel">添加通知</h4>
      </div>
          <form action="systemmessage_insert.php" method="post" id="ad_insert" enctype='multipart/form-data'>
        <div class="modal-body">
              <table width="100%" border="0" cellspacing="0" cellpadding="0"  class="modal-table">
              <tr>
                  <td align="left">标题</td>
                  <td><input type="text" name="title" class="form-control"></td>
                </tr>
            <tr>
                  <td align="left">内容</td>
                  <td><input type="text" name="content" class="form-control"></td>
                </tr>
            <tr>
                  <td align="left" width="120">目标</td>
                  <td><select name="target_type"  class="form-control">
                      <option value="0">买家</option>
                      <option value="1">卖家</option>
                      <option value="2">全部</option>
                    </select></td>
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
<?php include 'footer.php'?>
<script src="js/date.js"></script>
<script>
$('.datetSelect').datetimepicker({
	lang:'ch',
	timepicker:false,
	format:'Y-m-d',
	formatDate:'Y-m-d'
});
	function tj(){
		$("#frm").contents().find("#ad_update").submit();	
	}
	function edit(id){
		var h = "coupon_edit.php?id=" + id;
		$("#frm").attr("src",h);
		$("#myModa2").modal('show');
		//showEditDiv();
	}

    </script>
</body>
</html>