package com.autoparts.buyers.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据存数
 * Created by:Liuhuacheng
 * Created time:15-5-18
 */
public class Preferences {
    private static Preferences instance;
    public final String AUTO_LOGIN = "auto_login";
    public final String FIRST_RUNNING = "first";
    public final String LOGINED = "logined";
    public final String LOGIN_COOKIE = "cookie";
    public static final String LOGIN_ID = "id";
    public final String LOGIN_PASSWORD = "password";
    public final String LOGIN_PHONE = "phone";
    public final String LOGIN_USERNAME = "username";
    public final String LOGIN_USERNO = "userNo";
    public final String LOGIN_USERPortrait = "portrait";
    public final String USER_ORDER = "order";
    public final String mRecordPath = "mRecordPath";
    public final String SHARE_ID = "share_id";//分享id
    public final String ENB = "enb";//用户审核状态 0是未申请审核 1-审核通过 2-审核不通过 3-待审核
    private Context context;
    private SharedPreferences.Editor editor;
    private SharedPreferences spf;


    public String CAR_NAME = "car_name",CAR_ID = "car_id";
    public String YEAR_NAME = "year_name",YEAR_ID = "year_id";

    private Preferences(Context paramContext) {
        this.context = paramContext.getApplicationContext();
        this.spf = paramContext.getSharedPreferences(Constants.SharedName, 0);
        this.editor = this.spf.edit();
    }

    public static Preferences getInstance(Context paramContext) {
        try {
            if (instance == null)
                instance = new Preferences(paramContext);
            Preferences localPreferences = instance;
            return localPreferences;
        } finally {
        }
    }

    public void clearData() {
        this.editor.clear();
        this.editor.commit();
    }

    //设置用户数据
    public void setStringData(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringData(String key) {
        return spf.getString(key, "").trim();
    }

    //设置用户数据
    public void setIntData(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getIntData(String key) {
        return spf.getInt(key, 0);
    }

    //设置用户数据
    public void setBooleanData(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBooleanData(String key) {
        return spf.getBoolean(key, true);
    }

    public String getCookie() {
        return this.spf.getString("cookie", "").trim();
    }

    public void getCookieFromHead(Header paramHeader) {
        String str1 = paramHeader.getName();
        if ((str1 != null) && (str1.equals("Set-Cookie"))) {
            String str2 = paramHeader.getValue();
            paramHeader.getName();
            if (str2 != null) {
                setCookie(str2);
                Utils.showLog("====cookie=" + str2);
            }
        }
    }

    public String getData(String paramString) {
        return this.spf.getString(paramString, "").trim();
    }

    public boolean getFirstRunning() {
        return this.spf.getBoolean("first", false);
    }

    public boolean getIsLogin() {
        return this.spf.getBoolean("logined", false);
    }

    public String getLoginPassword() {
        return this.spf.getString("password", "").trim();
    }

    public String getLoginUserPortrait() {
        return this.spf.getString("portrait", "").trim();
    }

    public String getLoginUsername() {
        return this.spf.getString("username", "").trim();
    }


    public String getUserNO() {
        return this.spf.getString("userNo", "").trim();
    }

    public void saveUsetInfo(ResponseModel paramResponseModel) {
        if (paramResponseModel != null) {
            Map<String, String> params = paramResponseModel.getMap();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                setStringData(key, value);
            }
        }
    }

    public void setCommonData(String paramString1, String paramString2) {
        this.editor.putString(paramString1, paramString2);
        this.editor.commit();
    }

    public void setCookie(String paramString) {
        this.editor.putString("cookie", paramString);
        this.editor.commit();
    }

    public void setFirstRunning(boolean paramBoolean) {
        this.editor.putBoolean("first", paramBoolean);
        this.editor.commit();
    }

    public void setIsLogin(boolean paramBoolean) {
        this.editor.putBoolean("logined", paramBoolean);
        this.editor.commit();
    }

    public void setUserID(String paramString) {
        this.editor.putString(LOGIN_ID, paramString);
        this.editor.commit();
    }

    public String getUserID() {
        return this.spf.getString(LOGIN_ID, "").trim();
    }

    public void setLoginPassword(String paramString) {
        this.editor.putString("password", paramString);
        this.editor.commit();
    }

    public void setLoginPhone(String paramString) {
        this.editor.putString(LOGIN_PHONE, paramString);
        this.editor.commit();
    }

    public String getLoginPhone() {
        return this.spf.getString(LOGIN_PHONE, "").trim();
    }


    public void setLoginUserPortrait(String paramString) {
        this.editor.putString("portrait", paramString);
        this.editor.commit();
    }

    public void setLoginUsername(String paramString) {
        this.editor.putString("username", paramString);
        this.editor.commit();
    }

    public void setUserNO(String paramString) {
        this.editor.putString("userNo", paramString);
        this.editor.commit();
    }

    public boolean getOrder() {
        return this.spf.getBoolean(USER_ORDER, false);
    }

    public void setOrder(boolean b) {
        this.editor.putBoolean(USER_ORDER, b);
        this.editor.commit();
    }

    public void setRecordPath(String path) {
        this.editor.putString(mRecordPath, path);
        this.editor.commit();
    }

    public String getRecordPath() {
        return this.spf.getString(mRecordPath, "").trim();
    }

    public int getUserState() {
        String str = this.spf.getString(ENB, "").trim();
        int state = CommonData.getInstance(context).getIntData(str);
        return state;
    }
}
