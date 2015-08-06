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
 * 询价-配件
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class CommonListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private CommonListAdapter adapter;
    private ListView mListView;
    String title = "";
    String[] strings = null;
    String[] strings1 = {"国产", "美国进口","日本", "德国","上海","天津"};
    String[] strings2 = {"不限", "北京","天津", "上海","广州","1-10KM"};
    String[] strings3 = {"原厂","副厂"};

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
        int position = getIntent().getIntExtra("position",0);
        if (position == 0){
            strings  = strings1;
        }else if (position == 1){
            strings  = strings2;
        }else if (position == 2){
            strings  = strings3;
        }
        mListView = (ListView) findViewById(R.id.mListView);
        adapter = new CommonListAdapter(context, strings);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0){//原厂
            onFinish("1",strings[i]);
        }else {
            onFinish("0",strings[i]);
        }
    }

    public void onFinish(String id, String name) {
        Intent intent = new Intent();
        intent.putExtra(CommonData.INQUIRE_NAME, name);
        intent.putExtra(CommonData.INQUIRE_ID, id);
        setResult(RESULT_OK, intent);
        finish();
    }
}
