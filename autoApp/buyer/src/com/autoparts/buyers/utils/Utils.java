package com.autoparts.buyers.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
    private static final String TAG = "Utils";

    public static enum NetWorkMethod {NO, CMNET, CMWAP, WIFI}

    //注，正式上线后要改为false
    private static final boolean isShowLog = true;
    private static final boolean isShowToast = true;

    public static int getDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int displayHeight = wm.getDefaultDisplay().getHeight();
        return displayHeight;
    }

    public static int getDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int displaywidth = wm.getDefaultDisplay().getWidth();
        return displaywidth;
    }

    public static byte[] readStream(InputStream inputStream) {
        byte[] bre = null;
        if (inputStream != null) {
            byte[] buf = new byte[64];
            int len;
            ByteArrayOutputStream bouf = new ByteArrayOutputStream();
            while (true) {
                try {
                    len = inputStream.read(buf);
                    if (len == -1) {
                        break;
                    }
                    bouf.write(buf, 0, len);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            bre = bouf.toByteArray();
        }

        return bre;
    }

    /*
   * 字符串截取
   * @param String
   * return List<String>
   * */
    public static List<String> strSplit(String str) {
        List<String> lists = new ArrayList<String>();
        String temp_str = str;
        String[] strings = temp_str.split(",");
        lists = Arrays.asList(strings);
        return lists;
    }

    public static String getString(String string) {
        String result = null;
        String v = "'" + string + "'";
        try {
            result = new JSONTokener(v).nextValue().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getSystemDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);//获取年份
        int month = ca.get(Calendar.MONTH) + 1;//获取月份
        int day = ca.get(Calendar.DATE);//获取日
        return year + "年" + month + "月" + day + "日";

    }


    public static String getSystemDotDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);//获取年份
        int month = ca.get(Calendar.MONTH) + 1;//获取月份
        int day = ca.get(Calendar.DATE);//获取日
        return year + month + day + "";

    }


    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 完整的判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    // 只创建文件
    public static boolean createFile(File file) {
        try {
            if (file.exists()) {
                return true;
            } else {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    Utils.showLog("目录", "========>" + parentFile);
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }

            if (!file.exists()) {

                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 检测网络连接是否可用
     *
     * @param ctx
     * @return true 可用; false 不可用
     */
    public static boolean isNetwork(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo[] netinfo = cm.getAllNetworkInfo();
        if (netinfo == null) {
            return false;
        }
        for (int i = 0; i < netinfo.length; i++) {
            if (netinfo[i].isConnected()) {
                return true;
            }
        }
        return false;
    }


    /**
     * @param context
     * @return
     * @author sky
     * 获取当前的网络状态  -1：没有网络NO  1：WIFI网络 WIFI 2：wap网络CMWAP 3：net网络 CMNET
     */
    public static NetWorkMethod getAPNType(Context context) {
        NetWorkMethod netType = NetWorkMethod.NO;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            Log.e("networkInfo.getExtraInfo()", "networkInfo.getExtraInfo() is " + networkInfo.getExtraInfo());
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = NetWorkMethod.CMNET;
            } else {
                netType = NetWorkMethod.CMWAP;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NetWorkMethod.WIFI;
        }
        return netType;
    }


    /**
     * auther:liuHuaCheng
     * 提示用户网络错误对话框
     */
    public static void AlertNetError(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("网络有错误");
        builder.setMessage("无法连接网络，请检查网络配置");
        builder.setPositiveButton("网络设置", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    public static void browser(Context context, String weburl) {
        Uri uri = Uri.parse(weburl);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(it);
    }


    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        return width;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        return height;
    }

    public static void showLog(String TAG, String message) {
        if (isShowLog)
            Log.d(TAG, message);
    }

    public static void showLog(String message) {
        if (isShowLog)
            Log.d("Debug", message);
    }

    public static void showLog(int i, String message) {
        if (isShowLog)
            Log.d("Debug", i + "==" + message);
    }

    public static void showToastShort(Context context, String message) {
        if (isShowToast)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }

    public static String GetCurrentTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param a
     * @return
     */
    public static String parseTime(String a) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);//MMM dd hh:mm:ss Z yyyy
        Date date = sdf.parse(a);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }


    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param a
     * @return
     */
    public static String parseDate(String a) throws ParseException {
        String dateString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (a == null) {
            Date date = new Date();
            dateString = sdf.format(date);
        } else {
            dateString = sdf.format(a);
        }

        return dateString;
    }

    /**
     * 获取时间格式
     */
    public static String getTimeHH(Object time) {
        try {
            long date = Long.valueOf((String) time);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return df.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            e.getStackTrace();
        }
        return "";
    }

    /**
     * 获取时间格式
     */
    public static String getTime(Object time) {
        try {
            long date = Long.valueOf((String) time);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            e.getStackTrace();
        }
        return "";
    }

    /**
     * 获取时间格式
     */
    public static String getTime(String time) {
        try {
            long date = Long.valueOf(time);
//            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 hh:ss");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return df.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            e.getStackTrace();
        }
        return "";
    }


    /**
     * 获取网络状态
     *
     * @return 0--无网络连接，1--wifi连接，2--GPRS连接
     */

    public static int GetNetworkType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        showLog(TAG, "info===" + info);
        if (info == null) {
            return 0;
        } else {
            int type = info.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                return 1;
            } else {
                showLog(TAG, "info type ===" + type);
                return 2;
            }
        }

    }


    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    //获取屏幕方向
    public static String getScreenDirection(Context context) {
        String screenDirection = "PORTRAIT";
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏
            screenDirection = "LANDSCAPE";
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            //竖屏
            screenDirection = "PORTRAIT";
        }
        return screenDirection;
    }

    public static String getJson(byte[] bytes) {
        String json = null;
        try {
            json = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 判断是否有sd卡
     *
     * @return boolean true 是 ， false 否
     */
    public static boolean isSDCARDMounted() {
        String status = Environment.getExternalStorageState();

        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }

    //浮点型数据保留小数点
    public static double getDouble(float d) {
        BigDecimal bg = new BigDecimal(d);
        double f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    //获取手机型号
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    public static boolean isHeadsetOn(Context context) {
        AudioManager localAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        boolean b = localAudioManager.isWiredHeadsetOn();
        return b;
    }


    //浮点型数据保留小数点
    public static String getFloatFormat(float d) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(d);
    }

    //浮点型数据保留小数点
    public static String getFloatFormat(String str) {
        float f2 = Float.parseFloat(str);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(f2);

    }

    //获取TextView现实的数据
    public static String getTextString(String str) {
        if (!TextUtils.isEmpty(str) && !str.equals("null")) {
            return str;
        } else {
            return "";
        }
    }

    //获取TextView现实的数据
    public static String getFloatString(String str) {
        if (str != null) {
            if (str.contains(".0")) {
//                str = str.substring(0, str.length() - (str.indexOf(".") - 2));
            }
            return str;

        }else {
            return "";
        }
    }

    //判断字符串是否为空
    public static boolean getStringNull(String str) {
        if (!TextUtils.isEmpty(str)&& !str.equals("null")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 查询手机内非系统应用
     * @param context
     * @return
     */
    public  List<PackageInfo> getAllAppsNoSystem(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            Utils.showLog(i,"pak====="+pak.packageName);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }

    public static void call(Context context,String tel){

        //                startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+info.get("mobile"))));
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
