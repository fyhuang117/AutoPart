package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.InquirySortlistAdapter;
import com.autoparts.sellers.model.CommonLetterModel;
import com.autoparts.sellers.model.ContactUtils;
import com.autoparts.sellers.utils.CommonData;

import java.util.ArrayList;
import java.util.List;

/**
 * 配件分类 -item
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquirySortItemActivity extends BaseActivity {
    private Context context;

    private List<CommonLetterModel> mList;
    private ContactUtils contactUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inquiry_sort_item);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        context = this;
        String title = getIntent().getStringExtra("title");
        setTitle(title);

        contactUtils = new ContactUtils();
        mList = new ArrayList<CommonLetterModel>();
        ListView listView = (ListView) findViewById(android.R.id.list);
        mList = CommonData.getSortList();
        mList = contactUtils.getListKey(mList);
        InquirySortlistAdapter listAdapter = new InquirySortlistAdapter(context,mList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, InquirySortListActivity.class);
                intent.putExtra("title",mList.get(i).getUser_name());
                startActivityForResult(intent,CommonData.REQUEST_CODE_SORT);
            }
        });

    }
    //优化ViewPager ListView滑动卡屏问题 使用Fragment

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
