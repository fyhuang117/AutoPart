package com.autoparts.buyers.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.autoparts.buyers.R;
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
 * 评价
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class UserEvaluateActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    String title = "";
    private String orderid;

    private RatingBar ratingbar1,ratingbar2,ratingbar3;
    private TextView ratingbar_score1,ratingbar_score2,ratingbar_score3;
    private float match = 0,service = 0,speed = 0;
    private int score = 1;//1-好评2-中评3-差评
    private TextView comment_text;

    private TextView evaluate_select1,evaluate_select2,evaluate_select3;

//    {
//        "id":"用户标识",
//            "orderid":"订单标识(int)",
//            "score":"打分(int )",
//            "comments":"评价留言",
//            "match":"匹配度(int 1-5)",
//            "service":"服务态度(int 1-5)",
//            "speed":"发货速度(int 1-5)"
//    }


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
        orderid = getIntent().getStringExtra(CommonData.INQUIRE_ID);

        ratingbar1 = (RatingBar) findViewById(R.id.ratingbar1);
        ratingbar2 = (RatingBar) findViewById(R.id.ratingbar2);
        ratingbar3 = (RatingBar) findViewById(R.id.ratingbar3);

        ratingbar_score1 = (TextView) findViewById(R.id.ratingbar_score1);
        ratingbar_score2 = (TextView) findViewById(R.id.ratingbar_score2);
        ratingbar_score3 = (TextView) findViewById(R.id.ratingbar_score3);
        ratingbar_score1.setText("0分");
        ratingbar_score2.setText("0分");
        ratingbar_score3.setText("0分");

        evaluate_select1 = (TextView) findViewById(R.id.evaluate_select1);
        evaluate_select2 = (TextView) findViewById(R.id.evaluate_select2);
        evaluate_select3 = (TextView) findViewById(R.id.evaluate_select3);
        evaluate_select1.setOnClickListener(this);
        evaluate_select2.setOnClickListener(this);
        evaluate_select3.setOnClickListener(this);
        evaluate_select1.setSelected(true);

        comment_text = (TextView) findViewById(R.id.comment_text);

        ratingbar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                match = rating;
                ratingbar_score1.setText(rating + "分");
            }
        });

        ratingbar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                service = rating;
                ratingbar_score2.setText(rating + "分");
            }
        });

        ratingbar3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                speed = rating;
                ratingbar_score3.setText(rating+"分");
            }
        });

    }

    public void commit(View view) {
        String commment = comment_text.getText().toString();
        if (match == 0 || service == 0 || speed == 0){
            Utils.showToastShort(context,"请先添加评分");
        }else if (TextUtils.isEmpty(commment)){
            Utils.showToastShort(context,"请输入评价内容");
        }else {
            showDialog(getString(R.string.order_evaluate_hint), true);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.evaluate_select1:
                score = 1;
                evaluate_select1.setSelected(true);
                evaluate_select2.setSelected(false);
                evaluate_select3.setSelected(false);
                break;
            case R.id.evaluate_select2:
                score = 2;
                evaluate_select1.setSelected(false);
                evaluate_select2.setSelected(true);
                evaluate_select3.setSelected(false);
                break;
            case R.id.evaluate_select3:
                score = 3;
                evaluate_select1.setSelected(false);
                evaluate_select2.setSelected(false);
                evaluate_select3.setSelected(true);
                break;
            case R.id.confirm_ok:
                getData(Constants.ORDER_SCORE);
                break;
        }
    }


    //获取订单摘要
    public void getData(final String url) {
        String commment = comment_text.getText().toString();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderid", orderid);
        params.put("score", score);
        params.put("comments", commment);
        params.put("match", match);
        params.put("service", service);
        params.put("speed", speed);
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                Utils.showToastShort(context, getString(R.string.order_score_success));
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
