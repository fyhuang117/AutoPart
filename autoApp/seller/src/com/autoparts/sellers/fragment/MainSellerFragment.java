package com.autoparts.sellers.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.autoparts.sellers.R;
import com.autoparts.sellers.activity.*;
import com.autoparts.sellers.adapter.ADImageAdapter;
import com.autoparts.sellers.adapter.MainOrderBeingAdapter;
import com.autoparts.sellers.autoscrollviewpager.AutoScrollViewPager;
import com.autoparts.sellers.network.HttpClientUtils;
import com.autoparts.sellers.network.HttpResultHandler;
import com.autoparts.sellers.network.ResponseModel;
import com.autoparts.sellers.preferences.Preferences;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.utils.Constants;
import com.autoparts.sellers.utils.JPushInit;
import com.autoparts.sellers.utils.Utils;
import com.autoparts.sellers.view.viewpagerindicator.CirclePageIndicator;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;

public class MainSellerFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Context context;
    private TextView tv_title;
    private AutoScrollViewPager viewPager;
    private ADImageAdapter adAdapter;
    private LinearLayout buyer_order_view;//登陆后的订单显示
    //滚动新闻
    private int currentItem = 0; // 当前图片的索引号
    private int[] images = {R.drawable.ad_viewpagerft01, R.drawable.ad_viewpagerft02};

    private ListView mListView;//正在进行中的订单
    private MainOrderBeingAdapter adapter;
    public Preferences preferences;

    private RelativeLayout my_inquiry_view, my_order_view, search_view;
    private Vector<HashMap<String, Object>> mList;
    private LinearLayout data_null_view;

    private Vector<HashMap<String, Object>> ads;

    private TextView my_bidding_num, my_order_num;
    private SwipeRefreshLayout swipeRefreshLayout;


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
        View view = inflater.inflate(R.layout.main_seller_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        context = getActivity();
        initADView(view);

        buyer_order_view = (LinearLayout) view.findViewById(R.id.buyer_order_view);
        my_inquiry_view = (RelativeLayout) view.findViewById(R.id.my_inquiry_view);
        my_inquiry_view.setOnClickListener(this);
        my_order_view = (RelativeLayout) view.findViewById(R.id.my_order_view);
        my_order_view.setOnClickListener(this);
        search_view = (RelativeLayout) view.findViewById(R.id.search_view);
        search_view.setOnClickListener(this);
        mListView = (ListView) view.findViewById(R.id.mListView);
        mList = new Vector<HashMap<String, Object>>();
        adapter = new MainOrderBeingAdapter(context, mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        mListView.setVisibility(View.VISIBLE);

        data_null_view = (LinearLayout) view.findViewById(R.id.data_null_view);
        if (data_null_view != null) ;
        data_null_view.setVisibility(View.GONE);

        my_bidding_num = (TextView) view.findViewById(R.id.my_bidding_num);
        my_order_num = (TextView) view.findViewById(R.id.my_order_num);
        my_bidding_num.setVisibility(View.INVISIBLE);
        my_order_num.setVisibility(View.INVISIBLE);

        if (preferences.getIsLogin()) {
            String alias = JPushInit.getDeviceInfo(context);
            Utils.showLog("alis==" + alias);
            new JPushInit(context).setAlias(alias);
            CommonData.getInstance(context).setUserAlias(alias);
        }

        refresh(view);

    }

    //刷新view与逻辑
    public void refresh(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(Constants.INQUIRE_LIST);
            }
        });
    }


    public void setNum() {
        String biddingNum = preferences.getStringData(preferences.BIDDING_NUM);
        String orderNum = preferences.getStringData(preferences.ORDER_NUM);
        if (!TextUtils.isEmpty(biddingNum) && !biddingNum.equals("0")) {
            my_bidding_num.setText(biddingNum);
            my_bidding_num.setVisibility(View.VISIBLE);

        } else {
            my_bidding_num.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(orderNum) && !orderNum.equals("0")) {
            my_order_num.setText(orderNum);
            my_order_num.setVisibility(View.VISIBLE);

        } else {
            my_order_num.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (preferences.getIsLogin()) {
            getData(Constants.BIDDING_NUM);
            getData(Constants.ORDER_NUM);
            //获取用户信息
            CommonData.getInstance(context).getUserInfoData();
        }
        getData(Constants.INQUIRE_LIST);

    }

    public void initADView(View view) {
        ads = new Vector<HashMap<String, Object>>();
        adAdapter = new ADImageAdapter(context, ads);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        viewPager = (AutoScrollViewPager) view.findViewById(R.id.viewPager);
        viewPager.startAutoScroll(3000);
        viewPager.setInterval(3000);
        viewPager.setCycle(true);
//        viewPager.setBorderAnimation(true);
        viewPager.setAdapter(adAdapter);// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        CirclePageIndicator mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);
        mIndicator.setOnPageChangeListener(new MyPageChangeListener());

        getData(Constants.USER_AD);

    }

    @Override
    public void onClick(View view) {
        int userState = preferences.getUserState();
        switch (view.getId()) {
            case R.id.topBar_left_layout:
                Intent intentLogin = new Intent(context, MainDrawerActivity.class);
                startActivity(intentLogin);
                break;

            case R.id.topBar_right_layout:
                String tel = "";
                Utils.call(context, tel);
                break;
            case R.id.my_inquiry_view:
                if (!preferences.getIsLogin()) {
                    login();
                } else if (userState != 1) {
                    CommonData.getInstance(context).showUserState(userState);
                } else {
                    my_bidding();
                }
                break;
            case R.id.my_order_view:
                if (!preferences.getIsLogin()) {
                    login();
                } else if (userState != 1) {
                    CommonData.getInstance(context).showUserState(userState);
                } else {
                    my_order();
                }
                break;
            case R.id.search_view:
                search();
                break;
        }
    }

    //我竞价列表
    public void my_bidding() {
        Intent intent = new Intent(context, SellerMyBiddingActivity.class);
        startActivity(intent);
    }

    //我的订单
    public void my_order() {
        Intent intent = new Intent(context, SellerOrderTabActivity.class);
        startActivity(intent);
    }

    //搜索
    public void search() {
        Intent intent = new Intent(context, SellerSearchActivity.class);
        startActivityForResult(intent, CommonData.REQUEST_SEARCH);
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
                currentItem = (currentItem + 1) % images.length;
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
        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, SellerInquiryDetailActivity.class);
        intent.putExtra(CommonData.MAPS, mList.get(i));
        startActivity(intent);
    }

    //获取询价单
    public void getData(final String url) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("car_id", year_id);
        params.put("part_type", parttypeid);
        params.put("part_band", partbandid);

        HttpClientUtils.post(context, url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                if (url.equals(Constants.INQUIRE_LIST)) {
                    setData(response);
                } else if (url.equals(Constants.USER_AD)) {
                    setAD(response);
                } else if (url.equals(Constants.BIDDING_NUM)) {
                    preferences.setStringData(preferences.BIDDING_NUM, response.getMap().get("count"));
                    setNum();
                } else if (url.equals(Constants.ORDER_NUM)) {
                    preferences.setStringData(preferences.ORDER_NUM, response.getMap().get("count"));
                    setNum();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                swipeRefreshLayout.setRefreshing(false);
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
            setDataNull(false);
        } else {
            adapter.setData(mList);
            setDataNull(true);
        }
    }

    public void setAD(ResponseModel response) {
        ads = response.getMaps();
        if (ads != null && ads.size() > 0) {
            adAdapter.setData(ads);
        }
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

    public void login() {
        Utils.showToastShort(context, "请先登录");
        Intent intent = new Intent(context, UserLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {//听力测试结束
            switch (requestCode) {
                case CommonData.REQUEST_SEARCH:
                    year_id = data.getStringExtra("year");
                    parttypeid = data.getStringExtra("sort");
                    partbandid = data.getStringExtra("brand");

                    if (TextUtils.isEmpty(year_id)) {
                        year_id = "0";
                    }
                    if (TextUtils.isEmpty(parttypeid)) {
                        parttypeid = "0";
                    }
                    if (TextUtils.isEmpty(partbandid)) {
                        partbandid = "0";
                    }
                    break;
            }
        }
    }

    private String parttypeid = "0";//配件ID
    private String year_id = "0";//年款 ID
    private String partbandid = "0";//品牌 ID

}
