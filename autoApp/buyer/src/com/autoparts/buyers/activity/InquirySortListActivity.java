package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.CommonListAdapter;
import com.autoparts.buyers.adapter.InquireSortAdapter;
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
 * 配件分类--详细列表
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquirySortListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private InquireSortAdapter adapter;
    private ListView mListView;
    String title = "";

    private Vector<HashMap<String, Object>> mList;
    private String parttypeid;
    private String url;
    private boolean requestData = true;
    public static Vector<HashMap<String, Object>> mList_data = null;
    private int is_select_car = 1, is_select_quality = 1;
//    "is_select_car":"是否需要选择车型(int 0|1)",
//    "is_select_quality":"是否需要选择品质(int 0|1)"

    private ImageView icon_search;
    private EditText search_view, search_input;
    private LinearLayout search_input_view, search_parent;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inquiry_sort_list_view);
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        context = this;
        initNullDataView();
        title = getIntent().getStringExtra("title");
        parttypeid = getIntent().getStringExtra(CommonData.INQUIRE_ID);
        url = getIntent().getStringExtra(CommonData.INQUIRE_URL);
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
        adapter = new InquireSortAdapter(context, mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        search_input_view = (LinearLayout) findViewById(R.id.search_input_view);
        search_input = (EditText) findViewById(R.id.search_input);
        search_input_view.setVisibility(View.GONE);

        search_parent = (LinearLayout) findViewById(R.id.search_parent);
        search_parent.setVisibility(View.VISIBLE);

        icon_search = (ImageView) findViewById(R.id.icon_search);
        search_view = (EditText) findViewById(R.id.search_view);
        icon_search.setOnClickListener(this);

        if (mList != null && mList.size() == 0) {
            requestData = true;
            getData(parttypeid, requestData);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.icon_search:
                search();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        title = (String) mList.get(i).get("nam");
        String id = (String) mList.get(i).get("parttypeid");
        is_select_car = CommonData.getInstance(context).getIntData((String) mList.get(i).get("is_select_car"));
        is_select_quality = CommonData.getInstance(context).getIntData((String) mList.get(i).get("is_select_quality"));
        requestData = false;
        getData(id, requestData);
    }

    public void getData(final String id, final boolean b) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("par", id);
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
//        {
//                "parttypeid": 8,
//                "nam": "发动机",
//                "pic": "http://123.56.87.239:81/auto/resource/parttype/",
//                "is_select_car": 0,
//                "is_select_quality": 0
//        }
        if (b) {
            mList = responseModel.getMaps();
            if (mList != null && mList.size() > 0) {
                adapter.setData(mList);
                adapter.notifyDataSetChanged();
                setDataNull(false);
                mListView.setVisibility(View.VISIBLE);
            } else {
                setDataNull(true);
                mListView.setVisibility(View.GONE);
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
        Intent intent = new Intent(context, InquirySortListActivity.class);
        intent.putExtra("title", title);
        intent.putExtra(CommonData.INQUIRE_ID, "");
        intent.putExtra(CommonData.INQUIRE_URL, url);
        InquirySortListActivity.mList_data = mList;
        startActivityForResult(intent, CommonData.REQUEST_CODE_SORT);
    }


    public void onFinish(String id, String name) {
        Intent intent = new Intent();
        intent.putExtra(CommonData.INQUIRE_NAME, name);
        intent.putExtra(CommonData.INQUIRE_ID, id);
        intent.putExtra("car", is_select_car);
        intent.putExtra("quality", is_select_quality);
        setResult(RESULT_OK, intent);
        finish();
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

    //搜索配件
    public void search() {
        String content = search_view.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Utils.showToastShort(context, "请先输入关键字");
        } else {
            String search_url = Constants.SEARCH_PARTTYPE;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("name", content);
            HttpClientUtils.post(context, search_url, params, new HttpResultHandler() {
                @Override
                public void onResultFail(String message, int statusCode) {
                    super.onResultFail(message, statusCode);
                }

                @Override
                public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                    super.onResultSuccess(headers, response, message, statusCode);
                    setSearchData(response);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    disProgressDialog();
                }
            });
        }
    }

    public void setSearchData(ResponseModel responseModel) {
        mList = responseModel.getMaps();
        if (mList != null && mList.size() > 0) {
            adapter.setData(mList);
            adapter.notifyDataSetChanged();
            setDataNull(false);
            search_input_view.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.GONE);
            search_parent.setVisibility(View.GONE);
            search_input_view.setVisibility(View.VISIBLE);
        }
    }

    public void commit_input(View view) {
        String content = search_input.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Utils.showToastShort(context, "请先输入配件名称");
        } else {
            Intent intent = new Intent();
            intent.putExtra(CommonData.INQUIRE_NAME, content);
            intent.putExtra(CommonData.INQUIRE_ID, "-1");
            intent.putExtra("car", 0);
            intent.putExtra("quality", 0);
            setResult(RESULT_OK, intent);
            finish();
        }
    }


}
