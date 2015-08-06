package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;

/**
 * 询价-配件-其它要求
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquiryOtherActivity extends BaseActivity {
    private Context context;
    private static final int requestCode_year = 0;
    private TextView brand, location, inquiry_text_quality;
    private TextView inquiry_add, inquiry_minus;
    private EditText inquiry_num;
    private String parttypeid;

    private int is_select_quality;
    private RelativeLayout quality_view;


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
        inquiry_text_quality = (TextView) findViewById(R.id.inquiry_text_quality);
        location = (TextView) findViewById(R.id.location);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            String brandStr = b.getString("brand");
            int num = b.getInt(CommonData.INQUIRE_NUM, 1);
            String locationStr = b.getString("location");
            String quality = b.getString("quality");
            brand.setText(brandStr);
            inquiry_text_quality.setText(quality);
            location.setText(locationStr);
            inquiry_num.setText(num + "");
        }

        parttypeid = getIntent().getStringExtra(CommonData.INQUIRE_ID);
        is_select_quality = getIntent().getIntExtra("quality",1);
        quality_view = (RelativeLayout) findViewById(R.id.quality_view);
        if (is_select_quality == 0){
            quality_view.setVisibility(View.GONE);
        }
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
            case R.id.my_view3:
                break;
            case R.id.my_view4:
                break;
        }
    }

    //配件品牌
    public void category(View view) {
        if (TextUtils.isEmpty(parttypeid)){
            Utils.showToastShort(context,"请先选择配件分类");
        }else if (parttypeid.equals("-1")){
            Utils.showToastShort(context,"该配件暂无相关品牌");
        }else {
            Intent intent = new Intent(context, SelectModeActivity.class);
            intent.putExtra("title", "配件品牌");
            intent.putExtra(CommonData.INQUIRE_ID, parttypeid);//"parttypeid":"配件类型标识(int)"
            startActivityForResult(intent, CommonData.REQUEST_INQUIRY_BRAND);
        }
    }

    //配件品质
    public void quality(View view) {
        Intent intent = new Intent(context, CommonListActivity.class);
        intent.putExtra("title", "品质");
        intent.putExtra("position", 2);
        startActivityForResult(intent, CommonData.REQUEST_INQUIRY_QUALITY);
    }

    //商家位置
    public void location(View view) {
        InquiryCircleActivity.mList_data = null;
        Intent intent = new Intent(context, InquiryCircleActivity.class);
        intent.putExtra("title", "商家位置");
        startActivityForResult(intent, CommonData.REQUEST_INQUIRY_LOCATION);

    }

    //询价提交
    public void commit(View view) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        int num = Integer.parseInt(inquiry_num.getText().toString().trim());
        bundle.putInt(CommonData.INQUIRE_NUM, num);

        bundle.putString("brand", brand.getText().toString().trim());
        bundle.putString(CommonData.INQUIRE_ID, partbandid);

        bundle.putString("areaid", areaid);
        bundle.putString("location", location.getText().toString().trim());

        bundle.putString("qualityid", parttype_quality);
        bundle.putString("quality", inquiry_text_quality.getText().toString().trim());
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private String partbandid = "";//品牌id
    private String areaid = "";//id
    private String parttype_quality = "";//id

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//听力测试结束
            String str = data.getStringExtra("data");
            switch (requestCode) {
                case CommonData.REQUEST_INQUIRY_BRAND:
                    partbandid = data.getStringExtra(CommonData.INQUIRE_ID);
                    String partband_name = data.getStringExtra(CommonData.INQUIRE_NAME);
                    brand.setText(partband_name);//品牌
                    break;
                case CommonData.REQUEST_INQUIRY_QUALITY:
                    parttype_quality = data.getStringExtra(CommonData.INQUIRE_ID);
                    String quality_name = data.getStringExtra(CommonData.INQUIRE_NAME);
                    inquiry_text_quality.setText(quality_name);//商圈

                    break;
                case CommonData.REQUEST_INQUIRY_LOCATION:
                    areaid = data.getStringExtra(CommonData.INQUIRE_ID);
                    String area_name = data.getStringExtra(CommonData.INQUIRE_NAME);
                    location.setText(area_name);//商圈


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
