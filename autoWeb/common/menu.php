<?php
	if(isset($_SESSION[SESSION_KEY])){
		$_user_session = unserialize($_SESSION[SESSION_KEY]);
	}
?>
<div class="menuRfax">
	<div class="cont">
		<span class="ddHover"> </span>
		<dl>
			<dt>
				<a href="javascript:showUserInfo();" id="showUserInfo"><img src="../img/iconUser.png" alt="用户名-<?php echo $_user_session->username;?>" title="用户名-<?php echo $_user_session->username;?>" /></a>
			</dt>
			<?php 
				$_self = $_SERVER['PHP_SELF'];
				if(strpos($_self, '/app/')){
					echo '<dd class="active">';
				}else{
					echo '<dd>';
				}
				echo '<a href="../app/app.php"><img src="../img/iconApp.png" /><span>应用</span></a>
								</dd>';
				
				if(strpos($_self, '/statistics/')){
					echo '<dd class="active">';
				}else{
					echo '<dd>';
				}
				echo '<a href="../statistics/statistics.php"><img src="../img/iconChart.png" /><span>统计</span></a>
								</dd>';
				
				if(strpos($_self, '/system/')){
					echo '<dd class="active">';
				}else{
					echo '<dd>';
				}
				echo '<a href="../system/user.php"><img src="../img/iconManage.png" /><span>设置</span></a>
								</dd>';
			?>
		</dl>
	</div>
</div>
<div class="userInfo">
	<div class="cont">
		<h1><a href="javascript:userSet();" class="btnUserSet"><img src="../img/iconSettings.png" width="32" /></a><?php echo $_user_session->username;?></h1>
		<h5></h5>
		<p></p>
		<hr>
		<h5></h5>
	</div>
	<a href="../u/logout.php" class="aLogout">退  出</a>
</div>