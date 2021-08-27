package cn.baililuohui.dropdownmenu

import android.view.View
import android.view.ViewGroup

interface MenuAdapter {
    fun getMenuCount(): Int
    fun onMenuTabCreate(tab: DrawableTabView, position: Int)
    fun onViewShow(view: View, position: Int, tab: DrawableTabView)
    fun onViewDismiss(view: View, position: Int, tab: DrawableTabView)
    fun onCreateView(parent: ViewGroup, position: Int): View
}