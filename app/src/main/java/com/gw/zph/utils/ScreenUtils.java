package com.gw.zph.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

public class ScreenUtils {
    public static int getScreenWidth(Activity mActivity){
        WindowManager manager = mActivity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
//        int height = outMetrics.heightPixels;
        return width;
    }

    /**
     * 获取屏幕尺寸
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenSize(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2){
            return new Point(display.getWidth(), display.getHeight());
        }else{
            Point point = new Point();
            display.getSize(point);
            return point;
        }
    }
    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad() {
//        String isP=systempripertiesGet("ro.build.characteristics");
//       if("default".equals(isP)){
//           return false;
//       }else if("tablet".equals(isP)){
//           return true;
//       }
       return false;
    }
    /**
     * 判断是否是手机还是平板
     * default是手机
     * tablet是平板
     * ​在*device/qcom/.mk中增加PRODUCT_CHARACTERISTICS =default 不会影响其他功能.
     * 一个重要原因是该属性最终默认值即为default.
     * 只有在机型为can**, canc**_ct, vir**,virg**_ct,ke**o​时该属性才会被赋值为nosdcard.
     * ro.build.characteristics这个属性用途有:字符串中的product属性,判断是手机还是平板电脑的函数getDeviceType().
     * */
    private static String systempripertiesGet(String key){
        String vaule=null;
        Class<?> mClassType=null;
        Method mGetMethod=null;
         try{
             mClassType=Class.forName("android.os.SystemProperties");
             mGetMethod=mClassType.getDeclaredMethod("get",String.class);

         }catch (Exception e){}
         if(null!=mGetMethod){
             try{
                 vaule= (String) mGetMethod.invoke(mClassType,key);
             }catch (Exception e){}
         }
        return vaule;
    }
}
