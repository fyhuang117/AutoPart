package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.SellerBiddingAdapter;

/**
 * 询价中-列表
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MyInquiryActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private SellerBiddingAdapter adapter;
    private ListView mListView;
    String title = "";
    String[] strings = {"保险杠", "前保险杠骨架"};

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);
        title = getString(R.string.my_inquiry_text);
        setTitle(title);
        init();

    }

    private void init() {
        context = this;
        mListView = (ListView) findViewById(R.id.mListView);
//        adapter = new SellerBiddingAdapter(context, strings,null);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
        int divider = (int) getResources().getDimension(R.dimen.common_padding_middle);
        mListView.setDividerHeight(divider);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        setResult(RESULT_OK);
//        finish();
        Intent intent = new Intent(context, MyInquiryListActivity.class);
        intent.putExtra("title",strings[i]);
        startActivity(intent);

    }
}
