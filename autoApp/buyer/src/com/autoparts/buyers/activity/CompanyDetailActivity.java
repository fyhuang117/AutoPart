package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.CommonListAdapter;
import com.autoparts.buyers.network.JsonParserUtils;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.ImageLoaderBitmapUtil;
import com.autoparts.buyers.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 卖家详情页面
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class CompanyDetailActivity extends BaseActivity {
    private Context context;
    private String title;
    private TextView company_name, tel, address;
    private ImageView user_photo_profile;
    private RatingBar ratingbar1, ratingbar2, ratingbar3;
    private TextView ratingbar_score1, ratingbar_score2, ratingbar_score3;
    private ImageView company_level_image;
    private Map<String, String> sell_info;

    private List<RatingBar> ratingBars = new ArrayList<RatingBar>();
    private List<TextView> texts = new ArrayList<TextView>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.company_view);
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.company_title));
        init();

    }

    private void init() {
        context = this;
        sell_info = (Map<String, String>) getIntent().getSerializableExtra("sell_info");
        title = sell_info.get("nam");
        company_name = (TextView) findViewById(R.id.company_name);
        tel = (TextView) findViewById(R.id.tel);
        address = (TextView) findViewById(R.id.address);
        user_photo_profile = (ImageView) findViewById(R.id.user_photo_profile);
        company_level_image = (ImageView) findViewById(R.id.company_level_image);

        company_name.setText(title);


        tel.setText(sell_info.get("sell_tel"));
        address.setText(sell_info.get("sell_address"));

        String pic = sell_info.get("sell_pic");
        if (!TextUtils.isEmpty(pic)) {
            ImageLoaderBitmapUtil.getInstance(context).showBitmap(pic, user_photo_profile);
        }

        String sell_level = sell_info.get("sell_level");
        int level = CommonData.getInstance(context).getIntData(sell_level);
        if (level>15){
            level = 15;
        }
        company_level_image.setImageResource(CommonData.images[level-1]);


        ratingbar1 = (RatingBar) findViewById(R.id.ratingbar1);
        ratingbar2 = (RatingBar) findViewById(R.id.ratingbar2);
        ratingbar3 = (RatingBar) findViewById(R.id.ratingbar3);
        ratingBars.add(ratingbar1);
        ratingBars.add(ratingbar2);
        ratingBars.add(ratingbar3);


        ratingbar_score1 = (TextView) findViewById(R.id.ratingbar_score1);
        ratingbar_score2 = (TextView) findViewById(R.id.ratingbar_score2);
        ratingbar_score3 = (TextView) findViewById(R.id.ratingbar_score3);
        texts.add(ratingbar_score1);
        texts.add(ratingbar_score2);
        texts.add(ratingbar_score3);

        String sell_score = (String) sell_info.get("sell_score");
        String[] scores = JsonParserUtils.jsonParseStringArray(sell_score);

        if (scores != null && sell_info.size() > 0) {
            for (int i = 0; i < scores.length; i++) {
                float score = Float.parseFloat(scores[i]);
                RatingBar ratingBar = ratingBars.get(i);
                ratingBar.setRating(score);
                TextView textView = texts.get(i);
                textView.setText(score + "分");
            }
        }

    }

    public void tel(View view) {
        Utils.call(context, sell_info.get("sell_tel"));
    }


}
