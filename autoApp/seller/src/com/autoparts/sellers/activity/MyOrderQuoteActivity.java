package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.autoparts.sellers.R;

/**
 * 进行中的订单--我的报价详情
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MyOrderQuoteActivity extends BaseActivity {
    private Context context;
    String title = "";

    private LinearLayout inquire_item_view;

    TextView inquiry_title, inquiry_num;
    TextView inquiry_time, inquiry_cancel, inquiry_no;
    TextView inquiry_min_money, inquiry_seller_num;
    View line;
    LinearLayout inquiry_btn_view, inquiry_data_view;
    Button order_del,order_contact,order_pay,order_evaluate;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.my_order_quote_view);
        super.onCreate(savedInstanceState);
        title = getString(R.string.my_order_detail);
        setTitle(title);
        init();
    }

    private void init() {
        context = this;

        inquire_item_view = (LinearLayout) findViewById(R.id.inquire_item_view);
        inquiry_title = (TextView) findViewById(R.id.inquiry_title);
        inquiry_num = (TextView) findViewById(R.id.inquiry_num);
        inquiry_cancel = (TextView) findViewById(R.id.inquiry_cancel);
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

        inquiry_cancel.setOnClickListener(this);
        inquire_item_view.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.inquire_item_view:
                my_order_list();
                break;
            case R.id.inquiry_cancel:
                showDialog("是否取消询价？", true);
                break;
            case R.id.confirm_ok:
                finish();
                break;
        }
    }

    //我的订单报价列表
    public void my_order_list() {
        Intent intent = new Intent(context,MyOrderQuoteListActivity.class);
        intent.putExtra("content","保险杠");
        startActivity(intent);

    }


}
