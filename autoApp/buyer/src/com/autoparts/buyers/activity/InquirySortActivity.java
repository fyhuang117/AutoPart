package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.CommonLetterlistAdapter;
import com.autoparts.buyers.adapter.CommonListAdapter;
import com.autoparts.buyers.adapter.InquirySortlistAdapter;
import com.autoparts.buyers.fragment.SortAllFragment;
import com.autoparts.buyers.fragment.SortHotFragment;
import com.autoparts.buyers.model.CommonLetterModel;
import com.autoparts.buyers.model.ContactUtils;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Utils;
import com.autoparts.buyers.view.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 已修改不用此类  InquirySortNewActivity修改到这
 * 配件分类
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class InquirySortActivity extends BaseFragmentActivity {
    private Context context;
    private static final String[] CONTENT = new String[] { "热门分类", "全部分类"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inquiry_sort);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        String title = getString(R.string.inquiry_text_sort);
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
            if (position == 0){
                return new SortHotFragment().newInstance(position);
            }else {
                return new SortAllFragment().newInstance(position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = CONTENT[position % CONTENT.length].toUpperCase();
            return title;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK,data);
            finish();
            switch (requestCode) {
                case CommonData.REQUEST_CODE_SORT:
                    setResult(RESULT_OK,data);
                    finish();
                    break;
            }
        }
    }

}
