<?php
require_once '../common/constant.php';
require_once '../common/core.php';

if(isset($_GET['s'])){
	?>
<!doctype html>
<html>
	<head>
	<meta charset="UTF-8">
	<title>分享</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.5, minimum-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-capable" content="yes"/>
	<meta name="full-screen" content="yes"/>
	<meta name="screen-orientation" content="portrait"/>
	<meta name="x5-fullscreen" content="true"/>
	<meta name="360-fullscreen" content="true"/>
	<style>
	*::-webkit-input-placeholder {
	 color: #eec06a;
	}
	 *:-moz-placeholder {
	 color: #eec06a;
	}
	 *:-ms-input-placeholder {
		/* IE10+ */
		color: #eec06a;
	}
	</style>
	</head>

	<body style="background:#000;">
	<div style="position:absolute; width:320px; height:340px; margin:auto; top:0; left:0; right:0; bottom:0; text-align:center; color:#7e7e7e; font-weight:bold;">
		<form id="form1" name="form1" action="share_save.php" method="post">
		<img src="images/logo.png" width="250" style="margin:10px auto 40px auto;" />
		<input type="hidden" name="share_user_id" value="<?php echo $_GET['s'];?>"/>
		<p style=" font-size:0.9em;">现货，实惠，应用尽有！</p>
		<p style=" font-size:0.9em;">哦了汽配送您<span style="color:#eec06a;">50元</span>优惠劵，赶快领取吧！</p>
		<input type="text" name="tel" id="tel" style="line-height:40px; font-size:1em; margin:24px 6px 12px 6px; text-align:center; border:1px solid #eec06a; border-radius:12px; background:#000; color:#eec06a; width:80%; padding:0;" placeholder="请输入自己的电话号码">
		<a href="javascript:void(0);" style="display:block; line-height:38px; font-size:1.2em; font-weight:bold; border-radius:12px; background:#eec06a; color:#000; width:80%; margin:auto; text-decoration:none; padding:2px;" onClick="document.getElementById('form1').submit()">领取专车卷</a>
	</form>
	</div>
<div style="position:fixed; bottom:12px; text-align:center; width:100%;"> <a href="" style="color:#a98c4f; text-decoration:none; font-size:12px; padding:4px 24px;">下载哦了汽配APP</a> </div>
</body>
</html>
<?php 
 }
?>
