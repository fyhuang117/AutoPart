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
import com.autoparts.buyers.adapter.CommonListAdapter;
import com.autoparts.buyers.adapter.GridAdapter;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import com.autoparts.buyers.view.MyGridView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 填写订单
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class OrderWriteActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    String title = "";
    private TextView order_discount;

    private LinearLayout order_no_view;
    private TextView order_no,order_time;

    private TextView order_buyer_name,order_phone,order_address;

    private TextView inquiry_title, inquiry_name, inquiry_message;
    private TextView inquiry_money;
    private TextView inquiry_text_pay_style, inquiry_text_send_style;//支付方式,送货方式

    private ImageView video_recorder_play_btn;
    private MediaPlayer mMediaPlayer;
    private boolean mPlayState; // 播放状态
    private String company;

    private Map<String, String> maps;
    private MyGridView mGridView;
    private GridAdapter mGridadapter;
    private List<String> mGridPhotos;

    private LinearLayout other_info;
    private TextView brand, quality, num, location;

    private String mRecordPath;
    private String fileName = Constants.cacheDir + "/download.amr";//音频下载路径
    private String downloadUrl = "";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.order_write);
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        setTitle(title);
        init();

    }

    private void init() {
        context = this;
        order_discount = (TextView) findViewById(R.id.order_discount);
        setViewData();
    }

    //下单
    public void order(View view) {
        Intent intent = new Intent(context, PayAlipayActivity.class);
        startActivity(intent);
    }

    //优惠券
    public void coupon(View view) {
        Intent intent = new Intent(context, CouponActivity.class);
        intent.putExtra("boolean", true);
        startActivityForResult(intent, CommonData.REQUEST_INQUIRY_COUPON);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CommonData.REQUEST_INQUIRY_COUPON:
                    String coupon = data.getStringExtra("data");
                    order_discount.setText("￥" + coupon);
                    break;
            }
        }
    }


    public void setViewData() {
        maps = (Map<String, String>) getIntent().getSerializableExtra(CommonData.MAPS);

        inquiry_title = (TextView) findViewById(R.id.inquiry_title);
        inquiry_name = (TextView) findViewById(R.id.inquiry_name);
        inquiry_money = (TextView) findViewById(R.id.inquiry_money);
        inquiry_message = (TextView) findViewById(R.id.inquiry_message);

        //订单号 状态等 隐藏
        order_no_view = (LinearLayout) findViewById(R.id.order_no_view);
        order_no_view.setVisibility(View.GONE);
        order_buyer_name = (TextView) findViewById(R.id.order_buyer_name);
        order_phone = (TextView) findViewById(R.id.order_phone);
        order_address = (TextView) findViewById(R.id.order_address);

        order_buyer_name.setText(maps.get("nam"));
        order_phone.setText(maps.get("sell_tel"));
        order_address.setText(maps.get("sell_address"));

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

        inquiry_name.setText(maps.get("car"));

        //支付方式
        int pay = CommonData.getInstance(context).getIntData(maps.get("pay"));
        String pay_style = CommonData.getInstance(context).getPayStyle(pay);
        inquiry_text_pay_style.setText(pay_style);

        //送货方式
        String pof = maps.get("pof");
        String del = maps.get("del");
        String str = CommonData.getInstance(context).getDel(del, pof);
        inquiry_text_send_style.setText(str);


        String mes = maps.get("mes");
        String aum = maps.get("aum");
        message(mes, aum);
    }

    public void setItemClick() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonData.getInstance(context).showImage(context, mGridPhotos, position);
            }
        });
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
        video_recorder_play_btn = (ImageView) findViewById(R.id.video_recorder_play_btn);
        video_recorder_play_btn.setOnClickListener(this);

        if (TextUtils.isEmpty(aum)) {
            video_recorder_play_btn.setVisibility(View.GONE);
        } else {
            video_recorder_play_btn.setVisibility(View.VISIBLE);
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

}
