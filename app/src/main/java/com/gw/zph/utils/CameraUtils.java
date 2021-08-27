package com.gw.zph.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.UUID;

public class CameraUtils {

    //本地缓存的URL路径
    public static String cacheURL;
    //拍完照所保存的照片路径
    private static File cameraImgpath;
    // 照相机保存文件的文件夹名
    public static final String CAMERA_PATH = "cameras/";
    // 保存头像文件的文件夹名
    public static final String HEAD_PATH = "head/";
    // 照片文件的后缀名
    public static final String IAMGE_SUFFIX_JPG = ".jpg";

    //视频文件后缀
    public static final String VIDEO_SUFFIX_MP4=".mp4";
    //录像照所保存的视频路径
    private static File cameraVideopath;

    private CameraUtils() {
    }

    /**
     * 打开相机，拍完照并返回
     *
     * @param activity
     * @param requestCode
     */
    public static void startCameraForResult(Activity activity, int requestCode) {

        Intent intent = startCameraIntent(activity);
        if (intent != null){
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static Intent startCameraIntent(Activity activity){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean bPermission = CheckPermission(activity, 0);
            if (!bPermission)
                return null;
        }

        // 利用系统自带的相机应用:拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cacheURL == null) {
            cacheURL = activity.getExternalCacheDir().getAbsolutePath();
        }

        File cameraPath = new File(cacheURL, CAMERA_PATH);
        if (!cameraPath.exists()) {
            cameraPath.mkdirs();
        }

        cameraImgpath = new File(cameraPath, handleUUID(UUID.randomUUID().toString() + IAMGE_SUFFIX_JPG));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Uri contentUri = FileProvider.getUriForFile(activity, "com.gw.slbdc.provider", cameraImgpath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            //告诉系统拍照界面，拍完照片需要将图片保存的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraImgpath));
        }
        return intent;
    }

    @TargetApi(23)
    private static boolean CheckPermission(Activity activity, int requestCode) {
        if(activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            activity.requestPermissions(new String[]{Manifest.permission.CAMERA},200);
            return false;
        }
        if(activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
            return false;
        }
        return true;
    }

    /**
     * 从本地相册获取图片
     *
     * @param activity
     * @param requestCode
     */
    public static void startGalleryForResult(Activity activity, int requestCode) {
        if (cacheURL == null) {
            cacheURL = activity.getExternalCacheDir().getAbsolutePath();
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取相机拍完照所保存图片的绝对路径
     *
     * @return
     */
    public static File getImgUrl() {
        if (cameraImgpath == null) {
            File cameraPath = new File(cacheURL, CAMERA_PATH);
            cameraImgpath = new File(cameraPath, handleUUID(UUID.randomUUID().toString()
                    + IAMGE_SUFFIX_JPG));
        }
        return cameraImgpath;
    }

    /**
     * 获取保存图片剪切完成之后的路径
     *
     * @return
     */
    public static File getHeadImgUrl() {
        File headPath = new File(cacheURL, HEAD_PATH);
        if (!headPath.exists()) {
            headPath.mkdirs();
        }
        File headImg = new File(headPath, handleUUID(UUID.randomUUID().toString()
                + IAMGE_SUFFIX_JPG));
        return headImg;
    }

    /**
     * 把UUID中的-进行过滤
     *
     * @param str
     * @return
     */
    public static String handleUUID(String str) {
        String result = str.replaceAll("-", "");// 替换中文标号
        return result;
    }



    /**
     * 打开相机，录像并返回
     *
     * @param activity
     * @param requestCode
     */
    public static void startCameraVideoForResult(Activity activity, int requestCode) {
        Intent intent = startCameraVideoIntent(activity);
        if (intent != null){
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static Intent startCameraVideoIntent(Activity activity){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean bPermission = CheckPermission(activity, 0);
            if (!bPermission)
                return null;
        }

        // 利用系统自带的相机应用:拍照
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (cacheURL == null) {
            cacheURL = activity.getExternalCacheDir().getAbsolutePath();
        }

        File cameraPath = new File(cacheURL, CAMERA_PATH);
        if (!cameraPath.exists()) {
            cameraPath.mkdirs();
        }
        cameraVideopath = new File(cameraPath, handleUUID(UUID.randomUUID().toString()
                + VIDEO_SUFFIX_MP4));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Uri contentUri = FileProvider.getUriForFile(activity, "com.gw.slbdc.provider", cameraVideopath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            //告诉系统拍照界面，拍完照片需要将图片保存的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraVideopath));
        }

        return intent;
    }

    /**
     * 获取相机录像所保存视频的绝对路径
     *
     * @return
     */
    public static File getVideoUrl() {
        if (cameraVideopath == null) {
            File cameraPath = new File(cacheURL, CAMERA_PATH);
            cameraVideopath = new File(cameraPath, handleUUID(UUID.randomUUID().toString()
                    + VIDEO_SUFFIX_MP4));
        }
        return cameraVideopath;
    }
}
