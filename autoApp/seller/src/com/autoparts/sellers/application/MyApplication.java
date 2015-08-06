package com.autoparts.sellers.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import cn.jpush.android.api.JPushInterface;
import com.autoparts.sellers.utils.Constants;
import com.autoparts.sellers.utils.Utils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {
    private final static String TAG = "MyApplication";

    private List<Activity> activityList = new LinkedList<Activity>();

    private static MyApplication instance;

    public MyApplication() {

    }


    /**
     * 开始调用
     */
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        instance = this;
        initImageLoader(getApplicationContext());

        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.setLatestNotificationNumber(getApplicationContext(), 3);
        super.onCreate();
    }

    /**
     * 结束调用
     */
    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
//		removeAllActivity();
        super.onTerminate();
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    // 单例模式中获取唯一的MyApplication实例
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;

    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 容器中清除Activity
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }


    public void removeAllActivity() {
        Utils.showLog("activityList===" + activityList.size());
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.removeAll(activityList);
        Utils.showLog("activityListnew===" + activityList.size());
    }

    public void goHome() {
        Utils.showLog("goHome size==" + activityList.size());
        Activity Main = null;
        for (int i = 0; i < activityList.size(); i++) {
            Activity activity = activityList.get(i);
            String name = activity.getClass().getName();
            if (!name.contains("NavigationDrawerActivity")) {
                Utils.showLog("goHome===="+name);
                activity.finish();
            }else {
                Main = activity;
                Utils.showLog("goHome!!!===="+name);
            }
        }
        addActivity(Main);
        Utils.showLog("goHome size==" + activityList.size());
    }

    public void settingLogout() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        //修改缓存目录
        File cacheDir;
        cacheDir = new File(Environment.getExternalStorageDirectory() + Constants.cacheDir);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(500 * 1024 * 1024) // 500 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .memoryCacheSize(10 * 1024 * 1024)
                .diskCache(new UnlimitedDiscCache(cacheDir)) // default
                .diskCacheFileCount(100)
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
