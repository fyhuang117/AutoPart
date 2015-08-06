package com.autoparts.buyers.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.application.MyApplication;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.PopupWindowUtil;
import com.autoparts.buyers.utils.Utils;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的订单tab
 * Created by:Liuhuacheng
 * Created time:14-9-6
 */
public class MyOrderActivity extends TabActivity implements View.OnClickListener {

    private Context context;
    private static final String[] mTextviewArray = new String[]{"进行中", "待评价", "已完成"};

    public LinearLayout topBar_right_layout, topBar_left_layout;
    public TextView topBar_title, topBar_left_text, topBar_right_text;

    private PopupWindowUtil popupWindowUtil;
    private TabHost tabHost;
    private TextView tab0;
    private String[][] states = {
            {"0", "1", "4"},
            {"5"},
            {"2"}

    };
    private String[][] states_fliter = {
            {"0", "1", "4"},
            {"0"},
            {"1"},
            {"4"}
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order);
        context = this;
        MyApplication application = (MyApplication) this.getApplication();
        application.addActivity(this);
        initView();
//        getData();
    }

    private void initView() {
        topBar_right_text = (TextView) findViewById(R.id.topBar_right_text);
        topBar_title = (TextView) findViewById(R.id.topBar_title);
        topBar_title.setText(R.string.my_order_text);
//        topBar_right_text.setText("筛选");
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

//        tabHost.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                return true;
//            }
//        });

        getTabWidget().getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tabHost.setCurrentTab(0);
                int curr = tabHost.getCurrentTab();
                if (curr == 0) {
                    show(CommonData.orderFilter, getTabWidget().getChildAt(0));
                } else {
                    tabHost.setCurrentTab(0);
                }
            }
        });

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
        ImageView tip_down = (ImageView) mlayout.findViewById(R.id.tip_down);
        if (index == 0) {
            tab0 = title;
            tip_down.setVisibility(View.VISIBLE);
        }
        return mlayout;
    }

    private Intent getTabItemIntent(int index) {
        Intent intent = new Intent(this, MyOrderItemActivity.class);
        intent.putExtra("position", index);
        intent.putExtra("states", states[index]);
        return intent;
    }

    public void show(final String[] content, final View inquiry_order_btn) {
        popupWindowUtil.position = 1;
        popupWindowUtil.show(context, inquiry_order_btn, content, new PopupWindowUtil.CallBackName() {
            @Override
            public void name(int position) {
                String name = CommonData.orderFilter[position];
                tab0.setText(name);
                Intent mIntent = new Intent(CommonData.ACTION_NAME);
                mIntent.putExtra("states", states_fliter[position]);
                mIntent.putExtra("position", position);
                //发送广播
                sendBroadcast(mIntent);

            }
        });
    }
//
//    //获取全部订单
//    public void getData() {
//        String url = Constants.ORDER_GET;
//        Map<String, Object> params = new HashMap<String, Object>();
//        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
//            @Override
//            public void onResultFail(String message, int statusCode) {
//                super.onResultFail(message, statusCode);
//            }
//
//            @Override
//            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
//                super.onResultSuccess(headers, response, message, statusCode);
//                setData(response);
//            }
//
//            @Override
//            public void onResultJson(String json) {
//                super.onResultJson(json);
//            }
//        });
//
//    }
//
//    public void setData(ResponseModel responseModel) {
//
//    }


}
