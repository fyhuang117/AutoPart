package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.YearListAdapter;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.utils.Constants;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 年款选择
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquiryYearActivity extends BaseActivity{
    private Context context;
    String title = "";
    private String lineid;

    private ExpandableListView expandListView;
    private YearListAdapter adapter;
    private Vector<HashMap<String, Object>> mLists;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_expandable_listview);
        super.onCreate(savedInstanceState);
        title = getString(R.string.inquiry_text_year);
        setTitle(title);
        init();

    }

    private void init() {
        context = this;
        initNullDataView();
        mLists = new Vector<HashMap<String, Object>>();
        adapter = new YearListAdapter(context, mLists);
        initView();
        lineid = getIntent().getStringExtra(CommonData.INQUIRE_ID);
        getData();
//        setData();

    }

    public void initView() {
        expandListView = (ExpandableListView) findViewById(R.id.expandListView);
        setViewData();

    }

    public void setViewData(){
        expandListView.setAdapter(adapter);
        int groupCount = expandListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            expandListView.expandGroup(i);
        };
        expandListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        expandListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, final int i, int i1, long l) {
                Intent intent = new Intent();
                Vector<HashMap<String, Object>> maps = (Vector<HashMap<String, Object>>) mLists.get(i).get("car");
                String name = (String) maps.get(i1).get("nam");
                String year = (String) mLists.get(i).get("year");
                String id = (String) maps.get(i1).get("carid");
                intent.putExtra(CommonData.INQUIRE_NAME,year +" "+name);
                intent.putExtra(CommonData.INQUIRE_ID,id);
                setResult(RESULT_OK,intent);
                finish();
                return false;
            }
        });
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.topBar_right_layout:
                Intent intent = new Intent(context, InquiryModelHistoryActivity.class);
                startActivityForResult(intent, CommonData.REQUEST_INQUIRY_HISTORY);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//听力测试结束
            switch (requestCode) {
                case CommonData.REQUEST_INQUIRY_HISTORY:
                    setResult(RESULT_OK, data);
                    finish();
                    break;
            }
        }
    }

    public void getData() {
        String url = Constants.INQUIRE_YEARS;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lineid",lineid);//"lineid":"车系标识(int)"
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
//            "lis": [
//            {
//                "year": "2015",
//                    "car": [
//                {
//                    "carid": 3,
//                        "nam": "Sportback 手动进取型",
//                        "vol": "1.4",
//                        "pic": "http://123.56.87.239:81/auto/resource/car/1434756874.png"
//                }
//                ]
//            }
//            ]
//        }
        mLists = responseModel.getMaps();
        if (mLists != null && mLists.size() > 0) {
            adapter.setData( mLists);
            setViewData();
            expandListView.setVisibility(View.VISIBLE);
            setDataNull(false);
        }else {
            expandListView.setVisibility(View.GONE);
            setDataNull(true,"暂无相关年款数据，请重新选择车型");
        }

    }

}
