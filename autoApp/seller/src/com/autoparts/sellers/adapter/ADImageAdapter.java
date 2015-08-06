package com.autoparts.sellers.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.autoparts.sellers.R;
import com.autoparts.sellers.activity.WebViewActivity;
import com.autoparts.sellers.utils.ImageLoaderBitmapUtil;
import com.autoparts.sellers.utils.Utils;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by:Liuhuacheng
 * Created time:15-6-28
 */
public class ADImageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private Vector<HashMap<String, Object>> mLists;

    public ADImageAdapter(Context context,Vector<HashMap<String, Object>> mLists) {
        this.context = context;
        this.mLists = mLists;
        inflater = LayoutInflater.from(context);
    }

    public void setData(Vector<HashMap<String, Object>> mLists ){
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
                Intent intent = new Intent(context,WebViewActivity.class);
                intent.putExtra("title",(String)mLists.get(position).get("tit"));
                intent.putExtra("url",(String)mLists.get(position).get("url"));
                context.startActivity(intent);
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
