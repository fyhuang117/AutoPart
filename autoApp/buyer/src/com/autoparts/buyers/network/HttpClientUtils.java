package com.autoparts.buyers.network;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;
import com.autoparts.buyers.R;
import com.autoparts.buyers.activity.UserLoginActivity;
import com.autoparts.buyers.preferences.Preferences;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.DialogUtil;
import com.autoparts.buyers.utils.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.entity.FileUploadEntity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.*;
import java.util.Map;

/**
 * 网络请求类
 * Created by:Liuhuacheng
 * Created time:14-10-22
 */
public class HttpClientUtils {

    public static AsyncHttpClient client = new AsyncHttpClient();
    private static Dialog progressDilog;

    public static AsyncHttpClient getClient() {

        return client;
    }

    /**
     * @param context       上下文
     * @param params        请求参数 Map<String, String>
     * @param url           请求地址
     * @param resultHandler 回调接口
     */
    public static void post(final Context context, final String url, Map<String, Object> params, final HttpResultHandler resultHandler) {
        boolean b = Utils.isNetwork(context);
        //无网络情况下，判断是否有缓存存在
        if (!b) {
            String message = context.getString(R.string.network_unavailable);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            resultHandler.onFinish();
        } else {

            HttpUtils http = new HttpUtils();

            // 设置返回文本的编码， 默认编码UTF-8
            //http.configResponseTextCharset("GBK");

            // 自动管理 cookie
//        http.configCookieStore(preferencesCookieStore);
            String paramsJson = null;
            if (url.equals(Constants.ORDER_GET_STATE)) {
                paramsJson = (String) params.get("json");
            } else {
                paramsJson = CommonData.getInstance(context).getParams(params);
            }

            final String str = url + "?params=" + paramsJson;
            Utils.showLog("post url=" + str);
            RequestParams requestParams = new RequestParams(); // 默认编码UTF-8
            String charset = "UTF-8";
            try {
                requestParams.setBodyEntity(new StringEntity(paramsJson, charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            String pic = (String) params.get("pic");
//            if (!TextUtils.isEmpty(pic)) {
//                Utils.showLog("pic==="+pic);
//                requestParams.addBodyParameter("pic", new File("pic"));
////                requestParams.setBodyEntity(new FileUploadEntity(new File(pic), "binary/octet-stream"));
//            }
            http.send(HttpRequest.HttpMethod.POST, url, requestParams, new RequestCallBack<String>() {
                @Override
                public void onStart() {
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    if (isUploading) {
                    } else {
                    }
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String json = responseInfo.result;
                    Utils.showLog(str + "=json====" + json);
                    Utils.showLog("=json====" + json);
                    String message = "success";
                    int state = responseInfo.statusCode;
                    Utils.showLog("=state====" + state);
                    ResponseModel responseModel = JsonParserUtils.jsonParse(responseInfo.result);
                    resultHandler.onResultSuccess(responseInfo.getAllHeaders(), responseModel, message, state);
                    resultHandler.onResultJson(json);
                    resultHandler.onFinish();
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    int code = error.getExceptionCode();
                    String message = msg;
                    Utils.showLog("onFailure code=" + code);
                    Utils.showLog("onFailure message=" + message);
                    if (message.contains("SocketTimeoutExceptio")){
                        Utils.showToastShort(context,"请求超时，请检查网络后重试");
                    }else if (code == 400){
                        Utils.showToastShort(context,message);

                    }
                    resultHandler.onResultFail(message, code);
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
