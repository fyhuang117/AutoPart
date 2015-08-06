package com.autoparts.buyers.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.MyMessageAdapter;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 卖家 我的消息
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class UserMessageActivity extends BaseActivity {
    private Context context;
    private ListView mListView;
    private Vector<HashMap<String, Object>> mList;
    private MyMessageAdapter adapter;
    private int clickPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);
        init();
        initNullDataView();
    }

    private void init() {
        String title = getString(R.string.message_title);
        setTitle(title);
        context = this;
        mListView = (ListView) findViewById(R.id.mListView);
        mListView.setDivider(null);
        int divider = (int) getResources().getDimension(R.dimen.common_padding_middle);
        mListView.setDividerHeight(divider);

        mList = new Vector<HashMap<String, Object>>();
        adapter = new MyMessageAdapter(context, mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        getData(Constants.USER_MESSAGE_LIST);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        clickPosition = position;
        showDialog("是否删除该条消息？", true);
        return super.onItemLongClick(parent, view, position, id);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.confirm_ok:
                getData(Constants.USER_MESSAGE_DEL);
                break;
        }
    }

    //获取系统消息列表
    public void getData(final String url) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (clickPosition != -1) {
            String message_id = (String) mList.get(clickPosition).get("message_id");
            params.put("message_id", message_id);
        }
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (url.equals(Constants.USER_MESSAGE_LIST)) {
                    setData(response);
                } else {
                    Utils.showToastShort(context, "信息删除成功");
                    mList.remove(clickPosition);
                    adapter.setData(mList);
                }
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