package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.InquirySortlistAdapter;
import com.autoparts.sellers.model.CommonLetterModel;
import com.autoparts.sellers.model.ContactUtils;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.utils.Constants;
import com.autoparts.sellers.utils.Utils;
import org.apache.http.Header;

import java.util.*;

/**
 * 配件一级分类
 * Created by:Liuhuacheng
 * Created time:15-5-22
 */
public class InquirySortNewActivity extends BaseActivity {
    int mNum;
    private List<CommonLetterModel> mList;
    private ContactUtils contactUtils;
    private Context context;
    private ListView mListView;
    private InquirySortlistAdapter adapter;
    String url = Constants.INQUIRE_PART_LIST;

    private ImageView icon_search;
    private EditText search_view, search_input;
    private LinearLayout search_input_view, search_parent;

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inquiry_sort_new);
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        context = this;
        setTitle(getString(R.string.inquiry_text_sort));
        contactUtils = new ContactUtils();
        mList = new ArrayList<CommonLetterModel>();
        mListView = (ListView) findViewById(R.id.mListView);

        mList = new ArrayList<CommonLetterModel>();
        adapter = new InquirySortlistAdapter(context, mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        getData("0");
        initSearch();
    }

    public void initSearch() {
        search_input_view = (LinearLayout) findViewById(R.id.search_input_view);
        search_input = (EditText) findViewById(R.id.search_input);
        search_input_view.setVisibility(View.GONE);

        search_parent = (LinearLayout) findViewById(R.id.search_parent);
        search_parent.setVisibility(View.GONE);

        icon_search = (ImageView) findViewById(R.id.icon_search);
        search_view = (EditText) findViewById(R.id.search_view);
        icon_search.setOnClickListener(this);
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
        super.onItemClick(adapterView, view, i, l);
        Intent intent = new Intent(context, InquirySortListActivity.class);
        InquirySortListActivity.mList_data = null;
        intent.putExtra("title", mList.get(i).getUser_name());
        String parttypeid = mList.get(i).getUser_id();
        intent.putExtra(CommonData.INQUIRE_ID, parttypeid);
        intent.putExtra(CommonData.INQUIRE_URL, url);
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

    public void getData(final String par) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("par", par);
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (par.equals("0"))
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
//                "parttypeid": 6,
//                    "nam": "头灯",
//                    "pic": "http://123.56.87.239:81/auto/resource/parttype/1434756933.png",
//                    "is_select_car": 1,
//                    "is_select_quality": 1
//            }
//            ]
//        }

        Vector<HashMap<String, Object>> maps = responseModel.getMaps();
        if (maps != null && maps.size() > 0) {
            getList(maps);
        }
    }


    //搜索配件
    public void search() {
        String content = search_view.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Utils.showToastShort(context, "请先输入关键字");
        } else {
            showProgressDialog();
            closeInputMethod(search_view);
//            String search_url = Constants.SEARCH_PARTTYPE;
            String search_url = "";
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
        Vector<HashMap<String, Object>> maps = responseModel.getMaps();
        if (maps != null && maps.size() > 0) {
            getList(maps);
            setDataNull(false);
            search_input_view.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.GONE);
            search_parent.setVisibility(View.VISIBLE);
            search_input_view.setVisibility(View.VISIBLE);
//            search_view.setHint("");
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

    public void getList(Vector<HashMap<String, Object>> maps) {
        mList.clear();
        for (int i = 0; i < maps.size(); i++) {
            HashMap<String, Object> map = maps.get(i);
            String parttypeid = (String) map.get("parttypeid");
            String nam = (String) map.get("nam");
            String pic = (String) map.get("pic");
            CommonLetterModel commonLetterModel = new CommonLetterModel();
            commonLetterModel.setUser_id(parttypeid);
            commonLetterModel.setUser_image(pic);
            commonLetterModel.setUser_name(nam);
            String key = "#";
            if (!TextUtils.isEmpty(nam)) {
                key = new ContactUtils().getPinYinHeadChar(nam.substring(0, 1));
                key = key.toUpperCase();
                if (TextUtils.isEmpty(key)) {
                    key = "#";
                }
            }

            commonLetterModel.setUser_key(key);
            mList.add(commonLetterModel);
        }

        mList = contactUtils.getListKey(mList);
        adapter.setData(mList);
        adapter.notifyDataSetChanged();
    }


}