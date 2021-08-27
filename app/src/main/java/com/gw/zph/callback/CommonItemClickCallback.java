package com.gw.zph.callback;

import android.view.View;

/**
 * 通用列表条目点击回调接口
 * @param <T>
 */
@FunctionalInterface
public interface CommonItemClickCallback<T> {
    /**
     * 点击回调接口
     * @param view 点击的控件
     * @param pos 条目位置
     * @param model 条目对应的数据
     */
    void onClick(View view, int pos, T model);
}
