/*******************************************************************************
 * Copyright 2012 Steven Rudenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.action_content.fragment.ModelFragment;
import com.autoparts.sellers.adapter.CommonLetterlistAdapter;
import com.autoparts.sellers.model.CommonLetterModel;
import com.autoparts.sellers.model.ContactUtils;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.utils.Constants;
import com.autoparts.sellers.view.MyLetterListView;
import org.apache.http.Header;
import shared.ui.actionscontentview.ActionsContentView;

import java.util.*;

/**
 * 车型选择
 * Created by:Liuhuacheng
 * Created time:15-1-29
 */
public class InquiryModelActivity extends BaseFragmentActivity {

    private static final String STATE_URI = "state:uri";
    private static final String STATE_FRAGMENT_TAG = "state:fragment_tag";

    private ActionsContentView viewActionsContentView;

    private int currentState = 0;
    private String currentContentFragmentTag = null;
    private CommonLetterlistAdapter actionsAdapter;
    private ListView mListView;
    private MyLetterListView letterListView;

    // 首字母对应的位置
    private Map<String, Integer> mIndexer;
    private Context context;

    private View overlayView;
    private TextView overlay;
    private OverlayThread overlayThread;
    private Handler handler;
    private String name = "";
    private Handler handlerSelected = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
            Vector<HashMap<String, Object>> lines = (Vector<HashMap<String, Object>>) map.get("line");


//            intent.putExtra("data", name);
            //map.get("name") + " " +
            String name = "" + lines.get(msg.arg1).get("nam");
            String id = (String) lines.get(msg.arg1).get("lineid");
            Intent intent = new Intent();
            intent.putExtra(CommonData.INQUIRE_NAME, name);
            intent.putExtra(CommonData.INQUIRE_ID, id);

            setResult(RESULT_OK, intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.lnquiry_model);
        super.onCreate(savedInstanceState);
        context = this;
        String title = getString(R.string.inquiry_text_model);
        setTitle(title);
//        setRightView(getString(R.string.inquiry_text_history), -1);

        if (savedInstanceState != null) {
            currentState = savedInstanceState.getInt(STATE_URI);
            currentContentFragmentTag = savedInstanceState.getString(STATE_FRAGMENT_TAG);
        }

        init();
        initView();
        initOverlay();
    }

    private void init() {
        viewActionsContentView = (ActionsContentView) findViewById(R.id.actionsContentView);
        viewActionsContentView.setSwipingType(ActionsContentView.SWIPING_EDGE);
        viewActionsContentView.showActions();
        viewActionsContentView.setSpacingWidth(0);
        viewActionsContentView.setShadowVisible(true);
        viewActionsContentView.setShadowVisible(true);
        viewActionsContentView.setFadeValue(0);
//        viewActionsContentView.setSwipingType(ActionsContentView.SWIPING_ALL);
        viewActionsContentView.setSwipingType(ActionsContentView.SWIPING_EDGE);
        int value = 80;
        final float mDensity = getResources().getDisplayMetrics().density;

        viewActionsContentView.setActionsSpacingWidth((int) (value * mDensity));
        viewActionsContentView.setShadowWidth((int) (5 * mDensity));

        updateContent(currentState);

        getData();

    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.actions);
        mList = new ArrayList<CommonLetterModel>();
        actionsAdapter = new CommonLetterlistAdapter(context, mList);

        mListView.setAdapter(actionsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long flags) {

                name = mList.get(position).getUser_name();
                viewActionsContentView.showContent();
                updateContent(position);
            }
        });
        contactUtils = new ContactUtils();

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.topBar_right_layout:
                Intent intent = new Intent(context, InquiryModelHistoryActivity.class);
                startActivityForResult(intent, CommonData.REQUEST_INQUIRY_HISTORY);
                break;
        }
    }

    private class LetterListViewListener implements MyLetterListView.OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(final String s) {
            if (mIndexer.get(s) != null) {
                int position = mIndexer.get(s);
                mListView.setSelection(position);
            }
            overlay.setText(s);
            overlayView.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
//                //延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 500);
        }
    }

    //初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView);
        letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
        overlayThread = new OverlayThread();
        handler = new Handler();

        LayoutInflater inflater = LayoutInflater.from(this);
        overlayView = inflater.inflate(R.layout.contact_overlay, null);
        overlay = (TextView) overlayView.findViewById(R.id.mTextView);
        overlayView.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlayView, lp);
    }

    //设置overlay不可见
    private class OverlayThread implements Runnable {
        public void run() {
            overlayView.setVisibility(View.GONE);
        }
    }

    private List<CommonLetterModel> mList;
    private ContactUtils contactUtils;

    //
    public void onActionsButtonClick(View view) {
        if (viewActionsContentView.isActionsShown())
            viewActionsContentView.showContent();
        else
            viewActionsContentView.showActions();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_URI, currentState);
        outState.putString(STATE_FRAGMENT_TAG, currentContentFragmentTag);

        super.onSaveInstanceState(outState);
    }

    public void updateContent(int state) {
        ModelFragment fragment = null;
        String tag = "";
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction tr = fm.beginTransaction();

        if (currentState != state) {
            final Fragment currentFragment = fm.findFragmentByTag(currentContentFragmentTag);
            if (currentFragment != null)
                tr.hide(currentFragment);
        }
//        if (AboutFragment.ABOUT_URI.equals(uri)) {
        tag = ModelFragment.TAG;
        final ModelFragment foundFragment = (ModelFragment) fm.findFragmentByTag(tag);
        if (foundFragment != null) {
            fragment = foundFragment;
        } else {
            fragment = new ModelFragment(handlerSelected);
        }
//        }

        if (fragment.isAdded()) {
            tr.show(fragment);
        } else {
            tr.replace(R.id.content, fragment, tag);
        }
        tr.commit();
        if (mList != null && state < mList.size()) {
            fragment.model_data(mList.get(state).getUser_id());
        }

        currentState = state;
        currentContentFragmentTag = tag;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//听力测试结束
            switch (requestCode) {
                case CommonData.REQUEST_INQUIRY_HISTORY:
                    setResult(RESULT_OK, data);
                    finish();
                    break;
            }
        }
    }


    public void getData() {
        String url = Constants.INQUIRE_BAND;
        Map<String, Object> params = new HashMap<String, Object>();
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                setData(response);
            }

            @Override
            public void onResultJson(String json) {
                super.onResultJson(json);
            }
        });

    }

    public void setData(ResponseModel responseModel) {

//        {
//                "bandid":"品牌标识(int)",
//                "nam":"品牌名称",
//                "pic":"url",
//                "first":"首字母"
//        }


        Vector<HashMap<String, Object>> maps = responseModel.getMaps();
        for (int i = 0; i < maps.size(); i++) {
            HashMap<String, Object> map = maps.get(i);
            String bandid = (String) map.get("bandid");
            String nam = (String) map.get("nam");
            String pic = (String) map.get("pic");
            String first = (String) map.get("first");
            CommonLetterModel commonLetterModel = new CommonLetterModel();

            commonLetterModel.setUser_id(bandid);
            commonLetterModel.setUser_image(pic);
            commonLetterModel.setUser_name(nam);
            if (TextUtils.isEmpty(first)) {
                first = "#";
            }
            commonLetterModel.setUser_key(first);
            mList.add(commonLetterModel);
        }

        mList = contactUtils.getListKey(mList);
        mIndexer = contactUtils.getmIndexer();
        actionsAdapter.setData(mList);
        actionsAdapter.notifyDataSetChanged();
    }


}
