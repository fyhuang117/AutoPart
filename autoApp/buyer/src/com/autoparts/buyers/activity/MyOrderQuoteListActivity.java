package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.MyOrderQuoteAdapter;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.PopupWindowUtil;
import com.autoparts.buyers.utils.Utils;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 进行中的订单--商家报价列表
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MyOrderQuoteListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;

    private MyOrderQuoteAdapter adapter;
    private ListView mListView;
    String title = "";
    private Button inquiry_order_btn, inquiry_seller_btn;
    private PopupWindowUtil popupWindowUtil;

    private HashMap<String, Object> maps = new HashMap<String, Object>();
    private TextView order_parts;

    private Vector<HashMap<String, Object>> mList;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.my_order_quote_list_view);
        super.onCreate(savedInstanceState);
        title = getString(R.string.inquiry_text_list);
        setRightView("取消询价", -1);
        setTitle(title);
        init();
        initNullDataView();
    }

    private void init() {
        context = this;
        order_parts = (TextView) findViewById(R.id.order_parts);

        maps = (HashMap<String, Object>) getIntent().getSerializableExtra(CommonData.MAPS);
        if (maps != null) {
            String par = (String) maps.get("par");
            if (TextUtils.isEmpty(par)){
                par = context.getString(R.string.inquiry_parts_null);
            }
            order_parts.setText(par);
        }

        popupWindowUtil = new PopupWindowUtil();
        mListView = (ListView) findViewById(R.id.mListView);
        mList = new Vector<HashMap<String, Object>>();
        adapter = new MyOrderQuoteAdapter(context, mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        inquiry_order_btn = (Button) findViewById(R.id.inquiry_order_btn);
        inquiry_seller_btn = (Button) findViewById(R.id.inquiry_seller_btn);
        inquiry_order_btn.setOnClickListener(this);
        inquiry_seller_btn.setOnClickListener(this);

        getData(Constants.ORDER_INQUIRY_BIDDING);//获取询价单对应的竞价单
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, MyInquiryListDetailActivity.class);
        intent.putExtra(CommonData.MAPS, mList.get(i));
        intent.putExtra(CommonData.INQUIRE_NAME, order_parts.getText());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.inquiry_order_btn:
                String[] content = getResources().getStringArray(R.array.sort_list);
                show(content, inquiry_order_btn);
                break;
            case R.id.inquiry_seller_btn:
                String[] content1 = getResources().getStringArray(R.array.filter_list);
                showFilter(content1, inquiry_seller_btn);
                break;
            case R.id.topBar_right_layout:
                showDialog("是否取消询价", true);
                break;
            case R.id.confirm_ok:
                getData(Constants.ORDER_DEL);
                break;
        }
    }

    //我的询价单详情
    public void my_order_detail(View view) {
        Intent intent = new Intent(context, MyOrderQuoteDetailActivity.class);
        intent.putExtra(CommonData.MAPS, maps);
        startActivityForResult(intent, CommonData.REQUEST_ORDER_CANCEL);

    }

    public void show(final String[] content, final Button inquiry_order_btn) {
        popupWindowUtil.show(context, inquiry_order_btn, content, new PopupWindowUtil.CallBackName() {
            @Override
            public void name(int position) {
                inquiry_order_btn.setText(content[position]);
                sort_type = (position + 1) + "";
                getData(Constants.ORDER_INQUIRY_BIDDING);//获取询价单对应的竞价单
            }
        });
    }

    public void showFilter(final String[] content, final Button inquiry_order_btn) {
        popupWindowUtil.show(context, inquiry_order_btn, content, new PopupWindowUtil.CallBackName() {
            @Override
            public void name(int position) {
                inquiry_order_btn.setText(content[position]);
                avi = position + "";
                getData(Constants.ORDER_INQUIRY_BIDDING);//获取询价单对应的竞价单
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CommonData.REQUEST_ORDER_CANCEL://订单取消回调
                    setResult(RESULT_OK, data);
                    finish();
                    break;
            }
        }
    }

    //取消订单,查询询价单对应的竞价单
    public void getData(final String url) {
        showProgressDialog();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("inquiryid", maps.get("inquiryid"));
        getParams(params);
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (url.equals(Constants.ORDER_DEL)) {
                    Utils.showToastShort(context, getString(R.string.order_has_del));
                    setResult(RESULT_OK);
                    finish();
                } else {
                    setData(response);
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

//        "biddingid": 1,
//                "nam": "\u65fa\u65fa\u6c7d\u4fee",
//                "sell_pic": "",
//                "business_area": "",
//                "sell_level": 1,
//                "sell_score": [
//        0,
//                0,
//                0
//        ],
//        "pri": "100.00",
//                "avi": 1,
//                "qau": 1,
//                "pay": 0,
//                "del": 0,
//                "pof": "1.00",
//                "ban": [
//        "\u52a8\u5de5"
//        ],
//        "pic1": "",
//                "pic2": "",
//                "pic3": ""
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

    private String avi = "2";//是否现货 int 0-否 1-是 2-全部
    private String business_area = "0";//商圈标识 int 0-全部
    private String sort_type = "0";//排序规则(int 0-默认 1-用户等级 2-用户信用 3-价格升序 4-价格降序 5-距离)

    public void getParams(Map<String, Object> params) {
        params.put("avi", avi);
        params.put("business_area", business_area);
        params.put("sort_type", sort_type);

    }


}
