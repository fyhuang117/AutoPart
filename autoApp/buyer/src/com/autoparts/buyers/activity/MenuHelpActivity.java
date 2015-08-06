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
 * 帮助
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MenuHelpActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private CommonListAdapter adapter;
    private ListView mListView;
    String title = "";
    String[] strings = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        setTitle(title);
        setRightView("下单", -1);
        init();

    }

    private void init() {
        context = this;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topBar_right_layout:
                Intent intent = new Intent(context, OrderWriteActivity.class);
                intent.putExtra("title", getString(R.string.order_text_write));
                startActivity(intent);
                break;


        }
    }

}
