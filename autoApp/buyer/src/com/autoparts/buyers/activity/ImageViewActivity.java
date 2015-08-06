package com.autoparts.buyers.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by:Liuhuacheng
 * Created time:15-6-23
 */
public class ImageViewActivity extends BaseActivity {

    private Context context;
    private ImageView image_show;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_layout);
        context = this;
        image_show = (ImageView) findViewById(R.id.image_show);
        image_show.setOnClickListener(this);

        url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            ImageLoader.getInstance().displayImage(url, image_show, getOptions(),new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                }
                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    Utils.showToastShort(context, "图片加载失败");
                }
                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                }
                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.image_show:
                finish();
                break;
        }
    }

    public DisplayImageOptions getOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.custom_default_bg)
                .showImageForEmptyUri(R.drawable.custom_default_bg)
                .showImageOnFail(R.drawable.custom_default_bg)
                .cacheOnDisk(true)
                .considerExifParams(true)
//                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//完全伸展
                .build();
        return options;
    }
}
