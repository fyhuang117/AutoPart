package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.network.HttpClientEntity;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 用户登录--第一次补全信息
 * Created by:Liuhuacheng
 * Created time:15-1-29
 */
public class UserLoginInfoActivity extends BaseActivity {
    private Context context;
    private TextView login_user_deal;

    private Button login_btn;
    private EditText user_name, user_shop_address;

    private Timer timer;
    private int time = 60;
    private int recLen = time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.user_login_info);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setTitle(getString(R.string.login_user_info));
        context = this;
        login_user_deal = (TextView) findViewById(R.id.login_user_deal);
        login_user_deal.setOnClickListener(this);

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        user_name = (EditText) findViewById(R.id.user_name);
        user_shop_address = (EditText) findViewById(R.id.user_shop_address);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.login_btn:
                getData();
                break;
        }
    }


    public void getData() {
        String nam = user_name.getText().toString().trim();
        String adr = user_shop_address.getText().toString().trim();
        if (TextUtils.isEmpty(nam)) {
            Utils.showToastShort(context, getString(R.string.login_null_name));
        } else if (TextUtils.isEmpty(adr)) {
            Utils.showToastShort(context, getString(R.string.login_null_address));
        } else {
            String url = Constants.USER_UP_INFO;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("nam", nam);
            params.put("adr", adr);
            params.put("ton", "39.23472837");
            params.put("lat", "116.2374237");
            HttpClientUtils.post(context, url, params, new HttpResultHandler() {
                @Override
                public void onResultFail(String message, int statusCode) {
                    super.onResultFail(message, statusCode);
                }

                @Override
                public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                    super.onResultSuccess(headers, response, message, statusCode);
                    Intent intent = new Intent(context, MainDrawerActivity.class);
                    startActivity(intent);
                    application.removeAllActivity();
                    finish();

                }
            });

        }
    }


}