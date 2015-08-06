package com.autoparts.buyers.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.action_content.ExamplesActivity;
import com.autoparts.buyers.model.InquireCarModel;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.*;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import org.apache.http.Header;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 询价-配件
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquiryActivity extends BaseActivity {
    private Context context;
    private TextView inquiry_model, inquiry_year, inquiry_sort;

    private Dialog photoDialog;
    private ImageView photo1, photo2, photo3;
    private ImageView delete1, delete2, delete3;
    private List<ImageView> photos = new ArrayList<ImageView>();
    private List<ImageView> deletes = new ArrayList<ImageView>();

    private CameraUtils cameraUtils;
    private int photoPosition = 0;
    private LinearLayout model_view;
    private String sortName;

    private Bundle bundleOther = null, bundleVoice = null;
    private LinearLayout photo_select_view;
    private Map<Integer, String> file_path = new HashMap<Integer, String>();
    private String[] pics = {"pic1", "pic2", "pic3"};

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inquiry);
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra("title");
        setTitle(title);
        setLeftView("", R.drawable.icon_back);
        init();
        initPhotoDialog();
        cameraUtils = new CameraUtils(InquiryActivity.this);
    }

    private void init() {
        context = this;
        inquiry_model = (TextView) findViewById(R.id.inquiry_model);
        inquiry_year = (TextView) findViewById(R.id.inquiry_year);
        inquiry_sort = (TextView) findViewById(R.id.inquiry_sort);
        model_view = (LinearLayout) findViewById(R.id.model_view);
        getCarModel();//获取历史记录第一条数据
//        inquiry_model.setText("奥迪 奥迪A3");
//        inquiry_year.setText("2015 发动机");

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
        photo1.setImageResource(R.drawable.icon_photo_upload_nor);
        photo2.setImageResource(R.drawable.icon_photo_upload_nor);
        photo3.setImageResource(R.drawable.icon_photo_upload_nor);

        delete1 = (ImageView) findViewById(R.id.delete1);
        delete2 = (ImageView) findViewById(R.id.delete2);
        delete3 = (ImageView) findViewById(R.id.delete3);
        delete1.setOnClickListener(this);
        delete2.setOnClickListener(this);
        delete3.setOnClickListener(this);
        deletes.add(delete1);
        deletes.add(delete2);
        deletes.add(delete3);

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
                photos.get(0).setImageResource(R.drawable.icon_photo_default);
                if (file_path.containsKey(0)) {
                    file_path.remove(0);
                }
                break;
            case R.id.delete2:
                delete2.setVisibility(View.INVISIBLE);
                photos.get(1).setImageResource(R.drawable.icon_photo_default);
                if (file_path.containsKey(1)) {
                    file_path.remove(1);
                }
                break;
            case R.id.delete3:
                delete3.setVisibility(View.INVISIBLE);
                photos.get(2).setImageResource(R.drawable.icon_photo_default);
                if (file_path.containsKey(2)) {
                    file_path.remove(2);
                }
                break;
        }
    }

    //车型选择
    public void model(View view) {
        Intent intent = new Intent(context, InquiryModelActivity.class);
        startActivityForResult(intent, CommonData.REQUEST_CODE_MODEL);
    }

    //年款
    public void year(View view) {
        if (TextUtils.isEmpty(id_model)) {
            Utils.showToastShort(context, "请先选择车型");
        } else {
            Intent intent = new Intent(context, InquiryYearActivity.class);
            intent.putExtra(CommonData.INQUIRE_ID, id_model);
            startActivityForResult(intent, CommonData.REQUEST_CODE_YEAR);
        }
    }

    //配件分类
    public void sort(View view) {
        Intent intent = null;
        intent = new Intent(context, InquirySortNewActivity.class);
        startActivityForResult(intent, CommonData.REQUEST_CODE_SORT);
    }

    //捎句话
    public void voice(View view) {
        Intent intent = new Intent(context, InquiryVoiceTwoActivity.class);
        if (bundleVoice != null)
            intent.putExtras(bundleVoice);
        startActivityForResult(intent, CommonData.REQUEST_INQUIRY_VOICE);
    }

    //其它要求
    public void other(View view) {
        Intent intent = new Intent(context, InquiryOtherActivity.class);
        intent.putExtra(CommonData.INQUIRE_ID, parttypeid);
        if (bundleOther != null)
            intent.putExtras(bundleOther);
        startActivityForResult(intent, CommonData.REQUEST_INQUIRY_OTHER);
    }

    //询价提交
    public void commit(View view) {
        if (TextUtils.isEmpty(id_model)) {
            Utils.showToastShort(context, "请先选择车型");
        } else if (TextUtils.isEmpty(year_id)) {
            Utils.showToastShort(context, "请先选择年款");
        } else if (TextUtils.isEmpty(parttypeid)) {
            Utils.showToastShort(context, "请先选择配件分类");
        } else {
//            preferences.setOrder(true);
//            finish();
//            Utils.showToastShort(context, "询价成功");
            getData();
        }

    }

    private String name_model = "";//选择车型 名称
    private String id_model = "";//选择车型 ID
    private String parttypeid = "";//配件ID

    private String year_name = "";//年款 名称
    private String year_id = "";//年款 ID

    private String partbandid = "";//品牌 ID
    private String areaid = "";//商圈 ID
    private String qualityid = "1";//品质 ID   默认原厂

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//听力测试结束
            switch (requestCode) {
                case CommonData.REQUEST_CODE_MODEL:
                    String name = data.getStringExtra(CommonData.INQUIRE_NAME);
                    String id = data.getStringExtra(CommonData.INQUIRE_ID);
                    setModel(name, id);

                    //选择车型后 情况年款
                    year_name  = data.getStringExtra(CommonData.INQUIRE_YEAR_NAME);
                    year_id = data.getStringExtra(CommonData.INQUIRE_YEAR_ID);

                    if (TextUtils.isEmpty(year_name)){
                        year_name = "";
                        year_id = "";
                    }
                    inquiry_year.setText(year_name);
                    break;
                case CommonData.REQUEST_CODE_YEAR:
                    year_name = data.getStringExtra(CommonData.INQUIRE_NAME);
                    year_id = data.getStringExtra(CommonData.INQUIRE_ID);
                    inquiry_year.setText(year_name);
                    break;
                case CommonData.REQUEST_CODE_SORT:
                    String sort = data.getStringExtra(CommonData.INQUIRE_NAME);
                    parttypeid = data.getStringExtra(CommonData.INQUIRE_ID);
//                    Utils.showToastShort(context,"parttypeid=="+parttypeid);
                    inquiry_sort.setText(sort);
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
                case CommonData.REQUEST_INQUIRY_OTHER:
                    partbandid = data.getStringExtra(CommonData.INQUIRE_ID);
                    areaid = data.getStringExtra("areaid");
                    qualityid = data.getStringExtra("qualityid");
                    bundleOther = data.getExtras();
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
            }
        }
    }

    //设置车型
    public void setModel(String name, String id) {
        name_model = name;
        id_model = id;
        inquiry_model.setText(name_model);
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


    public void getData() {
        int c = 1;
        if (bundleOther != null) {
            c = bundleOther.getInt(CommonData.INQUIRE_NUM, 1);
        }
        String mes = "";
        if (bundleVoice != null) {
            mes = bundleVoice.getString(CommonData.INQUIRE_MES);
        }

        Map<String, Object> params = new HashMap<String, Object>();
//        if (parttypeid.equals("-1")){
//            parttypeid = "";
//        }
        params.put("parttypeid", parttypeid);//配件分类id
        params.put("partbandid", partbandid);//品牌id
        params.put("carid", year_id);//汽车年款
        params.put("businessarea_id", areaid);//商圈
        params.put("parttype_quality", qualityid);//品质
        params.put("c", c);//数量
        params.put("mes", mes);//文本留言
        params.put("pic1", "");
        params.put("pic2", "");
        params.put("pic3", "");
        params.put("aum", "");

        setFile(params);
//            "parttypeid":"配件分类(int)",
//                    "partbandid":"x,x,x,",
//                    "carid":"汽车年款(int)",
//                    "c":"数量(int)",
//                    "pic1":"16进制的图片数据",
//                    "pic2":"16进制的图片数据",
//                    "pic3":"16进制的图片数据",x
//                    "mes":"文本留言",
//                    "aum":"16进制的语音数据"

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
            String url = Constants.ORDER_SAVE;
            showProgressDialog();
            HttpClientUtils.post(context, url, params, new HttpResultHandler() {
                @Override
                public void onResultFail(String message, int statusCode) {
                    super.onResultFail(message, statusCode);
                }

                @Override
                public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                    super.onResultSuccess(headers, response, message, statusCode);
                    Utils.showToastShort(context, "询价成功");
                    saveCarModel(id_model, name_model,year_id,year_name);

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

    public void saveCarModel(String id, String name,String year_id,String year_name) {
        preferences.setStringData(preferences.CAR_NAME, name);
        preferences.setStringData(preferences.CAR_ID, id);

        preferences.setStringData(preferences.YEAR_NAME, year_name);
        preferences.setStringData(preferences.YEAR_ID, year_id);

        DbUtils db = DbUtils.create(this);
        InquireCarModel inquireCarModel = new InquireCarModel();
        inquireCarModel.setId(id);
        inquireCarModel.setName(name);
        inquireCarModel.setYear_id(year_id);
        inquireCarModel.setYear_name(year_name);
        try {
            db.save(inquireCarModel); // 使用saveBindingId保存实体时会为实体的id赋值
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void getCarModel() {
//        List<InquireCarModel> mLists = null;
//        InquireCarModel entity = null;
//        DbUtils db = DbUtils.create(context);
//        try {
////            entity = db.findFirst(InquireCarModel.class);//通过类型查找
//            mLists = db.findAll(InquireCarModel.class);//通过类型查找
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
////        if (entity != null) {
////            setModel(entity.getName(), entity.getId());
////        }
//        if (mLists != null && mLists.size() > 0) {
//            entity = mLists.get(mLists.size() - 1);
//            setModel(entity.getName(), entity.getId());
//        }

        String car_name = preferences.getStringData(preferences.CAR_NAME);
        String car_id = preferences.getStringData(preferences.CAR_ID);

        if (!TextUtils.isEmpty(car_name)) {
            setModel(car_name, car_id);
        }

        String y_name = preferences.getStringData(preferences.YEAR_NAME);
        String y_id = preferences.getStringData(preferences.YEAR_ID);

        if (!TextUtils.isEmpty(y_name)) {
            year_name = y_name;
            year_id = y_id;
            inquiry_year.setText(year_name);
        }

    }
}
