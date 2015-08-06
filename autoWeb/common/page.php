       
		<script>
function post(page){
	document.getElementById('_page_v').value = page;
	document.getElementById('_page').submit();
}
</script>
        <?php 
$lastpage = ($datacount % PAGE_COUNT === 0)?intval($datacount / PAGE_COUNT):intval($datacount / PAGE_COUNT) + 1;
$previous = ($page > 1)?$page - 1:1;
$next = ($page == $lastpage)?$page:$page + 1;
if($datacount == 0){
	$lastpage = 1;
	$next = 1;
}
if($lastpage > 1){
	
	echo '<div class="pageDiv">';
	echo ' 第'.$page.'页（共'.$datacount.'条记录）';
	echo '<label id="ul_fy">';
			/*
	$start = 1;
	if($page > 3){
		$start = $page - 2;
	}
	while($start < $page + 3 && $start <= $lastpage){
		if($start == $page){
			echo '<span class="fy" onclick="javascript:post('.$start.');">';
		}else{
			echo '<span onclick="javascript:post('.$start.');">';
		}
		echo '&nbsp;'.$start++.'&nbsp;</span>';
	}
			*/
	if($page > 1){
		echo '<span onclick="javascript:post(1);">&nbsp;首页&nbsp;</span>';
		echo '<span onclick="javascript:post('.$previous.');">&nbsp;上一页&nbsp;</span>';
	}
	if($lastpage > 1 && $page != $lastpage){
		echo '<span onclick="javascript:post('.$next.');">&nbsp;下一页&nbsp;</span>';
		echo '<span onclick="javascript:post('.$lastpage.');">&nbsp;末页&nbsp;</span>';
	}
	echo '</div>';
	
	/*
	echo '<tr>';
	echo '<td colspan="4"><span class="span_fy"> 第'.$page.'页（共'.$datacount.'条记录）</span>';
	echo '<ul class="pagination" style="clear:both;">';
	echo '<li><a href="javascript:post(1);">第一页</a></li>';
	echo '<li><a href="javascript:post('.$previous.');">&nbsp;&laquo;&nbsp;</a></li>';
	$start = 1;
	if($page > 3){
		$start = $page - 2;
	}
	while($start < $page + 3 && $start <= $lastpage){
		if($start == $page){
			echo '<li class="active">';
		}else{
			echo '<li>';
		}
		echo '<a href="javascript:post('.$start.');">&nbsp;'.$start++.'&nbsp;</a></li>';
	}
	echo '<li><a href="javascript:post('.$next.');">&nbsp;&raquo;&nbsp;</a></li>';
	echo '<li><a href="javascript:post('.$lastpage.');">最后一页</a></li>';
	echo '</div>';
	*/
}

?>