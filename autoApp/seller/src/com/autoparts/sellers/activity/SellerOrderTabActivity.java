package com.autoparts.sellers.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.autoparts.sellers.R;
import com.autoparts.sellers.application.MyApplication;
import com.autoparts.sellers.utils.PopupWindowUtil;

/**
 * 我的订单tab
 * Created by:Liuhuacheng
 * Created time:14-9-6
 */
public class SellerOrderTabActivity extends TabActivity implements View.OnClickListener {

    private Context context;
    private static final String[] mTextviewArray = new String[]{"已完成","待付款","待发货", "待收货","待评价"};

    public LinearLayout topBar_right_layout, topBar_left_layout;
    public TextView topBar_title, topBar_left_text, topBar_right_text;

    private PopupWindowUtil popupWindowUtil;
    private TabHost tabHost;

    private String[][] states = {
            {"2"},
            {"0"},
            {"1"},
            {"4"},
            {"5"},

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_order);
        context = this;
        MyApplication application = (MyApplication) this.getApplication();
        application.addActivity(this);
        initView();
    }

    private void initView() {
        topBar_right_text = (TextView) findViewById(R.id.topBar_right_text);
        topBar_title = (TextView) findViewById(R.id.topBar_title);
        topBar_title.setText(R.string.my_order_text);
        int padding = (int) getResources().getDimension(R.dimen.common_padding_middle);
        topBar_right_text.setPadding(padding, 0, padding, 0);
        topBar_left_layout = (LinearLayout) findViewById(R.id.topBar_left_layout);
        topBar_right_layout = (LinearLayout) findViewById(R.id.topBar_right_layout);
        topBar_left_layout.setOnClickListener(this);
        topBar_right_layout.setOnClickListener(this);

        tabHost = getTabHost();
        for (int i = 0; i < mTextviewArray.length; i++) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i)).setContent(getTabItemIntent(i));
            tabHost.addTab(tabSpec);
        }
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(mTextviewArray[0])) {
                    topBar_right_text.setVisibility(View.VISIBLE);
                } else {
                    topBar_right_text.setVisibility(View.GONE);
                }
            }
        });
        popupWindowUtil = new PopupWindowUtil();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topBar_left_layout:
                finish();
                break;
            default:
                break;
        }
    }

    private View getTabItemView(int index) {
        //自定义Tab布局
        LinearLayout mlayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.my_order_tab_item, null);
        TextView title = (TextView) mlayout.findViewById(R.id.complain_item_title);
        title.setText(mTextviewArray[index]);
        return mlayout;
    }

    private Intent getTabItemIntent(int index) {
        Intent intent = new Intent(this, SellerOrderItemActivity.class);
        intent.putExtra("position", index);
        intent.putExtra("states", states[index]);
        return intent;
    }

}
