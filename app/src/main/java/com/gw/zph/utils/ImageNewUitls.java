package com.gw.zph.utils;

import  android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.gw.zph.base.listener.ImageCompressImp;
import com.lzy.imagepicker.util.FileUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ImageNewUitls {



    private static File saveFile(Bitmap bm, String fileName) throws IOException {
        File fileDir = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_HOME_PATH + Constants.TAKE_MEDIA_FILE_PATH);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File myCaptureFile = new File(fileDir, System.currentTimeMillis() + "_" + fileName);
        if (!myCaptureFile.exists()) myCaptureFile.createNewFile();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }


    /**
     * 根据原位图生成一个新的位图，并将原位图所占空间释放
     *
     * @param srcBmp 原位图
     * @return 新位图
     */
    public static Bitmap copy(Bitmap srcBmp) {
        Bitmap destBmp = null;
        try {
            // 创建一个临时文件
            File file = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_HOME_PATH + Constants.TAKE_MEDIA_FILE_PATH_TEMP + "/tmp.txt");
            if (file.exists()) {// 临时文件 ， 用一次删一次
                file.delete();
            }
            file.getParentFile().mkdirs();
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            int width = srcBmp.getWidth();
            int height = srcBmp.getHeight();
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, width * height * 4);
            // 将位图信息写进buffer
            srcBmp.copyPixelsToBuffer(map);
            // 释放原位图占用的空间
            srcBmp.recycle();
            // 创建一个新的位图
            destBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            map.position(0);
            // 从临时缓冲中拷贝位图信息
            destBmp.copyPixelsFromBuffer(map);
            channel.close();
            randomAccessFile.close();
            file.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
            destBmp = null;
            return srcBmp;
        }
        return destBmp;
    }




    public static byte[] File2byte(File tradeFile) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
