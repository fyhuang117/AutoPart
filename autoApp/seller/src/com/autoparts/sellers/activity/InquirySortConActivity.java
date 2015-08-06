package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.GridViewAdapter;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.view.MyGridView;

import java.util.List;

/**
 * 易损件分类
 * Created by:Liuhuacheng
 * Created time:14-11-7
 */
public class InquirySortConActivity extends BaseActivity {
    /**
     * Called when the activity is first created.
     */
    private static final String TAG = "PracticalInfoActivity";
    private Context context;
    private List<String> mLists;
    private MyGridView gridView;
    private String[] item_titles;
    private GridViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inquiry_sort_gridview);
        super.onCreate(savedInstanceState);
        context = this;
        init();
    }

    private void init() {
        String title = getIntent().getStringExtra("title");
        setTitle(title);
        gridView = (MyGridView) findViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);

        mLists = CommonData.getSort();
//        if (title.contains("汽保")) {
//            mLists = CommonData.getSort2();
//        }

        adapter = new GridViewAdapter(context, mLists);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(context, InquirySortListActivity.class);
        intent.putExtra("title",mLists.get(position));
        intent.putExtra("position",position);
        startActivityForResult(intent, CommonData.REQUEST_CODE_SORT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK,data);
            finish();
            switch (requestCode) {
                case CommonData.REQUEST_CODE_SORT:
                    setResult(RESULT_OK,data);
                    finish();
                    break;
            }
        }
    }

}