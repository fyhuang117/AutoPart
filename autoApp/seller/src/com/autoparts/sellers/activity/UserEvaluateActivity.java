package com.autoparts.sellers.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.utils.Utils;

/**
 * 评价
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class UserEvaluateActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    String title = "";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.user_evaluate);
        super.onCreate(savedInstanceState);
        title = getString(R.string.order_text_evaluate);
        setTitle(title);
        init();

    }

    private void init() {
        context = this;

    }

    public void commit(View view) {
        finish();
        Utils.showToastShort(context, "评价成功");

    }

}
