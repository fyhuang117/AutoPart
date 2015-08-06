<?php 
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/Inquiry.class.php';

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

$_inquiry = new Inquiry();
$datacount = 0;
$inquiry_list = array();
if($key == ''){
	$datacount = $_inquiry->getCount();
	$inquiry_list = $_inquiry->getListByPage($start, $limit);
}else{
	//$_car->nam = $key;
	//$datacount = $_car->getCountByNam();
	//$car_list = $_car->getListByPageAndNam($start, $limit);
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
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table table-striped" id="ul_zy">
         
          <tr>
            <th width="14%" align="center">用户</th>
            <th width="14%" align="center">配件类型</th>
            <th width="20%" align="center">汽车</th>
            <th width="10%" align="center">数量</th>
            <th  width="20%" align="center">建立时间</th>
            <th align="center">状态</th>
          </tr>
          <?php 
    
          	foreach ($inquiry_list as $_inquiry_){
          		echo '<tr class="bottbords">
							<td align="center">'.$_inquiry_->user->nam.'</td>
				            <td align="center">'.$_inquiry_->parttype->nam.'</td>
							<td align="center">'.$_inquiry_->car->nam.'</td>
							<td align="center">'.$_inquiry_->c.'</td>
				            <td align="center">'.$_inquiry_->create_time.'</td>
							<td align="center">';
          		switch ($_inquiry_->status){
          			case INQUIRY_STATUS_COMPLETE:
          				echo '已完成';
          				break;
          			case INQUIRY_STATUS_DEL:
          				echo '已删除';
          				break;
          			case INQUIRY_STATUS_NORMAL:
          				echo '询价中';
          				break;
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
<?php include 'footer.php'?>


</body>
</html>