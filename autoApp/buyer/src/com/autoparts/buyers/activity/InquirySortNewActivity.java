package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.InquirySortlistAdapter;
import com.autoparts.buyers.model.CommonLetterModel;
import com.autoparts.buyers.model.ContactUtils;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.JsonParserUtils;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import org.apache.http.Header;

import java.util.*;

/**
 * 配件  一级分类
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
    private boolean isSearch = false;

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
        initNullDataView();
        contactUtils = new ContactUtils();
        mList = new ArrayList<CommonLetterModel>();
        mListView = (ListView) findViewById(R.id.mListView);

        mList = new ArrayList<CommonLetterModel>();
        adapter = new InquirySortlistAdapter(context, mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        isSearch = false;
        getData("0");
        initSearch();
    }

    public void initSearch() {
        search_input_view = (LinearLayout) findViewById(R.id.search_input_view);
        search_input = (EditText) findViewById(R.id.search_input);
        search_input_view.setVisibility(View.GONE);

        search_parent = (LinearLayout) findViewById(R.id.search_parent);
        search_parent.setVisibility(View.VISIBLE);

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
        if (isSearch) {
            String name = mList.get(i).getUser_name();
            String id = mList.get(i).getUser_id();
            onSearchClick(id,name);

        } else {
            Intent intent = new Intent(context, InquirySortListActivity.class);
            InquirySortListActivity.mList_data = null;
            intent.putExtra("title", mList.get(i).getUser_name());
            String parttypeid = mList.get(i).getUser_id();
            intent.putExtra(CommonData.INQUIRE_ID, parttypeid);
            intent.putExtra(CommonData.INQUIRE_URL, url);
            startActivityForResult(intent, CommonData.REQUEST_CODE_SORT);
        }
    }

    public void onSearchClick(String id, String name) {
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
                case CommonData.REQUEST_CODE_SORT:
                    setResult(RESULT_OK, data);
                    finish();
                    break;
            }
        }
    }

    public void getData(final String par) {
        showProgressDialog();
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
            public void onFinish() {
                super.onFinish();
                disProgressDialog();
            }
        });

    }

    public void setData(ResponseModel responseModel) {
        Vector<HashMap<String, Object>> maps = responseModel.getMaps();
        if (maps != null && maps.size() > 0) {
            isSearch = false;
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
        Vector<HashMap<String, Object>> maps = responseModel.getMaps();
        if (maps != null && maps.size() > 0) {
            isSearch = true;
            getList(maps);
            setDataNull(false);
            search_input_view.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.GONE);
//            search_parent.setVisibility(View.VISIBLE);
//            search_input_view.setVisibility(View.VISIBLE);
//            search_view.setHint("");
            setDataNull(true, getString(R.string.inquiry_model_search_null));
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
        adapter.setSearch(isSearch);
        adapter.setData(mList);
        adapter.notifyDataSetChanged();
    }


}