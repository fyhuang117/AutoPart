<?php


	define('DATABASE_HOST','127.0.0.1');
	define('DATABASE_USER','root');
	define('DATABASE_PASSWORD','');
	define('DATABASE_NAME','db_auto');
	
	define('APP_VERSION',1);
	define('DAT_VERSION',2);
	define('IS_UPDATE_NOW',FALSE);
	
	define('VERSION_STATUS_NO_UPDATE',0);
	define('VERSION_STATUS_APP_UPDATE',1);
	define('VERSION_STATUS_APP_DAT_UPDATE',2);
	define('VERSION_STATUS_APP_UPDATE_NOW',3);
	define('VERSION_STATUS_DAT_UPDATE',4);
	
	define('ADMIN_USERNAME', 'admin');
	define('ADMIN_PASSWORD','admin');
	define('ADMIN_SESSION_KEY','ajdnciiej');
	define('PAGE_COUNT',10);

	define('PART_TYPE_N',0);		//配件
	define('PART_TYPE_V',1);		//易损品
	define('PART_TYPE_C',2);		//消耗品
	
	define('BIDDING_READ_STATUS_NO',0);
	define('BIDDING_READ_STATUS_YES',1);
	
	define('BIDDING_STATUS_NORMAL',1);
	define('BIDDING_STATUS_ORDER',2);
	define('BIDDING_STATUS_DEL',0);
	define('BIDDING_STATUS_END',3);
	
	define('INQUIRY_STATUS_NORMAL',1);
	define('INQUIRY_STATUS_DEL',0);
	define('INQUIRY_STATUS_COMPLETE', 2);
	
	define('ORDER_STATUS_NOPAY',0);				//未支付
	define('ORDER_STATUS_NOSEND',1);			//未发货
	define('ORDER_STATUS_COMPLETE',2);			//完成
	define('ORDER_STATUS_CANCEL',3);			//取消
	define('ORDER_STATUS_SEND',4);				//已发货
	define('ORDER_STATUS_SCORE',5);
	
	define('SERVICE_URL','http://123.57.53.94/auto/resource/');

/*
	define('UPDATE_URL','');
	define('CUSTOMSERVICE_URL',SERVICE_URL.'customservicefile.php?fn=');
	define('CUSTOMSERVICE_TEL','');
	define('LP1_URL',SERVICE_URL.'lp1.php?fn=');
	define('LP2_URL',SERVICE_URL.'lp2.php?fn=');
	define('PIC_URL',SERVICE_URL.'pic.php?fn=');
	define('LICENSE_URL',SERVICE_URL.'license.php?fn=');
	define('AGENT_URL',SERVICE_URL.'agent.php?fn=');
	define('BAND_PIC_URL',SERVICE_URL.'bandpic.php?fn=');
	define('CAR_PIC_URL',SERVICE_URL.'carpic.php?fn=');
	define('PARTTYPE_PIC_URL',SERVICE_URL.'parttypepic.php?fn=');
	define('PARTBAND_PIC_URL',SERVICE_URL.'partbandpic.php?fn=');
	define('INQUIRY_PIC1_URL',SERVICE_URL.'inquirypic1.php?fn=');
	define('INQUIRY_PIC2_URL',SERVICE_URL.'inquirypic2.php?fn=');
	define('INQUIRY_PIC3_URL',SERVICE_URL.'inquirypic3.php?fn=');
	define('INQUIRY_AUM_URL',SERVICE_URL.'inquiryaum.php?fn=');
	define('BIDDING_PIC1_URL',SERVICE_URL.'biddingpic1.php?fn=');
	define('BIDDING_PIC2_URL',SERVICE_URL.'biddingpic2.php?fn=');
	define('BIDDING_PIC3_URL',SERVICE_URL.'biddingpic3.php?fn=');
	define('BIDDING_AUM_URL',SERVICE_URL.'biddingaum.php?fn=');
	define('ORDER_PIC1_URL',SERVICE_URL.'orderpic1.php?fn=');
	define('ORDER_PIC2_URL',SERVICE_URL.'orderpic2.php?fn=');
	define('ORDER_PIC3_URL',SERVICE_URL.'orderpic3.php?fn=');
	define('AD_PIC_URL',SERVICE_URL.'adpic.php?fn=');
	
	define('CUSTOMSERVICE_PATH','D:/auto_file/customservice/');
	define('USER_LP1_PATH','D:/auto_file/user_lp1/');
	define('USER_LP2_PATH','D:/auto_file/user_lp2/');
	define('USER_PIC_PATH','D:/auto_file/user/');
	define('USER_LICENSE_PATH', 'D:/auto_file/user_license/');
	define('USER_AGENT_PATH','D:/auto_file/user_agent/');
	define('BAND_PIC_PATH','D:/auto_file/band/');
	define('CAR_PIC_PATH','D:/auto_file/car/');
	define('PARTTYPE_PIC_PATH','D:/auto_file/parttype/');
	define('PARTBAND_PIC_PATH','D:/auto_file/partband/');
	define('INQUIRY_PIC1_PATH','D:/auto_file/inquiry_1/');
	define('INQUIRY_PIC2_PATH','D:/auto_file/inquiry_2/');
	define('INQUIRY_PIC3_PATH','D:/auto_file/inquiry_3/');
	define('INQUIRY_AUM_PATH','D:/auto_file/inquiry_aum/');
	define('BIDDING_PIC1_PATH','D:/auto_file/bidding_1/');
	define('BIDDING_PIC2_PATH','D:/auto_file/bidding_2/');
	define('BIDDING_PIC3_PATH','D:/auto_file/bidding_3/');
	define('BIDDING_AUM_PATH','D:/auto_file/bidding_aum/');
	define('ORDER_PIC1_PATH','D:/auto_file/order_1/');
	define('ORDER_PIC2_PATH','D:/auto_file/order_2/');
	define('ORDER_PIC3_PATH','D:/auto_file/order_3/');
	define('AD_PIC_PATH','D:/auto_file/ad/');
*/

	define('UPDATE_URL','');
	define('CUSTOMSERVICE_URL',SERVICE_URL.'customservice/');
	define('CUSTOMSERVICE_TEL','');
	define('LP1_URL',SERVICE_URL.'user_lp1/');
	define('LP2_URL',SERVICE_URL.'user_lp2/');
	define('PIC_URL',SERVICE_URL.'user/');
	define('LICENSE_URL',SERVICE_URL.'user_license/');
	define('AGENT_URL',SERVICE_URL.'user_agent/');
	define('BAND_PIC_URL',SERVICE_URL.'band/');
	define('CAR_PIC_URL',SERVICE_URL.'car/');
	define('PARTTYPE_PIC_URL',SERVICE_URL.'parttype/');
	define('PARTBAND_PIC_URL',SERVICE_URL.'partband/');
	define('INQUIRY_PIC1_URL',SERVICE_URL.'inquiry_1/');
	define('INQUIRY_PIC2_URL',SERVICE_URL.'inquiry_2/');
	define('INQUIRY_PIC3_URL',SERVICE_URL.'inquiry_3/');
	define('INQUIRY_AUM_URL',SERVICE_URL.'inquiry_aum/');
	define('BIDDING_PIC1_URL',SERVICE_URL.'bidding_1/');
	define('BIDDING_PIC2_URL',SERVICE_URL.'bidding_2/');
	define('BIDDING_PIC3_URL',SERVICE_URL.'bidding_3/');
	define('BIDDING_AUM_URL',SERVICE_URL.'bidding_aum/');
	define('ORDER_PIC1_URL',SERVICE_URL.'order_1/');
	define('ORDER_PIC2_URL',SERVICE_URL.'order_2/');
	define('ORDER_PIC3_URL',SERVICE_URL.'order_3/');
	define('AD_PIC_URL',SERVICE_URL.'ad/');
	
	define('CUSTOMSERVICE_PATH','/usr/share/nginx/html/auto/resource/customservice/');
	define('USER_LP1_PATH','/usr/share/nginx/html/auto/resource/user_lp1/');
	define('USER_LP2_PATH','/usr/share/nginx/html/auto/resource/user_lp2/');
	define('USER_PIC_PATH','/usr/share/nginx/html/auto/resource/user/');
	define('USER_LICENSE_PATH', '/usr/share/nginx/html/auto/resource/user_license/');
	define('USER_AGENT_PATH','/usr/share/nginx/html/auto/resource/user_agent/');
	define('BAND_PIC_PATH','/usr/share/nginx/html/auto/resource/band/');
	define('CAR_PIC_PATH','/usr/share/nginx/html/auto/resource/car/');
	define('PARTTYPE_PIC_PATH','/usr/share/nginx/html/auto/resource/parttype/');
	define('PARTBAND_PIC_PATH','/usr/share/nginx/html/auto/resource/partband/');
	define('INQUIRY_PIC1_PATH','/usr/share/nginx/html/auto/resource/inquiry_1/');
	define('INQUIRY_PIC2_PATH','/usr/share/nginx/html/auto/resource/inquiry_2/');
	define('INQUIRY_PIC3_PATH','/usr/share/nginx/html/auto/resource/inquiry_3/');
	define('INQUIRY_AUM_PATH','/usr/share/nginx/html/auto/resource/inquiry_aum/');
	define('BIDDING_PIC1_PATH','/usr/share/nginx/html/auto/resource/bidding_1/');
	define('BIDDING_PIC2_PATH','/usr/share/nginx/html/auto/resource/bidding_2/');
	define('BIDDING_PIC3_PATH','/usr/share/nginx/html/auto/resource/bidding_3/');
	define('BIDDING_AUM_PATH','/usr/share/nginx/html/auto/resource/bidding_aum/');
	define('ORDER_PIC1_PATH','/usr/share/nginx/html/auto/resource/order_1/');
	define('ORDER_PIC2_PATH','/usr/share/nginx/html/auto/resource/order_2/');
	define('ORDER_PIC3_PATH','/usr/share/nginx/html/auto/resource/order_3/');
	define('AD_PIC_PATH','/usr/share/nginx/html/auto/resource/ad/');

	define('ID_ENCODE_KEY','pK7Fdtzu5T-0');
	define('EASYMOB_ID_KEY','skdJKJfdisj-0');
	
	define('IS_DEBUG',TRUE);
	
	define('LOG_PATH','/usr/share/nginx/html/auto/log/log');
	
	define('EASYMOB_URL','');
	define('EASYMOB_CLIENT_ID','');
	define('EASYMOB_SECRET_KEY','');

	define('BUYER_SCORE_GOOD',1);
	define('BUYER_SCORE_NORMAL',2);
	define('BUYER_SCORE_BAD',3);

	define('TRANSACTION_TYPE_IN',0);
	define('TRANSACTION_TYPE_OUT',1);

	define('COUPON_GET_TYPE_SHARE',0);				//分享
	define('COUPON_GET_TYPE_REGISTER',1);			//注册
	define('COUPON_GET_TYPE_FIRST_ORDER',2);		//首单成功
	define('COUPON_GET_TYPE_SHARE_FIRST_ORDER',3);	//被分享人首单成功
	define('COUPON_GET_TYPE_DATE',5);				//特定时间

	define('SORT_TYPE_DEFAULT',0);					//默认
	define('SORT_TYPE_USER_LEVEL',1);				//用户等级
	define('SORT_TYPE_USER_SCORE',2);				//用户信用
	define('SORT_TYPE_PRICE_ASC',3);				//价格升序
	define('SORT_TYPE_PRICE_DESC',4);				//价格降序
	define('SORT_TYPE_DISTANCE',5);					//距离

	define('VERSION_NUM_SELL',3);					//卖家版本号
	define('VERSION_STR_SELL','1.2');					//卖家版本名
	define('VERSION_URL_SELL_ANDROID','http://123.57.53.94/auto/download/sell.apk');			//卖家下载地址
	
	define('VERSION_NUM_BUY',3);					//买家版本号
	define('VERSION_STR_BUY','1.2');					//买家版本名
	define('VERSION_URL_BUY_ANDROID','http://123.57.53.94/auto/download/buy.apk');			//买家下载地址

	define('NO_CAR_MESSAGE','无相关车型信息');
?>