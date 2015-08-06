<?php 
session_start();
if(!isset($_SESSION[ADMIN_SESSION_KEY]) || $_SESSION[ADMIN_SESSION_KEY] != 1){
	echo '<meta charset="utf-8"/>';
	echo '<script>';
	echo 'document.location.href = "../pages/login.html";';
	echo '</script>';
	die();
}
?>