package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.CommonListAdapter;
import com.autoparts.buyers.adapter.MyOrderListAdapter;
import com.autoparts.buyers.utils.Utils;

/**
 * 我的订单
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class OrderHistoryActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private ListView mListView;
    private MyOrderListAdapter adapter;
    String title = "";
    String[] strings = {"前保险杠总成", "后保险杠总成"};

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.my_order_view);
        super.onCreate(savedInstanceState);
        title = getString(R.string.my_order_text);
        setTitle(title);
        init();
    }

    private void init() {
        context = this;
        mListView = (ListView) findViewById(R.id.mListView);
        adapter = new MyOrderListAdapter(context, strings);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

    }

    public void order(View view) {
        Intent intent = new Intent(context, PayAlipayActivity.class);
        startActivity(intent);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Utils.showToastShort(context,"111");
        Intent intent = new Intent(context, MyOrderDetailActivity.class);
        startActivity(intent);
    }
}
