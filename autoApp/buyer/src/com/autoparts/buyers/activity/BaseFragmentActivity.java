package com.autoparts.buyers.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.application.MyApplication;
import com.autoparts.buyers.preferences.Preferences;
import com.autoparts.buyers.utils.DialogUtil;

/**
 * Created by:Liuhuacheng
 * Created time:15-1-29
 */
public class BaseFragmentActivity extends FragmentActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (MyApplication) this.getApplication();
        application.addActivity(this);
        preferences = Preferences.getInstance(this);
//        initDialog();
        initData();
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
        }
        if (imageID != -1) {
            topBar_left_image_parent.setVisibility(View.VISIBLE);
            topBar_left_image.setImageResource(imageID);
        }else {
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
                finish();
                break;
            case R.id.confirm_ok:
                dismissDialog();
                break;
            case R.id.confirm_cancel:
                dismissDialog();
                break;
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


    //ListView数据为空 的情况
    private LinearLayout data_null_view;

    public void initNullDataView() {
        data_null_view = (LinearLayout) findViewById(R.id.data_null_view);
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


}
