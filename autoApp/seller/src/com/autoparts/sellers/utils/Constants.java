package com.autoparts.sellers.utils;

import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.ResponseModel;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 常量配置
 * Created by:Liuhc
 * Created time:15-3-3
 */
public class Constants {

    public static final String SharedName = "autoparts";
    public static final String cacheDir = "/AutoParts/cache";

    public static final int rows = 10;//请求列表行数
    public static final int status = 0;//接口成功状态
    public static final int type = 1;//"type":"用户标识(0-买家1-卖家)"

    public static final String HOST = "http://123.57.53.94/";
    //    public static final String HOST = "http://192.168.1.110";
    public static final String HOST_URL = HOST + "auto/services/";

    //登录验证码securitycode.php
    public static String USER_LOGIN_CODE = HOST_URL + "securitycode.php";

    public static String USER_PHONE = HOST_URL + "customservicetel.php"; //获取客服电话
    public static String USER_LOGIN = HOST_URL + "login.php"; //
    public static String USER_CODE = HOST_URL + "securitycode.php";
    public static String USER_UP_INFO = HOST_URL + "baseuserinfo.php";
    public static String USER_UP_ALL_INFO = HOST_URL + "updateuserinfo.php";
    public static String USER_INFO = HOST_URL + "userinfo.php";
    public static String USER_VERSION = HOST_URL + "version.php";
    public static String USER_SERVICE = HOST_URL + "customservice.php";
    public static String USER_AD = HOST_URL + "adsummaries.php";
    public static String USER_WALLET = HOST_URL + "wallet.php";
    public static String USER_WALLET_DETAIL = HOST_URL + "transaction.php";
    public static String USER_WALLET_GET = HOST_URL + "requestout.php";//提现
    public static String USER_MESSAGE_COUNT = HOST_URL + "messagecount.php";//用户消息数量
    public static String USER_MESSAGE_LIST = HOST_URL + "messagelist.php";//用户消息列表
    public static String USER_MESSAGE_DEL = HOST_URL + "delmessage.php";//用户消息删除
    public static String USER_REQUEST_AUDIT = HOST_URL + "user_requestaudit.php";//提交审核
    public static String USER_SHARE = HOST + "auto/pages/sharep.php?s=";//分享
    public static String USER_ALIAS = HOST_URL + "user_alias.php";//设置用户别名

    public static String INQUIRE_LIST = HOST_URL + "inquirylist.php";//获取所有询价单
    public static String INQUIRE_DETAIL = HOST_URL + "inquiry.php";//询价单详情
    public static String INQUIRE_SAVE = HOST_URL + "savebidding.php";//新建竞价单
    public static String INQUIRE_BIDDING_LIST = HOST_URL + "userbiddingsummaries.php";//获取用户对应的竞价单
    public static String INQUIRE_BIDDING_DEL = HOST_URL + "delbidding.php";//删除竞价单
    public static String ORDER_BIDDING = HOST_URL + "bidding.php";//获取竞价单信息
    public static String ORDER_BIDDING_Update = HOST_URL + "updatebidding.php";//修改竞价单信息

    public static String BIDDING_NUM = HOST_URL + "biddingcount.php";//获取竞价单的数量
    public static String ORDER_NUM = HOST_URL + "ordercount.php";//获取未完成的订单的数量


    public static String INQUIRE_BAND = HOST_URL + "band.php";//获取汽车品牌列表
    public static String INQUIRE_BAND_SUB = HOST_URL + "subband.php";//获取汽车品牌--子品牌
    public static String INQUIRE_YEARS = HOST_URL + "years.php";//获取车系对应的年份
    public static String INQUIRE_PART_BAND = HOST_URL + "partband.php";//获取配件品牌列表
    public static String INQUIRE_AREA = HOST_URL + "area.php";//商圈

    public static String INQUIRE_PART_LIST = HOST_URL + "parttype.php";//获取配件列表
    public static String INQUIRE_PART_V_LIST = HOST_URL + "vparttype.php";//获取易损品列表
    public static String INQUIRE_PART_C_LIST = HOST_URL + "cparttype.php";//获取消耗品列表

    public static String ORDER_SAVE = HOST_URL + "saveinquiry.php";//新建保存询价单
    public static String ORDER_GET_summaries = HOST_URL + "inquirysummaries.php";//获取询价单摘要列表
    public static String ORDER_DEL = HOST_URL + "delinquiry.php";//删除询价单
    public static String ORDER_GET = HOST_URL + "ordersummaries.php";//获取我的订单
    public static String ORDER_GET_STATE = HOST_URL + "orderstatuslist.php";//根据订单状态 获取我的订单

    public static String ORDER_INQUIRY_BIDDING = HOST_URL + "inquirybiddingsummaries.php";//获取询价单对应的竞价单列表
    public static String ORDER_SAVE_ORDER = HOST_URL + "saveorder.php";//下单
    public static String ORDER_DETAIL = HOST_URL + "order.php";//订单详情
    public static String ORDER_PAY = HOST_URL + "payorder.php";//付款
    public static String ORDER_SEND = HOST_URL + "sendorder.php";//替卖家发货
    public static String ORDER_RECEIVE = HOST_URL + "receiveorder.php";//订单收货
    public static String ORDER_SCORE = HOST_URL + "buyerscore.php";//评价

    //注册
    public static String USER_REGISTER = HOST_URL + "common/user/register/nextStep";


    public void getData() {
        String url = Constants.INQUIRE_YEARS;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bandid", "bandid");
        HttpClientUtils.post(null, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                setData(response);
            }

            @Override
            public void onResultJson(String json) {
                super.onResultJson(json);
            }
        });

    }

    public void setData(ResponseModel responseModel) {
        Vector<HashMap<String, Object>> maps = responseModel.getMaps();
    }

    /*
    * 等级的算法，跟页面的显示 ，这个还要具体定一下
    *
    *
    * "parttype_quality":"品质(int)",
"avi":"是否现货(int)",
"qau":"质保(int)",
"pay":"支付方式(int)",
"del":"交付方式(int)",

    *
    * */

}
