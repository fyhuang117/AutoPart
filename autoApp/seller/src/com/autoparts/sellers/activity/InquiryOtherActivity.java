package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.utils.CommonData;

/**
 * 询价-配件-其它要求
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquiryOtherActivity extends BaseActivity {
    private Context context;
    private static final int requestCode_year = 0;
    private TextView brand, location;
    private TextView inquiry_add, inquiry_minus;
    private EditText inquiry_num;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inquiry_other);
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.my_inquiry_other));
        setLeftView("", R.drawable.icon_back);
        init();
    }

    private void init() {
        context = this;
        inquiry_add = (TextView) findViewById(R.id.inquiry_add);
        inquiry_num = (EditText) findViewById(R.id.inquiry_num);
        inquiry_minus = (TextView) findViewById(R.id.inquiry_minus);
        inquiry_add.setOnClickListener(this);
        inquiry_minus.setOnClickListener(this);

        brand = (TextView) findViewById(R.id.brand);
        location = (TextView) findViewById(R.id.location);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.inquiry_minus:
                setNum(0);
                break;
            case R.id.inquiry_add:
                setNum(1);
                break;
        }
    }

    //配件品牌
    public void category(View view) {
        Intent intent = new Intent(context, SelectModeActivity.class);
        intent.putExtra("title","配件品牌");
        intent.putExtra("position",0);
        startActivityForResult(intent, CommonData.REQUEST_INQUIRY_BRAND);
    }

    //商家位置
    public void location(View view) {
        Intent intent = new Intent(context, CommonListActivity.class);
        intent.putExtra("title","商家位置");
        intent.putExtra("position",1);
        startActivityForResult(intent,  CommonData.REQUEST_INQUIRY_LOCATION);

    }
    //询价提交
    public void commit(View view) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("brand",brand.getText().toString().trim());
        bundle.putString("num",inquiry_num.getText().toString().trim());
        bundle.putString("location",location.getText().toString().trim());
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//听力测试结束
            String str = data.getStringExtra("data");

            switch (requestCode) {
                case CommonData.REQUEST_INQUIRY_BRAND:
                    brand.setText(str);
                    break;
                case CommonData.REQUEST_INQUIRY_LOCATION:
                    location.setText(str);
                    break;
            }
        }
    }

    public void setNum(int state) {
        int num = Integer.parseInt(inquiry_num.getText().toString());
        if (state == 0) {//减去
            if (num == 1) {
                num = 1;
            } else {
                num--;
            }
        } else if (state == 1) {//加上
            num++;
        }
        inquiry_num.setText(num + "");
    }


}
