package com.gw.zph.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

public class InputUtils {

    public static final String KEY = "KeyboardHeight";

    /**
     * 隐藏键盘
     *
     * @param context
     * @param paramEditText
     */
    public static void hideSoftInput(Context context, View paramEditText) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    /**
     * 显示键盘
     *
     * @param context
     * @param paramEditText
     */
    public static void showKeyBoard(final Context context, final View paramEditText) {
        paramEditText.requestFocus();
        paramEditText.post(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(paramEditText, 0);
            }
        });
    }

    /**
     * 判断键盘是否显示
     *
     * @param paramActivity
     * @return
     */
    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity)
                - getAppHeight(paramActivity);

        Log.i("haha", "键盘是否弹起高度判断：" + height);
        if (Build.MANUFACTURER.toLowerCase().contains("meizu")) {
            //魅族手机专门处理
            int smartBarHeight = getSmartBarHeight(paramActivity);
            if (height == smartBarHeight) {
                return false;
            } else {
                return true;
            }
        }
        return height != 0;
    }


    /**
     * 获取魅族手机SmartBar的高度
     *
     * @return
     */
    private static int getSmartBarHeight(Activity paramActivity) {
//        if (ActionBarProxy.hasSmartBar()) {
        try {
            Class e = Class.forName("com.android.internal.R$dimen");
            Object obj = e.newInstance();
            Field field = e.getField("mz_action_button_min_height");
            int parseInt = Integer.parseInt(field.get(obj).toString());
            int smartBarHeight = paramActivity.getResources().getDimensionPixelSize(parseInt);
            Log.i("haha", "SmartBar的高度：" + smartBarHeight);
            return smartBarHeight;
        } catch (Exception var6) {
            var6.printStackTrace();
        }
//        }
        return 0;
    }

    /**
     * 获取屏幕高度
     *
     * @param paramActivity
     * @return
     */
    public static int getScreenHeight(Activity paramActivity) {
        Display display = paramActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @param paramActivity
     * @return
     */
    public static int getStatusBarHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;

    }

    /**
     * 获取ActionBar高度
     *
     * @param paramActivity
     * @return
     */
    @SuppressWarnings("unused")
    public static int getActionBarHeight(Activity paramActivity) {
//        if (true) return dip2px(paramActivity, 56);

        // test on samsung 9300 android 4.1.2, this value is 96px
        // but on galaxy nexus android 4.2, this value is 146px
        // statusbar height is 50px
        // I guess 4.1 Window.ID_ANDROID_CONTENT contain statusbar
//		int contentViewTop = paramActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

//        return contentViewTop - getStatusBarHeight(paramActivity);

//        int[] attrs = create int[]{android.R.attr.actionBarSize};
//        TypedArray ta = paramActivity.obtainStyledAttributes(attrs);
//        return ta.getDimensionPixelSize(0, dip2px(paramActivity, 56));
        if (paramActivity.getActionBar() != null) {
            return paramActivity.getActionBar().getHeight();
        } else {
            return 0;
        }
    }

    /**
     * 获取App高度
     *
     * @param paramActivity
     * @return
     */
    // below status bar,include actionbar, above softkeyboard
    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        Log.i("hehe", "getAppHeight:" + localRect.height());
        return localRect.height();
    }

    /**
     * 获取App显示区域高度
     *
     * @param paramActivity
     * @return
     */
    // below actionbar, above softkeyboard
    public static int getAppContentHeight(Activity paramActivity) {
        Log.i("hehe", "getScreenHeight:" + getScreenHeight(paramActivity));
        Log.i("hehe", "getStatusBarHeight:" + getStatusBarHeight(paramActivity));
        Log.i("hehe", "getActionBarHeight:" + getActionBarHeight(paramActivity));
        Log.i("hehe", "getKeyboardHeight:" + getKeyboardHeight(paramActivity));
        int appContentHeight = getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity)
                - getActionBarHeight(paramActivity) - getKeyboardHeight(paramActivity) - dip2px(paramActivity, 50);

        if (Build.MANUFACTURER.toLowerCase().contains("meizu")) {
            return appContentHeight - getSmartBarHeight(paramActivity);
        }
        return appContentHeight;
    }

    /**
     * 获取键盘高度
     *
     * @param paramActivity
     * @return
     */
    public static int getKeyboardHeight(Activity paramActivity) {
        int height = getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity)
                - getAppHeight(paramActivity);
        if (Build.MANUFACTURER.toLowerCase().contains("meizu")) {
            height = height - getSmartBarHeight(paramActivity);
        }
        if (height == 0) {
            height = getIntShareData(paramActivity, KEY, getScreenHeight(paramActivity) / 2 - getActionBarHeight(paramActivity));
        } else {
            putIntShareData(paramActivity, KEY, height);
        }
        return height;
    }

    /**
     * 转换dp和px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, int dipValue) {
        float reSize = context.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    /**
     * 获取int
     *
     * @param key
     * @param defValue
     * @return
     */
    public static int getIntShareData(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return sp.getInt(KEY, defValue);
    }

    /**
     * 存储int
     *
     * @param key
     * @param value
     */
    public static void putIntShareData(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putInt(KEY, value);
        et.commit();
    }

    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


}
