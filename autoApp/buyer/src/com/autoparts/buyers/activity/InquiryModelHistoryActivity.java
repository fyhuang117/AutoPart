package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.CarHistoryListAdapter;
import com.autoparts.buyers.adapter.CommonListAdapter;
import com.autoparts.buyers.model.InquireCarModel;
import com.autoparts.buyers.utils.CommonData;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 车型--历史记录
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquiryModelHistoryActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private CarHistoryListAdapter adapter;
    private ListView mListView;
    String title = "";
    private List<InquireCarModel> mLists;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);
        initNullDataView();
        init();

    }

    private void init() {
        context = this;
        title = getString(R.string.inquiry_text_history);
        setTitle(title);
        mLists = new ArrayList<InquireCarModel>();
        mListView = (ListView) findViewById(R.id.mListView);
        adapter = new CarHistoryListAdapter(context, mLists);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        setData();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String name = mLists.get(i).getName();
        String id = mLists.get(i).getId();

        String year_name = mLists.get(i).getYear_name();
        String year_id = mLists.get(i).getYear_id();

        Intent intent = new Intent();
        intent.putExtra(CommonData.INQUIRE_NAME, name);
        intent.putExtra(CommonData.INQUIRE_ID, id);
        intent.putExtra(CommonData.INQUIRE_YEAR_NAME, year_name);
        intent.putExtra(CommonData.INQUIRE_YEAR_ID, year_id);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void setData() {
        // 查找
        DbUtils db = DbUtils.create(context);
        try {
            mLists = db.findAll(InquireCarModel.class);//通过类型查找
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (mLists != null && mLists.size() > 0) {
            Collections.reverse(mLists);
            adapter.setData(mLists);
        } else {
            mListView.setVisibility(View.GONE);
            setDataNull(true);
        }
    }
}
