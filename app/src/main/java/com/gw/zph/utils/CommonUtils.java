package com.gw.zph.utils;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

public class CommonUtils {
    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getWindowWidth(Context context) {
        //context的方法，获取windowManager
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        //获取屏幕对象
        Display defaultDisplay = windowManager.getDefaultDisplay();
        //获取屏幕的宽、高，单位是像素
        return defaultDisplay.getWidth();
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getWindowHeight(Context context) {
        //context的方法，获取windowManager
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        //获取屏幕对象
        Display defaultDisplay = windowManager.getDefaultDisplay();
        //获取屏幕的宽、高，单位是像素
        return defaultDisplay.getHeight();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
