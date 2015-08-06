package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.network.*;
import com.autoparts.sellers.utils.Constants;
import com.autoparts.sellers.utils.Utils;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 用户登录
 * Created by:Liuhuacheng
 * Created time:15-1-29
 */
public class UserLoginActivity extends BaseActivity {
    private Context context;
    private TextView login_user_deal;

    private Button login_btn, login_code_btn;
    private EditText login_phone, login_code;

    private Timer timer;
    private int time = 60;
    private int recLen = time;
    private boolean isLogin = false;//是否是重新登录的表示，如果true，则登录成功后，finish

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.user_login);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setTitle(getString(R.string.login_commit));
        context = this;
        isLogin = getIntent().getBooleanExtra("isLogin", false);
        login_user_deal = (TextView) findViewById(R.id.login_user_deal);
        login_user_deal.setOnClickListener(this);

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        login_code_btn = (Button) findViewById(R.id.login_code_btn);
        login_code_btn.setOnClickListener(this);

        login_phone = (EditText) findViewById(R.id.login_phone);
        login_code = (EditText) findViewById(R.id.login_code_phone);

        String phone = preferences.getLoginPhone();
        if (!TextUtils.isEmpty(phone)){
            login_phone.setText(phone);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.login_user_deal:
                userAgreement();
                break;
            case R.id.login_btn:
//                login();
                login();
                break;
            case R.id.login_code_btn://获取手机验证码
                setTimer();
                getMessageCode();
                break;
        }
    }

    public void login() {
        final String phone = login_phone.getText().toString().trim();
        String code = login_code.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Utils.showToastShort(context, getString(R.string.login_null_phone));
        } else if (phone.length() != 11) {
            Utils.showToastShort(context, getString(R.string.login_error_phone));
        } else if (TextUtils.isEmpty(code)) {
            Utils.showToastShort(context, getString(R.string.login_null_code));
        } else {
            showProgressDialog();
            String url = Constants.USER_LOGIN;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("tel",phone);
            params.put("sec",code);
            params.put("type", Constants.type);
            HttpClientUtils.post(context, url, params, new HttpResultHandler() {
                @Override
                public void onResultFail(String message, int statusCode) {
                    super.onResultFail(message, statusCode);
                    if (statusCode == 401){
                        Utils.showToastShort(context,"手机号或验证码错误");
                    }
                }
                @Override
                public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                    super.onResultSuccess(headers, response, message, statusCode);
                    preferences.setLoginPhone(phone);
                    setData(response,statusCode);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    disProgressDialog();
                }
            });
        }
    }

    public void setData(ResponseModel response,int statusCode) {
        //解析的数据都是map集合里面，直接根据返回json的key去取值
        preferences.setIsLogin(true);
        Map<String, String> map = response.getMap();
        String id = map.get("id");
        String need = map.get("need");

        preferences.setUserID(id);
        if (isLogin) {
            finish();
        } else {
            if (need.equals("0")) {
                Intent intent = new Intent(context, MainDrawerActivity.class);
                startActivity(intent);
                application.removeAllActivity();
                finish();

            }else {
                Intent intent = new Intent(context, UserLoginInfoActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    //获取短信验证码
    public void getMessageCode() {
        String phone = login_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Utils.showToastShort(context, getString(R.string.login_null_phone));
        } else if (phone.length() != 11) {
            Utils.showToastShort(context, getString(R.string.login_error_phone));
        } else {
            String url = Constants.USER_CODE;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("tel",phone);
            params.put("type", Constants.type);
            HttpClientUtils.post(context, url, params, new HttpResultHandler() {
                @Override
                public void onResultFail(String message, int statusCode) {
                    super.onResultFail(message, statusCode);
                }
                @Override
                public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                    super.onResultSuccess(headers, response, message, statusCode);
                    Utils.showToastShort(context,"验证码已发送到您的手机上，请注意查收");

                }
            });

        }
    }

    public void userAgreement() {
        Intent intent = new Intent(context, UserAgreementActivity.class);
        startActivity(intent);
    }

    //设置定时器
    public void setTimer() {
        login_code_btn.setEnabled(false);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {      // UI thread
                    @Override
                    public void run() {
                        recLen--;
                        String str = getString(R.string.login_code_timer);
                        login_code_btn.setText(String.format(str, recLen));
                        if (recLen <= 0) {
                            setButtonable();
                        }
                    }
                });
            }
        }, 10, 1000);
    }

    public void setButtonable() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
            recLen = time;
            login_code_btn.setText(getString(R.string.login_get_code));
            login_code_btn.setEnabled(true);
        }
    }


}