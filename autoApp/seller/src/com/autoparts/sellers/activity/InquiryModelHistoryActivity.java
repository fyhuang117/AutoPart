package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.CommonListAdapter;

/**
 * 车型--历史记录
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquiryModelHistoryActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private CommonListAdapter adapter;
    private ListView mListView;
    String title = "";
    String[] strings = {"宝马3系 2005款 1.6L 316i 运动设计套装",
            "宝马3系 2005款 1.6L 316i 手动款",
    "宝马3系 2005款 1.6L 3020Li 超悦豪华款"};

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);
        title = getString(R.string.inquiry_text_history);
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
        intent.putExtra("data",strings[i]);
        setResult(RESULT_OK,intent);
        finish();

    }
}
