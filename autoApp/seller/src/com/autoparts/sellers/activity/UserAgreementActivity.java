package com.autoparts.sellers.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.utils.Constants;
import com.autoparts.sellers.utils.DialogUtil;
import com.autoparts.sellers.utils.Utils;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户 服务条款
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class UserAgreementActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    String title = "";
    private WebView mWebView;
    private Dialog loadingDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.webview);
        super.onCreate(savedInstanceState);
        title = getString(R.string.login_deal);
        setTitle(title);
        init();

    }

    private void init() {
        context = this;
        loadingDialog = DialogUtil.loadingProgress(context);
        getData();
    }

    private void initWebView(String url) {
        mWebView = (WebView) findViewById(R.id.webview);
        //设置WebView属性，能够执行Javascript脚本
        mWebView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        mWebView.getSettings().setBuiltInZoomControls(false);// 设置支持缩放
        mWebView.getSettings().setSavePassword(false); // 设置是否保存密码
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
//        webview_fragment.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
        // 设置支持JavaScript 脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 设置支持各种不同的设备
        mWebView.getSettings()
                .setUserAgentString(
                        "Mozilla/5.0   (iPad;  U;  CPU  OS   3_2  like  Mac   OS  X;  en-us)"
                                + " AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10");
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        mWebView.setInitialScale(100);

        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingDialog.dismiss();

            }
        });
    }

    public void getData() {
        String url = Constants.USER_SERVICE;
        Map<String, Object> params = new HashMap<String, Object>();

        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                String url = response.getMap().get("url");
                Utils.showLog("url=="+url);
//                url = "wap.baidu.com";
                initWebView(url);


            }
        });

    }

    public void setData(ResponseModel responseModel){
//        "app":"应用版本号(int)",
//        "dat":"数据版本号(int)",
//        "sta":"0-无更新1-建议软件更新但没有数据更新2-建议软件更新并且有数据更新3-强制软件更新4-无应用更新但有数据更新(int)",
//        "g_dat":"新版本下载地址"

    }

}
