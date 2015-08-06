package com.autoparts.sellers.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.MyOrderListAdapter;
import com.autoparts.sellers.adapter.SellerBiddingAdapter;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.utils.Constants;
import com.autoparts.sellers.utils.Utils;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 我的订单详情
 * Created by:Liuhuacheng
 * Created time:15-5-24
 */
public class SellerOrderItemActivity extends BaseActivity {
    private Context context;
    String title = "";
    String[] strings = null;
    int mNum;
    private MyOrderListAdapter adapter;
    private TextView data_null;
    private ListView mListView;
    private int handlerPosition = 0;
    private String item_orderid;

    private String[] states;
    private Vector<HashMap<String, Object>> mList;
    private int item_sta;

    private LinearLayout search_parent;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handlerPosition = msg.what;
            int clickPosition = msg.arg1;
            Intent intent = null;
            item_sta = Integer.parseInt((String) mList.get(clickPosition).get("sta"));
            item_orderid = (String) mList.get(clickPosition).get("orderid");

            switch (handlerPosition) {
                case 0://取消
//                    showDialog("是否取消询价？", true);
                    break;
                case 1://联系卖家
//                    intent = new Intent(context, ChatActivity.class);
//                    intent.putExtra(CommonData.ORDER_STATE, item_sta);
//                    intent.putExtra(CommonData.INQUIRE_ID, item_orderid);
//                    startActivityForResult(intent, CommonData.REQUEST_ORDER_SEND);

                    intent = new Intent(context, CompanyDetailActivity.class);
                    intent.putExtra("buyer", (String) mList.get(clickPosition).get("buy"));
                    startActivity(intent);


                    break;
                case 2://删除
                    showDialog("是否删除该订单？", true);
                    break;
                case 3://付款
                    intent = new Intent(context, PayAlipayActivity.class);
                    intent.putExtra(CommonData.INQUIRE_ID, item_orderid);
                    startActivityForResult(intent, CommonData.REQUEST_ORDER_PAY);
                    break;
                case 4://评价
                    intent = new Intent(context, UserEvaluateActivity.class);
                    intent.putExtra(CommonData.INQUIRE_ID, item_orderid);
                    startActivityForResult(intent, CommonData.REQUEST_ORDER_SCORE);
                    break;
                case 5://发货
                    showDialog(getString(R.string.order_receive_hint), true);
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
        initNullDataView();
    }

    private void init() {
        context = this;

        search_parent = (LinearLayout) findViewById(R.id.search_parent);
        search_parent.setVisibility(View.GONE);

        findViewById(R.id.top_view).setVisibility(View.GONE);
        mNum = getIntent().getIntExtra("position", 0);
        states = getIntent().getStringArrayExtra("states");
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setDivider(null);
        int divider = (int) getResources().getDimension(R.dimen.common_padding_middle);
        mListView.setDividerHeight(divider);
        mListView.setOnItemClickListener(this);

        mList = new Vector<HashMap<String, Object>>();
        adapter = new MyOrderListAdapter(context, mList, handler);
        mListView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(states);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.confirm_ok:
                if (handlerPosition == 0) {
                    Utils.showToastShort(context, "该询价单已取消");
                }
                if (handlerPosition == 2) {
                    Utils.showToastShort(context, "订单信息已删除");
                }
                else if (handlerPosition == 5) {//收货
                    getOpeData(Constants.ORDER_SEND);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        super.onItemClick(adapterView, view, i, l);
        Intent intent = null;
        intent = new Intent(context, SellerOrderDetailActivity.class);
        intent.putExtra(CommonData.MAPS, mList.get(i));
        String id = item_orderid = (String) mList.get(i).get("orderid");
        intent.putExtra(CommonData.INQUIRE_ID,id);
        startActivity(intent);
    }


    //获取全部订单
    public void getData(String[] json_states) {
        String url = Constants.ORDER_GET_STATE;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("json", getJson(json_states));
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

    public String getJson(String[] json_states) {
        JSONObject jsonObject = new JSONObject();
        try {
            String id = preferences.getUserID();
            Utils.showLog("id==" + id);
            if (!TextUtils.isEmpty(id)) {
                jsonObject.put("id", id);
            }
            if (json_states != null && json_states.length > 0) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < json_states.length; i++) {
                    jsonArray.put(json_states[i]);
                }
                jsonObject.put("sta", jsonArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction().trim();
            if (action.equals(CommonData.ACTION_NAME)) {
                states = intent.getStringArrayExtra("states");
                getData(states);
            }
        }
    };

    //获取全部订单
    public void getOpeData(final String url) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderid", item_orderid);
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (url.equals(Constants.ORDER_SEND)) {
                    Utils.showToastShort(context, getString(R.string.order_receive_success));
                    getData(states);
                }
            }

            @Override
            public void onResultJson(String json) {
                super.onResultJson(json);
            }
        });

    }

}
