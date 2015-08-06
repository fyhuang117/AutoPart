package com.autoparts.buyers.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.autoparts.buyers.R;
import com.autoparts.buyers.alipay.PayUtil;
import com.autoparts.buyers.alipay.Result;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝支付
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class PayAlipayActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private String title = "";
    private String orderid,pay_money;
    private PayUtil payUtil;
    private static final int SDK_PAY_FLAG = 1;
    private float price = 0.1f;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.alipay_pay);
        super.onCreate(savedInstanceState);
        title = getString(R.string.pay_title);
        setTitle(title);
        init();

    }

    public void pay_later(View view) {
        finish();
//        application.goHome();
    }

    private void init() {
        context = this;
        orderid = getIntent().getStringExtra(CommonData.INQUIRE_ID);
        pay_money = getIntent().getStringExtra("money");
        payUtil = new PayUtil(mHandler);

    }

    public void pay_now(View view) {
        float money = 0.1f;//交易金额
        String countNum = orderid;//订单号
        payUtil.pay(PayAlipayActivity.this, pay_money + "", countNum);
//        finish();
    }


    //通知服务器 付款成功
    public void getData(final String url) {
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
                        getData(Constants.ORDER_PAY);
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
