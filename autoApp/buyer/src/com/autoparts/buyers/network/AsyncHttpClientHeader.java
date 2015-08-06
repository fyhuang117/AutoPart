package com.autoparts.buyers.network;

import android.content.Context;
import android.text.TextUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.autoparts.buyers.preferences.Preferences;
import com.autoparts.buyers.utils.Utils;

/**
 * 网络请求头文件
 * Created by:Liuhuacheng
 * Created time:14-9-6
 */
public class AsyncHttpClientHeader {
    public static void setHeader(Context context, AsyncHttpClient client) {
//
//        client.removeAllHeaders();
//        client.removeHeader("Cookie");

        Preferences preferences = Preferences.getInstance(context);
        client.addHeader("Accept-Encoding", "gzip, deflate");
        client.addHeader("Accept-Language", "zh-CN");
        client.addHeader("Accept", "application/json, application/xml, text/html, text/*, image/*, */*");
//      client.addHeader("Content-Type","application/json");
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
//        client.addHeader("Content-Type","multipart/form-data; boundary=---------------------------7de383267909a2");
        String cookie = preferences.getCookie();
        if (!TextUtils.isEmpty(cookie)) {
            Utils.showLog("request Cookie=" + cookie);
            client.addHeader("Cookie", cookie);
        }else {
            client.removeHeader("Cookie");
        }
    }

    public static void setHeaderPro(Context context, AsyncHttpClient client) {
        Preferences preferences = Preferences.getInstance(context);
        client.addHeader("Accept-Encoding", "gzip, deflate");
        client.addHeader("Accept-Language", "zh-CN");
        client.addHeader("Accept", "application/json, application/xml, text/html, text/*, image/*, */*");
        client.addHeader("Content-Type","multipart/form-data");
        String cookie = preferences.getCookie();
        if (!TextUtils.isEmpty(cookie)) {
            Utils.showLog("request Cookie="+cookie);
            client.addHeader("Cookie", cookie);
        }
    }
}
