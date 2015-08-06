package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.CommonListAdapter;
import com.autoparts.buyers.adapter.MenuCouponAdapter;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 现金优惠券
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class CouponActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    String title = "";
    private Vector<HashMap<String, Object>> mList;
    private MenuCouponAdapter adapter;
    private ListView mListView;
    private LinearLayout data_null_view;

    private boolean isClick = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);
        title = "优惠券";
        setTitle(title);
        init();

    }

    private void init() {
        context = this;
        mListView = (ListView) findViewById(R.id.mListView);

        mListView.setDivider(null);
        int divider = (int) getResources().getDimension(R.dimen.common_margin);
        mListView.setDividerHeight(divider);
        mList = new Vector<HashMap<String, Object>>();
        adapter = new MenuCouponAdapter(context, mList);
        mListView.setAdapter(adapter);
        isClick = getIntent().getBooleanExtra("boolean", false);
        if (isClick) {
            mListView.setOnItemClickListener(this);
        }
        data_null_view = (LinearLayout) findViewById(R.id.data_null_view);
        getData();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.putExtra(CommonData.INQUIRE_NAME, (String)mList.get(i).get("price"));
        intent.putExtra(CommonData.INQUIRE_NUM, (String)mList.get(i).get("need_min"));
        setResult(RESULT_OK, intent);
        finish();

    }


    //获取优惠券
    public void getData() {
        String url = Constants.USER_COUPON;
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
        mList = responseModel.getMaps();
        if (mList != null && mList.size() > 0) {
            adapter.setData(mList);
            setDataNull(false);
        } else {
            setDataNull(true);
        }
    }

    public void setDataNull(boolean b) {
        if (b) {
            if (data_null_view != null) {
                data_null_view.setVisibility(View.VISIBLE);
            }
            mListView.setVisibility(View.GONE);

        } else {
            mListView.setVisibility(View.VISIBLE);
            if (data_null_view != null) {
                data_null_view.setVisibility(View.GONE);
            }
        }
    }
}
