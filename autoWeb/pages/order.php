<?php 
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/Order.class.php';

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

$_order = new Order();
$datacount = 0;
$order_list = array();
if($key == ''){
	$datacount = $_order->getCount();
	$order_list = $_order->getListByPage($start, $limit);
}else{
	$_order->num = $key;
	$datacount = $_order->getCountByNum();
	$order_list = $_order->getListByPageAndNum($start, $limit);
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
              <p>
           编号: <input  class="form-control" style="width:320px; display:inline-block;" name="key" placeholder="输入检索内容..." type="text" value="<?php echo $key;?>">
            <a class="btn btn-default" href="javascript:document.getElementById('search_form').submit();">搜索</a></p>
            </form>
      
      
      	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table table-striped" id="ul_zy">
          
          <tr>
          	<th align="center">编号</th>
            <th align="center">卖家</th>
            <th align="center">买家</th>
            <th align="center">配件</th>
            <th align="center">数量</th>
            <th align="center">建立时间</th>
            <th align="center">状态</th>
            <th align="center" class="border_ri_no"></th>
          </tr>
          <?php 
    
          	foreach ($order_list as $_order_){
          		echo '<tr class="bottbords">
							<td align="center">'.$_order_->num.'</td>
				            <td align="center">'.$_order_->sel.'</td>
							<td align="center">'.$_order_->buy.'</td>
							<td align="center">'.$_order_->par.'</td>
							<td align="center">'.$_order_->c.'</td>
				            <td align="center">'.$_order_->create_time.'</td>
							<td align="center">';
          		switch ($_order_->status){
          			case ORDER_STATUS_CANCEL:
          				echo '已取消';
          				break;
          			case ORDER_STATUS_COMPLETE:
          				echo '已完成';
          				break;
          			case ORDER_STATUS_NOPAY:
          				echo '未支付';
          				break;
          			case ORDER_STATUS_NOSEND:
          				echo '未发货';
          				break;
          			case ORDER_STATUS_SEND:
          				echo '已发货';
          				break;
          			case ORDER_STATUS_SCORE:
          				echo '待评价';
          				break;
          		}
				echo '</td>
    				<td align="center" class="border_ri_no">';
				if($_order_->is_freeze == 1){
					echo '<a href="../pages/order_unfreeze.php?id='.$_order_->id.'" class="a_css">解冻</a>';
				}else{
					echo '<a href="../pages/order_freeze.php?id='.$_order_->id.'" class="a_css">冻结</a>';
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
