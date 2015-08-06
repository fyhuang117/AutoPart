/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.autoparts.buyers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.autoparts.buyers.R;
import com.autoparts.buyers.network.HttpClientEntity;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.view.viewpagerindicator.CirclePageIndicator;
import com.loopj.android.http.RequestParams;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class SplashActivity extends BaseActivity {
    private Context context;

    private int currentPosition = 0;
    private ImageView welcom_bg;
    private CirclePageIndicator mIndicator;
    private int[] images = {R.drawable.welcom_image1, R.drawable.welcom_image2, R.drawable.welcom_image3};
    private long time = 1500;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                welcom_bg.setVisibility(View.GONE);
            } else {
                start();
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.slpash_view);
        init();
    }

    private void init() {
        context = this;
        welcom_bg = (ImageView) findViewById(R.id.welcom_bg);
        boolean b = preferences.getFirstRunning();
//        Utils.showToastShort(context,"b=="+b);
        if (true) {
            handler.sendEmptyMessageDelayed(1, time);
        } else {
            handler.sendEmptyMessageDelayed(0, time);
            initViewPager();
        }

//        checkVersion();
//        showDialog("发现新版本",true);
    }

    public void initViewPager() {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ImageAdapter());
        pager.setCurrentItem(currentPosition);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                currentPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private class ImageAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        ImageAdapter() {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.splash_item, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            if (position == images.length - 1) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        start();
                    }
                });
            } else {
                imageView.setOnClickListener(null);
            }
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            imageView.setBackground(getResources().getDrawable(images[position]));
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

    public void start() {
        preferences.setFirstRunning(true);
        Intent intent = new Intent(context, MainDrawerActivity.class);
        startActivity(intent);
        finish();
    }

    public void checkVersion() {
        String url = Constants.USER_REGISTER;
        RequestParams params = new RequestParams();
        HttpClientEntity.get(context, url, new HttpResultHandler() {

        });
    }

}