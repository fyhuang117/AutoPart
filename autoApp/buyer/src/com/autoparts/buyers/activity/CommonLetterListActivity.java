package com.autoparts.buyers.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.CommonLetterlistAdapter;
import com.autoparts.buyers.model.CommonLetterModel;
import com.autoparts.buyers.model.ContactUtils;
import com.autoparts.buyers.view.MyLetterListView;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 师资介绍
 * Created by:Liuhuacheng
 * Created time:14-12-16
 */
public class CommonLetterListActivity extends BaseActivity {
    private Context context;
    private CommonLetterlistAdapter listAdapter;
    private List<CommonLetterModel> mList;
    private ListView mListView;
    private RelativeLayout mListView_parent_view;

    private TextView overlay;
    private View overlayView;
    private MyLetterListView letterListView;
    private OverlayThread overlayThread;
    private Handler handler;
    private ContactUtils contactUtils;
    // 首字母对应的位置
    private Map<String, Integer> mIndexer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_letter_view);
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        return super.onCreateDialog(id);
    }

    private void init() {
        context = this;
        mListView_parent_view = (RelativeLayout) findViewById(R.id.mListView_parent_view);
        mListView = (ListView) findViewById(R.id.mListView);
        initOverlay();
        setTitle("Title");
        mList = new ArrayList<CommonLetterModel>();
        listAdapter = new CommonLetterlistAdapter(context,mList);
//        mListView.setDividerHeight(0);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(this);
        setDataShow(true);
        getData();
        setData();
    }

    public void getData() {
//        String url = ConstantUtil.requestUrl[ConstantUtil.REQUEST_TERCHERS_LIST];
//        HttpRequestInstance instance = HttpRequestInstance.getInstance(context);
//        RequestParams params = new RequestParams();
//        instance.post(context, params, url, new HttpResultHandler() {
//            @Override
//            public void onResultSuccess(int statusCode, Response response) {
//                super.onResultSuccess(statusCode, response);
//                setData(response);
//            }
//
//            @Override
//            public void onResultFail(int statusCode, String message) {
//                super.onResultFail(statusCode, message);
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                //对话框取消
//                removeDialog(ConstantUtil.DIALOG_LOADING);
//            }
//        });

    }

    public void setData() {
//        if (response.getMaps() != null && response.getMaps().size() > 0) {
            mList =new  ContactUtils().getContactList();
            mList = contactUtils.getListKey(mList);
            mIndexer = contactUtils.getmIndexer();
            listAdapter.setData(mList);
            listAdapter.notifyDataSetChanged();
            setDataShow(false);
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        super.onItemClick(parent, view, position, id);
//        Intent intent = new Intent(this, SchoolTercersDetailActivity.class);
//        intent.putExtra("tercer",(mList.get(position)));
//        startActivity(intent);
    }

    /**
     * 控制没有数据的显示
     *
     * @param isNodata 没有数据就值为true
     */
    private void setDataShow(boolean isNodata) {
        //有上拉刷新
        if (isNodata) {
            mListView_parent_view.setVisibility(View.GONE);
        } else {
            mListView_parent_view.setVisibility(View.VISIBLE);
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
        contactUtils = new ContactUtils();
        letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
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



}