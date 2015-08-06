<?php 
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/Coupon.class.php';

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

$_coupon = new Coupon();
$datacount = 0;
$coupon_list = array();
if($key == ''){
	$datacount = $_coupon->getCount();
	$coupon_list = $_coupon->getListByPage($start, $limit);
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
              <a href="javascript:$('#myModal1').modal('show');" style=" float:right; margin-bottom:6px;" class="btn btn-primary">添加优惠券</a>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table table-striped" id="ul_zy">
              <tr>
            <th align="center">名称</th>
            <th align="center">额度</th>
            <th align="center">最低消费</th>
            <th align="center">领取方式</th>
            <th align="center">领取时间</th>
            <th align="center">领取数量</th>
            <th align="center">开始时间</th>
            <th align="center">结束时间</th>
            <th width="6%" align="center">状态</th>
            <th align="center"></th>
          </tr>
          <?php 
          	foreach ($coupon_list as $_coupon_){
          		echo '<tr>
			            <td align="center">'.$_coupon_->name.'</td>
			            <td align="center">'.$_coupon_->price.'</td>
			            <td align="center">';
          		if($_coupon_->need_min > 0){
          			echo '满'.$_coupon_->need_min.'元';
          		}else{
          			echo '无';
          		}
			    echo '</td>
			            <td align="center">';
			    switch ($_coupon_->get_type){
			    	case COUPON_GET_TYPE_SHARE:
			    		echo '被分享人';
			    		break;
			    	case COUPON_GET_TYPE_REGISTER:
			    		echo '注册';
			    		break;
			    	case COUPON_GET_TYPE_FIRST_ORDER:
			    		echo '首单';
			    		break;
			    	case COUPON_GET_TYPE_SHARE_FIRST_ORDER:
			    		echo '被分享人首单,分享人领';
			    		break;
			    	case COUPON_GET_TYPE_DATE:
			    		echo '特定时间';
			    		break;
			    }
			    echo '</td>
			            <td align="center">';
			    if($_coupon_->get_date != '0000-00-00'){
			    	echo $_coupon_->get_date;
			    }
			    echo '</td>
						<td align="center">'.$_coupon_->get_count.'</td>
      					<td align="center">'.$_coupon_->begin_date.'</td>
    					<td align="center">'.$_coupon_->end_date.'</td>
			            <td align="center">';
			    if($_coupon_->status == 1){
			    	echo '可领取';
			    }else{
			    	echo '不可领取';
			    }
			    echo '</td>
			            <td align="center">';
			    echo '<a href="#" class="a_css" onClick="edit('.$_coupon_->id.');">编辑</a>&nbsp;&nbsp;';
			    if($_coupon_->status == 1){
			    	echo '<a href="../pages/coupon_close.php?coupon_id='.$_coupon_->id.'" style="color:#F60;">禁用</a>';
			    }else{
			    	echo '<a href="../pages/coupon_open.php?coupon_id='.$_coupon_->id.'">启用</a>';
			    }
			    echo '</td>
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
        <h4 class="modal-title" id="myModalLabel">添加优惠券</h4>
      </div>
          <form action="coupon_insert.php" method="post" id="ad_insert" enctype='multipart/form-data'>
        <div class="modal-body">
              <table width="100%" border="0" cellspacing="0" cellpadding="0"  class="modal-table">
              <tr>
                  <td align="left">名称</td>
                  <td><input type="text" name="name" class="form-control"></td>
                </tr>
            <tr>
                  <td align="left">额度</td>
                  <td><input type="text" name="price" class="form-control"></td>
                </tr>
               <tr>
                  <td align="left">最低消费</td>
                  <td><input type="text" name="need_min" class="form-control"></td>
                </tr>
            <tr>
                  <td align="left" width="120">获取方式</td>
                  <td><select name="get_type"  class="form-control">
                      <option value="<?php echo COUPON_GET_TYPE_SHARE;?>">被分享人</option>
                      <option value="<?php echo COUPON_GET_TYPE_REGISTER;?>">注册</option>
                      <option value="<?php echo COUPON_GET_TYPE_FIRST_ORDER;?>">首单</option>
                      <option value="<?php echo COUPON_GET_TYPE_SHARE_FIRST_ORDER;?>">被分享人首单,分享人领</option>
                      <option value="<?php echo COUPON_GET_TYPE_DATE;?>">特定时间</option>
                    </select></td>
                </tr>
               <tr>
                  <td align="left">领取时间</td>
                  <td><input type="text" name="get_date" class="form-control datetSelect"></td>
                </tr>
                 <tr>
                  <td align="left">领取数量</td>
                  <td><input type="text" name="get_count" class="form-control"></td>
                </tr>
            <tr>
                  <td align="left">开始时间</td>
                  <td><input type="text" name="begin_date" class="form-control datetSelect"></td>
                </tr>
            <tr>
                  <td align="left">结束时间</td>
                  <td><input type="text" name="end_date" class="form-control datetSelect"></td>
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
                    <iframe id="frm" src="" frameborder="no" scrolling="no" style="width:100%; height:480px; overflow:hidden;"></iframe>
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