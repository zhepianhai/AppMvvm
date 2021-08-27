package com.gw.safty.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by dell on 2016/8/22.
 */
public class PhoneUtils {


    /**
     * 跳转到拨打电话的界面，需要以下权限
     * <uses-permission android:name="android.permission.CALL_PHONE" />
     *
     * @param mContext
     * @param num
     */
    public static void callPhone(Context mContext, String num) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + num);
        intent.setData(data);
        mContext.startActivity(intent);
    }


}
