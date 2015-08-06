package com.autoparts.sellers.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.*;

/**
 * 保存图片工具类
 * Created by:Liuhuacheng
 * Created time:14-11-14
 */
public class BitmapUtil {
    private static final String TAG = "SaveBitmapUtil";
    private Context context;
    private Dialog dialog;
    ProgressDialog progressDialog;


    public BitmapUtil(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("正在保存图片，请稍候");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
    }

    //更新相册
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Utils.showLog("handleMessage:" + Build.VERSION.SDK_INT);
            progressDialog.cancel();
            if (Build.VERSION.SDK_INT <= 18) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            } else {
                MediaScannerConnection.scanFile(context, new String[]{"file://" + Environment.getExternalStorageDirectory()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        // TODO Auto-generated method stub
                    }
                });
            }
        }
    };

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    // 将Bitmap转换成InputStream
    public static InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
    public static Bitmap decodeFile(File f) {
        // decode image size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
        // Find the correct scale value. It should be the power of 2.
        final int REQUIRED_SIZE = 360;//2700
        int width_tmp = options.outWidth, height_tmp = options.outHeight;
        int scale = 1;

        int scaleWidth = width_tmp / REQUIRED_SIZE;
        int scaleHeight = height_tmp / REQUIRED_SIZE;
        scale = scaleHeight > scaleWidth ? scaleWidth : scaleHeight;
//        if (scale <= 1) {
//            return null;
//        }
        // decode with inSampleSize
        options.inSampleSize = scale;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = false;
        try {
            bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
            return null;
        }
        return bitmap;

    }

    // Bitmap转换成byte[]
    public static Bitmap BitmapRotate(Bitmap bm, int rotate) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        final int widthOrig = bm.getWidth();
        final int heightOrig = bm.getHeight();
        bitmap = Bitmap.createBitmap(bm, 0, 0, widthOrig, heightOrig, matrix, true);
        return bitmap;
    }

    // Bitmap转换成byte[]
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] readInput(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        out.close();
        in.close();
        return out.toByteArray();
    }

    //第一：我们先看下质量压缩方法：
    public static ByteArrayInputStream compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //判断如果图片大于1M,进行压缩避免在生成图,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            Utils.showLog("params", "options==" + options);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        if (image != null && !image.isRecycled()) {
            image.recycle();
        }
        return isBm;
    }

    //第一：我们先看下质量压缩方法：
    public static byte[] compressBitmapToByte(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //判断如果图片大于1M,进行压缩避免在生成图,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            Utils.showLog("params", "options==" + options);
        }
        byte[] b = baos.toByteArray();//把压缩后的数据baos存放到ByteArrayInputStream中
        if (image != null && !image.isRecycled()) {
            image.recycle();
        }
        return b;
    }

    /**
     * 二进制转字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString();
    }

    public static byte[] getByteArrayFromFile(String fileName) {
        File file = null;
        try {
            file = new File(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            return null;
        }
        byte[] byteArray = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = fis.read()) != -1) {
                bytestream.write(ch);
            }
            byteArray = bytestream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return byteArray;
    }

}
