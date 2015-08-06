package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.GridAdapter;
import com.autoparts.buyers.alipay.PayUtil;
import com.autoparts.buyers.alipay.Result;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import com.autoparts.buyers.view.MyGridView;
import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单详情--付款
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MyOrderDetailPayActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private LinearLayout order_no_view;
    private RelativeLayout order_money_view;
    private View line;
    String title = "";

    private TextView order_state, order_no, order_time;
    private TextView order_phone, order_address, order_buyer_name;
    private TextView inquiry_title, inquiry_name, inquiry_message;
    private TextView order_money, order_company;

    private TextView order_freight, order_moneys, order_real_moneys;//运费,商品金额，实付款
    private RelativeLayout order_discount_select_view, order_text_discount_view;
    private TextView order_discount, order_text_discount;

    private TextView order_pay_style, order_send_style;

    private String orderid;
    private int sta;//订单状态
    private MyGridView mGridView;
    private List<String> mGridPhotos;

    private double real_money = 0.0;
    private String sel;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.my_order_detail);
        super.onCreate(savedInstanceState);
        title = "订单详情";
        setTitle(title);
        init();

    }

    private void init() {
        context = this;
        order_no_view = (LinearLayout) findViewById(R.id.order_no_view);
        order_no_view.setVisibility(View.VISIBLE);
        findViewById(R.id.line).setVisibility(View.VISIBLE);
        order_money_view = (RelativeLayout) findViewById(R.id.order_money_view);
        order_money_view.setVisibility(View.VISIBLE);

        order_state = (TextView) findViewById(R.id.order_state);
        order_no = (TextView) findViewById(R.id.order_no);
        order_time = (TextView) findViewById(R.id.order_time);

        order_money = (TextView) findViewById(R.id.order_money);
        order_company = (TextView) findViewById(R.id.order_company);

        inquiry_title = (TextView) findViewById(R.id.inquiry_title);
        inquiry_name = (TextView) findViewById(R.id.inquiry_name);

        order_buyer_name = (TextView) findViewById(R.id.order_buyer_name);
        order_phone = (TextView) findViewById(R.id.order_phone);
        order_address = (TextView) findViewById(R.id.order_address);

        order_freight = (TextView) findViewById(R.id.order_freight);
        order_moneys = (TextView) findViewById(R.id.order_moneys);
        order_real_moneys = (TextView) findViewById(R.id.order_real_moneys);

        order_discount_select_view = (RelativeLayout) findViewById(R.id.order_discount_select_view);
        order_discount = (TextView) findViewById(R.id.order_discount);

        order_text_discount_view = (RelativeLayout) findViewById(R.id.order_text_discount_view);
        order_text_discount = (TextView) findViewById(R.id.order_text_discount);

        if (sta != 0) {
            //优惠券屏蔽
            order_discount_select_view.setVisibility(View.GONE);
            order_text_discount_view.setVisibility(View.GONE);
        } else {
            //优惠券屏蔽
            order_discount_select_view.setVisibility(View.VISIBLE);
            order_text_discount_view.setVisibility(View.VISIBLE);
        }

        order_pay_style = (TextView) findViewById(R.id.order_pay_style);
        order_send_style = (TextView) findViewById(R.id.order_send_style);

        setViewData();
    }

    public void setViewData() {
        inquiry_title = (TextView) findViewById(R.id.inquiry_title);
        inquiry_name = (TextView) findViewById(R.id.inquiry_name);

        orderid = getIntent().getStringExtra(CommonData.INQUIRE_ID);
        getData(Constants.ORDER_DETAIL);

    }


    public void setInitData(Map<String, String> maps) {
        inquiry_title.setText((String) maps.get("par"));
        inquiry_name.setText((String) maps.get("car"));

        sta = Integer.parseInt((String) maps.get("sta"));
        setBottomState(sta);

        order_state.setText(CommonData.getOrderState(sta));
        order_no.setText(String.format(getString(R.string.order_text_no), (String) maps.get("num")));

        sel = (String) maps.get("sel");
        String tel = preferences.getLoginPhone();
        String adr = preferences.getStringData("adr");
        String nam = preferences.getStringData("nam");
        order_buyer_name.setText(nam);
        order_phone.setText(tel);
        order_address.setText(adr);

        mGridView = (MyGridView) findViewById(R.id.mGridView);
        mGridPhotos = new ArrayList<String>();
        GridAdapter mGridadapter = new GridAdapter(context, mGridPhotos);
        mGridView.setAdapter(mGridadapter);
        String pic1 = (String) maps.get("pic1");
        String pic2 = (String) maps.get("pic2");
        String pic3 = (String) maps.get("pic3");
//        String pic3 = "http://img4.imgtn.bdimg.com/it/u=253641591,834822028&fm=21&gp=0.jpg";
        if (!TextUtils.isEmpty(pic1)) {
            mGridPhotos.add(pic1);
        }
        if (!TextUtils.isEmpty(pic2)) {
            mGridPhotos.add(pic2);
        }
        if (!TextUtils.isEmpty(pic3)) {
            mGridPhotos.add(pic3);
        }
        setItemClick();
        mGridadapter.setData(mGridPhotos);
    }

    public void setItemClick() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonData.getInstance(context).showImage(context, mGridPhotos, position);
            }
        });
    }


    public void pay() {
//        Intent intent = new Intent(context, PayAlipayActivity.class);
//        intent.putExtra(CommonData.INQUIRE_ID, orderid);
//        intent.putExtra("money", real_money + "");
//        startActivityForResult(intent, CommonData.REQUEST_ORDER_PAY);

        pay_now(real_money + "");


    }

    public void seller() {
//        Intent intent = new Intent(context, ChatActivity.class);
//        intent.putExtra(CommonData.ORDER_STATE, sta);
//        intent.putExtra(CommonData.INQUIRE_ID, orderid);
//        startActivityForResult(intent, CommonData.REQUEST_ORDER_SEND);

        Intent intent = new Intent(context, CompanyCantactActivity.class);
        intent.putExtra("sel",sel);
        startActivity(intent);
    }

    public void score() {
        Intent intent = new Intent(context, UserEvaluateActivity.class);
        intent.putExtra(CommonData.INQUIRE_ID, orderid);
        startActivityForResult(intent, CommonData.REQUEST_ORDER_SCORE);

    }

    public void del() {

    }

    //获取订单详情
    public void getData(final String url) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderid", orderid);
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (url.equals(Constants.ORDER_DETAIL)) {
                    setData(response);
                } else if (url.equals(Constants.ORDER_RECEIVE)) {
                    Utils.showToastShort(context, getString(R.string.order_receive_success));
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Utils.showToastShort(context, getString(R.string.order_has_del));
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onResultJson(String json) {
                super.onResultJson(json);
            }
        });

    }

    public void setData(ResponseModel responseModel) {
        Map<String, String> map = responseModel.getMap();

        setInitData(map);
        String pri = map.get("pri");
        String time = map.get("begin_time");
        String sel = map.get("sel");
        String pof = map.get("pof");//邮费

        order_money.setText("￥" + pri);
        order_moneys.setText("￥" + pri);
        order_time.setText(String.format(getString(R.string.order_text_time), time));

        if (!TextUtils.isEmpty(sel)) {
            String[] sellers = sel.split(",");
            if (sellers != null && sellers.length>0) {
                order_company.setText(sellers[0]);
            }
        }

        if (!TextUtils.isEmpty(pof)) {
            order_freight.setText("￥" + pof);
        } else {
            order_freight.setText("包邮");
        }

        setMoney(Double.parseDouble(pri) + Double.parseDouble(pof));
        //支付方式
        int pay = CommonData.getInstance(context).getIntData(map.get("pay"));
        String pay_style = CommonData.getInstance(context).getPayStyle(pay);
        order_pay_style.setText(pay_style);

        //送货方式
        String del = map.get("del");
        String str = CommonData.getInstance(context).getDel(del, pof);
        order_send_style.setText(str);

    }

    public void setMoney(double money) {
        real_money = money;
        order_real_moneys.setText("￥" + real_money);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CommonData.REQUEST_ORDER_PAY://付款成功
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                case CommonData.REQUEST_INQUIRY_COUPON:
                    String coupon = data.getStringExtra(CommonData.INQUIRE_NAME);
                    String need_min = data.getStringExtra(CommonData.INQUIRE_NUM);
                    double need_min_money = Double.parseDouble(need_min);
                    if (need_min_money > real_money) {
                        Utils.showToastShort(context, "该订单无法使用此优惠券");
                    } else {
                        order_discount.setText("￥" + coupon);
                        order_text_discount.setText("-￥" + coupon);
                        double money = real_money - Double.parseDouble(coupon);
                        setMoney(money);
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.order_del:
                break;
            case R.id.order_contact:
                seller();
                break;
            case R.id.order_pay:
                pay();
                break;
            case R.id.order_evaluate:
                score();
                break;
            case R.id.order_receipt:
                showDialog(getString(R.string.order_receive_hint), true);
                break;
            case R.id.confirm_ok:
                getData(Constants.ORDER_RECEIVE);
                break;
        }
    }

    LinearLayout inquiry_btn_view;
    Button order_del, order_contact, order_pay, order_evaluate, order_receipt;

    public void setBottomState(int sta) {
        inquiry_btn_view = (LinearLayout) findViewById(R.id.inquiry_btn_view);
        order_del = (Button) findViewById(R.id.order_del);
        order_contact = (Button) findViewById(R.id.order_contact);
        order_pay = (Button) findViewById(R.id.order_pay);
        order_evaluate = (Button) findViewById(R.id.order_evaluate);
        order_receipt = (Button) findViewById(R.id.order_receipt);

        order_del.setOnClickListener(this);
        order_contact.setOnClickListener(this);
        order_pay.setOnClickListener(this);
        order_evaluate.setOnClickListener(this);
        order_receipt.setOnClickListener(this);

        inquiry_btn_view.setVisibility(View.VISIBLE);
        order_pay.setVisibility(View.GONE);
        order_del.setVisibility(View.GONE);
        order_contact.setVisibility(View.GONE);
        order_evaluate.setVisibility(View.GONE);
        order_receipt.setVisibility(View.GONE);

        if (sta == 0) {//待付款
            order_pay.setVisibility(View.VISIBLE);
        } else if (sta == 1) {//待发货
            order_contact.setVisibility(View.VISIBLE);
        } else if (sta == 4) {//待收货
            order_receipt.setVisibility(View.VISIBLE);
            order_contact.setVisibility(View.VISIBLE);
        } else if (sta == 5) {//待评价
            order_evaluate.setVisibility(View.VISIBLE);
        }

    }

    //优惠券
    public void coupon(View view) {
        Intent intent = new Intent(context, CouponActivity.class);
        intent.putExtra("boolean", true);
        startActivityForResult(intent, CommonData.REQUEST_INQUIRY_COUPON);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    private PayUtil payUtil;
    private static final int SDK_PAY_FLAG = 1;

    public void pay_now(String pay_money) {
        payUtil = new PayUtil(mHandler);
        String countNum = orderid;//订单号
        payUtil.pay(MyOrderDetailPayActivity.this, pay_money + "", countNum);
//        finish();
    }


    //通知服务器 付款成功
    public void getData(final String url,String pay_money) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderid", orderid);
        params.put("real_price", pay_money);
        params.put("user_coupon_id", "0");

        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                Utils.showToastShort(context, getString(R.string.order_pay_success));
                setResult(RESULT_OK);
                finish();

            }

            @Override
            public void onResultJson(String json) {
                super.onResultJson(json);
            }
        });

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    Utils.showLog("msg.obj===" + msg.obj);
                    Utils.showLog("msg.resultObj===" + resultObj);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        getData(Constants.ORDER_PAY,real_money + "");
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
//                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;

                default:
                    break;
            }
        }

    };

}
