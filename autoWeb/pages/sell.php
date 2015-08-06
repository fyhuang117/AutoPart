<?php 
require_once '../common/constant.php';
require_once '../common/verify_permission.php';
require_once '../model/User.class.php';

$page = 1;

if(isset($_POST['page'])){
	$page = intval($_POST['page']);
}

$start = ($page - 1) * PAGE_COUNT;
$limit = PAGE_COUNT;

$key = '';
$type = '';
if(isset($_POST['key'])){
	$key = $_POST['key'];
	$type = $_POST['type'];
}

$_user = new User();
$_user->type = 1;
$datacount = 0;
$user_list = array();
if($key == ''){
	$datacount = $_user->getCount();
	$user_list = $_user->getListByPage($start, $limit);
}else{
	$datacount = $_user->getCountByNam($type,$key);
	$user_list = $_user->getListByNamAndPage($type,$key,$start, $limit);
}

?>
<!DOCTYPE html>
<html lang="utf-8">
  <head>
  	<?php include 'head.php'?>
    
  </head>
<body>
<body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <a class="navbar-brand" href="#">易配购后台维护系统</a>
        </div>
        
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
      
        <div class="col-sm-3 col-md-2 sidebar">
          <?php include 'menu.php'?>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

          <br>

          <div class="table-responsive">
          <form action="" method="post" id="search_form">
            <p><input class="form-control" style="width:320px; display:inline-block;" name="key" placeholder="输入检索内容..." type="text" value="<?php echo $key;?>"> <select name="type"  class="form-control" style="width:240px; display:inline-block;">
                <option value="nam" <?php echo $type=='nam'?'selected':'';?>>用户名</option>
                <option value="tel" <?php echo $type=='tel'?'selected':'';?>>电话</option>
              </select>
              <a class="btn btn-default" href="javascript:document.getElementById('search_form').submit();">搜索</a></p>
             </form>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table table-striped" id="ul_zy">
          <tr>
            <th width="14%" align="center">电话</th>
            <th width="10%" align="center">用户名</th>
            <th  width="10%" align="center">商圈</th>
            <th  width="20%" align="center">地址</th>
            <th  width="10%" align="center">身份证</th>
            <th  width="10%" align="center">营业执照</th>
            <th  width="10%" align="center">状态</th>
            <th width="30%" align="center" class="border_ri_no"></th>
          </tr>
          <?php 
    
          	foreach ($user_list as $_user_){
          		echo '<tr class="bottbords">
				            <td width="14%" align="center">'.$_user_->tel.'</td>
				            <td width="10%" align="center">'.$_user_->nam.'</td>
							<td width="10%" align="center">'.$_user_->business_area->name.'</td>
				            <td  width="20%" align="center">'.$_user_->adr.'</td>
							<td width="10%" align="center">';
          		if($_user_->lp1 != ''){
          			echo '<img src="'.LP1_URL.$_user_->lp1.'" height="50" width="50"/>';
          		}
          		if($_user_->lp2 != ''){
          			echo '<img src="'.LP2_URL.$_user_->lp2.'" height="50" width="50"/>';
          		}
				echo '</td>
							<td width="10%" align="center">';
				if($_user_->license != ''){
					echo '<img src="'.LICENSE_URL.$_user_->license.'" height="50" width="50"/>';
				}
				echo '</td>
							<td width="10%" align="center">';
				if($_user_->enb == 0){
					echo '未请求审核';
				}else if($_user_->enb == 1){
					echo '已审核';
				}else if($_user_->enb == 3){
					echo '待审核';
				}else{
					echo '审核未通过';
				}
				echo '</td>
							 <td width="30%" align="center" class="border_ri_no"><a href="#" class="a_css" onClick="edit('.$_user_->id.');">编辑</a>&nbsp;&nbsp;';
				if($_user_->enb == 3){
					echo '<a href="../pages/user_audit.php?id='.$_user_->id.'" class="a_css">审核通过</a>&nbsp;&nbsp;';
					echo '<a href="../pages/user_refuse.php?id='.$_user_->id.'" class="a_css">审核不通过</a>&nbsp;&nbsp;';
				}
				if($_user_->status == 1){
					echo '<a href="../pages/user_close.php?id='.$_user_->id.'" class="a_css">禁用</a>';
				}else{
					echo '<a href="../pages/user_open.php?id='.$_user_->id.'" class="a_css">启用</a>';
				}
				echo '</td>
				          
				       </tr>';
          	}
          ?>
          <form action='' method='post' id='_page'>
      <input type='hidden' name='page' id='_page_v' value='1'/>
      <input type='hidden' name='key' id='_page_key' value='<?php echo $key;?>'/>
       <input type='hidden' name='type' id='_page_type' value='<?php echo $type;?>'/>
    </form>
    
        </table>

<?php 
 			require_once '../common/page.php';
 		?>
				
            
          </div>
        </div>
      </div>
    </div>
    
    
    
    
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">编辑用户</h4>
                  </div>
                  <div class="modal-body">
                    <iframe id="frm" src="" frameborder="no" scrolling="no" style="width:100%; height:80px; overflow:hidden;"></iframe>
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
		
		var h = "user_edit.php?id=" + id;
		$("#frm").attr("src",h);
		
		$('#myModal').modal('show');
		//showEditDiv();
	}

    </script>
    
    
  </body>

</body>
</html>
