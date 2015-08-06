package com.autoparts.sellers.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by:Liuhuacheng
 * Created time:15-5-18
 */
public class CameraUtils {

    private Activity context;
    private File tempFile;

    public CameraUtils(Activity paramActivity) {
        this.context = paramActivity;
    }

    private String getPhotoFileName() {
        Date localDate = new Date(System.currentTimeMillis());
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return localSimpleDateFormat.format(localDate) + ".jpg";
    }

    public void camera() {
        this.tempFile = new File(Environment.getExternalStorageDirectory() +Constants.cacheDir, getPhotoFileName());
        Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        localIntent.putExtra("output", Uri.fromFile(this.tempFile));
        this.context.startActivityForResult(localIntent, CommonData.PHOTO_REQUEST_TAKEPHOTO);
    }

    public void gallery() {
        Intent localIntent = new Intent("android.intent.action.GET_CONTENT");
        localIntent.setType("image/*");
        this.context.startActivityForResult(localIntent, CommonData.PHOTO_REQUEST_GALLERY);
    }

    public File getTempFile() {
        return this.tempFile;
    }
}
