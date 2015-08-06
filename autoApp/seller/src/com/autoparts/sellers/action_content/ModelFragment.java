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
package com.autoparts.sellers.action_content;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.ExpandListViewAdapter;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.utils.Constants;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 车型内容选择
 * Created by:Liuhuacheng
 * Created time:15-1-29
 */
public class ModelFragment extends Fragment {
    private Context context;
    public static final String TAG = ModelFragment.class.getSimpleName();

    private static final String ABOUT_SCHEME = "settings";
    private static final String ABOUT_AUTHORITY = "about";
    public static final Uri ABOUT_URI = new Uri.Builder()
            .scheme(ABOUT_SCHEME)
            .authority(ABOUT_AUTHORITY)
            .build();

    private ExpandableListView expandListView;
    private ExpandListViewAdapter adapter;
    private Vector<HashMap<String, Object>> mLists;

    private Handler handler;
    private String bandid;
    private LinearLayout data_null_view;

    public ModelFragment(Handler handler) {
        this.handler = handler;

    }

    public void model_data(String bandid) {
        this.bandid = bandid;
        getData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        mLists = new Vector<HashMap<String, Object>>();
        adapter = new ExpandListViewAdapter(context, mLists);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.about, container, false);
        initView(v);
        return v;
    }

    public void initView(View view) {
        data_null_view = (LinearLayout) view.findViewById(R.id.data_null_view);
        expandListView = (ExpandableListView) view.findViewById(R.id.expandListView);
        setViewData();

    }

    public void setViewData() {
        expandListView.setAdapter(adapter);
        int groupCount = expandListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            expandListView.expandGroup(i);
        }
        expandListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        expandListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, final int i, int i1, long l) {
                Message message = new Message();
                message.what = 0;
                message.obj = mLists.get(i);
                message.arg1 = i1;
                if (handler != null) {
                    handler.sendMessage(message);
                }
                return false;
            }
        });
    }

    public void getData() {
        String url = Constants.INQUIRE_BAND_SUB;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bandid", bandid);
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
//            "subbandid":"子品牌标识(int)",
//                "name":"子品牌名称",
//                "line":[{
//            "lineid":"车系标识(int)",
//                    "nam":"车系名称"
//        }
        mLists = responseModel.getMaps();
        if (mLists != null && mLists.size() > 0) {
            adapter.setData(mLists);
            setViewData();
            data_null_view.setVisibility(View.GONE);
            expandListView.setVisibility(View.VISIBLE);
        }else {
            data_null_view.setVisibility(View.VISIBLE);
            expandListView.setVisibility(View.GONE);
        }


    }


}
