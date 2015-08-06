package com.autoparts.buyers.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.MyMessageAdapter;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import com.autoparts.buyers.view.viewpagerindicator.TabPageMessageIndicator;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 我的消息
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MenuUserMessageActivity extends TabActivity implements View.OnClickListener {

    private Context context;
    private static final String[] mTextviewArray = new String[]{"系统消息", "商家消息"};

    public LinearLayout topBar_right_layout, topBar_left_layout;
    public TextView topBar_title, topBar_left_text, topBar_right_text;
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_message_tab);
        context = this;
        initView();
    }

    private void initView() {
        topBar_right_text = (TextView) findViewById(R.id.topBar_right_text);
        topBar_title = (TextView) findViewById(R.id.topBar_title);
        topBar_title.setText(R.string.message_title);
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
        Intent intent = new Intent(this, UserMessageActivity.class);
        intent.putExtra("position", index);
        return intent;
    }

}
