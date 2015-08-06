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
 * 在线咨询
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class ChatActivity extends BaseActivity implements AdapterView.OnItemClickListener {
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
        init();

    }

    private void init() {
        context = this;
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.topBar_right_layout:
                Intent intent = new Intent(context, SellerOrderDetailActivity.class);
                intent.putExtra("title", getString(R.string.order_text_write));
                startActivity(intent);
                break;


        }
    }

}
