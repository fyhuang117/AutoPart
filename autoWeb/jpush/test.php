<?php

require_once '../common/constant.php';
require_once '../jpush/JPush.class.php';

//推送消息
$_jpush = new JPush();
$_jpush->buy_push('865370026929530', '测试推送');


?>