package com.autoparts.sellers.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.CommonListAdapter;
import com.autoparts.sellers.adapter.UserWalletListAdapter;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.utils.Constants;
import com.autoparts.sellers.utils.DialogUtil;
import com.autoparts.sellers.utils.Utils;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 询价-配件
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class UserWalletDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private UserWalletListAdapter adapter;
    private ListView mListView;
    String title = "";
    private Vector<HashMap<String, Object>> mList;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);
        title = "账户明细";
        setTitle(title);
        init();
        initNullDataView();

    }

    private void init() {
        context = this;
        mList = new Vector<HashMap<String, Object>>();
        mListView = (ListView) findViewById(R.id.mListView);
        adapter = new UserWalletListAdapter(context, mList);
        mListView.setAdapter(adapter);

        getData(Constants.USER_WALLET_DETAIL);
    }


    //获取账户详情
    public void getData(final String url) {
        Map<String, Object> params = new HashMap<String, Object>();
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
        mList = responseModel.getMaps();
        if (mList != null && mList.size() > 0) {
            adapter.setData(mList);
            setDataNull(false);
        } else {
            adapter.setData(mList);
            setDataNull(true);
        }

    }


}
