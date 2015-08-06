package com.autoparts.buyers.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.application.MyApplication;
import com.autoparts.buyers.preferences.Preferences;
import com.autoparts.buyers.utils.DialogUtil;
import com.autoparts.buyers.utils.FileUtils;
import com.autoparts.buyers.utils.ImageLoaderBitmapUtil;
import com.autoparts.buyers.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by:Liuhuacheng
 * Created time:15-1-29
 */
public class BaseActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener , AdapterView.OnItemLongClickListener{
    private long mExitTime = 0;
    public boolean isBack = false;

    /*TopBar View*/
    public TextView topBar_title, topBar_left_text, topBar_right_text;
    public LinearLayout topBar_right_layout, topBar_left_layout;
    private ImageView topBar_left_image, topBar_right_image;
    private RelativeLayout topBar_left_image_parent, topBar_right_image_parent;

    /*Bottom View*/
    private LinearLayout bottom_main_view, bottom_user_view, bottom_more_view;

    private Dialog commonDialog;
    private TextView confirm_content, confirm_ok, confirm_cancel;
    public Preferences preferences;
    public MyApplication application;
    public ImageLoaderBitmapUtil imageLoaderUtil;

    private boolean isLeftBack = true;
    private FileUtils fileUtils;
    public Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (MyApplication) this.getApplication();
        application.addActivity(this);
        preferences = Preferences.getInstance(this);
        imageLoaderUtil = ImageLoaderBitmapUtil.getInstance(this);
        fileUtils = new FileUtils(this, 1);
        initDialog();
        initData();
        loadingDialog = DialogUtil.loadingProgress(this);
    }

    public void showProgressDialog(){
        if (loadingDialog != null){
            loadingDialog.setCancelable(true);
            loadingDialog.show();
        }
    }
    public void disProgressDialog(){
        if (loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initData() {
        topBar_title = (TextView) findViewById(R.id.topBar_title);
        topBar_left_text = (TextView) findViewById(R.id.topBar_left_text);
        topBar_right_text = (TextView) findViewById(R.id.topBar_right_text);
        topBar_right_layout = (LinearLayout) findViewById(R.id.topBar_right_layout);
        topBar_left_layout = (LinearLayout) findViewById(R.id.topBar_left_layout);
        if (topBar_left_layout != null)
            topBar_left_layout.setOnClickListener(this);

        topBar_right_image = (ImageView) findViewById(R.id.topBar_right_image);
        topBar_right_image_parent = (RelativeLayout) findViewById(R.id.topBar_right_image_parent);

        topBar_left_image = (ImageView) findViewById(R.id.topBar_left_image);
        topBar_left_image_parent = (RelativeLayout) findViewById(R.id.topBar_left_image_parent);
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            topBar_title.setText(title);
        }
    }

    public void setNotShowLeft(String rightText, int imageID) {
        topBar_left_layout.setVisibility(View.GONE);
    }

    /*
        * 这是左侧布局
        * @param leftText 文字，不显示则设置”“
        * @param imageID  显示图片id
        * */
    public void setLeftView(String leftText, int imageID) {
        topBar_left_layout.setOnClickListener(this);
        if (!TextUtils.isEmpty(leftText)) {
            topBar_left_text.setText(leftText);
            int padding = (int) getResources().getDimension(R.dimen.common_padding_middle);
            topBar_left_text.setPadding(padding, 0, padding, 0);
        } else {
            topBar_left_text.setText("");
            topBar_left_text.setPadding(0, 0, 0, 0);

        }
        if (imageID != -1) {
            topBar_left_image_parent.setVisibility(View.VISIBLE);
            topBar_left_image.setImageResource(imageID);
        } else {
            isLeftBack = false;
            topBar_left_image_parent.setVisibility(View.GONE);
        }
    }

    /*
    * 这是右侧布局
    * @param rightText 文字，不显示则设置”“
    * @param imageID  显示图片id
    * */
    public void setRightView(String rightText, int imageID) {
        topBar_right_layout.setOnClickListener(this);
        if (!TextUtils.isEmpty(rightText)) {
            topBar_right_text.setText(rightText);
            int padding = (int) getResources().getDimension(R.dimen.common_padding_middle);
            topBar_right_text.setPadding(padding, 0, padding, 0);
        }
        if (imageID != -1) {
            topBar_right_image_parent.setVisibility(View.VISIBLE);
            topBar_right_image.setImageResource(imageID);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topBar_left_layout:
                if (isLeftBack) {
                    finish();
                }
                break;
            case R.id.confirm_ok:
                dismissDialog();
                break;
            case R.id.confirm_cancel:
                dismissDialog();
                break;
//            case R.id.bottom_main_view:
////                Utils.showToastShort(BaseActivity.this, "main");
//                break;
//            case R.id.bottom_user_view:
////                Utils.showToastShort(BaseActivity.this, "user");
//                break;
//            case R.id.bottom_more_view:
////                Utils.showToastShort(BaseActivity.this, "more");
//                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isBack && (System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }



    public void initDialog() {
        commonDialog = DialogUtil.createDialog(this, R.layout.common_dialog, R.style.CustomDialog);
        confirm_content = (TextView) commonDialog.findViewById(R.id.confirm_content);
        confirm_ok = (TextView) commonDialog.findViewById(R.id.confirm_ok);
        confirm_cancel = (TextView) commonDialog.findViewById(R.id.confirm_cancel);
        confirm_ok.setOnClickListener(this);
        confirm_cancel.setOnClickListener(this);
        DialogUtil.setDialogParams(this, commonDialog, R.dimen.dialog_width_margin);
    }

    public void showDialog(String content, boolean isShowCancel) {
        confirm_content.setText(content);
        if (!isShowCancel) {
            confirm_cancel.setVisibility(View.GONE);
            confirm_ok.setBackgroundResource(R.drawable.dialog_btn_bottom_selector);
        } else {
            confirm_cancel.setVisibility(View.VISIBLE);
            confirm_ok.setBackgroundResource(R.drawable.dialog_btn_left_selector);
        }
        commonDialog.show();
    }

    public void showDialogText(String content, String ok, String cancel) {
        confirm_content.setText(content);
        confirm_ok.setText(ok);
        confirm_cancel.setText(cancel);
        commonDialog.show();
    }

    public void dismissDialog() {
        if (commonDialog != null && commonDialog.isShowing()) {
            commonDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        application.removeActivity(this);
    }

    /**
     * 关闭软键盘
     */
    public void closeInputMethod(View view) {
        //关闭软键盘
        InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    //ListView数据为空 的情况
    private LinearLayout data_null_view;
    private TextView data_null_text;

    public void initNullDataView() {
        data_null_view = (LinearLayout) findViewById(R.id.data_null_view);
        data_null_text = (TextView) findViewById(R.id.data_null_text);
        if (data_null_view != null);
        data_null_view.setVisibility(View.GONE);
    }


    public void setDataNull(boolean b) {
        if (b) {
            if (data_null_view != null) {
                data_null_view.setVisibility(View.VISIBLE);
//            mListView.setVisibility(View.GONE);
            }
        } else {
            if (data_null_view != null) {
                data_null_view.setVisibility(View.GONE);
//            mListView.setVisibility(View.VISIBLE);
            }
        }
    }
    public void setDataNull(boolean b,String content) {
        if (b) {
            if (data_null_view != null) {
                data_null_view.setVisibility(View.VISIBLE);
                data_null_text.setText(content);
//            mListView.setVisibility(View.GONE);
            }
        } else {
            if (data_null_view != null) {
                data_null_view.setVisibility(View.GONE);
//            mListView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}
