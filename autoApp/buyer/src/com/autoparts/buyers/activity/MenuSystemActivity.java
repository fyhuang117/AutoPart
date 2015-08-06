package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.network.HttpClientEntity;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import org.apache.http.Header;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用信息
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MenuSystemActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    String title = "";
    private TextView version;
    private int currentCode;
    private String currentVersion;
    public String fileName = "autopart_buyer.apk";//apk名称
    public final static String sd_card = Environment.getExternalStorageDirectory() + "/";
    private String downloadUrl;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.more_system);
        super.onCreate(savedInstanceState);
        title = getString(R.string.more_system);
        setTitle(title);
        init();

    }

    private void init() {
        context = this;
        version = (TextView) findViewById(R.id.version);
        initVersion();
    }

    public void commit(View view) {
//        Utils.showToastShort(context, "当前版本已为最新版本");
        getData();
    }


    /**
     * 获取版本信息
     */
    private void initVersion() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            currentVersion = info.versionName;
            currentCode = info.versionCode;
            version.setText("V" + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void getData() {
        String url = Constants.USER_VERSION;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", "0");//"type":"用户标识(0-买家1-卖家)"
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                setData(response);


            }
        });

    }

    public void setData(ResponseModel responseModel) {
//        "app":"应用版本号(int)",
//        "dat":"数据版本号(int)",
//        "sta":"0-无更新1-建议软件更新但没有数据更新2-建议软件更新并且有数据更新3-强制软件更新4-无应用更新但有数据更新(int)",
//        "g_dat":"新版本下载地址"

        String version_num = responseModel.getMap().get("version_num");
        String version_str = responseModel.getMap().get("version_str");
        downloadUrl = responseModel.getMap().get("download");

        int version = 0;
        if (!TextUtils.isEmpty(version_num)) {
            version = Integer.parseInt(version_num);
        }

        if (version > currentCode) {
            String version_update = "发现新版本,是否立即更新!";
            showDialog(version_update, true);
        } else {
            String version_hint = "当前版本已为最新版本";
            Utils.showToastShort(context, version_hint);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.confirm_ok:
                Utils.showToastShort(context, "正在下载新版本，请稍后");
                synsDownload();
                break;
            case R.id.icon_search:
                break;
        }
    }

    //下载apk文件
    private void synsDownload() {
        String path = sd_card+fileName;
        final File file = new File(path);
        HttpClientEntity.getClient().get(downloadUrl, new FileAsyncHttpResponseHandler(file) {
            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                // Do something with the file `response`
                String path = response.getAbsolutePath();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(path)),
                        "application/vnd.android.package-archive");
                startActivity(intent);
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }
        });
    }

}
