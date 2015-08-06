/*
 * Copyright (C) 2013 Antonio Leiva
 * Copyright (C) 2013 Manuel Peinado
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.autoparts.buyers.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.MainDrawerAdapter;
import com.autoparts.buyers.fragment.MainBuyerFragment;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.ImageLoaderBitmapUtil;
import com.autoparts.buyers.utils.Utils;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class MainDrawerActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FrameLayout drawer_view;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mCityNames;
    //    private int[] mCityImages;
    private TextView user_shop_name;
    private ImageView icon_profile;
    private MainDrawerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_navigation_drawer);
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        context = this;
        setTitle(getString(R.string.app_name));
        setRightView("客服", -1);
        isBack = true;
        mTitle = mDrawerTitle = getTitle();
        mCityNames = getResources().getStringArray(R.array.drawer_items);
//        TypedArray typedArray = getResources().obtainTypedArray(R.array.city_images);
//        mCityImages = new int [typedArray.length()];
//        for (int i = 0; i < typedArray.length(); ++i) {
//            mCityImages[i] = typedArray.getResourceId(i, 0);
//        }
//        typedArray.recycle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        drawer_view = (FrameLayout) findViewById(R.id.drawer_view);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        // set up the drawer's list view with items and click listener

        adapter = new MainDrawerAdapter(context, mCityNames);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(this);

        // enable ActionBar app icon to behave as action to toggle nav drawer

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
            }

            public void onDrawerOpened(View drawerView) {
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        user_shop_name = (TextView) findViewById(R.id.user_shop_name);
        icon_profile = (ImageView) findViewById(R.id.icon_profile);

        if (preferences.getIsLogin()) {
            getData(Constants.USER_INFO);
        }
        getData(Constants.USER_PHONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.getIsLogin()) {
            getData(Constants.USER_MESSAGE_COUNT);
            setLeftView("", R.drawable.ic_home_left_menu);
        } else {
            setLeftView("登录", -1);
        }
        setViewData();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
//        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private Class[] activitys = {UserMessageActivity.class, CouponActivity.class, null, MenuSystemActivity.class};

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Class activity = activitys[position];
        if (activity != null) {
            Intent intent = new Intent(context, activitys[position]);
            startActivity(intent);
        } else {
            if (position == 2) {
                String share_id =  preferences.getStringData(preferences.SHARE_ID);
                CommonData.getInstance(context).share(MainDrawerActivity.this,share_id);
            }
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new MainBuyerFragment();
        Bundle args = new Bundle();
//        args.putInt(SampleFragment.ARG_IMAGE_RES, mCityImages[position]);
//        args.putInt(SampleFragment.ARG_ACTION_BG_RES, R.drawable.ab_background);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(drawer_view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topBar_left_layout:
                if (preferences.getIsLogin()) {
                    if (mDrawerLayout.isDrawerOpen(drawer_view)) {
                        mDrawerLayout.closeDrawer(drawer_view);
                    } else {
                        mDrawerLayout.openDrawer(drawer_view);
                    }
                } else {
                    Intent intentLogin = new Intent(context, UserLoginActivity.class);
                    startActivity(intentLogin);
                }

                break;
            case R.id.topBar_right_layout:
                String tel = preferences.getStringData("tel");
                if (TextUtils.isEmpty(tel)) {
                    Utils.showToastShort(context, "暂无客服电话");
                } else {
                    Utils.call(context, tel);
                }
                break;

        }
    }

    //退出登录
    public void exit(View view) {
        preferences.setIsLogin(false);
//        preferences.clearData();
        application.removeAllActivity();
        Intent intent = new Intent(context, UserLoginActivity.class);
        startActivity(intent);
    }

    //用户信息
    public void user_info(View view) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        startActivity(intent);
    }

    //获取用户信息
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
                if (Constants.USER_PHONE.equals(url)) {
                    String tel = response.getMap().get("tel");
                    if (!TextUtils.isEmpty(tel)) {
                        preferences.setStringData("tel", tel);
                    }
                    return;
                } else if (url.equals(Constants.USER_INFO)) {
                    preferences.saveUsetInfo(response);
                    setViewData();
                } else if (url.equals(Constants.USER_MESSAGE_COUNT)) {
                    String count = response.getMap().get("count");
                    adapter.setData(count);
                }

            }
        });

    }

    public void setViewData() {
        String pic = preferences.getStringData("pic");
        if (!TextUtils.isEmpty(pic)) {
            ImageLoaderBitmapUtil.getInstance(context).showBitmap(pic, icon_profile);
        }

        String nam = preferences.getStringData("nam");
        String adr = preferences.getStringData("adr");
        Utils.showLog("nam==" + nam);
        Utils.showLog("adr==" + adr);
        user_shop_name.setText(nam);
    }

}
