package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.GridViewAdapter;
import com.autoparts.buyers.model.CommonLetterModel;
import com.autoparts.buyers.model.ContactUtils;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import org.apache.http.Header;

import java.util.*;

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
    private GridView gridView;
    private RelativeLayout sort_view;
    private String[] item_titles;
    private GridViewAdapter adapter;
    private Vector<HashMap<String, Object>> mList;

    private int position = 0;//position ==0易损件 == 1询价汽保耗材
    private String[] urls = {Constants.INQUIRE_PART_V_LIST, Constants.INQUIRE_PART_C_LIST};
    private int state = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inquiry_sort_gridview);
        super.onCreate(savedInstanceState);
        context = this;
        init();
        initNullDataView();
    }

    private void init() {
        String title = getIntent().getStringExtra("title");
        setTitle(title);
        position = getIntent().getIntExtra("position", 0);
        state = getIntent().getIntExtra("state", 0);
        sort_view = (RelativeLayout) findViewById(R.id.sort_view);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);

        mList = new Vector<HashMap<String, Object>>();
//        if (title.contains("汽保")) {
//            mLists = CommonData.getSort2();
//        }
        mList = new Vector<HashMap<String, Object>>();
        adapter = new GridViewAdapter(context, mList);
        adapter.setPosition(position);
        gridView.setAdapter(adapter);

        getData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        Intent intent = new Intent(context, InquirySortListActivity.class);
        InquirySortListActivity.mList_data = null;

        intent.putExtra("title", (String) mList.get(i).get("nam"));
        String parttypeid = (String) mList.get(i).get("parttypeid");
        intent.putExtra(CommonData.INQUIRE_ID, parttypeid);
        intent.putExtra(CommonData.INQUIRE_URL, urls[position]);
        startActivityForResult(intent, CommonData.REQUEST_CODE_SORT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CommonData.REQUEST_CODE_SORT:
                    setResult(RESULT_OK, data);
                    finish();
                    break;
            }
        }
    }

    public void getData() {
        String url = urls[position];
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("par", "0");
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                setData(response);
            }

            @Override
            public void onResultJson(String json) {
                super.onResultJson(json);
            }
        });

    }

    public void setData(ResponseModel responseModel) {
//        {
//            "parttypeid": 10,
//                "nam": "制动系",
//                "pic": "http://123.56.87.239:81/auto/resource/parttype/1434795365.png",
//                "is_select_car": 0,
//                "is_select_quality": 0
//        }
        mList = responseModel.getMaps();
        if (mList != null && mList.size() > 0) {
            adapter.setData(mList);
            adapter.notifyDataSetChanged();
            sort_view.setVisibility(View.VISIBLE);
            setDataNull(false);
        } else {
            setDataNull(true);
            sort_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topBar_left_layout:
                onFinish();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void onFinish() {
        if (state == 0) {
            Intent intent = new Intent();
            intent.putExtra("finish", true);
            setResult(RESULT_OK, intent);
        }
        finish();
    }


}