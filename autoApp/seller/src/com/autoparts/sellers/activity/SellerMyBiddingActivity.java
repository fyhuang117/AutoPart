package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.SellerBiddingAdapter;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.utils.Constants;
import com.autoparts.sellers.utils.Utils;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 我的竞价中
 * Created by:Liuhuacheng
 * Created time:15-5-24
 */
public class SellerMyBiddingActivity extends BaseActivity {
    private Context context;
    String title = "";
    String[] strings = null;
    private SellerBiddingAdapter adapter;
    private ListView mListView;
    private int handlerPosition = 0;
    private String del_id;
    private int del_position = -1;


    private ImageView icon_search;
    private EditText search_view;

    private Vector<HashMap<String, Object>> mList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handlerPosition = msg.what;
            del_position = msg.arg1;
            del_id = (String) mList.get(msg.arg1).get("biddingid");
            switch (handlerPosition) {
                case 0://取消
                    detail(del_position);
                    break;
            }

        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.seller_my_bidding_view);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        context = this;
        initNullDataView();
        setTitle(getString(R.string.bid_text_bidding));
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setDivider(null);
        int divider = (int) getResources().getDimension(R.dimen.common_padding_middle);
        mListView.setDividerHeight(divider);
        mListView.setOnItemClickListener(this);

        mList = new Vector<HashMap<String, Object>>();
        adapter = new SellerBiddingAdapter(context, mList, handler);
        mListView.setAdapter(adapter);

        icon_search = (ImageView) findViewById(R.id.icon_search);
        search_view = (EditText) findViewById(R.id.search_view);
        icon_search.setOnClickListener(this);

        getData(Constants.INQUIRE_BIDDING_LIST);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.confirm_ok:
                if (handlerPosition == 0) {
                    getData(Constants.INQUIRE_BIDDING_DEL);
                }
                if (handlerPosition == 2) {
                    Utils.showToastShort(context, "订单信息已删除");
                }
                break;
            case R.id.icon_search:
                getData(Constants.INQUIRE_BIDDING_LIST);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        super.onItemClick(adapterView, view, i, l);
        detail(i);
    }


    public void detail(int position) {
        Intent intent = null;
        intent = new Intent(context, SellerBiddingDetailActivity.class);
        String id = (String) mList.get(position).get("biddingid");
        intent.putExtra(CommonData.INQUIRE_ID, id);
        intent.putExtra(CommonData.MAPS, mList.get(position));
        startActivityForResult(intent, CommonData.REQUEST_BIDDING_DETAIL);
    }

    //取消竞价
    public void getData(final String url) {
        showProgressDialog();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("biddingid", del_id);
        String parttype_name = search_view.getText().toString();
        params.put("parttype_name", parttype_name);
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (url.equals(Constants.INQUIRE_BIDDING_LIST)) {
                    setData(response);
                } else {
                    Utils.showToastShort(context, "竞价单已取消");
                    mList.remove(del_position);
                    adapter.setData(mList);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                disProgressDialog();
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

    public void setDataNull(boolean b) {
        if (b) {
            if (data_null_view != null) {
                data_null_view.setVisibility(View.VISIBLE);
//            mListView.setVisibility(View.GONE);
            }
        } else {
            if (data_null_view != null) {
                data_null_view.setVisibility(View.GONE);
//            mListView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CommonData.REQUEST_BIDDING_DETAIL:
                    getData(Constants.INQUIRE_BIDDING_LIST);

                    break;
            }
        }
    }


}
