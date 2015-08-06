package com.autoparts.buyers.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.autoparts.buyers.R;
import com.autoparts.buyers.application.MyApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * ImageLoader 图片加载工具类
 * Created by:Liuhuacheng
 * Created time:14-11-20
 */
public class ImageLoaderBitmapUtil {
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private LinearLayout.LayoutParams params;
    private Context context;

    private static ImageLoaderBitmapUtil instance;

    public static synchronized ImageLoaderBitmapUtil getInstance(Context context) {
        if (instance == null) {
            instance = new ImageLoaderBitmapUtil(context);
        }
        return instance;
    }

    private ImageLoaderBitmapUtil(Context context) {
        this.context = context.getApplicationContext();
    }


    public void getBitmapOptions(int radius) {
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_photo_default)
                .showImageForEmptyUri(R.drawable.icon_photo_default)
                .showImageOnFail(R.drawable.icon_photo_default)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(radius))
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//完全伸展
                .build();
    }

    public DisplayImageOptions getOptions(int drawable) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(drawable)
                .showImageOnFail(drawable)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        return options;
    }

    public DisplayImageOptions getOptionsLoading(int drawable) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawable)
                .showImageForEmptyUri(drawable)
                .showImageOnFail(drawable)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .cacheInMemory(true)
                .build();
        return options;
    }

    public DisplayImageOptions getPicOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.icon_photo_default)
                .showImageForEmptyUri(R.drawable.icon_photo_default)
                .showImageOnFail(R.drawable.icon_photo_default)
                .cacheOnDisk(true)
                .considerExifParams(true)
//                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//完全伸展
                .build();
        return options;
    }

    public DisplayImageOptions getPicOptionsCache(int drawable) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.icon_default_big)
                .showImageForEmptyUri(drawable)
                .showImageOnFail(drawable)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
//                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//完全伸展
                .build();
        return options;
    }

    public void showBitmap(String url, ImageView imageView, int radius) {
        getBitmapOptions(radius);
        imageLoader.displayImage(url, imageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
            }
        });
    }

    public void showBitmap(String url, ImageView imageView) {
        Utils.showLog("showBitmap url=" + url);
        imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, imageView, getPicOptions());
    }

    public void showBitmapDefault(String url, ImageView imageView, int drawable) {
        imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, imageView, getOptions(drawable), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                int height = loadedImage.getHeight();
                int width = loadedImage.getWidth();
            }
        });
    }

    public void showListImage(String url, ImageView imageView, int drawable) {
        ImageLoader.getInstance().displayImage(url, imageView, getOptions(drawable));
    }

    public void showListImage(String url, ImageView imageView, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(url, imageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
//                Utils.showLog("imageUri=="+imageUri+"===loadedImage"+loadedImage.getWidth());
            }
        });
    }

    public void showThumbnailImage(String url, ImageSize targetSize, final ImageView imageView,
                                   DisplayImageOptions options) {

        ImageLoader.getInstance().loadImage(url, targetSize, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                if (loadedImage != null && imageView != null) {
//                    holder.picture_item_photo.setImageBitmap(loadedImage);
//                    ImageView imageViewByTag = (ImageView) imageLayout.findViewWithTag(imageUri);
                    imageView.setImageBitmap(loadedImage);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "Input/Output error";
                        break;
                    case DECODING_ERROR:
                        message = "Image can't be decoded";
                        break;
                    case NETWORK_DENIED:
                        message = "Downloads are denied";
                        break;
                    case OUT_OF_MEMORY:
                        message = "Out Of Memory error";
                        System.gc();
                        break;
                    case UNKNOWN:
                        message = "Unknown error";
                        break;
                }
                Utils.showLog("message===" + message);
//                spinner.setVisibility(View.GONE);
            }

        });

    }

    public void getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();
        Utils.showLog("width==" + width);
        params = new LinearLayout.LayoutParams(width, width);
    }

}
