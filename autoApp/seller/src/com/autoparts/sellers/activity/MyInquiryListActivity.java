package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.MyInquiryListAdapter;

/**
 * 询价-详细列表
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MyInquiryListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private MyInquiryListAdapter adapter;
    private ListView mListView;
    String title = "";
    String[] strings = {"远大汽车配件销售有限公司", "北京华德宝销售有限公司"};
    private LinearLayout inquiry_order_view, inquiry_seller_view;
    private Button inquiry_order_btn, inquiry_seller_btn;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.my_inquiryt_view);
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        setTitle(title);
        setRightView(getString(R.string.inquiry_text_cancel), -1);
        init();

    }

    private void init() {
        context = this;
        mListView = (ListView) findViewById(R.id.mListView);
        adapter = new MyInquiryListAdapter(context, strings);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
        int divider = (int) getResources().getDimension(R.dimen.common_padding_middle);
        mListView.setDividerHeight(divider);
        mListView.setOnItemClickListener(this);
        inquiry_order_view = (LinearLayout) findViewById(R.id.inquiry_order_view);
        inquiry_seller_view = (LinearLayout) findViewById(R.id.inquiry_seller_view);
        inquiry_order_btn = (Button) findViewById(R.id.inquiry_order_btn);
        inquiry_seller_btn = (Button) findViewById(R.id.inquiry_seller_btn);
        inquiry_order_btn.setOnClickListener(this);
        inquiry_seller_btn.setOnClickListener(this);
        setVisible(1);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        setResult(RESULT_OK);
//        finish();
        Intent intent = new Intent(context,SellerBiddingDetailActivity.class);
        intent.putExtra("title",strings[i]);
        startActivity(intent);


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.inquiry_order_btn:
                setVisible(0);
                break;
            case R.id.inquiry_seller_btn:
                setVisible(1);
                break;
        }
    }

    public void setVisible(int state) {
        if (state == 0) {
            inquiry_order_view.setVisibility(View.VISIBLE);
            inquiry_seller_view.setVisibility(View.GONE);
            inquiry_order_btn.setBackgroundColor(getResources().getColor(R.color.text_orange));
            inquiry_seller_btn.setBackgroundResource(R.drawable.btn_bg_grey_selector);
        } else if (state == 1) {
            inquiry_order_view.setVisibility(View.GONE);
            inquiry_seller_view.setVisibility(View.VISIBLE);
            inquiry_seller_btn.setBackgroundColor(getResources().getColor(R.color.text_orange));
            inquiry_order_btn.setBackgroundResource(R.drawable.btn_bg_grey_selector);
        }

    }
}
