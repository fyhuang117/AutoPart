package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.CommonListAdapter;
import com.autoparts.buyers.utils.CommonData;

/**
 * 汽保耗材配件分类--详细列表
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquiryConsumableListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private CommonListAdapter adapter;
    private ListView mListView;
    String title = "";
    String[] strings = {"前保险杠总成", "后保险杠总成",
            "前保险杠杠皮", "后保险杠杠皮",
            "前保险杠骨架", "后保险杠骨架",
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);
        context = this;
        title = getIntent().getStringExtra("title");
        int position = getIntent().getIntExtra("position",0);
        strings = CommonData.ss[position].split(",");
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