package com.autoparts.buyers.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.application.MyApplication;
import com.autoparts.buyers.preferences.Preferences;

/**
 * Created by:Liuhuacheng
 * Created time:15-1-29
 */
public class BaseFragment extends Fragment implements View.OnClickListener {

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

    private boolean isLeftBack = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (MyApplication) getActivity().getApplication();
        application.addActivity(getActivity());
        preferences = Preferences.getInstance(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void initData(View view) {
        topBar_title = (TextView) view.findViewById(R.id.topBar_title);
        topBar_left_text = (TextView) view.findViewById(R.id.topBar_left_text);
        topBar_right_text = (TextView) view.findViewById(R.id.topBar_right_text);
        topBar_right_layout = (LinearLayout) view.findViewById(R.id.topBar_right_layout);
        topBar_left_layout = (LinearLayout) view.findViewById(R.id.topBar_left_layout);
        if (topBar_left_layout != null)
            topBar_left_layout.setOnClickListener(this);

        topBar_right_image = (ImageView) view.findViewById(R.id.topBar_right_image);
        topBar_right_image_parent = (RelativeLayout) view.findViewById(R.id.topBar_right_image_parent);

        topBar_left_image = (ImageView) view.findViewById(R.id.topBar_left_image);
        topBar_left_image_parent = (RelativeLayout) view.findViewById(R.id.topBar_left_image_parent);
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
                    getActivity().finish();
                }
                break;
        }
    }

}
