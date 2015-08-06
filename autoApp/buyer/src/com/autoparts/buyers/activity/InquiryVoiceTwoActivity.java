package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.RecordUtil;
import com.autoparts.buyers.utils.Utils;

import java.io.IOException;
import java.util.UUID;

/**
 * 询价-配件-捎句话
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquiryVoiceTwoActivity extends BaseActivity {
    private Context context;
    private static final int requestCode_year = 0;
    private LinearLayout voice_layout;
    private EditText message;

    private ImageView mDisplayVoicePlay;
    private ProgressBar mDisplayVoiceProgressBar;
    private TextView mDisplayVoiceTime;

    //    private ImageButton voive_image;
    private RelativeLayout mRecordLayout;
    private RelativeLayout pro_view;
    private ImageView mRecordVolume;
    private ImageView mRecordLight_1;
    private ImageView mRecordLight_2;
    private ImageView mRecordLight_3;
    private TextView mRecordTime;
    private ProgressBar mRecordProgressBar;

    private Animation mRecordLight_1_Animation;
    private Animation mRecordLight_2_Animation;
    private Animation mRecordLight_3_Animation;

    private MediaPlayer mMediaPlayer;
    private RecordUtil mRecordUtil;
    private static final int MAX_TIME = 30;// 最长录音时间 30秒
    private static final int MIN_TIME = 1;// 最短录音时间

    private static final int RECORD_NO = 0; // 不在录音
    private static final int RECORD_ING = 1; // 正在录音
    private static final int RECORD_ED = 2; // 完成录音
    private int mRecord_State = 0; // 录音的状态
    private float mRecord_Time;// 录音的时间
    private double mRecord_Volume;// 麦克风获取的音量值
    private boolean mPlayState; // 播放状态
    private int mPlayCurrentPosition;// 当前播放的时间
    private static final String PATH = "/sdcard/KaiXin/Record/";// 录音存储路径
    private String mRecordPath;// 录音的存储名称
    private int mMAXVolume;// 最大音量高度
    private int mMINVolume;// 最小音量高度


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inquiry_voice_new);
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.my_inquiry_voice));
        setLeftView("", R.drawable.icon_back);
        init();

    }

    private void init() {
        context = this;
        message = (EditText) findViewById(R.id.message);
        voice_layout = (LinearLayout) findViewById(R.id.voice_layout);
        mDisplayVoicePlay = (ImageView) findViewById(R.id.voice_display_voice_play);
        mDisplayVoiceProgressBar = (ProgressBar) findViewById(R.id.voice_display_voice_progressbar);
        mDisplayVoiceTime = (TextView) findViewById(R.id.voice_display_voice_time);
//        voive_image = (ImageButton) findViewById(R.id.inquiry_voice);
        mRecordLayout = (RelativeLayout) findViewById(R.id.voice_record_layout);
        pro_view = (RelativeLayout) findViewById(R.id.pro_view);
        mRecordVolume = (ImageView) findViewById(R.id.voice_recording_volume);
        mRecordLight_1 = (ImageView) findViewById(R.id.voice_recordinglight_1);
        mRecordLight_2 = (ImageView) findViewById(R.id.voice_recordinglight_2);
        mRecordLight_3 = (ImageView) findViewById(R.id.voice_recordinglight_3);
        mRecordTime = (TextView) findViewById(R.id.voice_record_time);
        mRecordProgressBar = (ProgressBar) findViewById(R.id.voice_record_progressbar);

        // 设置当前的最小声音和最大声音值
        mMINVolume = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4.5f, getResources()
                        .getDisplayMetrics());
        mMAXVolume = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 65f, getResources()
                        .getDisplayMetrics());
        getVoice();
        setListener();
    }

    public void getVoice() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String mess = bundle.getString(CommonData.INQUIRE_MES);
            message.setText(mess);
            mRecordPath = bundle.getString(CommonData.INQUIRE_VOICE);
            mRecord_Time = bundle.getFloat("time",0);

            if (!TextUtils.isEmpty(mRecordPath)) {
                // 录音成功,则显示录音成功后的界面
                voice_layout.setVisibility(View.VISIBLE);
                mDisplayVoicePlay.setImageResource(R.drawable.globle_player_btn_play);

                mDisplayVoiceProgressBar.setMax((int) mRecord_Time);
                mDisplayVoiceProgressBar.setProgress(0);
                mDisplayVoiceTime.setText((int) mRecord_Time + "″");
            }
        }
    }



    //确认
    public void commit(View view) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putFloat("time", mRecord_Time);
        bundle.putString(CommonData.INQUIRE_VOICE, mRecordPath);
        bundle.putString(CommonData.INQUIRE_MES, message.getText().toString());
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 监听事件
     */
    private void setListener() {
        mRecordLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    // 开始录音
                    case MotionEvent.ACTION_DOWN:
                        voice_layout.setVisibility(View.INVISIBLE);
                        pro_view.setVisibility(View.VISIBLE);
                        if (mRecord_State != RECORD_ING) {
                            // 开始动画效果
                            startRecordLightAnimation();

                            // 设置录音保存路径
                            mRecordPath = PATH + UUID.randomUUID().toString() + ".amr";
                            // 实例化录音工具类
                            mRecordUtil = new RecordUtil(mRecordPath);
                            try {
                                // 开始录音
                                mRecordUtil.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // 修改录音状态
                            mRecord_State = RECORD_ING;
                            new Thread(new Runnable() {
                                public void run() {
                                    // 初始化录音时间
                                    mRecord_Time = 0;
                                    while (mRecord_State == RECORD_ING) {
                                        // 大于最大录音时间则停止录音
                                        if (mRecord_Time >= MAX_TIME) {
                                            mRecordHandler.sendEmptyMessage(0);
                                        } else {
                                            try {
                                                // 每隔200毫秒就获取声音音量并更新界面显示
                                                Thread.sleep(200);
                                                mRecord_Time += 0.2;
                                                if (mRecord_State == RECORD_ING) {
                                                    mRecord_Volume = mRecordUtil.getAmplitude();
                                                    mRecordHandler.sendEmptyMessage(1);
                                                }
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }).start();
                        }
                        break;
                    // 停止录音
                    case MotionEvent.ACTION_UP:
                        pro_view.setVisibility(View.INVISIBLE);
                        if (mRecord_State == RECORD_ING) {
                            // 停止动画效果
                            stopRecordLightAnimation();
                            // 修改录音状态
                            mRecord_State = RECORD_ED;
                            try {
                                // 停止录音
                                mRecordUtil.stop();
                                // 初始录音音量
                                mRecord_Volume = 0;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // 如果录音时间小于最短时间
                            if (mRecord_Time <= MIN_TIME) {
                                // 显示提醒
                                Toast.makeText(context, "录音时间过短", Toast.LENGTH_SHORT).show();
                                // 修改录音状态
                                mRecord_State = RECORD_NO;
                                // 修改录音时间
                                mRecord_Time = 0;
                                // 修改显示界面
                                mRecordTime.setText("0″");
                                mRecordProgressBar.setProgress(0);
                                // 修改录音声音界面
                                ViewGroup.LayoutParams params = mRecordVolume
                                        .getLayoutParams();
                                params.height = 0;
                                mRecordVolume.setLayoutParams(params);
                            } else {
                                preferences.setRecordPath(mRecordPath);
                                // 录音成功,则显示录音成功后的界面
                                pro_view.setVisibility(View.INVISIBLE);
                                voice_layout.setVisibility(View.VISIBLE);
                                mDisplayVoicePlay.setImageResource(R.drawable.globle_player_btn_play);
                                mDisplayVoiceProgressBar.setMax((int) mRecord_Time);
                                mDisplayVoiceProgressBar.setProgress(0);
                                mDisplayVoiceTime.setText((int) mRecord_Time + "″");
                            }
                        }
                        break;
                }
                return false;
            }
        });
        mDisplayVoicePlay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
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
                        new Thread(new Runnable() {

                            public void run() {

                                mDisplayVoiceProgressBar
                                        .setMax((int) mRecord_Time);
                                mPlayCurrentPosition = 0;
                                while (mMediaPlayer.isPlaying()) {
                                    mPlayCurrentPosition = mMediaPlayer
                                            .getCurrentPosition() / 1000;
                                    mDisplayVoiceProgressBar
                                            .setProgress(mPlayCurrentPosition);
                                }
                            }
                        }).start();
                        // 修改播放状态
                        mPlayState = true;
                        // 修改播放图标
                        mDisplayVoicePlay
                                .setImageResource(R.drawable.globle_player_btn_stop);

                        mMediaPlayer
                                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    // 播放结束后调用
                                    public void onCompletion(MediaPlayer mp) {
                                        // 停止播放
                                        mMediaPlayer.stop();
                                        // 修改播放状态
                                        mPlayState = false;
                                        // 修改播放图标
                                        mDisplayVoicePlay
                                                .setImageResource(R.drawable.globle_player_btn_play);
                                        // 初始化播放数据
                                        mPlayCurrentPosition = 0;
                                        mDisplayVoiceProgressBar
                                                .setProgress(mPlayCurrentPosition);
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
                            mDisplayVoicePlay
                                    .setImageResource(R.drawable.globle_player_btn_play);
                            mPlayCurrentPosition = 0;
                            mDisplayVoiceProgressBar
                                    .setProgress(mPlayCurrentPosition);
                        } else {
                            mPlayState = false;
                            mDisplayVoicePlay
                                    .setImageResource(R.drawable.globle_player_btn_play);
                            mPlayCurrentPosition = 0;
                            mDisplayVoiceProgressBar
                                    .setProgress(mPlayCurrentPosition);
                        }
                    }
                }
            }
        });
    }

    /**
     * 用来控制动画效果
     */
    Handler mRecordLightHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mRecord_State == RECORD_ING) {
                        mRecordLight_1.setVisibility(View.VISIBLE);
                        mRecordLight_1_Animation = AnimationUtils.loadAnimation(
                                context, R.anim.voice_anim);
                        mRecordLight_1.setAnimation(mRecordLight_1_Animation);
                        mRecordLight_1_Animation.startNow();
                    }
                    break;

                case 1:
                    if (mRecord_State == RECORD_ING) {
                        mRecordLight_2.setVisibility(View.VISIBLE);
                        mRecordLight_2_Animation = AnimationUtils.loadAnimation(context, R.anim.voice_anim);
                        mRecordLight_2.setAnimation(mRecordLight_2_Animation);
                        mRecordLight_2_Animation.startNow();
                    }
                    break;
                case 2:
                    if (mRecord_State == RECORD_ING) {
                        mRecordLight_3.setVisibility(View.VISIBLE);
                        mRecordLight_3_Animation = AnimationUtils.loadAnimation(context, R.anim.voice_anim);
                        mRecordLight_3.setAnimation(mRecordLight_3_Animation);
                        mRecordLight_3_Animation.startNow();
                    }
                    break;
                case 3:
                    if (mRecordLight_1_Animation != null) {
                        mRecordLight_1.clearAnimation();
                        mRecordLight_1_Animation.cancel();
                        mRecordLight_1.setVisibility(View.VISIBLE);

                    }
                    if (mRecordLight_2_Animation != null) {
                        mRecordLight_2.clearAnimation();
                        mRecordLight_2_Animation.cancel();
                        mRecordLight_2.setVisibility(View.GONE);
                    }
                    if (mRecordLight_3_Animation != null) {
                        mRecordLight_3.clearAnimation();
                        mRecordLight_3_Animation.cancel();
                        mRecordLight_3.setVisibility(View.GONE);
                    }

                    break;
            }
        }
    };
    /**
     * 用来控制录音
     */
    Handler mRecordHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mRecord_State == RECORD_ING) {
                        // 停止动画效果
                        stopRecordLightAnimation();
                        // 修改录音状态
                        mRecord_State = RECORD_ED;
                        try {
                            // 停止录音
                            mRecordUtil.stop();
                            // 初始化录音音量
                            mRecord_Volume = 0;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // 根据录音修改界面显示内容
                        pro_view.setVisibility(View.INVISIBLE);
                        voice_layout.setVisibility(View.VISIBLE);
                        mDisplayVoicePlay.setImageResource(R.drawable.globle_player_btn_play);
                        mDisplayVoiceProgressBar.setMax((int) mRecord_Time);
                        mDisplayVoiceProgressBar.setProgress(0);
                        mDisplayVoiceTime.setText((int) mRecord_Time + "″");
                    }
                    break;

                case 1:
                    // 根据录音时间显示进度条
                    mRecordProgressBar.setProgress((int) mRecord_Time);
                    // 显示录音时间
                    mRecordTime.setText((int) mRecord_Time + "″");
                    // 根据录音声音大小显示效果
                    ViewGroup.LayoutParams params = mRecordVolume.getLayoutParams();
                    if (mRecord_Volume < 200.0) {
                        params.height = mMINVolume;
                    } else if (mRecord_Volume > 200.0 && mRecord_Volume < 400) {
                        params.height = mMINVolume * 2;
                    } else if (mRecord_Volume > 400.0 && mRecord_Volume < 800) {
                        params.height = mMINVolume * 3;
                    } else if (mRecord_Volume > 800.0 && mRecord_Volume < 1600) {
                        params.height = mMINVolume * 4;
                    } else if (mRecord_Volume > 1600.0 && mRecord_Volume < 3200) {
                        params.height = mMINVolume * 5;
                    } else if (mRecord_Volume > 3200.0 && mRecord_Volume < 5000) {
                        params.height = mMINVolume * 6;
                    } else if (mRecord_Volume > 5000.0 && mRecord_Volume < 7000) {
                        params.height = mMINVolume * 7;
                    } else if (mRecord_Volume > 7000.0 && mRecord_Volume < 10000.0) {
                        params.height = mMINVolume * 8;
                    } else if (mRecord_Volume > 10000.0 && mRecord_Volume < 14000.0) {
                        params.height = mMINVolume * 9;
                    } else if (mRecord_Volume > 14000.0 && mRecord_Volume < 17000.0) {
                        params.height = mMINVolume * 10;
                    } else if (mRecord_Volume > 17000.0 && mRecord_Volume < 20000.0) {
                        params.height = mMINVolume * 11;
                    } else if (mRecord_Volume > 20000.0 && mRecord_Volume < 24000.0) {
                        params.height = mMINVolume * 12;
                    } else if (mRecord_Volume > 24000.0 && mRecord_Volume < 28000.0) {
                        params.height = mMINVolume * 13;
                    } else if (mRecord_Volume > 28000.0) {
                        params.height = mMAXVolume;
                    }
                    mRecordVolume.setLayoutParams(params);
                    break;
            }
        }

    };

    /**
     * 开始动画效果
     */
    private void startRecordLightAnimation() {
        mRecordLightHandler.sendEmptyMessageDelayed(0, 0);
        mRecordLightHandler.sendEmptyMessageDelayed(1, 1000);
        mRecordLightHandler.sendEmptyMessageDelayed(2, 2000);
    }

    /**
     * 停止动画效果
     */
    private void stopRecordLightAnimation() {
        mRecordLightHandler.sendEmptyMessage(3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//听力测试结束
            switch (requestCode) {
                case requestCode_year:
//                    Utils.showToastShort(context,"11111");
                    break;
            }
        }
    }


}
