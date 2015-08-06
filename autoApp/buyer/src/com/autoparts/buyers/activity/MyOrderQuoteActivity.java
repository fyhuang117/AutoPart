package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.GridAdapter;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Utils;
import com.autoparts.buyers.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 进行中的订单--我的报价详情
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MyOrderQuoteActivity extends BaseActivity {
    private Context context;
    String title = "";

    private LinearLayout inquire_item_view;

    TextView inquiry_title, inquiry_num, inquiry_name;
    TextView inquiry_time, inquiry_state, inquiry_no;
    TextView inquiry_min_money, inquiry_seller_num;
    View line;
    LinearLayout inquiry_btn_view, inquiry_data_view;
    Button order_del, order_contact, order_pay, order_evaluate;
    private HashMap<String, Object> maps;
    private MyGridView mGridView;
    private GridAdapter mGridadapter;
    private List<String> mGridPhotos;

    public MyOrderQuoteActivity() {
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.my_order_quote_view);
        super.onCreate(savedInstanceState);
        title = "询价单";
        setTitle(title);
        init();
    }

    private void init() {
        context = this;

        inquire_item_view = (LinearLayout) findViewById(R.id.inquire_item_view);
        inquiry_title = (TextView) findViewById(R.id.inquiry_title);
        inquiry_num = (TextView) findViewById(R.id.inquiry_num);
        inquiry_name = (TextView) findViewById(R.id.inquiry_name);
        inquiry_state = (TextView) findViewById(R.id.inquiry_state);
        inquiry_time = (TextView) findViewById(R.id.inquiry_time);
        line = findViewById(R.id.line);
        inquiry_min_money = (TextView) findViewById(R.id.inquiry_min_money);
        inquiry_seller_num = (TextView) findViewById(R.id.inquiry_seller_num);
        inquiry_no = (TextView) findViewById(R.id.inquiry_no);
        inquiry_btn_view = (LinearLayout) findViewById(R.id.inquiry_btn_view);
        inquiry_data_view = (LinearLayout) findViewById(R.id.inquiry_data_view);

        order_del = (Button) findViewById(R.id.order_del);
        order_contact = (Button) findViewById(R.id.order_contact);
        order_pay = (Button) findViewById(R.id.order_pay);
        order_evaluate = (Button) findViewById(R.id.order_evaluate);

        inquiry_state.setText("竞价中");
        inquire_item_view.setOnClickListener(this);
        setData();
    }

    public void setData() {
        maps = (HashMap<String, Object>) getIntent().getSerializableExtra(CommonData.MAPS);

        String par = (String) maps.get("par");
        String car = (String) maps.get("car");
        if (TextUtils.isEmpty(par)){
            par = context.getString(R.string.inquiry_parts_null);
        }  if (TextUtils.isEmpty(car)){
            car = context.getString(R.string.inquiry_car_null);
        }
        inquiry_title.setText(par);
        inquiry_name.setText(car);

        String begin_time = (String) maps.get("begin_time");
        String end_time = (String) maps.get("end_time");
        String time = CommonData.getInstance(context).getTime(begin_time, end_time);
        inquiry_time.setText(time);

        int bc = CommonData.getInstance(context).getIntData((String) maps.get("bc"));
        if (bc > 0) {
            inquiry_num.setText(bc + "");
            inquiry_num.setVisibility(View.VISIBLE);
            inquiry_seller_num.setText(String.format(getString(R.string.order_mum_bc), bc));
        } else {
            inquiry_seller_num.setText("暂无报价");
        }

        String min_money = (String) maps.get("min");
        if (!TextUtils.isEmpty(min_money)) {
            inquiry_min_money.setText(String.format(getString(R.string.order_min_money), min_money));
        } else {

        }


        mGridView = (MyGridView) findViewById(R.id.mGridView);
        mGridPhotos = new ArrayList<String>();
        mGridadapter = new GridAdapter(context, mGridPhotos);
        mGridView.setAdapter(mGridadapter);
        String pic1 = (String) maps.get("pic1");
        String pic2 = (String) maps.get("pic2");
        String pic3 = (String) maps.get("pic3");

        if (!TextUtils.isEmpty(pic1)) {
            mGridPhotos.add(pic1);
        }
        if (!TextUtils.isEmpty(pic2)) {
            mGridPhotos.add(pic2);
        }
        if (!TextUtils.isEmpty(pic3)) {
            mGridPhotos.add(pic3);
        }

        mGridadapter.setData(mGridPhotos);
        setItemClick();
    }

    public void setItemClick(){
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonData.getInstance(context).showImage(context,mGridPhotos,position);
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.inquire_item_view:
                my_order_list();
                break;
        }
    }

    //我的订单报价列表
    public void my_order_list() {
        Intent intent = new Intent(context, MyOrderQuoteListActivity.class);
        intent.putExtra(CommonData.MAPS, maps);
        startActivityForResult(intent, CommonData.REQUEST_ORDER_CANCEL);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CommonData.REQUEST_ORDER_CANCEL://订单取消回调
                    setResult(RESULT_OK, data);
                    finish();
                    break;
            }
        }
    }

}
