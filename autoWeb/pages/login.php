<?php
require_once '../common/constant.php';

if(!isset($_POST['username']) || !isset($_POST['password'])){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("参数不全");';
	echo 'history.back();';
	echo '</script>';
	die();
}

$username = $_POST['username'];
$password = $_POST['password'];

if($username != ADMIN_USERNAME){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("用户名错误");';
	echo 'history.back();';
	echo '</script>';
	die();
}

if($password != ADMIN_PASSWORD){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'alert("密码错误");';
	echo 'history.back();';
	echo '</script>';
	die();
}

session_start();
	
$_SESSION[ADMIN_SESSION_KEY] = 1;
?>
<script>
	document.location.href = "../pages/user.php";
</script>