package com.gw.safty.common.utils

import com.gw.safty.common.widget.BasePopupWindow
import java.util.*

class BasePopupWindowUtil private constructor() {
    private var mDatas: ArrayList<BasePopupWindow> = ArrayList<BasePopupWindow>()
    set(value) {
        mDatas=value
        field = value
    }

    companion object {
        val instance: BasePopupWindowUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BasePopupWindowUtil()
        }
    }

    // 添加窗体
    fun addZPopupWindow(data: BasePopupWindow?) {
        if (mDatas == null) mDatas = ArrayList()
        data?.let { mDatas.add(it) }
    }

    // 移除窗体
    fun removeZPopupWindow(data: BasePopupWindow?) {
        if (mDatas != null) {
            mDatas.remove(data)
            mDatas.clear()
        }
    }

    // 清空数据
    fun clearZPopupWindow() {
        if (mDatas != null) {
            for (zPopupWindow in mDatas) {
                zPopupWindow?.dismiss()
            }
            mDatas.clear()
        }
    }
}