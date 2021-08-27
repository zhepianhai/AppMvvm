package com.gw.safty.common.utils;

import android.graphics.Color;
import android.widget.TextView;

public class DetailSettingUtil {

    /**
     * 设置文字颜色和填报状态
     *
     * @param textView
     * @param pos
     */
    public static void setTextViewColorByStatus(TextView textView, int pos) {
        if (pos == 0) {
            textView.setTextColor(Color.parseColor("#EE5E7B"));
            textView.setText("待填报");
        } else if (pos == 1) {
            textView.setTextColor(Color.parseColor("#FFC637"));
            textView.setText("未完成");
        } else if (pos == 2) {
            textView.setTextColor(Color.parseColor("#666666"));
            textView.setText("已完成");
        }
    }



}
