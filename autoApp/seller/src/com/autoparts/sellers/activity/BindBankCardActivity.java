package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.utils.Constants;
import com.autoparts.sellers.utils.DialogUtil;
import com.autoparts.sellers.utils.Utils;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * 绑定银行卡
 * Created by:haoming
 * Created time:15-4-29
 */
public class BindBankCardActivity extends BaseActivity {

    private Context context;
    private TextView bank_select_city, bank_card_select;//选择城市，选择银行
    private EditText bank_card_account, bank_card_username, bank_money;//银行账号，开户姓名，提现金额
    private String money = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.bind_bank_card_view);
        super.onCreate(savedInstanceState);
        context = this;
        initView();
    }

    private void initView() {
        setTitle("提现");

        bank_card_select = (TextView) findViewById(R.id.bank_card_select);
        bank_select_city = (TextView) findViewById(R.id.bank_select_city);

        bank_card_account = (EditText) findViewById(R.id.bank_card_account);
        bank_card_username = (EditText) findViewById(R.id.bank_card_username);
        bank_money = (EditText) findViewById(R.id.bank_money);

        money = getIntent().getStringExtra(CommonData.INQUIRE_NAME);
        bank_money.setHint("可提现金额" + money + "元");

    }

    public void commit(View view) {
        bindBankInfo();

    }

    private void bindBankInfo() {
        final String bank = bank_card_select.getText().toString().trim();
        final String city = bank_select_city.getText().toString().trim();
        final String account = bank_card_account.getText().toString().trim();
        final String userName = bank_card_username.getText().toString().trim();
        final String money = bank_money.getText().toString().trim();
        if (TextUtils.isEmpty(bank)) {
            Utils.showToastShort(context, "请先选择银行");
        } else if (TextUtils.isEmpty(city)) {
            Utils.showToastShort(context, "请先选择开户行所在地");
        } else if (TextUtils.isEmpty(account)) {
            Utils.showToastShort(context, "请先输入银行账号");
        } else if (TextUtils.isEmpty(userName)) {
            Utils.showToastShort(context, "请先输入开户姓名");
        } else if (TextUtils.isEmpty(money)) {
            Utils.showToastShort(context, "请先输入提现金额");
        } else {
            showProgressDialog();
            String url = Constants.USER_WALLET_GET;
            //获取订单摘要
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("bank_name", bank);
            params.put("bank_address", city);
            params.put("bank_no", account);
            params.put("bank_user", userName);
            params.put("price", money);

            HttpClientUtils.post(context, url, params, new HttpResultHandler() {
                @Override
                public void onResultFail(String message, int statusCode) {
                    super.onResultFail(message, statusCode);
                    if (statusCode == 404) {
                        Utils.showToastShort(context, "申请数额超过余额，请重新输入");
                    }
                }

                @Override
                public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                    super.onResultSuccess(headers, response, message, statusCode);
                    Utils.showToastShort(context, "提现申请已提交，系统将在15个工作日内处理您的申请");
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    disProgressDialog();
                }
            });
        }
    }

    public void local(View view) {
        Intent intent = new Intent(context, SelectItemActivity.class);
        intent.putExtra("title_id", SelectItemActivity.ProvincesTitle);
        startActivityForResult(intent, SelectItemActivity.ProvincesTitle);
    }

    public void bank(View view) {
        Intent intent = new Intent(context, SelectItemActivity.class);
        intent.putExtra("title_id", SelectItemActivity.BankTitle);
        startActivityForResult(intent, SelectItemActivity.BankTitle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SelectItemActivity.ProvincesTitle) {
            bank_select_city.setText(data.getStringExtra("returnStr"));
        }
        if (resultCode == SelectItemActivity.BankTitle) {
            bank_card_select.setText(data.getStringExtra("returnStr"));
        }
    }

}
