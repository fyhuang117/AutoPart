package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.CommonListAdapter;

/**
 * 配件品牌--详细列表
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquiryPartsBrandActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private CommonListAdapter adapter;
    private ListView mListView;
    String title = "";
    String[] strings = {"国产", "美国进口","日本", "德国","上海","天津"};

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        setTitle(title);
        init();

    }

    private void init() {
        context = this;
        mListView = (ListView) findViewById(R.id.mListView);
        adapter = new CommonListAdapter(context, strings);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.putExtra("sort",strings[i]);
        setResult(RESULT_OK,intent);
        finish();
    }
}
