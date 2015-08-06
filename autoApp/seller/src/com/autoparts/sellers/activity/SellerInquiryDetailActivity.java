package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.GridAdapter;
import com.autoparts.sellers.adapter.MyInquiryListAdapter;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.JsonParserUtils;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.utils.Constants;
import com.autoparts.sellers.utils.Utils;
import com.autoparts.sellers.view.MyGridView;
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
 * 我的订单--我的询价单详情
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class SellerInquiryDetailActivity extends BaseActivity {
    private Context context;
    private MyInquiryListAdapter adapter;
    private TextView inquiry_title, inquiry_name, inquiry_num, inquiry_brand,inquiry_area;
    private TextView inquiry_message, inquiry_quality, inquiry_time;
    String title = "";
    String parts = "";
    private LinearLayout inquiry_order_view, inquiry_seller_view;
    private Button inquiry_order_btn, inquiry_seller_btn;

    private ImageView video_recorder_play_btn;
    private MediaPlayer mMediaPlayer;
    private boolean mPlayState; // 播放状态
    private HashMap<String, Object> maps;


    private MyGridView mGridView;
    private GridAdapter mGridadapter;
    private List<String> mGridPhotos;

    private String mRecordPath;
    private String fileName = Constants.cacheDir + "/download.amr";//音频下载路径
    private String downloadUrl = "";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.seller_inquiryt_detail);
        super.onCreate(savedInstanceState);
        title = getString(R.string.inquiry_text_order);
        setTitle(title);
        init();
    }

    private void init() {
        context = this;
        setViewData();
    }

    public void setViewData() {
        maps = (HashMap<String, Object>) getIntent().getSerializableExtra(CommonData.MAPS);
        inquiry_title = (TextView) findViewById(R.id.inquiry_title);
        inquiry_name = (TextView) findViewById(R.id.inquiry_name);
        inquiry_num = (TextView) findViewById(R.id.inquiry_num);
        inquiry_brand = (TextView) findViewById(R.id.inquiry_brand);
        inquiry_message = (TextView) findViewById(R.id.inquiry_message);
        inquiry_quality = (TextView) findViewById(R.id.inquiry_quality);
        inquiry_area = (TextView) findViewById(R.id.inquiry_area);
        inquiry_time = (TextView) findViewById(R.id.inquiry_time);

        String par = (String) maps.get("par");
        String car = (String) maps.get("car");
        if (TextUtils.isEmpty(par)) {
            par = context.getString(R.string.inquiry_parts_null);
        }
        if (TextUtils.isEmpty(car)) {
            car = context.getString(R.string.inquiry_car_null);
        }

        inquiry_title.setText(par);
        inquiry_name.setText(car);

        inquiry_num.setText((String) maps.get("c"));
        String ban = (String) maps.get("ban");
        if (ban != null) {
            String[] bans = JsonParserUtils.jsonParseStringArray(ban);
            StringBuffer strB = new StringBuffer();
            for (int i = 0; i < bans.length; i++) {
                String s = bans[i].replace("\"", "");
                strB.append(s + " ");
            }
            inquiry_brand.setText(strB.toString());
        }else {
            inquiry_brand.setText(getString(R.string.inquiry_brand_nul));
        }

        String begin_time = (String) maps.get("begin_time");
        String end_time = (String) maps.get("end_time");
        String time = CommonData.getInstance(context).getTime(begin_time, end_time);
        inquiry_time.setText(time);

        int qua = Integer.parseInt((String) maps.get("parttype_quality"));
        inquiry_quality.setText(CommonData.getInstance(context).getQualityStyle(qua));
        String area = (String) maps.get("business_area");//商圈
        inquiry_area.setText(area);

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

        mGridadapter.setData(mGridPhotos);
        setItemClick();
        getData(Constants.INQUIRE_DETAIL);
    }

    public void setItemClick() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonData.getInstance(context).showImage(context, mGridPhotos, position);
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.video_recorder_play_btn:
                play();
                break;
        }
    }

    public void play() {
        File file = new File(mRecordPath);
        if (file.exists()) {
            startPlay();
        } else {
            downloadFile();
        }
    }

    //报价
    public void commit(View view) {
        if (!preferences.getIsLogin()){
            Utils.showToastShort(context,"请先登录");
            Intent intent = new Intent(context,UserLoginActivity.class);
            intent.putExtra("isLogin", true);
            startActivity(intent);
        }else if (preferences.getUserState() != 1){
            CommonData.getInstance(context).showUserState(preferences.getUserState());
        }else {
            Intent intent = new Intent(context, SellerBiddingActivity.class);
            intent.putExtra(CommonData.MAPS, maps);
            startActivity(intent);
            finish();
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

    //获取询价单详情
    public void getData(final String url) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("inquiryid", maps.get("inquiryid"));
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (url.equals(Constants.INQUIRE_DETAIL)) {
                    setData(response);
                }
            }

            @Override
            public void onResultJson(String json) {
                super.onResultJson(json);
            }
        });
    }

    public void setData(ResponseModel responseModel) {
        Map<String, String> map = responseModel.getMap();
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

    }

    public void setOtherInfo(Map<String, String> map) {
//        other_info = (LinearLayout) findViewById(R.id.other_info);
////        other_info.setVisibility(View.GONE);
//        brand = (TextView) findViewById(R.id.brand);
//        quality = (TextView) findViewById(R.id.quality);
//        num = (TextView) findViewById(R.id.num);
//        location = (TextView) findViewById(R.id.location);
//        String[] ban = JsonParserUtils.jsonParseStringArray(map.get("ban"));
//        if (ban!= null && ban.length>0) {
//            brand.setText(ban[0]);
//        }
//        int qua = Integer.parseInt(map.get("parttype_quality"));
//        quality.setText(CommonData.getInstance(context).getQualityStyle(qua));
//        num.setText(map.get("c"));
//
//        location.setText(map.get("business_area"));
    }


}
