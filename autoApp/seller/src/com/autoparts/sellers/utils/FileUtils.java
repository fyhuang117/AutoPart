package com.autoparts.sellers.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import org.apache.http.util.ByteArrayBuffer;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

/**
 * 文件操作工具类
 * Created by:Liuhuacheng
 * Created time:15-4-17
 */
public class FileUtils {

    private static final String TAG = "FileUtils";
    public final static String sd_card = Environment.getExternalStorageDirectory() + "/";
    public String cacheDir;
    public String dir = Constants.cacheDir;
    private String extension = "";


    public String getDir(){
        return sd_card+dir+"/";
    }

    /**
     * * 创建文件夹
     * * @param context
     * * @String dir   目录名
     * * @int i   目录名1代表在SD卡创建，2代表在缓存目录下创建，3代表都创建
     */
    public FileUtils(Context context, int i) {
        cacheDir = context.getCacheDir().getAbsolutePath();
        switch (i) {
            case 1:
                mkdir(context);
                break;
            case 2:
                mkdirCache(context);
                break;
            case 3:
                mkdir(context);
                mkdirCache(context);
                break;
        }
    }

    public String getCacheDir() {
        return cacheDir;
    }

    /**
     * SD卡创建文件夹
     * * @param context
     * * @String dir  文件夹目录名
     */
    public void mkdir(Context context) {
        File file;
        file = new File(sd_card + dir + "/");
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 缓存目录创建文件夹
     * * @param context
     * * @String dir   缓存目录名
     */
    public void mkdirCache(Context context) {
        File file;
        file = new File(cacheDir + "/" + dir + "/");
        if (!file.exists()) {
            file.mkdir();
            Utils.showLog(TAG, "!file.exists()");
        } else {
            Utils.showLog(TAG, "-=-=-=file.exists()");
        }
        Utils.showLog(TAG, "-=-=-=path====" + cacheDir + "/" + dir + "/");

    }

    /**
     * * 得到SD卡目录下具体文件
     * * @param context
     * * @String dir    SD卡文件名
     * * @String name   文件名
     */
    public File getSDFile(Context context, String key) {
        String name = String.valueOf(key.hashCode());
        return new File(sd_card + dir + "/" + name);
    }

    /**
     * *得到缓存目录下具体文件
     * * @param context
     * * @String name  缓存目录下文件名
     */
    public File getCacheFile(Context context, String key) {
        String name = String.valueOf(key.hashCode());
        return new File(cacheDir + "/" + dir + "/" + name);
    }

    /**
     * 保存图片到SD卡(无文件后缀)
     *
     * @throws java.io.IOException
     * @Context context
     * @String URL
     * @String data 字符串
     */
    public void saveFileInSD(String key, String data) throws IOException {
        String name = String.valueOf(key.hashCode());
        String path = sd_card + dir + "/";
        saveData(path, name, data);
    }

    /**
     * 保存图片到缓存文件
     *
     * @param key
     * @param data
     * @throws java.io.IOException
     */
    public void saveFileInCache(Context context, String key, String data) throws IOException {
        String path = cacheDir + "/" + dir + "/";
        String name = String.valueOf(key.hashCode());
        saveData(path, name, data);
    }

    /**
     * 图片保存工具类 文件形式
     *
     * @param path
     * @param fileName
     * @param data
     * @throws java.io.IOException
     */
    private void saveData(String path, String fileName, String data) throws IOException {
        // String name = MyHash.mixHashStr(AdName);
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        File file = new File(path + fileName + extension);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream outStream = new FileOutputStream(file);
        byte[] bytes = data.getBytes();
        outStream.write(bytes);
        outStream.close();
        Utils.showLog(TAG, "文件保存成功");
    }


    /**
     * 读取文件Cache
     *
     * @param key
     * @return
     * @throws java.io.IOException
     */
    public byte[] readFileInCache(Context context, String key) throws IOException {
        String name = String.valueOf(key.hashCode());
        byte[] tmp = readData(cacheDir + "/" + dir + "/", name);
        return tmp;
    }

    /**
     * 读取文件SD
     *
     * @param key
     * @return
     * @throws java.io.IOException
     */
    public byte[] readFileInSD(Context context, String key) throws IOException {
        String name = String.valueOf(key.hashCode());
        byte[] tmp = readData(sd_card + dir + "/", name);
        return tmp;
    }

    /**
     * 读取图片工具
     *
     * @param path
     * @param name
     * @return
     * @throws java.io.IOException
     */
    private byte[] readData(String path, String name) throws IOException {
        // String name = MyHash.mixHashStr(url);
        ByteArrayBuffer buffer = null;
        String paths = path + name + extension;
        Utils.showLog(TAG, "readData paths===" + paths);

        File file = new File(paths);
        if (!file.exists()) {
            return null;
        }
        InputStream inputstream = new FileInputStream(file);
        buffer = new ByteArrayBuffer(1024);
        byte[] tmp = new byte[1024];
        int len;
        while (((len = inputstream.read(tmp)) != -1)) {
            buffer.append(tmp, 0, len);
        }
        inputstream.close();
        return buffer.toByteArray();
    }


    /**
     * 判断文件是否存在  true存在  false不存在
     *
     * @param key
     * @return
     */
    public boolean compareSD(String key,String end_extension) {
        String name = String.valueOf(key.hashCode());
        String paths = sd_card + dir + "/" + name + end_extension;
        Utils.showLog(TAG, "compareSD paths===" + paths);
        File file = new File(paths);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 判断文件是否存在  true存在  false不存在
     *
     * @param key
     * @return
     */
    public boolean compareCache(Context context, String key) {
        String name = String.valueOf(key.hashCode());
        String paths = cacheDir + "/" + dir + "/" + name + extension;
        File file = new File(paths);
        if (!file.exists()) {
            return false;
        }
        return true;
    }


    //删除SD卡文件目录
    public boolean deleteDirectoryInSD() {
        String filePath = sd_card + dir;
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            Utils.showLog(TAG, "deleteDirectoryInSD list.length" + list.length);
            for (int i = 0; i < list.length; i++) {
                if (list[i].isDirectory()) {
//                    deleteDirectoryInSD(list[i].getAbsolutePath());
                } else {
                    list[i].delete();
                }
            }
        }
        file.delete();
        return true;
    }

    //删除缓存文件目录
    public boolean deleteDirectoryInCache() {
        String filePath = cacheDir + "/" + dir;
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            Utils.showLog(TAG, "deleteDirectoryInCache list.length" + list.length);
            for (int i = 0; i < list.length; i++) {
                if (list[i].isDirectory()) {
//                    deleteDirectoryInSD(list[i].getAbsolutePath());
                } else {
                    list[i].delete();
                }
            }
        }

        file.delete();
        return true;
    }


    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为M
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
        //1048576
    }

    /**
     * 文件大小单位转换
     *
     * @param size
     * @return
     */
    public static String setFileSize(long size) {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) size / (float) (1024 * 1024));

        if (f < 1.0) {
            float f2 = ((float) size / (float) (1024));

            return df.format(new Float(f2).doubleValue()) + "KB";

        } else {
            return df.format(new Float(f).doubleValue()) + "M";
        }

    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath)
            throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);

            if (file.isDirectory()) {// 处理目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {// 如果是文件，删除
                    file.delete();
                } else {// 目录
                    if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                        file.delete();
                    }
                }
            }
        }
    }

    public static void getFileConection(final Context context, final Handler handler, final String savePath, final String name) {
        final String filePath = savePath + "/" + name;
        Utils.showLog(TAG, "filePath====" + filePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = getFileFromServerWithName("http://img.fotomen.cn/2014/07/AdriennBalasko1.jpg", filePath);
                Utils.showLog(TAG, "b====" + b);
                Message message = new Message();
                message.what = 1;
                message.obj = filePath;
                handler.sendMessage(message);

            }
        }).start();
    }

    public static boolean getFileFromServerWithName(String urlDownload,
                                                    String fileName) {
        File f = new File(fileName.substring(0, fileName.lastIndexOf("/")));
        if (!f.exists()) {
            f.mkdirs();
        }

        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            URL url = new URL(urlDownload);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(fileName);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /* 在手机上打开文件的method */
    public static void openFile(File f, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

		    /* 调用getMIMEType()来取得MimeType */
        String type = getMIMEType(f);
            /* 设置intent的file与MimeType */
        Uri uri = Uri.fromFile(f);
        Utils.showLog(TAG, "uri=====" + uri);
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }

    /* 判断文件MimeType的method */
    private static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
            /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".")
                + 1, fName.length()).toLowerCase();

		    /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
              /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
            /*如果无法直接打开，就跳出软件列表给用户选择 */
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }

    public static Bitmap getBitmap(File file) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, o);
            o.inJustDecodeBounds = false;
            // Find the correct scale value. It should be the power of 2.
            int REQUIRED_SIZE = 300;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < 300 || height_tmp / 2 < 300)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, o2);
            return bitmap;
        } catch (FileNotFoundException e) {
        }
        return null;
    }
    /**
     * 判断文件是否存在  true存在  false不存在
     *
     * @param name
     * @return
     */
    public boolean compareSDFile(String name) {
        String paths = sd_card + dir + "/" + name;
        File file = new File(paths);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 判断文件是否存在  true存在  false不存在
     *
     * @param name
     * @return
     */
    public boolean compareCacheFile(Context context, String name) {
        String paths = cacheDir + "/" + dir + "/" + name;
        File file = new File(paths);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    public void saveBitmapInSD(String filename, Bitmap bitmap) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()))
            return;

        File file = new File(sd_card + dir + "/" + filename);
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outStream);
            outStream.flush();
            outStream.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveBitmapInCacheFile(String filename, Bitmap bitmap) {
        String paths = cacheDir + "/" + dir + "/" + filename;
        File file = new File(paths);
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outStream);
            outStream.flush();
            outStream.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Bitmap getBitmapInSD(String path) {
        File file = new File(sd_card + dir + "/" + path);
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            InputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();
            // Find the correct scale value. It should be the power of 2.
//            final int REQUIRED_SIZE = Utils.getScreenWidth(context) / 2;
//            int width_tmp = o.outWidth, height_tmp = o.outHeight;
//            Log.i("decodeFile", "width_tmp===" + width_tmp);
//            Log.i("decodeFile", "height_tmp===" + height_tmp);
            int scale = 1;
           /* while (true) {
                if (width_tmp <= REQUIRED_SIZE || height_tmp <= REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }*/

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Log.i("params", "scale===" + scale);
            InputStream inputStreamBitmap = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStreamBitmap, null, o2);
            inputStreamBitmap.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getBitmapInCacheFile(String path) {
        String paths = cacheDir + "/" + dir + "/" + path;
        File file = new File(paths);
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            InputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();
            // Find the correct scale value. It should be the power of 2.
//            final int REQUIRED_SIZE = Utils.getScreenWidth(context) / 2;
//            int width_tmp = o.outWidth, height_tmp = o.outHeight;
//            Log.i("decodeFile", "width_tmp===" + width_tmp);
//            Log.i("decodeFile", "height_tmp===" + height_tmp);
            int scale = 1;
           /* while (true) {
                if (width_tmp <= REQUIRED_SIZE || height_tmp <= REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }*/

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Log.i("params", "scale===" + scale);
            InputStream inputStreamBitmap = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStreamBitmap, null, o2);
            inputStreamBitmap.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 移动文件
     * @param srcFileName 	源文件完整路径
     * @param destDirName 	目的目录完整路径
     * @return 文件移动成功返回true，否则返回false
     */
    public  boolean moveFile(String srcFileName, String destDirName) {

        File srcFile = new File(srcFileName);
        if(!srcFile.exists() || !srcFile.isFile()) {
            Utils.showLog("moveFile===="+srcFileName);
            return false;
        }

        File destDir = new File(sd_card + dir);
        if (!destDir.exists()) {
            Utils.showLog("moveFile-=-1112222=-=-"+destDirName);
            destDir.mkdirs();
        }
        Utils.showLog("moveFile-=-11111=-=-"+destDirName + File.separator + srcFile.getName());

        return srcFile.renameTo(new File(destDirName + File.separator + srcFile.getName()));
    }


    public void copy(String srcFile, String destFile) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(sd_card+dir+"/"+destFile);
            byte[] b = new byte[1024];
            while (fis.read(b) != -1) {
                fos.write(b);
            }
            fos.close();
            fis.close();
            Log.i("jiemi", "encrypt===" + destFile);
            new File(srcFile).delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
