package com.autoparts.sellers.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
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
 * 用户资料
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class SellerUserInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    String title = "";
    private Dialog photoDialog;
    private int photoPosition = 0;//position 图片位置
    private ImageView user_photo1, user_photo2, user_photo3, user_photo4, user_photo_profile;
    private List<ImageView> photos = new ArrayList<ImageView>();
    private CameraUtils cameraUtils;
    private String updateFile;

    private RatingBar ratingbar1, ratingbar2, ratingbar3;
    private TextView ratingbar_score1, ratingbar_score2, ratingbar_score3;

    private TextView address, tel, user_title, user_state, user_success;
    private List<RatingBar> ratingBars = new ArrayList<RatingBar>();
    private List<TextView> texts = new ArrayList<TextView>();

    private ImageView company_level_image;
    private int userState = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.user_info);
        super.onCreate(savedInstanceState);
        title = getString(R.string.user_info);
        setTitle(title);
        init();
        initPhotoDialog();

        if (preferences.getIsLogin()) {
            //获取用户信息
            getUserInfoData();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.photo_camera:
                cameraUtils.camera();
                dimissDiaog();
                break;
            case R.id.photo_gallery:
                cameraUtils.gallery();
                dimissDiaog();
                break;
            case R.id.user_photo_profile:
                photoDialog.show();
                photoPosition = 3;
                break;
            case R.id.topBar_right_layout:
                if (userState == 3) {
                    Utils.showToastShort(context, getString(R.string.user_info_commit));
                } else {
                    getData();
                }
                break;
        }
    }


    private void init() {
        context = this;
        cameraUtils = new CameraUtils(SellerUserInfoActivity.this);
        user_photo1 = (ImageView) findViewById(R.id.user_photo1);
        user_photo2 = (ImageView) findViewById(R.id.user_photo2);
        user_photo3 = (ImageView) findViewById(R.id.user_photo3);
        user_photo4 = (ImageView) findViewById(R.id.user_photo4);
        user_photo_profile = (ImageView) findViewById(R.id.user_photo_profile);
        user_photo_profile.setOnClickListener(this);
        photos.add(user_photo1);
        photos.add(user_photo2);
        photos.add(user_photo3);
        photos.add(user_photo_profile);
        photos.add(user_photo4);

        tel = (TextView) findViewById(R.id.user_tel);
        address = (TextView) findViewById(R.id.user_address);
        user_title = (TextView) findViewById(R.id.user_title);
        user_state = (TextView) findViewById(R.id.user_state);
        user_success = (TextView) findViewById(R.id.user_success);

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

        company_level_image = (ImageView) findViewById(R.id.company_level_image);


        setUserState();

        setViewData();
    }

    private void setViewData() {
//        "id":"用户标识",
//                "nam":"用户名称",
//                "adr":"地址信息",
//                "ton":"经度"，
//        "lat":"纬度",
//                "lp1":"身份证正面16进制的图片数据",
//                "lp2":"身份证背面16进制的图片数据",
//                "pic":"其他图片16进制的图片数据",
//                "license":"营业执照图片16进制的图片数据",
//                "agent":"代理图片16进制的图片数据"


        tel.setText(preferences.getLoginPhone());
        String adr = preferences.getStringData("adr");
        String nam = preferences.getStringData("nam");
        if (!TextUtils.isEmpty(adr)) {
            address.setText(adr);
        }

        if (!TextUtils.isEmpty(nam)) {
            user_title.setText(nam);
        }
        String pic = preferences.getStringData("pic");
        if (!TextUtils.isEmpty(pic)) {
            ImageLoaderBitmapUtil.getInstance(context).showBitmap(pic, photos.get(3));
        }

        String license = preferences.getStringData("license");
        if (!TextUtils.isEmpty(license)) {
            ImageLoaderBitmapUtil.getInstance(context).showBitmap(license, photos.get(0));
        }
        String lp1 = preferences.getStringData("lp1");
        if (!TextUtils.isEmpty(lp1)) {
            ImageLoaderBitmapUtil.getInstance(context).showBitmap(lp1, photos.get(1));
        }
        String agent = preferences.getStringData("agent");
        if (!TextUtils.isEmpty(agent)) {
            ImageLoaderBitmapUtil.getInstance(context).showBitmap(agent, photos.get(2));
        }
        String lp2 = preferences.getStringData("lp2");
        if (!TextUtils.isEmpty(lp2)) {
            ImageLoaderBitmapUtil.getInstance(context).showBitmap(lp2, photos.get(4));
        }

        String sell_level = (String) preferences.getStringData("sell_level");//信用等级
        int level = CommonData.getInstance(context).getIntData(sell_level);
        if (level > 15) {
            level = 15;
        }
        company_level_image.setImageResource(CommonData.images[level - 1]);

        String sell_score = preferences.getStringData("sell_score");
        String[] scores = JsonParserUtils.jsonParseStringArray(sell_score);
        if (scores != null && scores.length > 0) {
            for (int i = 0; i < scores.length; i++) {
                float score = Float.parseFloat(scores[i]);
                RatingBar ratingBar = ratingBars.get(i);
                ratingBar.setRating(score);
                TextView textView = texts.get(i);
                textView.setText(score + "分");
            }
        }


    }

    public void upload_photo1(View view) {
        if (isUpdate()) {
            photoDialog.show();
            photoPosition = 0;
        }
    }

    public void upload_photo2(View view) {
        if (isUpdate()) {
            photoDialog.show();
            photoPosition = 1;
        }
    }

    public void upload_photo3(View view) {
        if (isUpdate()) {
            photoDialog.show();
            photoPosition = 2;
        }
    }

    public void upload_photo4(View view) {
        if (isUpdate()) {
            photoDialog.show();
            photoPosition = 4;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//听力测试结束
            switch (requestCode) {
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
                case CommonData.REQUEST_USER_TEL:
                    String strTel = data.getStringExtra("content");
                    tel.setText(strTel);
                    break;
                case CommonData.REQUEST_USER_ADDRESS:
                    String content = data.getStringExtra("content");
                    address.setText(content);
                    getData(Constants.USER_INFO);
                    break;
                case CommonData.REQUEST_USER_NAME:
                    String title = data.getStringExtra("content");
                    user_title.setText(title);
                    getData(Constants.USER_INFO);
                    break;

            }
        }
    }

    private void setPicToView(String picturePath) {
        updateFile = picturePath;
        String picUrl = ImageDownloader.Scheme.FILE.wrap(picturePath);
        imageLoaderUtil.showBitmap(picUrl, photos.get(photoPosition));
        updatePhoto();
    }

    public void tel(View view) {
        Intent intent = new Intent(context, UserInfoEditActivity.class);
        intent.putExtra("title", "电话");
        intent.putExtra("content", tel.getText().toString());
        startActivityForResult(intent, CommonData.REQUEST_USER_TEL);
    }

    public void address(View view) {
        Intent intent = new Intent(context, UserInfoEditActivity.class);
        intent.putExtra("title", "位置");
        intent.putExtra("position", 1);
        intent.putExtra("content", address.getText().toString());
        startActivityForResult(intent, CommonData.REQUEST_USER_ADDRESS);
    }

    public void name(View view) {
        Intent intent = new Intent(context, UserInfoEditActivity.class);
        intent.putExtra("title", "名称");
        intent.putExtra("position", 0);
        intent.putExtra("content", user_title.getText().toString());
        startActivityForResult(intent, CommonData.REQUEST_USER_NAME);
    }

    //获取用户信息
    public void getData(final String url) {
        CommonData.getInstance(context).getUserInfoData();
    }

    //修改头像
    public void updatePhoto() {
        String url = Constants.USER_UP_ALL_INFO;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("nam", "");
        params.put("adr", "");
        params.put("ton", "");
        params.put("lat", "");
        params.put("pic", "");
        params.put("lp1", "");
        params.put("lp2", "");
        params.put("license", "");
        params.put("agent", "");

        if (photoPosition == 3) {
            String pic = getPhoto(updateFile);
            params.put("pic", pic);
        } else if (photoPosition == 0) {
            String pic = getPhoto(updateFile);
            params.put("license", pic);
        } else if (photoPosition == 1) {
            String pic = getPhoto(updateFile);
            params.put("lp1", pic);
        } else if (photoPosition == 2) {
            String pic = getPhoto(updateFile);
            params.put("agent", pic);
        } else if (photoPosition == 4) {//店面全景照片
            String pic = getPhoto(updateFile);
            params.put("lp2", pic);
        }
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                Utils.showToastShort(context, "照片修改成功");
                getData(Constants.USER_INFO);
            }
        });
    }

    public String getPhoto(String filePath) {
        String pic = "";
        File file = new File(filePath);
        if (file != null) {
            pic = BitmapUtil.byte2hex(BitmapUtil.compressBitmapToByte(BitmapUtil.decodeFile(file)));
            Utils.showLog("pic==" + pic);
        }
        return pic;
    }

    //提交审核
    public void getData() {
        showProgressDialog();
        String url = Constants.USER_REQUEST_AUDIT;
        Map<String, Object> params = new HashMap<String, Object>();
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                Utils.showToastShort(context, "提交审核成功");
                preferences.setStringData("enb", "3");
                setUserState();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                disProgressDialog();
            }
        });

    }

    public void setUserState() {
        userState = preferences.getUserState();
        String state = getResources().getStringArray(R.array.audit_array)[userState];
        user_state.setText(state);
        if (userState != 1) {
            setRightView("提交审核", -1);
            user_success.setVisibility(View.GONE);
        } else {
            user_success.setVisibility(View.VISIBLE);
        }
    }

    public boolean isUpdate() {
        if (userState == 1 || userState == 3) {
            return false;
        } else {
            return true;
        }
    }

    //获取用户信息
    public void getUserInfoData() {
        final String url = Constants.USER_INFO;
        Map<String, Object> params = new HashMap<String, Object>();
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }
            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (url.equals(Constants.USER_INFO)) {
                    preferences.saveUsetInfo(response);
                    setUserState();
                    setViewData();
                }

            }
        });

    }

}
