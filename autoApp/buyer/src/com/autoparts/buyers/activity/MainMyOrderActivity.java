package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.MyInquiryAdapter;
import com.autoparts.buyers.view.viewpagerindicator.TabPageIndicator;

/**
 * 我的订单
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MainMyOrderActivity extends BaseFragmentActivity {
    private Context context;
    private static final String[] CONTENT = new String[]{"进行中", "待评价", "已完成"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inquiry_sort);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        String title = getString(R.string.my_order_text);
        setTitle(title);
        setRightView("筛选",-1);
        context = this;

        FragmentPagerAdapter adapter = new MyAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

    //优化ViewPager ListView滑动卡屏问题 使用Fragment
    public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public Fragment getItem(int position) {
            return new ArrayListFragment().newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = CONTENT[position % CONTENT.length].toUpperCase();
            return title;
        }
    }

    public class ArrayListFragment extends ListFragment {
        int mNum;
        private TextView viewpager_item_text;
        private ListView mListView;
        private Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                showDialog("是否取消询价", true);
            }
        };

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        public ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();
            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);
            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.viewpager_item, container, false);
            mListView = (ListView) v.findViewById(android.R.id.list);
            viewpager_item_text = (TextView) v.findViewById(R.id.viewpager_item_text);
            mListView.setDivider(null);
            int divider = (int) getResources().getDimension(R.dimen.common_padding_middle);
            mListView.setDividerHeight(divider);
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Intent intent = new Intent(context, MyOrderQuoteListActivity.class);
            startActivity(intent);
        }
    }

}
