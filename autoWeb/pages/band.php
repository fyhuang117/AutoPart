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
	body{ padding:0px;}
.bandList{ overflow:hidden; *display:inline-block; margin:0px; padding:0px;}
.bandList li{ padding:0px; width:33.3%; float:left; list-style:none; line-height:24px;}

</style>
    </head>
<body>
<div id="bandDiv"><ul class="bandList">

				  <?php 
                    $_partBand = new PartBand();
                    $partBand_list = $_partBand->getList();
                    foreach ($partBand_list as $_partBand_){
                        echo '<li><label><input type="checkbox" name="band_id[]" value="'.$_partBand_->id.'" id="pp_'.$_partBand_->id.'" />'.$_partBand_->nam.'</label></li>';
                    }
                ?>
  </ul> </div>            
</body>
</html>