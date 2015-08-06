package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.CommonListAdapter;
import com.autoparts.buyers.adapter.InquireCircleAdapter;
import com.autoparts.buyers.adapter.InquireSortAdapter;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 询价-其它--商圈
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquiryCircleActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private InquireCircleAdapter adapter;
    private ListView mListView;
    String title = "";

    private Vector<HashMap<String, Object>> mList;
    private boolean requestData = true;
    public static Vector<HashMap<String, Object>> mList_data = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        context = this;
        initNullDataView();
        title = getIntent().getStringExtra("title");
        setTitle(title);
//        mList = (Vector<HashMap<String, Object>>) getIntent().getSerializableExtra(CommonData.INQUIRE_LIST);

        if (mList == null) {
            mList = new Vector<HashMap<String, Object>>();
        }
        if (mList_data != null) {
            mList.clear();
            mList = mList_data;
        }

        mListView = (ListView) findViewById(R.id.mListView);
        adapter = new InquireCircleAdapter(context, mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        if (mList != null && mList.size() == 0) {
            requestData = true;
            getData("0", requestData);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        title = (String) mList.get(i).get("name");
        String id = mList.get(i).get("areaid") +"";
        requestData = false;
        getData(id, requestData);
    }

    public void getData(final String id, final boolean b) {
        String url = Constants.INQUIRE_AREA;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("areaparentid", id);
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                setData(response, b, id);
            }

            @Override
            public void onResultJson(String json) {
                super.onResultJson(json);
            }
        });

    }

    public void setData(ResponseModel responseModel, boolean b, String id) {
//        "areaid":"商圈标识(int)",
//                "name":"商圈名称",
//                "firstword":"首字母"

        if (b) {
            mList = responseModel.getMaps();
            if (mList != null && mList.size() > 0) {
                adapter.setData(mList);
                adapter.notifyDataSetChanged();
            } else {
                setDataNull(true,"暂无商圈信息");
            }
        } else {
            mList = responseModel.getMaps();
            if (mList != null && mList.size() > 0) {
                intentToData();
            } else {
                onFinish(id, title);
            }
        }
    }

    public void intentToData() {
        Intent intent = new Intent(context, InquiryCircleActivity.class);
        intent.putExtra("title", title);
        InquiryCircleActivity.mList_data = mList;
        startActivityForResult(intent, CommonData.REQUEST_INQUIRY_LOCATION);
    }


    public void onFinish(String id, String name) {
        Intent intent = new Intent();
        intent.putExtra(CommonData.INQUIRE_NAME, name);
        intent.putExtra(CommonData.INQUIRE_ID, id);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CommonData.REQUEST_INQUIRY_LOCATION:
                    setResult(RESULT_OK, data);
                    finish();
                    break;
            }
        }
    }
}