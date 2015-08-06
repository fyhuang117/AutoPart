package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.MyOrderQuoteAdapter;
import com.autoparts.sellers.utils.PopupWindowUtil;

/**
 * 进行中的订单--商家报价列表
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MyOrderQuoteListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private MyOrderQuoteAdapter adapter;
    private ListView mListView;
    String title = "";
    String[] strings = {"远大汽车配件销售有限公司", "北京华德宝销售有限公司"};
    private Button inquiry_order_btn, inquiry_seller_btn;
    private PopupWindowUtil popupWindowUtil;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.my_order_quote_list_view);
        super.onCreate(savedInstanceState);
        title = getString(R.string.inquiry_text_list);
        setTitle(title);
        init();
    }

    private void init() {
        context = this;
        popupWindowUtil = new PopupWindowUtil();
        mListView = (ListView) findViewById(R.id.mListView);
        adapter = new MyOrderQuoteAdapter(context, strings);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        inquiry_order_btn = (Button) findViewById(R.id.inquiry_order_btn);
        inquiry_seller_btn = (Button) findViewById(R.id.inquiry_seller_btn);
        inquiry_order_btn.setOnClickListener(this);
        inquiry_seller_btn.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context,SellerBiddingDetailActivity.class);
        intent.putExtra("company",strings[i]);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.inquiry_order_btn:
                String[] content = {"价格由高到低","价格由低到高","距离由远及近","距离由近及远"};
                show(content,inquiry_order_btn);
                break;
            case R.id.inquiry_seller_btn:
                String[] content1 = {"价格由高到低","价格由低到高","距离由远及近","距离由近及远"};
                show(content1,inquiry_seller_btn);
                break;
        }
    }

    //我的订单详情
    public void my_order_detail(View view) {
        Intent intent = new Intent(context,SellerInquiryDetailActivity.class);
        intent.putExtra("content","保险杠");
        startActivity(intent);

    }

    public void show(final String[] content,final Button inquiry_order_btn) {
        popupWindowUtil.show(context, inquiry_order_btn, content, new PopupWindowUtil.CallBackName() {
            @Override
            public void name(int position) {
                inquiry_order_btn.setText(content[position]);
            }
        });
    }

}
