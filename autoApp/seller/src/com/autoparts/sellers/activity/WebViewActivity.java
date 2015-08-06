package com.autoparts.sellers.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.autoparts.sellers.R;
import com.autoparts.sellers.utils.DialogUtil;
import com.autoparts.sellers.utils.Utils;

/**
 *
 * Created by:Liuhuacheng
 * Created time:15-1-19
 */
public class WebViewActivity extends BaseActivity {
    private Context context;
    private WebView mWebView;
    private String url;
    private String title;
    private Dialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.webview_fragment);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        context = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            title = bundle.getString("title");
        }
        Utils.showLog("url===" + url);
        Utils.showLog("title===" + title);
        setTitle(title);
        loadingDialog = DialogUtil.loadingProgress(context);
        loadingDialog.setCancelable(true);
        initWebView();


    }

    private void initWebView() {
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
}