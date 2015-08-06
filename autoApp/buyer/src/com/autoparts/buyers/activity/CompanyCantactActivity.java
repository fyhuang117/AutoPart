package com.autoparts.buyers.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.utils.Utils;

/**
 * 卖家详情页面
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class CompanyCantactActivity extends BaseActivity {
    private Context context;
    private String title;
    private TextView company_name, tel, address;
    private ImageView user_photo_profile;
    private String buyer;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.company_cantact);
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.company_title));
        init();

    }

    private void init() {
        context = this;
        company_name = (TextView) findViewById(R.id.company_name);
        tel = (TextView) findViewById(R.id.tel);
        address = (TextView) findViewById(R.id.address);

        buyer = getIntent().getStringExtra("sel");
        String[] s = buyer.split(",");
        if (s != null && s.length == 3) {
            company_name.setText(s[0]);
            tel.setText(s[1]);
            address.setText(s[2]);
        }


    }

    public void tel(View view) {
        Utils.call(context, tel.getText().toString().trim());
    }


}
