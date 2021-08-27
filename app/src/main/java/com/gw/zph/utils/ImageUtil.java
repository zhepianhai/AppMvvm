package com.gw.zph.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.amap.api.maps.model.LatLng;
import com.lzy.imagepicker.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;
public class ImageUtil {
    /*
     * bitmap设置任意透明度
     * */
    public static Bitmap getAlplaBitmap(Bitmap sourceImg, int number) {
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg.getWidth(), sourceImg.getHeight());
        number = number * 255 / 100;
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
        }
        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg.getHeight(), Bitmap.Config.ARGB_8888);
        return sourceImg;
    }

    /*
     * bitmap设置圆角
     * */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = Color.RED;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        final float roundPx = 200;

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getImagPath(Uri uri, Context context) {
        String imagePath = "";
        if (DocumentsContract.isDocumentUri(context, uri)) {
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath_base(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, context);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath_base(contentUri, null, context);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath_base(uri, null, context);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        return imagePath;
    }


    private static String getImagePath_base(Uri uri, String selection, Context context) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getVideoPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * 获取本地视频的第一帧 * * @param localPath * @return
     */
    public static Bitmap getLocalVideoBitmap(String localPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try { //根据文件路径获取缩略图
            retriever.setDataSource(localPath);
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    public static byte[] compressImageBytes(Bitmap bitmap){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 1024) {  //循环判断如果压缩后图片是否大于1M,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }

        recycleBitmap(bitmap);
        // 销毁时调用 释放
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return baos.toByteArray();
    }

    /**
     * 压缩图片（质量压缩）
     *
     * @param bitmap
     */
    public static File compressImage(Bitmap bitmap) {

        byte[] bitmapBytes = compressImageBytes(bitmap);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File fileDir = new File(Environment.getExternalStorageDirectory()+"/"+Constants.APP_HOME_PATH+Constants.TAKE_MEDIA_FILE_PATH);
        if (!fileDir.exists()){
            fileDir.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory()+"/"+Constants.APP_HOME_PATH+Constants.TAKE_MEDIA_FILE_PATH, filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(bitmapBytes);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {

            Timber.i("onActivityResult--compressImage-FileNotFoundException>"+e.getMessage());
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 压缩图片（质量压缩）
     *
     * @param bitmap
     */
    public static File compressImageByPath(Bitmap bitmap,String path) {

        byte[] bitmapBytes = compressImageBytes(bitmap);
        String filename =path;
        File fileDir = new File(Environment.getExternalStorageDirectory()+"/"+Constants.APP_HOME_PATH+Constants.TAKE_MEDIA_FILE_PATH);
        if (!fileDir.exists()){
            fileDir.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory()+"/"+Constants.APP_HOME_PATH+Constants.TAKE_MEDIA_FILE_PATH, filename );
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(bitmapBytes);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {

            Timber.i("onActivityResult--compressImage-FileNotFoundException>"+e.getMessage());
            e.printStackTrace();
        }

        return file;
    }


    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

    /**
     * 添加水印 时间，经纬度，地址
     */

    public static File AddWatermark(Context context, String path, LatLng latLng, String address,int type) {
        File file = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            file = new File(path);
            if (file.exists()) {
                Bitmap sourBitmap = BitmapFactory.decodeFile(path);
                float width = sourBitmap.getWidth();
                float height = sourBitmap.getHeight();
                float be = width / height;
                Bitmap sourBitmapNew = sourBitmap.copy(Bitmap.Config.ARGB_8888, true);
                //根据画板大小和手机分辨率计算出画笔的字体的大小
                WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                int mScreenHeigh = dm.heightPixels;
                int mScreenWidth = dm.widthPixels;

                int screenWidth = (int) width;
                int screenHeight = (int) height;
                float ratioWidth = (float)screenWidth / mScreenWidth;
                float ratioHeight = (float)screenHeight / mScreenHeigh;
                float RATIO = Math.min(ratioWidth, ratioHeight);
                int TEXT_SIZE = Math.round(35 * RATIO);

                //绘制面板
                Canvas canvas = new Canvas(sourBitmapNew);
                //设置画笔  经纬度的
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setAntiAlias(true);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(TEXT_SIZE*2);
                paint.setStrokeWidth(20);
                paint.setFakeBoldText(true);
                double lggt = latLng.latitude;
                double lttd = latLng.longitude;
                String lggtString = "北纬：" + lggt + "";
                String lttdString = "东经：" + lttd + "";
                canvas.drawText(lggtString, 20, TEXT_SIZE*2, paint);
                canvas.drawText(lttdString, 20, TEXT_SIZE*4, paint);
                Paint paintButtom = new Paint();
                paintButtom.setColor(Color.WHITE);
                paintButtom.setAntiAlias(true);
                paintButtom.setTextAlign(Paint.Align.LEFT);
                paintButtom.setTextSize(TEXT_SIZE*3/2);
                paintButtom.setStrokeWidth(12);
                paintButtom.setFakeBoldText(true);
                canvas.drawText(address, 20, height - 50, paintButtom);
                canvas.drawText(sdf1.format(new Date()), 20, height - TEXT_SIZE*4, paintButtom);
                //bitmap转换成File存储，压缩，生产文件
                File uploadFile = compressImage(sourBitmapNew);
                // 销毁时调用 释放
                if (sourBitmap != null && !sourBitmap.isRecycled()) {
                    sourBitmap.recycle();
                    sourBitmap = null;
                }
                if (sourBitmapNew != null && !sourBitmapNew.isRecycled()) {
                    sourBitmapNew.recycle();
                    sourBitmapNew = null;
                }
                //将已有的文件进行删除
                if (uploadFile.exists()) {
                    if(type==0) {
                        FileUtil.deleteFile(file.getPath(), "");
                    }
                    return uploadFile;
                } else {
                    return file;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Timber.i("onActivityResult--AddWatermark->"+e.getMessage());
        }
        return file;
    }


    /**
     * 添加水印 时间，经纬度，地址,默认名称
     */

    public static File AddWatermarkByName(Context context, String path, LatLng latLng, String address,int type,String thisName) {
        File file = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            file = new File(path);
            if (file.exists()) {
                Bitmap sourBitmap = BitmapFactory.decodeFile(path);
                float width = sourBitmap.getWidth();
                float height = sourBitmap.getHeight();
                float be = width / height;
                Bitmap sourBitmapNew = sourBitmap.copy(Bitmap.Config.ARGB_8888, true);
                //根据画板大小和手机分辨率计算出画笔的字体的大小
                WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                int mScreenHeigh = dm.heightPixels;
                int mScreenWidth = dm.widthPixels;

                int screenWidth = (int) width;
                int screenHeight = (int) height;
                float ratioWidth = (float)screenWidth / mScreenWidth;
                float ratioHeight = (float)screenHeight / mScreenHeigh;
                float RATIO = Math.min(ratioWidth, ratioHeight);
                int TEXT_SIZE = Math.round(35 * RATIO);

                //绘制面板
                Canvas canvas = new Canvas(sourBitmapNew);
                //设置画笔  经纬度的
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setAntiAlias(true);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(TEXT_SIZE*2);
                paint.setStrokeWidth(20);
                paint.setFakeBoldText(true);
                double lggt = latLng.latitude;
                double lttd = latLng.longitude;
                String lggtString = "北纬：" + lggt + "";
                String lttdString = "东经：" + lttd + "";
                canvas.drawText(lggtString, 20, TEXT_SIZE*2, paint);
                canvas.drawText(lttdString, 20, TEXT_SIZE*4, paint);
                Paint paintButtom = new Paint();
                paintButtom.setColor(Color.WHITE);
                paintButtom.setAntiAlias(true);
                paintButtom.setTextAlign(Paint.Align.LEFT);
                paintButtom.setTextSize(TEXT_SIZE*3/2);
                paintButtom.setStrokeWidth(12);
                paintButtom.setFakeBoldText(true);
                canvas.drawText(address, 20, height - 50, paintButtom);
                canvas.drawText(sdf1.format(new Date()), 20, height - TEXT_SIZE*4, paintButtom);
                //bitmap转换成File存储，压缩，生产文件
                File uploadFile = compressImageByPath(sourBitmapNew,thisName);
                // 销毁时调用 释放
                if (sourBitmap != null && !sourBitmap.isRecycled()) {
                    sourBitmap.recycle();
                    sourBitmap = null;
                }
                if (sourBitmapNew != null && !sourBitmapNew.isRecycled()) {
                    sourBitmapNew.recycle();
                    sourBitmapNew = null;
                }
                //将已有的文件进行删除
                if (uploadFile.exists()) {
                    if(type==0) {
                        FileUtil.deleteFile(file.getPath(), "");
                    }
                    return uploadFile;
                } else {
                    return file;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Timber.i("onActivityResult--AddWatermark->"+e.getMessage());
        }
        return file;
    }

    /**
     * 解决小米手机，拍照后会旋转角度的问题
     * */

    /**
     * 获取图片的旋转角度
     *
     * @param imgPath 图片路径
     * @return 返回旋转角度
     */
    public static int getBitmapRotateAngle(String imgPath) {
        // 判断图片方向
        int digree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imgPath);
        } catch (IOException e) {
            e.printStackTrace();

            exif = null;
        }
        if (exif != null) {
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }

        }
        return digree;
    }


    /**
     * @param path   图片路径
     * @param digree 旋转角度
     * @return
     */
    public static Bitmap creatBitmap(String path, int digree) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap bitmapRe;
        // 旋转图片
        Matrix m = new Matrix();
        m.postRotate(digree);
        bitmapRe = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), m, true);
        if (bitmap != bitmapRe) {
            bitmap.recycle();
        }
        return bitmapRe;

    }

    /**
     * @param path   图片路径
     * @param digree 旋转角度
     * @return 返回文件
     */
    public static File creatBitmapFile(String path, int digree) {
        File file;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap bitmapRe;
        // 旋转图片
        Matrix m = new Matrix();
        m.postRotate(digree);
        bitmapRe = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), m, true);
        file = compressImage(bitmap);
        if (!file.exists()) {
            file = new File(path);
        } else {
            FileUtil.deleteFile(path);
        }
        if (bitmap != bitmapRe) {
            bitmap.recycle();
        }
        return file;

    }


    /**
     * 将Base64转换成图片
     * @param string
     * @return
     */
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
