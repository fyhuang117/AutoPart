package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户资料--修改
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class UserInfoEditActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private String title = "";
    private EditText mEditText;
    private int position = 0;
    private String hint = "请先输入内容";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.user_info_edit);
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.topBar_right_layout:
                getData();
                break;
        }
    }

    private void init() {
        context = this;
        title = getIntent().getStringExtra("title");
        setTitle(title);

        mEditText = (EditText) findViewById(R.id.mEditText);
        String content = getIntent().getStringExtra("content");
        position = getIntent().getIntExtra("position", 0);

        if (position == 0) {
            hint = "请输入店铺名称";
        } else {
            hint = "请输入店铺地址";
        }
        mEditText.setHint(hint);
        mEditText.setText(content);
        mEditText.requestFocus();
        CommonData.setEditCursor(mEditText);
        // 监听文字框
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setRightView(getString(R.string.confirm_ok), -1);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getData() {
        final String nam = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(nam)) {
            Utils.showToastShort(context, "请先输入内容");
        }else {
            showProgressDialog();
            String url = Constants.USER_UP_ALL_INFO;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("nam", "");
            params.put("adr", "");
            if (position == 0) {
                params.put("nam", nam);
            }
            if (position == 1) {
                params.put("adr", nam);
            }
            params.put("ton", "");
            params.put("lat", "");
            params.put("lp1", "");
            params.put("lp2", "");
            params.put("pic", "");
            params.put("license", "");
            params.put("agent", "");
            HttpClientUtils.post(context, url, params, new HttpResultHandler() {
                @Override
                public void onResultFail(String message, int statusCode) {
                    super.onResultFail(message, statusCode);
                }

                @Override
                public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                    super.onResultSuccess(headers, response, message, statusCode);
                    closeInputMethod(mEditText);
                    Intent intent = new Intent();
                    intent.putExtra("content", nam);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                @Override
                public void onFinish() {
                    super.onFinish();
                    disProgressDialog();
                }
            });
        }
    }

//    "id":"用户标识",
//            "nam":"用户名称",
//            "adr":"地址信息",
//            "ton":"经度"，
//            "lat":"纬度",
//            "lp1":"身份证正面16进制的图片数据",
//            "lp2":"身份证背面16进制的图片数据",
//            "pic":"其他图片16进制的图片数据",
//            "license":"营业执照图片16进制的图片数据",
//            "agent":"代理图片16进制的图片数据"

}
