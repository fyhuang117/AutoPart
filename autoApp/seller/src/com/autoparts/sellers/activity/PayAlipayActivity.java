package com.autoparts.sellers.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.utils.Utils;

/**
 * 支付宝支付
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class PayAlipayActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    String title = "";

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
        application.goHome();
    }

    private void init() {
        context = this;

    }

    public void pay_now(View view) {
        finish();
        Utils.showToastShort(context, "支付成功");
        application.goHome();
//        finish();
    }

}
