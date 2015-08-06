<?php 
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/Share.class.php';

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

$_share = new Share();
$datacount = 0;
$share_list = array();
if($key == ''){
	$datacount = $_share->getCount();
	$share_list = $_share->getListByPage($start, $limit);
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
.shareUl {
	overflow: hidden;
*display:inline-block;
}
.shareUl li {
	float: left;
	width: 33.3%;
}
.shareNum {
	float: right;
	font-weight: bold;
}
.shareNum strong {
	color: #F30;
}
.shareList {
	margin: 12px;
	border: 1px solid #DEF4FE;
}
.shareList dt {
	overflow: hidden;
*display:inline-block;
	background-color: #DEF4FE;
	line-height: 24px;
	padding: 0 12px;
	cursor: pointer;
}
.shareList dd {
	padding: 0 12px;
	line-height: 20px;
	border-bottom: 1px solid #F2F2F2;
	display:none;
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
        <ul class="shareUl">
        <?php 
        	foreach ($share_list as $_obj_){
        		echo '<li>
			            <dl class="shareList">
			              <dt><span class="shareNum">分享次数：<strong>'.$_obj_->share_count.'</strong></span>'.$_obj_->share_user->nam.'</dt>';
        		
        		$_share_inner = new Share();
        		$_share_inner->share_user = $_obj_->share_user;
        		$share_inner_list = $_share_inner->getListByShare();
        		foreach ($share_inner_list as $_share_){
        			echo '<dd>'.$_share_->user->nam.'</dd>';
        		}
			     echo '</dl>
			          </li>';
        	}
        ?>
        </ul>
        <!--<a href="javascript:void(0);" class="btn btn-primary">提现成功</a>-->
         <form action='' method='post' id='_page'>
      <input type='hidden' name='page' id='_page_v' value='1'/>
      <input type='hidden' name='key' id='_page_key' value='<?php echo $key;?>'/>
    </form>
        <?php 
 			require_once '../common/page.php';
 		?>
      </div>
    </div>
  </div>
</div>

<!--<div class="modal fade" id="myModa2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
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
            </div>-->
<?php include 'footer.php'?>
<script>
$(function(){
	$(".shareList dt").click(function(){
		$(this).nextAll().fadeToggle();	
	});
})

    </script>
</body>
</html>