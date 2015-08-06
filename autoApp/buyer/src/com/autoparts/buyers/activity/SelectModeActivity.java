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
package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.autoparts.buyers.R;
import com.autoparts.buyers.action_content.fragment.ModelFragment;
import com.autoparts.buyers.adapter.CommonLetterlistAdapter;
import com.autoparts.buyers.model.CommonLetterModel;
import com.autoparts.buyers.model.ContactUtils;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.JsonParserUtils;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.view.MyLetterListView;
import org.apache.http.Header;
import shared.ui.actionscontentview.ActionsContentView;

import java.util.*;

/**
 * 获取配件品牌
 * Created by:Liuhuacheng
 * Created time:15-1-29
 */
public class SelectModeActivity extends BaseFragmentActivity {

    private static final String STATE_URI = "state:uri";
    private static final String STATE_FRAGMENT_TAG = "state:fragment_tag";

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
    private String parttypeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.select_model);
        super.onCreate(savedInstanceState);
        context = this;
        String title = getIntent().getStringExtra("title");
        parttypeid = getIntent().getStringExtra(CommonData.INQUIRE_ID);
        setTitle(title);
        initView();
        initNullDataView();
        initOverlay();
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
                String partbandid = mList.get(position).getUser_id();
                Intent intent = new Intent();
                intent.putExtra(CommonData.INQUIRE_ID, partbandid);
                intent.putExtra(CommonData.INQUIRE_NAME, name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        contactUtils = new ContactUtils();

        getData();
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

    public void setData() {
        mList = new ContactUtils().getContactList();
        mList = contactUtils.getListKey(mList);
        mIndexer = contactUtils.getmIndexer();
        actionsAdapter.setData(mList);
        actionsAdapter.notifyDataSetChanged();
    }

    public void getData() {
        String url = Constants.INQUIRE_PART_BAND;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parttypeid", parttypeid);//"parttypeid":"配件类型标识(int)"
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
//            "partbandid": 3,
//                "nam": "动工",
//                "pic": "http://123.56.87.239:81/auto/resource/partband/1434756909.png"
//        }

        Vector<HashMap<String, Object>> maps = responseModel.getMaps();
        if (maps != null && maps.size() > 0) {
            for (int i = 0; i < maps.size(); i++) {
                HashMap<String, Object> map = maps.get(i);
                String bandid = (String) map.get("partbandid");
                String nam = (String) map.get("nam");
                String pic = (String) map.get("pic");
                CommonLetterModel commonLetterModel = new CommonLetterModel();

                commonLetterModel.setUser_id(bandid);
                commonLetterModel.setUser_image(pic);
                commonLetterModel.setUser_name(nam);

                String key = "#";
                if (!TextUtils.isEmpty(nam)) {
                    key = new ContactUtils().getPinYinHeadChar(nam.substring(0, 1));
                    key = key.toUpperCase();
                    if (TextUtils.isEmpty(key)) {
                        key = "#";
                    }
                }
                commonLetterModel.setUser_key(key);

                mList.add(commonLetterModel);
            }


            mList = contactUtils.getListKey(mList);
            mIndexer = contactUtils.getmIndexer();
            actionsAdapter.setData(mList);
            actionsAdapter.notifyDataSetChanged();
            setDataNull(false);
        } else {
            setDataNull(true);
        }
    }


}
