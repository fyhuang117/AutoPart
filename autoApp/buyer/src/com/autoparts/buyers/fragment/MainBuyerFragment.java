package com.autoparts.buyers.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.activity.*;
import com.autoparts.buyers.adapter.MainOrderBeingAdapter;
import com.autoparts.buyers.autoscrollviewpager.AutoScrollViewPager;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.preferences.Preferences;
import com.autoparts.buyers.utils.*;
import com.autoparts.buyers.view.viewpagerindicator.CirclePageIndicator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;

public class MainBuyerFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Context context;
    private TextView tv_title;
    private AutoScrollViewPager viewPager;
    private RelativeLayout my_view1, my_view2, my_view3, my_view4;
    private RelativeLayout my_order;
    private ImageAdapter imageAdapter;
    private LinearLayout buyer_order_view;//登陆后的订单显示
    //滚动新闻
    private int currentItem = 0; // 当前图片的索引号
//    private int[] images = {R.drawable.ad_viewpagerft01, R.drawable.ad_viewpagerft02};

    private LinearLayout buyer_now_null;//正在进行中的订单是否存在，不存在则显示
    private ListView mListView;//正在进行中的订单
    private MainOrderBeingAdapter adapter;
    public Preferences preferences;
    private Vector<HashMap<String, Object>> mList;
    private Vector<HashMap<String, Object>> ads;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = Preferences.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_buyer_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        context = getActivity();
        my_view1 = (RelativeLayout) view.findViewById(R.id.my_view1);
        my_view1.setOnClickListener(this);
        my_view2 = (RelativeLayout) view.findViewById(R.id.my_view2);
        my_view2.setOnClickListener(this);
        my_view3 = (RelativeLayout) view.findViewById(R.id.my_view3);
        my_view3.setOnClickListener(this);
        my_order = (RelativeLayout) view.findViewById(R.id.my_order);
        my_order.setOnClickListener(this);
        initADView(view);

        buyer_order_view = (LinearLayout) view.findViewById(R.id.buyer_order_view);
        buyer_order_view.setVisibility(View.GONE);

        buyer_now_null = (LinearLayout) view.findViewById(R.id.buyer_now_null);
        mListView = (ListView) view.findViewById(R.id.mListView);

        mList = new Vector<HashMap<String, Object>>();
        adapter = new MainOrderBeingAdapter(context, mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        getData(Constants.USER_AD);

        if (preferences.getIsLogin()) {
            String alias = JPushInit.getDeviceInfo(context);
            Utils.showLog("alis=="+alias);
            new JPushInit(context).setAlias(alias);
            CommonData.getInstance(context).setUserAlias(alias);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (preferences.getIsLogin()) {
            getData(Constants.ORDER_GET_summaries);
            //获取用户信息
            CommonData.getInstance(context).getUserInfoData();
        }

    }


    public void showOrder(boolean b) {
        buyer_order_view.setVisibility(View.VISIBLE);
        if (b) {
            buyer_now_null.setVisibility(View.GONE);//有数据则隐藏
            mListView.setVisibility(View.VISIBLE);
        } else {
            buyer_now_null.setVisibility(View.VISIBLE);//无数据则显示
            mListView.setVisibility(View.GONE);
        }
    }

    public void initADView(View view) {
        ads = new Vector<HashMap<String, Object>>();
        imageAdapter = new ImageAdapter(ads);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        viewPager = (AutoScrollViewPager) view.findViewById(R.id.viewPager);
        viewPager.startAutoScroll(3000);
        viewPager.setInterval(3000);
        viewPager.setCycle(true);
//        viewPager.setBorderAnimation(true);
        viewPager.setAdapter(imageAdapter);// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        CirclePageIndicator mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);
        mIndicator.setOnPageChangeListener(new MyPageChangeListener());
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        int userState = preferences.getUserState();
        switch (view.getId()) {
            case R.id.my_view1:
                if (!preferences.getIsLogin()) {
                    login();
                } else if (userState != 1) {
                    CommonData.getInstance(context).showUserState(userState);
                } else {
                    intent = new Intent(context, InquiryActivity.class);
                    intent.putExtra("position", 0);
                    intent.putExtra("title", "询价配件");
                    startActivity(intent);
                }
                break;
            case R.id.my_view2:
                if (!preferences.getIsLogin()) {
                    login();
                } else if (userState != 1) {
                    CommonData.getInstance(context).showUserState(userState);
                } else {
                    //询价易损件
                    intent = new Intent(context, InquiryConsumableActivity.class);
                    intent.putExtra("position", 0);
                    intent.putExtra("title", getString(R.string.my_inquiry_wearing));
                    startActivity(intent);
                }
                break;
            case R.id.my_view3:
                if (!preferences.getIsLogin()) {
                    login();
                } else if (userState != 1) {
                    CommonData.getInstance(context).showUserState(userState);
                } else {
                    //询价汽保耗材
                    intent = new Intent(context, InquiryConsumableActivity.class);
//                intent = new Intent(context, InquiryConsumableActivity.class);
                    intent.putExtra("position", 1);
                    intent.putExtra("title", getString(R.string.my_inquiry_consumable));
                    startActivity(intent);
                }
                break;
            case R.id.my_view4:
                break;
            case R.id.topBar_left_layout:
//                Intent intentLogin = new Intent(context, UserLoginActivity.class);
                Intent intentLogin = new Intent(context, MainDrawerActivity.class);
                startActivity(intentLogin);
                break;
            case R.id.my_order:
                if (!preferences.getIsLogin()) {
                    login();
                } else if (userState != 1) {
                    CommonData.getInstance(context).showUserState(userState);
                } else {
                    my_order();
                }
                break;
            case R.id.topBar_right_layout:

                break;
        }
    }

    public void login() {
        Utils.showToastShort(context, "请先登录");
        Intent intent = new Intent(context, UserLoginActivity.class);
        startActivity(intent);
    }

    //我的订单
    public void my_order() {
        Intent intent = new Intent(context, MyOrderActivity.class);
        startActivity(intent);
    }

    // An ExecutorService that can schedule commands to run after a given delay,
    // or to execute periodically.
    private ScheduledExecutorService scheduledExecutorService;

    // 切换当前显示的图片
    private Handler handlerAD = new Handler() {
        public void handleMessage(android.os.Message msg) {
            viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
        }
    };


    /**
     * 换行切换任务
     *
     * @author Administrator
     */
    private class ScrollTask implements Runnable {
        public void run() {
            synchronized (viewPager) {
                System.out.println("currentItem: " + currentItem);
                currentItem = (currentItem + 1) % ads.size();
                handlerAD.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author Administrator
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
//            tv_title.setText((String) TJ_lists.get(currentItem).get("title"));
        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    private class ImageAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        private Vector<HashMap<String, Object>> mLists;

        public ImageAdapter(Vector<HashMap<String, Object>> mLists) {
            this.mLists = mLists;
            inflater = LayoutInflater.from(context);
        }

        public void setData(Vector<HashMap<String, Object>> mLists) {
            this.mLists = mLists;
            notifyDataSetChanged();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mLists.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.viewpager_info_item, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("title", (String) mLists.get(position).get("tit"));
                    intent.putExtra("url", (String) mLists.get(position).get("url"));
                    startActivity(intent);
                }
            });
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            spinner.setVisibility(View.GONE);

            if (mLists != null && mLists.size() > 0) {
                HashMap<String, Object> map = mLists.get(position);
                String pic = (String) map.get("pic");
                Utils.showLog("pic = " + pic);
                ImageLoaderBitmapUtil.getInstance(context).showListImage(pic, imageView, R.drawable.custom_default_bg);
            }
            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, MyOrderQuoteActivity.class);
        intent.putExtra(CommonData.MAPS, mList.get(i));
        startActivity(intent);
    }

    //获取订单摘要
    public void getData(final String url) {
        Map<String, Object> params = new HashMap<String, Object>();
        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (url.equals(Constants.ORDER_GET_summaries)) {
                    setData(response);
                } else {
                    setAD(response);
                }
            }

            @Override
            public void onResultJson(String json) {
                super.onResultJson(json);
            }
        });

    }

    public void setData(ResponseModel responseModel) {
        mList = responseModel.getMaps();
        if (mList != null && mList.size() > 0) {
            adapter.setData(mList);
            adapter.notifyDataSetChanged();
            showOrder(true);
        } else {
            showOrder(false);
        }

    }

    public void setAD(ResponseModel response) {
        ads = response.getMaps();
        if (ads != null && ads.size() > 0) {
            String pic = (String) ads.get(0).get("pic");
            String url = (String) ads.get(0).get("pic");
            imageAdapter.setData(ads);
        }
    }
}
