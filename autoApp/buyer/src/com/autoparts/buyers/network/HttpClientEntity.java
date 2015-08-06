package com.autoparts.buyers.network;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.autoparts.buyers.R;
import com.autoparts.buyers.activity.UserLoginActivity;
import com.autoparts.buyers.preferences.Preferences;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.DialogUtil;
import com.autoparts.buyers.utils.Utils;
import org.apache.http.Header;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 网络请求类
 * Created by:Liuhuacheng
 * Created time:14-10-22
 */
public class HttpClientEntity {

    public static AsyncHttpClient client = new AsyncHttpClient();
    private static Dialog progressDilog;

    public static AsyncHttpClient getClient() {

        return client;
    }

    /**
     * @param context       上下文
     * @param params        请求参数
     * @param url           请求地址
     * @param resultHandler 回调接口
     */
    public static void post(final Context context, RequestParams params, final String url, final HttpResultHandler resultHandler) {
        boolean b = Utils.isNetwork(context);
        //无网络情况下，判断是否有缓存存在
        if (!b) {
            String message = context.getString(R.string.network_unavailable);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
//            show(context);
//            if (client == null) {
            client = new AsyncHttpClient();
//            }
            AsyncHttpClientHeader.setHeader(context, client);
            final String str = url + "?params=" + params.toString();
            Utils.showLog("post url=" + str);
            client.setEnableRedirects(true);
            client.post(context, url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    Utils.showLog("onSuccess state====" + i);
                    String json = Utils.getJson(bytes);
                    Utils.showLog(str + "=json====" + json);
                    resultHandler.onResultJson(json);
                    ResponseModel responseModel = JsonParserUtils.jsonParse(json);
                    int state = responseModel.getState();
                    if (state == Constants.status) {//如果数据获取成功
                        String message = responseModel.getDiscription();
                        resultHandler.onResultSuccess(headers, responseModel, message, state);
                    } else if (state == 12) {
                        reLogin(context);
                    } else {
                        String desc = responseModel.getDiscription();
                        Utils.showToastShort(context, desc);
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Utils.showLog(url + "=onFailure state====" + i);
                    resultHandler.onResultFail("onFailure", -1);
                    Utils.showToastShort(context, "" + throwable.getMessage());
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    dismiss();
                    resultHandler.onFinish();
                }
            });
        }
    }

    /**
     * @param context       上下文
     * @param params        请求参数
     * @param url           请求地址
     * @param resultHandler 回调接口
     */
    public static void postDialog(final Context context, RequestParams params, final String url, final HttpResultHandler resultHandler) {
        boolean b = Utils.isNetwork(context);
        //无网络情况下，判断是否有缓存存在
        if (!b) {
            String message = context.getString(R.string.network_unavailable);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            show(context);
//            if (client == null) {
            client = new AsyncHttpClient();
//            }
            boolean isJson = false;
            AsyncHttpClientHeader.setHeader(context, client);
            final String str = url + "?params=" + params.toString();
            Utils.showLog("post url=" + str);
            client.post(context, url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    Utils.showLog("onSuccess state====" + i);
                    String json = Utils.getJson(bytes);
                    Utils.showLog(str + "=json====" + json);
                    resultHandler.onResultJson(json);
                    ResponseModel responseModel = JsonParserUtils.jsonParse(json);
                    int state = responseModel.getState();
                    if (state == Constants.status) {//如果数据获取成功
                        String message = responseModel.getDiscription();
                        resultHandler.onResultSuccess(headers, responseModel, message, state);
                    } else if (state == 12) {
                        reLogin(context);
                    } else {
                        String desc = responseModel.getDiscription();
                        Utils.showToastShort(context, desc);
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Utils.showLog(url + "=onFailure state====" + i);
                    resultHandler.onResultFail("onFailure", -1);
                    Utils.showToastShort(context, "" + throwable.getMessage());
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    dismiss();
                    resultHandler.onFinish();
                }
            });
        }
    }

    /**
     * @param context       上下文
     * @param url           请求地址
     * @param resultHandler 回调接口
     */
    public static void get(final Context context, final String url, final HttpResultHandler resultHandler) {
        boolean b = Utils.isNetwork(context);
        if (!b) {
            String message = context.getString(R.string.network_unavailable);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            if (client == null) {
                client = new AsyncHttpClient();
            }
            boolean isJson = false;
            AsyncHttpClientHeader.setHeader(context, client);
            Utils.showLog("get url====" + url);
            client.get(context, url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    Utils.showLog("onSuccess state====" + i);
                    String json = Utils.getJson(bytes);
                    Utils.showLog(url + "=json====" + json);
                    ResponseModel responseModel = JsonParserUtils.jsonParse(json);
                    int state = responseModel.getState();
                    if (state == Constants.status) {//如果数据获取成功
                        String message = responseModel.getDiscription();
                        resultHandler.onResultSuccess(headers, responseModel, message, state);
                    } else if (state == 12) {
                        reLogin(context);
                    } else {
                        String desc = responseModel.getDiscription();
                        Utils.showToastShort(context, desc);
                    }

                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Utils.showLog("onFailure state====" + i);
                    resultHandler.onResultFail("onFailure", -1);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    resultHandler.onFinish();
                }
            });
        }
    }

    public static InputStream httpRequest(Context context, String strUrl, String requestString, final HttpResultHandler callback) {
        show(context);
        String result = "";
        Utils.showLog("strUrl===" + strUrl);
        Utils.showLog("requestString===" + requestString);
        int res = 0;
        InputStream inputStream = null;
        try {
            URL requestUrl = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Accept", "application/json, application/xml, text/html, text/*, image/*, */*");
            conn.setRequestProperty("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
//            ("Content-Type", "application/x-www-form-urlencoded");

            String cookie = Preferences.getInstance(context).getCookie();
            Utils.showLog("Cookie=" + cookie);
            if (cookie != null) {
                conn.setRequestProperty("Cookie", cookie);
            }
            conn.setReadTimeout(50000);
            conn.setConnectTimeout(50000);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            if (requestString != null) {
                OutputStream outStrm = conn.getOutputStream();
//                DataOutputStream dos = new DataOutputStream(outStrm);
                outStrm.write(requestString.toString().getBytes());
                outStrm.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                res = conn.getResponseCode();
                Utils.showLog("res====" + res);

                if (res == 200 || res == 201) {
                    inputStream = conn.getInputStream();
                    //取得数据
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    //使用循环体来获得数据
                    while ((line = reader.readLine()) != null) {
                        result += line;
                    }
//                    result = sb1.toString();
                    Utils.showLog("json: ===" + result);
                    ResponseModel responseModel = JsonParserUtils.jsonParse(result);
                    int state = responseModel.getState();
                    if (state == 12) {
                        reLogin(context);
                    } else {
                        String message = responseModel.getDiscription();
                        callback.onResultSuccess(null, responseModel, message, state);
                    }
                } else {
                    callback.onResultFail("onFailure", res);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            callback.onResultFail("onFailure", -1);
            Utils.showLog("MalformedURLException: ");
        } catch (IOException e) {
            e.printStackTrace();
            callback.onResultFail("onFailure", -1);
            Utils.showLog("IOException: ");
        }
        dismiss();
        callback.onFinish();
        return inputStream;
    }

    public static void show(Context context) {
        if (progressDilog != null && progressDilog.isShowing()) {
        } else {
            try {
                progressDilog = DialogUtil.loadingProgress(context);
                progressDilog.show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    public static void dismiss() {
        if (progressDilog != null && progressDilog.isShowing()) {
            progressDilog.dismiss();
        }
    }

    public static void reLogin(Context context) {
        Preferences preferences = Preferences.getInstance(context);
        preferences.setLoginPassword("");
        Intent intent = new Intent(context, UserLoginActivity.class);
        intent.putExtra("isLogin", true);
        context.startActivity(intent);
    }
}
