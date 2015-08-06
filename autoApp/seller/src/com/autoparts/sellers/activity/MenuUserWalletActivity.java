package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.utils.Constants;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的钱包
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MenuUserWalletActivity extends BaseActivity {
    private Context context;
    private TextView wallet_money, wallet_money_will;
    private TextView wallet_money_all, wallet_money_get;
    private TextView detail;
    private String balance = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.menu_seller_wallet);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        context = this;
        String title = getString(R.string.seller_menu_wallet);
        setTitle(title);

        wallet_money = (TextView) findViewById(R.id.wallet_money);
        wallet_money_will = (TextView) findViewById(R.id.wallet_money_will);
        wallet_money_all = (TextView) findViewById(R.id.wallet_money_all);
        wallet_money_get = (TextView) findViewById(R.id.wallet_money_get);
        detail = (TextView) findViewById(R.id.detail);
        detail.setOnClickListener(this);
        getData(Constants.USER_WALLET);

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.detail:
                Intent intent = new Intent(context, UserWalletDetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    //获取订单摘要
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
        Map<String, String> map = responseModel.getMap();
        balance = map.get("balance");//"balance":"余额(float)",
        String pri = map.get("pri");//pri":"即将到账(float)"
        String in = map.get("in");//in":"累计收入(float)
        String out = map.get("out");//out":"累计提现(float)

        wallet_money.setText(balance);
        wallet_money_will.setText("即将到账" + pri + "元");
        wallet_money_all.setText(in);
        wallet_money_get.setText(out);

    }


    public void commit(View view) {
        Intent intent = new Intent(context, BindBankCardActivity.class);
        intent.putExtra(CommonData.INQUIRE_NAME, balance);
        startActivityForResult(intent,CommonData.REQUEST_MONEY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CommonData.REQUEST_MONEY:
                    getData(Constants.USER_WALLET);
                    break;
            }
        }
    }

}
