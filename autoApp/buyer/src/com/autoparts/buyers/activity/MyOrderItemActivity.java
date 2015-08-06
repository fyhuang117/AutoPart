package com.autoparts.buyers.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.MyInquiryAdapter;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
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
public class MyOrderItemActivity extends BaseActivity {
    private Context context;
    String title = "";
    int mNum;
    private MyInquiryAdapter adapter;
    private TextView viewpager_item_text;
    private ListView mListView;
    private int handlerPosition = 0;
    private int item_sta;
    private String item_orderid;

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

                    intent = new Intent(context, CompanyCantactActivity.class);
                    intent.putExtra("sel",  (String) mList.get(clickPosition).get("sel"));
                    startActivity(intent);

                    break;
                case 2://删除
                    showDialog("是否删除该订单？", true);
                    break;
                case 3://付款
                     intent = new Intent(context, MyOrderDetailPayActivity.class);
                    intent.putExtra(CommonData.MAPS, mList.get(clickPosition));
                    intent.putExtra(CommonData.INQUIRE_ID,item_orderid);
                    startActivity(intent);
                    break;
                case 4://评价
                    intent = new Intent(context, UserEvaluateActivity.class);
                    intent.putExtra(CommonData.INQUIRE_ID, item_orderid);
                    startActivityForResult(intent, CommonData.REQUEST_ORDER_SCORE);
                    break;
                case 5://收货
                    showDialog(getString(R.string.order_receive_hint), true);
                    break;
            }

        }
    };

    private String[] states;
    private Vector<HashMap<String, Object>> mList;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.my_order_item_view);
        super.onCreate(savedInstanceState);
        init();
        initNullDataView();

    }

    private void init() {
        context = this;
        findViewById(R.id.top_view).setVisibility(View.GONE);
        mNum = getIntent().getIntExtra("position", 0);

        mListView = (ListView) findViewById(android.R.id.list);
        viewpager_item_text = (TextView) findViewById(R.id.viewpager_item_text);
        mListView.setDivider(null);
        int divider = (int) getResources().getDimension(R.dimen.common_padding_middle);
        mListView.setDividerHeight(divider);

        mList = new Vector<HashMap<String, Object>>();
        adapter = new MyInquiryAdapter(context, mList, handler);
        mListView.setAdapter(adapter);
        adapter.setNum(mNum);
        mListView.setOnItemClickListener(this);

//        viewpager_item_text.setVisibility(View.VISIBLE);
//        mListView.setVisibility(View.GONE);
        states = getIntent().getStringArrayExtra("states");

        if (mNum == 0) {
            registerBoradcastReceiver();
        }
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
                } else if (handlerPosition == 2) {
                    Utils.showToastShort(context, "订单信息已删除");
                } else if (handlerPosition == 5) {//收货
                    getOpeData(Constants.ORDER_RECEIVE);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        super.onItemClick(adapterView, view, i, l);
        Intent intent = new Intent(context, MyOrderDetailPayActivity.class);
        intent.putExtra(CommonData.MAPS, mList.get(i));
        intent.putExtra(CommonData.INQUIRE_ID, (String)mList.get(i).get("orderid"));
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CommonData.REQUEST_ORDER_CANCEL://订单取消回调
                    break;
                case CommonData.REQUEST_ORDER_PAY://付款成功
                    break;
            }
        }
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
            mListView.setVisibility(View.VISIBLE);
            setDataNull(false);
        } else {
            mListView.setVisibility(View.GONE);
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

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(CommonData.ACTION_NAME);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

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
                if (url.equals(Constants.ORDER_RECEIVE)) {
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
