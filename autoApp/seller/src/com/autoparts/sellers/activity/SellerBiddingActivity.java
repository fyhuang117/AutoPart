package com.autoparts.sellers.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.JsonParserUtils;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.utils.*;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 询价单---报价
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class SellerBiddingActivity extends BaseActivity {

    private Context context;
    private TextView bid_zhibao, bid_brand, bid_quality, bid_xianhuo, bid_pay, bid_send;
    private EditText bid_money;

    private ImageView qua_image, brand_image;

    private Dialog photoDialog;
    private ImageView photo1, photo2, photo3;
    private ImageView delete1, delete2, delete3;
    private List<ImageView> photos = new ArrayList<ImageView>();
    private List<ImageView> deletes = new ArrayList<ImageView>();
    private Map<Integer, String> file_path = new HashMap<Integer, String>();
    private String[] pics = {"pic1", "pic2", "pic3"};

    private CameraUtils cameraUtils;
    private int photoPosition = 0;
    private String sortName;
    private int position = 0;

    private TextView inquiry_add, inquiry_minus, bid_num;
    private EditText inquiry_num;

    private HashMap<String, Object> maps;
    private LinearLayout photo_select_view;

    private Bundle bundleVoice = null;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.seller_bidding);
        super.onCreate(savedInstanceState);
        String title = getString(R.string.bid_text_title);
        setTitle(title);
        setLeftView("", R.drawable.icon_back);
        init();
        initPhotoDialog();
        cameraUtils = new CameraUtils(SellerBiddingActivity.this);
    }

    private void init() {
        context = this;
        maps = (HashMap<String, Object>) getIntent().getSerializableExtra(CommonData.MAPS);
        bid_money = (EditText) findViewById(R.id.bid_money);
        bid_zhibao = (TextView) findViewById(R.id.bid_zhibao);
        bid_brand = (TextView) findViewById(R.id.bid_brand);
        bid_quality = (TextView) findViewById(R.id.bid_quality);
        bid_xianhuo = (TextView) findViewById(R.id.bid_xianhuo);
        bid_pay = (TextView) findViewById(R.id.bid_pay);
        bid_send = (TextView) findViewById(R.id.bid_send);

        qua_image = (ImageView) findViewById(R.id.qua_image);
        brand_image = (ImageView) findViewById(R.id.brand_image);
        qua_image.setVisibility(View.GONE);
        brand_image.setVisibility(View.GONE);

        String ban = (String) maps.get("ban");
        if (ban != null) {
            String[] bans = JsonParserUtils.jsonParseStringArray(ban);
            StringBuffer strB = new StringBuffer();
            for (int i = 0; i < bans.length; i++) {
                String s = bans[i].replace("\"", "");
                strB.append(s + " ");
            }
            bid_brand.setText(strB.toString());
        }else {
            bid_brand.setText(getString(R.string.inquiry_brand_nul));
        }

        int qua = Integer.parseInt((String) maps.get("parttype_quality"));
        bid_quality.setText(CommonData.getInstance(context).getQualityStyle(qua));

        photo_select_view = (LinearLayout) findViewById(R.id.photo_select_view);
        photo_select_view.setVisibility(View.VISIBLE);
        photo1 = (ImageView) findViewById(R.id.photo1);
        photo2 = (ImageView) findViewById(R.id.photo2);
        photo3 = (ImageView) findViewById(R.id.photo3);
        photo1.setOnClickListener(this);
        photo2.setOnClickListener(this);
        photo3.setOnClickListener(this);
        photos.add(photo1);
        photos.add(photo2);
        photos.add(photo3);

        delete1 = (ImageView) findViewById(R.id.delete1);
        delete2 = (ImageView) findViewById(R.id.delete2);
        delete3 = (ImageView) findViewById(R.id.delete3);
        delete1.setOnClickListener(this);
        delete2.setOnClickListener(this);
        delete3.setOnClickListener(this);
        deletes.add(delete1);
        deletes.add(delete2);
        deletes.add(delete3);

        inquiry_add = (TextView) findViewById(R.id.inquiry_add);
        inquiry_num = (EditText) findViewById(R.id.inquiry_num);
        inquiry_minus = (TextView) findViewById(R.id.inquiry_minus);
        bid_num = (TextView) findViewById(R.id.bid_num);

        inquiry_add.setOnClickListener(this);
        inquiry_minus.setOnClickListener(this);
//        inquiry_num.setText((String) maps.get("c"));
        inquiry_add.setVisibility(View.GONE);
        inquiry_minus.setVisibility(View.GONE);
        inquiry_num.setVisibility(View.GONE);

        bid_num.setText((String) maps.get("c"));

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.photo1:
                photoDialog.show();
                photoPosition = 0;
                break;
            case R.id.photo2:
                photoDialog.show();
                photoPosition = 1;
                break;
            case R.id.photo3:
                photoDialog.show();
                photoPosition = 2;
                break;
            case R.id.photo_camera:
                cameraUtils.camera();
                dimissDiaog();
                break;
            case R.id.photo_gallery:
                cameraUtils.gallery();
                dimissDiaog();
                break;
            case R.id.delete1:
                delete1.setVisibility(View.INVISIBLE);
                photos.get(0).setImageResource(R.drawable.icon_photo_upload_nor);
                if (file_path.containsKey(0)) {
                    file_path.remove(0);
                }
                break;
            case R.id.delete2:
                delete2.setVisibility(View.INVISIBLE);
                photos.get(1).setImageResource(R.drawable.icon_photo_upload_nor);
                if (file_path.containsKey(1)) {
                    file_path.remove(1);
                }
                break;
            case R.id.delete3:
                delete3.setVisibility(View.INVISIBLE);
                photos.get(2).setImageResource(R.drawable.icon_photo_upload_nor);
                if (file_path.containsKey(2)) {
                    file_path.remove(2);
                }
                break;
            case R.id.inquiry_minus:
                setNum(0);
                break;
            case R.id.inquiry_add:
                setNum(1);
                break;
        }
    }

    //质保
    public void zhibao(View view) {
        position = 0;
        Intent intent = new Intent(context, CommonListActivity.class);
        intent.putExtra("title", "质保");
        intent.putExtra("position", position);
        startActivityForResult(intent, CommonData.REQUEST_BIDDING);
    }

    //品牌
    public void brand(View view) {
//        Intent intent = new Intent(context, SelectModeActivity.class);
//        intent.putExtra("title", "配件品牌");
//        startActivityForResult(intent, CommonData.REQUEST_INQUIRY_BRAND);
    }

    //品质
    public void quality(View view) {
//        position = 4;
//        Intent intent = new Intent(context, CommonListActivity.class);
//        intent.putExtra("title", "品质");
//        intent.putExtra("position", position);
//        startActivityForResult(intent, CommonData.REQUEST_BIDDING);
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

    //发货方式
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

    //配件分类
    public void sort(View view) {
        Intent intent = null;
        if (true) {
            intent = new Intent(context, InquirySortActivity.class);
        } else {
            intent = new Intent(context, InquirySortConActivity.class);
        }

        startActivityForResult(intent, CommonData.REQUEST_CODE_SORT);
    }

    //捎句话
    public void voice(View view) {
        Intent intent = new Intent(context, InquiryVoiceTwoActivity.class);
        if (bundleVoice != null)
            intent.putExtras(bundleVoice);
        startActivityForResult(intent, CommonData.REQUEST_INQUIRY_VOICE);
    }

    //询价提交
    public void commit(View view) {
        getData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//听力测试结束
            switch (requestCode) {
                case CommonData.REQUEST_BIDDING:
                    String str = data.getStringExtra("name");
                    String money = data.getStringExtra("money");
                    String id = data.getStringExtra("id");
                    int posi = data.getIntExtra("position", 0);

                    switch (posi) {
                        case 0:
                            bid_zhibao.setText(str);
                            qau = str;
                            break;
                        case 1:
                            bid_xianhuo.setText(str);
                            avi = id;
                            break;
                        case 2://付款方式
                            bid_pay.setText(str);
                            pay = id;
                            bid_send.setText("");
                            pof = "";
                            del = "";
                            break;
                        case 3:
                            bid_send.setText(str);
                            pof = money;
                            del = str;
                            if (!del.contains("自提")) {
                                bid_send.append("/邮费￥" + pof);
                            }
                            break;
                        case 4:
                            bid_quality.setText(str);
                            break;
                    }
                    break;
                case CommonData.REQUEST_INQUIRY_BRAND:
                    bid_brand.setText(data.getStringExtra("name"));
                    break;
                case CommonData.PHOTO_REQUEST_TAKEPHOTO:
                    setPicToView(cameraUtils.getTempFile().getPath());
                    break;
                case CommonData.PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        setPicToView(picturePath);
                    }
                    break;
                case CommonData.REQUEST_INQUIRY_VOICE:
                    bundleVoice = data.getExtras();
                    if (bundleVoice != null) {
                        String voice_path = bundleVoice.getString(CommonData.INQUIRE_VOICE);
                        if (!TextUtils.isEmpty(voice_path)) {
                            File file = new File(voice_path);
                            if (file.exists()) {
                                byte[] bytes = BitmapUtil.getByteArrayFromFile(voice_path);
                                if (bytes != null && bytes.length > 0) {
                                    String voice = BitmapUtil.byte2hex(bytes);

                                }
                            }

                        }
                    }
                    break;
            }
        }
    }

    private void initPhotoDialog() {
        photoDialog = DialogUtil.createDialog(this, R.layout.photo_select_dialog, R.style.CustomDialog);
        photoDialog.setCancelable(true);
        TextView photo_dialog_title = (TextView) photoDialog.findViewById(R.id.regist_dialog_title);
        TextView photo_camera = (TextView) photoDialog.findViewById(R.id.photo_camera);
        TextView photo_gallery = (TextView) photoDialog.findViewById(R.id.photo_gallery);
        photo_camera.setOnClickListener(this);
        photo_gallery.setOnClickListener(this);

        DialogUtil.setDialogParams(this, photoDialog, R.dimen.dialog_width_margin);
    }

    public void dimissDiaog() {
        if (photoDialog != null && photoDialog.isShowing()) {
            photoDialog.dismiss();
        }
    }

    private void setPicToView(String picturePath) {
        String picUrl = ImageDownloader.Scheme.FILE.wrap(picturePath);
        imageLoaderUtil.showBitmap(picUrl, photos.get(photoPosition));
        deletes.get(photoPosition).setVisibility(View.VISIBLE);
        file_path.put(photoPosition, picturePath);

    }

    public void setNum(int state) {
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

    private String avi = "";//是否现货
    private String pay = "";//付款方式
    private String del = "";//快递
    private String pof = "0";//邮费
    private String qau = "";//质保

    public void getData() {
//        int c = 1;
//        if (bundleOther != null) {
//            c = bundleOther.g etInt(CommonData.INQUIRE_NUM, 1);
//        }
        String mes = "";
        if (bundleVoice != null) {
            mes = bundleVoice.getString(CommonData.INQUIRE_MES);
        }
        String money = bid_money.getText().toString();
        if (TextUtils.isEmpty(money)) {
            Utils.showToastShort(context, "请输入报价金额");
        } else if (money.startsWith(".")) {
            Utils.showToastShort(context, "请正确输入金额");
        } else if (TextUtils.isEmpty(avi)) {
            Utils.showToastShort(context, "请选择是否现货");
        } else if (TextUtils.isEmpty(pay)) {
            Utils.showToastShort(context, "请选择付费方式");
        } else if (TextUtils.isEmpty(del)) {
            Utils.showToastShort(context, "请选择送货方式");
        } else {
            showProgressDialog();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("inquiryid", maps.get("inquiryid"));//配件分类id
            params.put("pri", money);//
            params.put("partbandid", "");//
            int qua = Integer.parseInt((String) maps.get("parttype_quality"));
            params.put("parttype_quality", qua);//商圈
            String c = bid_num.getText().toString();
            params.put("c", c);//数量

            params.put("avi", Integer.parseInt(avi));//是否现货
            params.put("pay", Integer.parseInt(pay));// 付款方式
            params.put("qau", qau);// 质保
            params.put("del", del);// 送货方式
            params.put("pof", pof);//邮费
            params.put("pic1", "");
            params.put("pic2", "");
            params.put("pic3", "");
            params.put("aum", "");
            params.put("mes", mes);
            setFile(params);
        }

    }

    public void setFile(final Map<String, Object> params) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (file_path != null && file_path.size() > 0) {
                    for (Map.Entry<Integer, String> entry : file_path.entrySet()) {
                        int key = entry.getKey();
                        String value = entry.getValue();
                        Utils.showLog(key, value);
                        if (!TextUtils.isEmpty(value)) {
                            params.put(pics[key], getPhoto(value));
                        }
                    }
                }
//
                String voice_path = "";
                if (bundleVoice != null) {
                    voice_path = bundleVoice.getString(CommonData.INQUIRE_VOICE);
                }

                if (!TextUtils.isEmpty(voice_path)) {
                    File file = new File(voice_path);
                    if (file.exists()) {
                        byte[] bytes = BitmapUtil.getByteArrayFromFile(voice_path);
                        if (bytes != null && bytes.length > 0) {
                            String voice = BitmapUtil.byte2hex(bytes);
                            params.put("aum", voice);
                        }
                    }

                }
                Message message = new Message();
                message.obj = params;
                handler.sendMessage(message);
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Map<String, Object> params = (Map<String, Object>) msg.obj;
            String url = Constants.INQUIRE_SAVE;
            HttpClientUtils.post(context, url, params, new HttpResultHandler() {
                @Override
                public void onResultFail(String message, int statusCode) {
                    super.onResultFail(message, statusCode);
                }

                @Override
                public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                    super.onResultSuccess(headers, response, message, statusCode);
                    Utils.showToastShort(context, "竞价成功");
                    finish();

                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    disProgressDialog();
                }
            });
        }
    };

    public String getPhoto(String filePath) {
        String pic = "";
        File file = new File(filePath);
        if (file != null) {
            pic = BitmapUtil.byte2hex(BitmapUtil.compressBitmapToByte(BitmapUtil.decodeFile(file)));
            Utils.showLog("pic==" + pic);
        }
        return pic;
    }


}
