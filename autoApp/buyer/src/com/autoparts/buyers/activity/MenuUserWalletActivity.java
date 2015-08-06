package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.MyMessageAdapter;
import com.autoparts.buyers.fragment.CouponFragment;
import com.autoparts.buyers.fragment.WalletFragment;
import com.autoparts.buyers.view.viewpagerindicator.TabPageIndicator;
import com.autoparts.buyers.view.viewpagerindicator.TabPageMessageIndicator;

/**
 * 我的钱包
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class MenuUserWalletActivity extends BaseFragmentActivity {
    private Context context;
    private static final String[] CONTENT = new String[] { "账户信息", "代金券"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.menu_waller);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        String title = getString(R.string.waller_title);
        setTitle(title);
        context = this;

        FragmentPagerAdapter adapter = new MyAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
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
            if (position == 0) {
                return new WalletFragment().newInstance();
            }else {
                return new CouponFragment().newInstance(position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = CONTENT[position % CONTENT.length].toUpperCase();
            return title;
        }
    }



}
