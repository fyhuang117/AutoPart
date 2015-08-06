package com.autoparts.buyers.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.widget.EditText;
import com.autoparts.buyers.R;
import com.autoparts.buyers.activity.ImageViewActivity;
import com.autoparts.buyers.model.CommonLetterModel;
import com.autoparts.buyers.model.ContactUtils;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.preferences.Preferences;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by:Liuhuacheng
 * Created time:15-5-18
 */
public class CommonData {

    public static final int PHOTO_REQUEST_GALLERY = 20002;
    public static final int PHOTO_REQUEST_TAKEPHOTO = 20001;
    public static final int REQUEST_CODE_MODEL = 100;
    public static final int REQUEST_CODE_SORT = 102;
    public static final int REQUEST_CODE_YEAR = 101;
    public static final int REQUEST_INQUIRY_HISTORY = 103;

    public static final int REQUEST_INQUIRY_OTHER = 104;//其它要求
    public static final int REQUEST_INQUIRY_BRAND = 105;//品牌分类
    public static final int REQUEST_INQUIRY_LOCATION = 106;//卖家位置
    public static final int REQUEST_INQUIRY_VOICE = 107;//语音
    public static final int REQUEST_INQUIRY_QUALITY = 108;//品质

    public static final int REQUEST_INQUIRY_COUPON = 109;//优惠券

    public static final int REQUEST_ORDER_CANCEL = 204;//订单取消
    public static final int REQUEST_USER_TEL = 201;//电话修改
    public static final int REQUEST_USER_ADDRESS = 202;//地址修改
    public static final int REQUEST_USER_NAME = 203;//名称

    public static final int REQUEST_ORDER_PAY = 301;//付款
    public static final int REQUEST_ORDER_SEND = 302;//发货
    public static final int REQUEST_ORDER_SCORE = 303;//评价


    public static final String ACTION_NAME = "action_name";//广播


    public static final String INQUIRE_NAME = "inquire_name";//名称
    public static final String INQUIRE_ID = "inquire_id";//i
    public static final String INQUIRE_YEAR_NAME = "inquire_year_name";//名称
    public static final String INQUIRE_YEAR_ID = "inquire_year_id";//id

    public static final String INQUIRE_parttypeid = "inquire_id";//i

    public static final String INQUIRE_URL = "inquire_url";//请求地址
    public static final String INQUIRE_LIST = "inquire_list";//询价配件列表
    public static final String INQUIRE_NUM = "inquire_num";//询价数量
    public static final String INQUIRE_VOICE = "inquire_voice";//语音
    public static final String INQUIRE_MES = "inquire_mes";//信息
    public static final String ORDER_STATE = "state";//订单状态


    public static final String MAPS = "maps";//数据传输

    public static int[] images = {R.drawable.rank_image1, R.drawable.rank_image2, R.drawable.rank_image3, R.drawable.rank_image4, R.drawable.rank_image5,
            R.drawable.rank_image6, R.drawable.rank_image7, R.drawable.rank_image8, R.drawable.rank_image9, R.drawable.rank_image10,
            R.drawable.rank_image11, R.drawable.rank_image12, R.drawable.rank_image13, R.drawable.rank_image14, R.drawable.rank_image15,
    };


    private static CommonData instance;
    private SharedPreferences spf;
    private Context context;
    private Preferences preferences;

    private CommonData(Context context) {
        this.context = context.getApplicationContext();
        preferences = Preferences.getInstance(context);
    }

    public static CommonData getInstance(Context context) {
        if (instance == null) {
            instance = new CommonData(context);
        }
        return instance;
    }


    public static final int[] wear_icons = new int[]{
            R.drawable.wear_icon_1,
            R.drawable.wear_icon_2,
            R.drawable.wear_icon_3,
            R.drawable.wear_icon_4,
            R.drawable.wear_icon_5,
            R.drawable.wear_icon_6,
            R.drawable.wear_icon_7,
            R.drawable.wear_icon_8,
            R.drawable.wear_icon_9,
            R.drawable.wear_icon_10,
    };

    public static final int[] consumable_icons = new int[]{
            R.drawable.consumable_icon_1,
            R.drawable.consumable_icon_2,
            R.drawable.consumable_icon_3,
            R.drawable.consumable_icon_4,
            R.drawable.consumable_icon_5,
            R.drawable.consumable_icon_6,
            R.drawable.consumable_icon_7,
            R.drawable.consumable_icon_8,
            R.drawable.consumable_icon_9,
            R.drawable.consumable_icon_10,
    };

    public static String[] orderFilter = {"全部", "待付款", "待发货", "待收货"};

    public static String[] orderSate = {"待付款", "待发货", "已完成", "取消", "待收货", "待评价"};


    public static String getOrderState(int state) {
        return orderSate[state];
    }

    public static List<String> getSort() {
        String[] strs = {"电器照明", "制动系", "底盘/悬挂", "皮带涨紧轮", "点火系", "雨刷", "滤清器", "油品"};
        List<String> mLists = new ArrayList<String>();
        mLists = Arrays.asList(strs);
        return mLists;
    }

    public static List<String> getSort2() {
        String[] strs = {"养护用品", "维修设备", "检测设备", "气动工具", "液压工具", "保养设备",
                "手动工具", "检测工具", "电动工具", "软件系统"};
        List<String> mLists = new ArrayList<String>();
        mLists = Arrays.asList(strs);
        return mLists;
    }

    public static String[] ss = {
            "刹车灯灯泡,前大灯灯泡,转向灯灯泡,前大灯灯泡,前雾灯灯泡,示宽灯灯泡,喇叭",
            "前刹车片,后刹车片,前刹车盘,后刹车盘,后刹车鼓",
            "前下摆臂,前下摆臂球头,前半轴内球笼,前半轴外球笼,前减震,后减震,前轮轴承,后轮轴承,转向横拉杆,转向横拉杆球头",
            "水泵,正时惰轮,正时涨紧轮,正时皮带,发电机皮带,助力泵皮带,空调泵皮带",
            "火花塞,点火线圈",
            "前风挡雨刷片,后风挡雨刷片",
            "机油滤清器,燃油滤清器,空调滤清器,空气滤清器",
            "汽机油,柴机油,自动变速箱油,齿轮油（手动变速箱油）,转向助力油,刹车油",
            "",
            ""
    };
    static String[] strs = {"电器照明", "制动系", "底盘/悬挂", "皮带涨紧轮", "点火系", "雨刷", "滤清器", "油品"};

    public static List<String> getSortTwo() {

        List<String> mLists = new ArrayList<String>();
        mLists = Arrays.asList(strs);
        return mLists;
    }

    public static List<CommonLetterModel> getSortList() {
        List<CommonLetterModel> contacts = new ArrayList<CommonLetterModel>();
        for (int i = 0; i < strs.length; i++) {
            CommonLetterModel contactModel = new CommonLetterModel();
            contactModel.setUser_position(i);
            contactModel.setUser_id(i + "");
            contactModel.setUser_image(i + "");
            String name = strs[i];
            contactModel.setUser_name(name);
//            String key = new ContactUtils().String2Alpha(name.substring(0, 1));

            String key = new ContactUtils().getPinYinHeadChar(name.substring(0, 1));
            key = key.toUpperCase();
            if (TextUtils.isEmpty(key)) {
                key = "#";
            }

            contactModel.setUser_key(key);
            contacts.add(contactModel);
        }
        return contacts;

    }


    public static void setEditCursor(EditText editText) {
        CharSequence text = editText.getText();
        //Debug.asserts(text instanceof Spannable);
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    public String getParams(Map<String, Object> params) {
        JSONObject jsonObject = new JSONObject();
        try {
            String id = preferences.getUserID();
            Utils.showLog("id==" + id);
            if (!TextUtils.isEmpty(id)) {
                jsonObject.put("id", id);
            }
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    jsonObject.put(key, value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getTime(String start_time, String end_time) {
        StringBuffer time = new StringBuffer();
        int time_mm = 1000 * 60;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new Date());

        try {
//            Date date_start = sDateFormat.parse(start_time);
            Date date_start = new Date();
            Date date_end = sDateFormat.parse(end_time);

            long long_start = date_start.getTime();
            long long_end = date_end.getTime();

            long tt = (long_end - long_start) / time_mm;
            long hh = tt / 60;//小时
            long mm = (tt % 60);//分钟
            if (hh != 0) {
                time.append(hh + "小时");
            }
            if (mm != 0) {
                time.append(mm + "分");
            }

            time.append("后自动失效");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time.toString();

    }

    public int getIntData(String str) {
        int state = 0;
        if (!TextUtils.isEmpty(str)) {
            state = Integer.parseInt(str);
        }
        return state;
    }

    //支付方式
    public String getPayStyle(int state) {
        String str = "";
        String[] pay = context.getResources().getStringArray(R.array.pay);
        if (state > 0 && state < pay.length) {
            str = pay[state];
        } else {
            str = pay[0];
        }
        return str;
    }

    //是否现货
    public String getAviStyle(int state) {
        String str = "";
        String[] pay = context.getResources().getStringArray(R.array.avis);
        if (state > 0 && state < pay.length) {
            str = pay[state];
        } else {
            str = pay[0];
        }
        return str;
    }

    //品质
    public String getQualityStyle(int state) {
        String str = "";
        String[] pay = context.getResources().getStringArray(R.array.parttype_quality);
        if (state > 0 && state < pay.length) {
            str = pay[state];
        } else {
            str = pay[0];
        }
        return str;
    }

    public void showImage(Context context, List<String> mGridPhotos, int position) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtra("url", mGridPhotos.get(position));
        context.startActivity(intent);
    }

    public String getDel(String str, String pof) {
        String data = "";
        if (!TextUtils.isEmpty(str)) {
            if (!str.contains("自提")) {
                data = str + "/邮费￥" + pof;
            } else {
                data = str;
            }
        }
        return data;

    }

    //获取用户信息
    public void getUserInfoData() {
        final String url = Constants.USER_INFO;
        Map<String, Object> params = new HashMap<String, Object>();
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (url.equals(Constants.USER_INFO)) {
                    preferences.saveUsetInfo(response);
                }

            }
        });

    }

    //设置用户别名
    public void setUserAlias(String alias) {
        final String url = Constants.USER_ALIAS;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("alias", alias);
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (url.equals(Constants.USER_INFO)) {
                    preferences.saveUsetInfo(response);
                }

            }
        });

    }

    public void share(Activity activity, String id) {
        String url = Constants.USER_SHARE + id;
        String shareContent = "哦了汽配欢迎您，领取优惠礼包！" + url;
        Intent sendIntent = new Intent();
        String title = "哦了汽配";
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        if (title != null) {
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        }

        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);
    }


    //-0是未申请审核 1-审核通过 2-审核不通过 3-待审核
    public void showUserState(int state) {
        String message = "您的账户尚未通过审核，请耐心等待";
        switch (state) {
            case 0:
                message = "您的账户尚未审核，请先提交审核";
                break;
            case 2:
                message = "您的账户审核不通过，请上传信息后,再次提交审核";
                break;
            case 3:
                message = "您的账户审核中，请耐心等待";
                break;
        }
        Utils.showToastShort(context, message);
    }

//    "parttype_quality":bool, 真=原厂
//    "avi"：bool, 真 = 现货
//    "qau":string 质保
//    "pay": bool， 真= 支付宝
//    "del":string //配送
//
//    define('ORDER_STATUS_NOPAY',0);				//未支付
//    define('ORDER_STATUS_NOSEND',1);			//未发货
//    define('ORDER_STATUS_COMPLETE',2);			//完成
//    define('ORDER_STATUS_CANCEL',3);			//取消
//    define('ORDER_STATUS_SEND',4);				//已发货
//    define('ORDER_STATUS_SEND',5);				//待评价

}
