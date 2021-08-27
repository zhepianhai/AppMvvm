package com.gw.safty.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.gw.safty.common.extensions.ActivityExtensionsKt;

/**
 * Created by ${lcarry} on 2017/4/25.
 * 吐司工具类
 */
public class ToastUtil {

    public static void showToast(Context context, String str) {
        if (TextUtils.isEmpty(str)){
            str="";
        }
        ActivityExtensionsKt.exShortToast(context,str);
    }

    public static void showToast(Context context, int stringRes) {
        ActivityExtensionsKt.exShortToast(context, stringRes);
    }
}
