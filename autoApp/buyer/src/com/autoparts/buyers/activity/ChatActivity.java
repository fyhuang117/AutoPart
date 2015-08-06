package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.CommonListAdapter;
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
 * 在线咨询
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class ChatActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private CommonListAdapter adapter;
    private ListView mListView;
    String title = "";
    private String orderid;
    private int sta;//订单状态

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);

        title = getIntent().getStringExtra("title");
        sta = getIntent().getIntExtra(CommonData.ORDER_STATE, 0);
        orderid = getIntent().getStringExtra(CommonData.INQUIRE_ID);
        setTitle(title);
        String rightText = "下单";
        if (sta == 1) {
            rightText = "发货";
        }
        setRightView(rightText, -1);
        init();

    }

    private void init() {
        context = this;
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.topBar_right_layout:
                if (sta == 1) {
                    showDialog("确定发货", true);
                } else {
                    Intent intent = new Intent(context, OrderWriteActivity.class);
                    intent.putExtra("title", getString(R.string.order_text_write));
                    startActivity(intent);
                }
                break;
            case R.id.confirm_ok:
                getData(Constants.ORDER_SEND);
                break;

        }
    }


    //发货
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
                Utils.showToastShort(context, getString(R.string.order_send_success));
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onResultJson(String json) {
                super.onResultJson(json);
            }
        });

    }

}
