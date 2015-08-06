package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.autoparts.sellers.utils.BitmapUtil;
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
import java.util.*;

/**
 * 询价-详细列表-商家报价详情
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class SellerBiddingDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private MyInquiryListAdapter adapter;
    private TextView inquiry_title, inquiry_name, inquiry_message;
    private TextView detail_quote_money;

    String title = "";
    private LinearLayout inquiry_order_view, inquiry_seller_view;
    private Button inquiry_order_btn, inquiry_seller_btn;

    private TextView inquiry_text_pay_style, inquiry_text_send_style;//支付方式,送货方式

    private ImageView video_recorder_play_btn;
    private MediaPlayer mMediaPlayer;
    private boolean mPlayState; // 播放状态
    private String company;

    private HashMap<String, Object> maps;
    private Map<String, String> sell_info;
    private MyGridView mGridView;
    private GridAdapter mGridadapter;
    private List<String> mGridPhotos;

    private LinearLayout other_info;
    private Button save_btn;

    private TextView bid_brand, bid_quality, bid_zhibao,
            bid_xianhuo, inquiry_add, inquiry_minus, bid_num;
    private EditText inquiry_num;

    private String mRecordPath;
    private String fileName = Constants.cacheDir + "/download.amr";//音频下载路径
    private String downloadUrl = "";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.seller_bidding_detail);
        super.onCreate(savedInstanceState);
        title = getString(R.string.inquiry_text_detail);
        setTitle(title);
        setRightView("取消竞价", -1);
        init();

    }

    private void init() {
        context = this;
        findViewById(R.id.bid_money_view).setVisibility(View.GONE);
        findViewById(R.id.bid_money_line).setVisibility(View.GONE);
        findViewById(R.id.detail_online_view).setVisibility(View.GONE);
        save_btn = (Button) findViewById(R.id.save_btn);
        save_btn.setVisibility(View.GONE);

        bid_brand = (TextView) findViewById(R.id.bid_brand);//品牌
        bid_quality = (TextView) findViewById(R.id.bid_quality);//品质
        bid_zhibao = (TextView) findViewById(R.id.bid_zhibao);//质保
        bid_xianhuo = (TextView) findViewById(R.id.bid_xianhuo);//现货
        inquiry_num = (EditText) findViewById(R.id.inquiry_num);//数量
        inquiry_add = (TextView) findViewById(R.id.inquiry_add);
        inquiry_minus = (TextView) findViewById(R.id.inquiry_minus);
        inquiry_minus.setOnClickListener(this);
        inquiry_add.setOnClickListener(this);
        bid_num = (TextView) findViewById(R.id.bid_num);
        setViewData();
    }

    public void setViewData() {
        maps = (HashMap<String, Object>) getIntent().getSerializableExtra(CommonData.MAPS);
        inquiry_title = (TextView) findViewById(R.id.inquiry_title);
        inquiry_name = (TextView) findViewById(R.id.inquiry_name);
        detail_quote_money = (TextView) findViewById(R.id.detail_quote_money);
        inquiry_message = (TextView) findViewById(R.id.inquiry_message);

        inquiry_text_pay_style = (TextView) findViewById(R.id.inquiry_text_pay_style);
        inquiry_text_send_style = (TextView) findViewById(R.id.inquiry_text_send_style);

        detail_quote_money.setText("￥" + maps.get("pri"));

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
        String ban = (String) maps.get("ban");
        if (!TextUtils.isEmpty(ban) && ban.length() > 3) {
            String[] bans = JsonParserUtils.jsonParseStringArray(ban);
            if (bans != null && bans.length > 0) {
                String s = bans[0].replace("\"", "");
                bid_brand.setText(s);
            }
        }
        String qau = (String) maps.get("qau");//质保
        bid_zhibao.setText(qau);

        String avi = CommonData.getInstance(context).getAviStyle(Integer.parseInt((String) maps.get("avi")));
        bid_xianhuo.setText(avi);

        //支付方式
        int payStyle = CommonData.getInstance(context).getIntData((String) maps.get("pay"));
        String pay_style = CommonData.getInstance(context).getPayStyle(payStyle);
        inquiry_text_pay_style.setText(pay_style);
        pay = payStyle + "";

        //送货方式
        String del = (String) maps.get("del");
        String pof = (String) maps.get("pof");
        inquiry_text_send_style.setText(del);
        if (!del.contains("自提")) {
            inquiry_text_send_style.append("/邮费￥" + pof);
        }

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
//        inquiry_add.setVisibility(View.GONE);
//        inquiry_minus.setVisibility(View.GONE);
//        inquiry_num.setVisibility(View.GONE);
        bid_num.setVisibility(View.GONE);
        inquiry_num.setText(map.get("c"));

        String quality = CommonData.getInstance(context).getQualityStyle(Integer.parseInt(map.get("parttype_quality")));
        bid_quality.setText(quality);

        inquiry_name.setText(map.get("car"));

        String ban = (String) map.get("ban");
        if (!TextUtils.isEmpty(ban) && ban.length() > 3) {
            String[] bans = JsonParserUtils.jsonParseStringArray(ban);
            if (bans != null && bans.length > 0) {
                String s = bans[0].replace("\"", "");
                bid_brand.setText(s);
            }
        }
        String qau = (String) map.get("qau");//质保
        bid_zhibao.setText(qau);

        String avi = CommonData.getInstance(context).getAviStyle(Integer.parseInt((String) map.get("avi")));
        bid_xianhuo.setText(avi);

        //支付方式
        int payStyle = CommonData.getInstance(context).getIntData((String) map.get("pay"));
        String pay_style = CommonData.getInstance(context).getPayStyle(payStyle);
        inquiry_text_pay_style.setText(pay_style);

        pay = payStyle + "";
        //送货方式
        del = (String) map.get("del");
        pof = (String) map.get("pof");
        inquiry_text_send_style.setText(del);
        if (!del.contains("自提")) {
            inquiry_text_send_style.append("/邮费￥" + pof);
        }

        parttypeid = map.get("parttypeid");

    }

    //在线咨询
    public void detail_online(View view) {
        Intent intent = new Intent(context, ChatActivity.class);
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
            case R.id.inquiry_minus:
                setNum(0);
                break;
            case R.id.inquiry_add:
                setNum(1);
                break;
            case R.id.topBar_right_layout:
                showDialog("是否取消询价？", true);

                break;
            case R.id.confirm_ok:
                getData(Constants.INQUIRE_BIDDING_DEL);
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
                    setResult(RESULT_OK);
                    finish();
                } else if (url.equals(Constants.INQUIRE_BIDDING_DEL)) {
                    Utils.showToastShort(context, "竞价单已取消");
                    setResult(RESULT_OK);
                    finish();
                } else {
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
        sell_info = map;
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
        video_recorder_play_btn = (ImageView) findViewById(R.id.video_recorder_play_btn);
        video_recorder_play_btn.setOnClickListener(this);

        if (TextUtils.isEmpty(aum)) {
            video_recorder_play_btn.setVisibility(View.GONE);
        } else {
            video_recorder_play_btn.setVisibility(View.VISIBLE);
        }
    }

    public void commit(View view) {
        update();
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

    public void setNum(int state) {
        save_btn.setVisibility(View.VISIBLE);
        int num = Integer.parseInt(inquiry_num.getText().toString());
        if (state == 0) {//减去
            if (num == 1) {
                num = 1;
            } else {
                num--;
            }
        } else if (state == 1) {//加上
            num++;
        }
        inquiry_num.setText(num + "");
    }

    private String parttypeid = "";//配件品牌
    private String avi = "";//是否现货
    private String pay = "";//付款方式
    private String del = "";//快递
    private String pof = "0";//邮费
    private String qau = "";//质保
    private String qua = "";//品质
    private int position = 0;

    //质保
    public void zhibao(View view) {
        position = 0;
        Intent intent = new Intent(context, CommonListActivity.class);
        intent.putExtra("title", "质保");
        intent.putExtra("position", position);
        startActivityForResult(intent, CommonData.REQUEST_BIDDING);
    }

    //是否现货
    public void xianhuo(View view) {
        position = 1;
        Intent intent = new Intent(context, CommonListActivity.class);
        intent.putExtra("title", "现货");
        intent.putExtra("position", position);
        startActivityForResult(intent, CommonData.REQUEST_BIDDING);
    }

    //付款方式
    public void pay(View view) {
        position = 2;
        Intent intent = new Intent(context, CommonListActivity.class);
        intent.putExtra("title", "付款方式");
        intent.putExtra("position", position);
        startActivityForResult(intent, CommonData.REQUEST_BIDDING);

    }

    public void send(View view) {
        if (TextUtils.isEmpty(pay)) {
            Utils.showToastShort(context, "请先选择付款方式");
        } else {
            position = 3;
            Intent intent = new Intent(context, CommonListActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("title", "发货方式");
            intent.putExtra("pay", pay);
            startActivityForResult(intent, CommonData.REQUEST_BIDDING);
        }
    }

    //品质
    public void quality(View view) {
        position = 4;
        Intent intent = new Intent(context, CommonListActivity.class);
        intent.putExtra("title", "品质");
        intent.putExtra("position", position);
        startActivityForResult(intent, CommonData.REQUEST_BIDDING);
    }

    //品牌
    public void brand(View view) {
        if (TextUtils.isEmpty(parttypeid)) {
            Utils.showToastShort(context, "请先选择配件分类");
        } else {
            Intent intent = new Intent(context, SelectModeActivity.class);
            intent.putExtra("title", "配件品牌");
            intent.putExtra(CommonData.INQUIRE_ID, parttypeid);//"parttypeid":"配件类型标识(int)"
            startActivityForResult(intent, CommonData.REQUEST_INQUIRY_BRAND);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//听力测试结束
            String str = data.getStringExtra("name");
            String money = data.getStringExtra("money");
            String id = data.getStringExtra("id");
            int posi = data.getIntExtra("position", 0);
            switch (requestCode) {
                case CommonData.REQUEST_BIDDING:
                    save_btn.setVisibility(View.VISIBLE);
                    switch (posi) {
                        case 0:
                            bid_zhibao.setText(str);
                            qau = str;
                            break;
                        case 1:
                            bid_xianhuo.setText(str);
                            avi = id;
                            break;
                        case 2:
                            inquiry_text_pay_style.setText(str);
                            pay = id;
                            inquiry_text_send_style.setText("");
                            pof = "";
                            del = "";
                            break;
                        case 3:
                            inquiry_text_send_style.setText(str);
                            pof = money;
                            del = str;
                            if (!del.contains("自提")) {
                                inquiry_text_send_style.append("/邮费￥" + pof);
                            }
                            break;
                        case 4:
                            bid_quality.setText(str);
                            qua = id;
                            break;
                    }
                    break;
                case CommonData.REQUEST_INQUIRY_BRAND:
                    parttypeid = data.getStringExtra(CommonData.INQUIRE_ID);
                    bid_brand.setText(data.getStringExtra(CommonData.INQUIRE_NAME));
                    break;
            }
        }
    }

    public void update() {
        showProgressDialog();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("biddingid", maps.get("biddingid"));//配件分类id
        params.put("pri", "");//
        params.put("partbandid", "");//配件品牌
        params.put("parttype_quality", qua);//品质
        String c = inquiry_num.getText().toString();
        params.put("c", c);//数量

        params.put("avi", avi);//是否现货
        params.put("pay", pay);// 付款方式
        params.put("qau", qau);// 质保
        params.put("del", del);// 送货方式
        params.put("pof", pof);//邮费
        params.put("pic1", "");
        params.put("pic2", "");
        params.put("pic3", "");
        params.put("aum", "");
        params.put("mes", "");

        String url = Constants.ORDER_BIDDING_Update;
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                Utils.showToastShort(context, "修改成功");
                save_btn.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                disProgressDialog();
            }
        });
    }


}
