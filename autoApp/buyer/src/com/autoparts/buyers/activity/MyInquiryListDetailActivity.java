package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.GridAdapter;
import com.autoparts.buyers.adapter.MyInquiryListAdapter;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.JsonParserUtils;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import com.autoparts.buyers.view.MyGridView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import org.apache.http.Header;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 询价-详细列表-商家报价详情
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MyInquiryListDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private MyInquiryListAdapter adapter;
    private TextView detail_quote_name, inquiry_title, inquiry_name, inquiry_message;
    private TextView inquiry_money;
    private TextView location_text;
    private LinearLayout xianhuo_view;
    String title = "";
    private LinearLayout inquiry_order_view, inquiry_seller_view;
    private Button inquiry_order_btn, inquiry_seller_btn;

    private TextView inquiry_text_pay_style, inquiry_text_send_style;//支付方式,送货方式

    private ImageView video_recorder_play_btn;
    private MediaPlayer mMediaPlayer;
    private boolean mPlayState; // 播放状态
    private String company;

    private HashMap<String, Object> maps;
    private Map<String, String> details;
    private Map<String, String> sell_info;
    private MyGridView mGridView;
    private GridAdapter mGridadapter;
    private List<String> mGridPhotos;

    private LinearLayout other_info;
    private TextView brand, quality, num, location, xianhuo;

    private String mRecordPath;
    private String fileName = Constants.cacheDir + "/download.amr";//音频下载路径
    private String downloadUrl = "";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.my_inquiryt_seller_detail);
        super.onCreate(savedInstanceState);
        title = getString(R.string.inquiry_text_detail);
        setTitle(title);
        init();

    }

    private void init() {
        context = this;
        xianhuo_view = (LinearLayout) findViewById(R.id.xianhuo_view);
        xianhuo_view.setVisibility(View.VISIBLE);

        location_text = (TextView) findViewById(R.id.location_text);
        location_text.setText("商圈");
        detail_quote_name = (TextView) findViewById(R.id.detail_quote_name);
        setViewData();
    }

    public void setViewData() {
        maps = (HashMap<String, Object>) getIntent().getSerializableExtra(CommonData.MAPS);
        detail_quote_name.setText((String) maps.get("nam"));
        inquiry_title = (TextView) findViewById(R.id.inquiry_title);
        inquiry_name = (TextView) findViewById(R.id.inquiry_name);
        inquiry_money = (TextView) findViewById(R.id.inquiry_money);
        inquiry_message = (TextView) findViewById(R.id.inquiry_message);

        inquiry_text_pay_style = (TextView) findViewById(R.id.inquiry_text_pay_style);
        inquiry_text_send_style = (TextView) findViewById(R.id.inquiry_text_send_style);

        inquiry_money.setText("￥" + maps.get("pri"));

        String par = getIntent().getStringExtra(CommonData.INQUIRE_NAME);
        inquiry_title.setText(par);

        mGridView = (MyGridView) findViewById(R.id.mGridView);
        mGridPhotos = new ArrayList<String>();
        mGridadapter = new GridAdapter(context, mGridPhotos);
        mGridView.setAdapter(mGridadapter);

        String pic1 = (String) maps.get("pic1");
        String pic2 = (String) maps.get("pic2");
        String pic3 = (String) maps.get("pic3");

        if (!TextUtils.isEmpty(pic1)) {
            mGridPhotos.add(pic1);
        }
        if (!TextUtils.isEmpty(pic2)) {
            mGridPhotos.add(pic2);
        }
        if (!TextUtils.isEmpty(pic3)) {
            mGridPhotos.add(pic3);
        }
        setItemClick();
        mGridadapter.setData(mGridPhotos);
        getData(Constants.ORDER_BIDDING);

    }

    public void setItemClick() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonData.getInstance(context).showImage(context, mGridPhotos, position);
            }
        });
    }

    public void setOtherInfo(Map<String, String> map) {
        other_info = (LinearLayout) findViewById(R.id.other_info);
//        other_info.setVisibility(View.GONE);
        brand = (TextView) findViewById(R.id.brand);
        quality = (TextView) findViewById(R.id.quality);
        num = (TextView) findViewById(R.id.num);
        location = (TextView) findViewById(R.id.location);
        xianhuo = (TextView) findViewById(R.id.xianhuo);
        String[] ban = JsonParserUtils.jsonParseStringArray(map.get("ban"));
        if (ban != null && ban.length > 0) {
            String s = ban[0].replace("\"", "");
            if (!TextUtils.isEmpty(s)) {
                brand.setText(s);
            }
        }

        int qua = Integer.parseInt(map.get("parttype_quality"));
        quality.setText(CommonData.getInstance(context).getQualityStyle(qua));
        num.setText(map.get("c"));

        location.setText(map.get("business_area"));
        String avi = map.get("avi");
        int state = 1;
        if (!TextUtils.isEmpty(avi)) {
            state = Integer.parseInt(avi);
        }
        if (state > 1) {
            state = 1;
        }
        String[] avis_text = getResources().getStringArray(R.array.avis_text);
        xianhuo.setText(avis_text[state]);
    }

    //在线咨询
    public void detail_online(View view) {
//        Intent intent = new Intent(context, ChatActivity.class);
//        startActivity(intent);
        String sel = sell_info.get("nam") + "," + sell_info.get("sell_tel") + "," + sell_info.get("sell_address");
        Intent intent = new Intent(context, CompanyCantactActivity.class);
        intent.putExtra("sel", sel);
        startActivity(intent);
    }

    //下单
    public void detail_order(View view) {
//        Intent intent = new Intent(context, OrderWriteActivity.class);
//        intent.putExtra("title", getString(R.string.order_text_write));
//        intent.putExtra(CommonData.INQUIRE_NAME, inquiry_title.getText().toString());
//        intent.putExtra(CommonData.MAPS, (java.io.Serializable) details);
//        startActivity(intent);

        getData(Constants.ORDER_SAVE_ORDER);


    }

    //商家简介
    public void company_detail(View view) {
        Intent intent = new Intent(context, CompanyDetailActivity.class);
        intent.putExtra("sell_info", (java.io.Serializable) sell_info);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.video_recorder_play_btn:
//                startPlay();
                Utils.showLog("fileName==" + fileName);
                File file = new File(mRecordPath);
                if (file.exists()) {
                    Utils.showLog("mRecordPath==" + mRecordPath);
                    startPlay();
                } else {
                    downloadFile();
                }
                break;
            case R.id.inquiry_order_btn:
                break;
            case R.id.inquiry_seller_btn:
                break;
        }
    }

    public void startPlay() {
        // 播放录音
        if (!mPlayState) {
            mMediaPlayer = new MediaPlayer();
            try {
                // 添加录音的路径
                mMediaPlayer.setDataSource(mRecordPath);
                // 准备
                mMediaPlayer.prepare();
                // 播放
                mMediaPlayer.start();
                // 根据时间修改界面
                // 修改播放状态
                mPlayState = true;
                // 修改播放图标
                video_recorder_play_btn.setImageResource(R.drawable.video_recorder_stop_btn);

                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    // 播放结束后调用
                    public void onCompletion(MediaPlayer mp) {
                        // 停止播放
                        mMediaPlayer.stop();
                        // 修改播放状态
                        mPlayState = false;
                        // 修改播放图标
                        video_recorder_play_btn.setImageResource(R.drawable.video_recorder_play_btn);
                        // 初始化播放数据
//                        mPlayCurrentPosition = 0;
//                        mDisplayVoiceProgressBar.setProgress(mPlayCurrentPosition);
                    }
                });

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (mMediaPlayer != null) {
                // 根据播放状态修改显示内容
                if (mMediaPlayer.isPlaying()) {
                    mPlayState = false;
                    mMediaPlayer.stop();
                    video_recorder_play_btn.setImageResource(R.drawable.video_recorder_play_btn);//                    mPlayCurrentPosition = 0;
//                    mDisplayVoiceProgressBar.setProgress(mPlayCurrentPosition);
                } else {
                    mPlayState = false;
                    video_recorder_play_btn.setImageResource(R.drawable.video_recorder_stop_btn);//                    mPlayCurrentPosition = 0;
//                    mDisplayVoiceProgressBar.setProgress(mPlayCurrentPosition);
                }
            }
        }
    }

    //取消订单,查询询价单对应的竞价单
    public void getData(final String url) {
        if (url.equals(Constants.ORDER_SAVE_ORDER)){
            loadingDialog.setCancelable(false);
            showProgressDialog();
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("biddingid", maps.get("biddingid"));
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (url.equals(Constants.ORDER_SAVE_ORDER)) {
                    Utils.showToastShort(context, getString(R.string.order_save_success));
//                    application.goHome();
                    String orderid = response.getMap().get("orderid");
                    //跳转到订单详情页面
                    application.goHome();

                    Intent intent = new Intent(context, MyOrderDetailPayActivity.class);
                    intent.putExtra(CommonData.INQUIRE_ID, orderid);
                    startActivity(intent);

                } else {
                    details = response.getMap();
                    setData(details);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (url.equals(Constants.ORDER_SAVE_ORDER)){
                    showProgressDialog();
                }
            }

        });

    }


    public void setData(Map<String, String> map) {
        sell_info = map;
        inquiry_name.setText(map.get("car"));

        //支付方式
        int pay = CommonData.getInstance(context).getIntData(map.get("pay"));
        String pay_style = CommonData.getInstance(context).getPayStyle(pay);
        inquiry_text_pay_style.setText(pay_style);

        //送货方式
        String pof = map.get("pof");
        String del = map.get("del");
        String str = CommonData.getInstance(context).getDel(del, pof);
        inquiry_text_send_style.setText(str);


        String mes = map.get("mes");
        String aum = map.get("aum");
        message(mes, aum);

        setOtherInfo(map);

    }

    public void message(String mes, String aum) {
        if (TextUtils.isEmpty(mes)) {
            inquiry_message.setText(getString(R.string.message_text_null));
        } else {
            inquiry_message.setText(mes);
        }
        downloadUrl = aum;
        fileName = Constants.cacheDir + "/" + downloadUrl.hashCode() + ".amr";
        mRecordPath = Environment.getExternalStorageDirectory() + fileName;
        LinearLayout voice_view = (LinearLayout) findViewById(R.id.voice_view);
        video_recorder_play_btn = (ImageView) findViewById(R.id.video_recorder_play_btn);
        video_recorder_play_btn.setOnClickListener(this);

        if (TextUtils.isEmpty(aum)) {
            voice_view.setVisibility(View.GONE);
        } else {
            voice_view.setVisibility(View.VISIBLE);
        }
    }


    public void downloadFile() {
        HttpUtils http = new HttpUtils();
        Utils.showLog("downloadUrl==" + downloadUrl);
        String target = mRecordPath;
        HttpHandler handler = http.download(downloadUrl, target,
                true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
//                        Utils.showToastShort(context,"音频下载成功");
                        Utils.showLog("onSuccess==");
                        startPlay();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Utils.showToastShort(context, "音频获取失败");
                    }
                });
//调用cancel()方法停止下载
//        handler.cancel();
    }


}
