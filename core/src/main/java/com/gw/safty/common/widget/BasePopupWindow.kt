package com.gw.safty.common.widget

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.*
import android.widget.PopupWindow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.gw.safty.common.R
import com.gw.safty.common.utils.BasePopupWindowUtil

abstract class BasePopupWindow(protected val context: Context) : PopupWindow(), ViewModelStoreOwner{
    private var wm: WindowManager
    private var maskView: View? = null
    private var maskHeight = 0
    private var maskGravity = Gravity.CENTER
    private var mViewModelStore: ViewModelStore = ViewModelStore()

    protected abstract fun generateCustomView(context: Context?): View?

    init{
        contentView = generateCustomView(context)
        maskHeight = getScreenH(context)
        wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(context.resources.getDrawable(android.R.color.transparent))
        animationStyle = R.style.BottomPushPopupWindow
        // 关闭所有ZPopupWindow
        // 关闭所有ZPopupWindow
        BasePopupWindowUtil.instance.clearZPopupWindow()
        // 注册相应广播
        // 注册相应广播
        regReceiver()
    }


    // 定义一个广播接收器，帮助关闭PopuWindow
    private val receiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(arg0: Context, intent: Intent) {
            val action = intent.action
            if (Intent.ACTION_SCREEN_OFF == action || Intent.ACTION_USER_PRESENT == action) {
                dismiss()
            }
        }
    }
    override fun showAtLocation(parent: View, gravity: Int, x: Int, y: Int) {
        this.showAtLocation(parent, gravity, x, y, true)
    }

    open fun showAtLocation(
        parent: View,
        gravity: Int,
        x: Int,
        y: Int,
        flag: Boolean
    ) {
        if (flag) {
            addMaskView(parent.windowToken, maskHeight, maskGravity)
        }
        super.showAtLocation(parent, gravity, x, y)
    }
    // 添加遮罩层
    private  fun addMaskView(token: IBinder, maskHeight: Int, gravity: Int) {
        val params = WindowManager.LayoutParams()
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = maskHeight
        params.format = PixelFormat.TRANSLUCENT
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
        params.token = token
        params.windowAnimations = android.R.style.Animation_Toast
        params.gravity = gravity
        maskView = View(context)
        maskView!!.setBackgroundColor(Color.parseColor("#66000000"))
        maskView!!.fitsSystemWindows = false
        maskView!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                removeMaskView()
                return@OnKeyListener true
            }
            false
        })
        wm!!.addView(maskView, params)
    }

    // 移除遮罩层
    private  fun removeMaskView() {
        if (maskView != null) {
            wm!!.removeViewImmediate(maskView)
            maskView = null
        }
    }
    // 注册广播接收器，接收暗屏广播，锁屏广播
    private  fun regReceiver() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        context.registerReceiver(receiver, filter)
        // 广播开启的同时，添加到管理类
        BasePopupWindowUtil.instance.clearZPopupWindow()
        BasePopupWindowUtil.instance.addZPopupWindow(this)
    }
    /**
     * 获取屏幕高度（包括状态栏的高度） - px
     */
    private  fun getScreenH(aty: Context): Int {
        val wm =
            aty.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay?.getMetrics(outMetrics)
        return outMetrics.heightPixels + getDaoHangHeight(aty)


    }

    /* 获取导航栏高度
     * @param context
     * @return
             */
    open fun getDaoHangHeight(context: Context): Int {
        val result = 0
        var resourceId = 0
        val rid =
            context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
        return if (rid != 0) {
            resourceId =
                context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            context.resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    override fun dismiss() {
        try {
            if (receiver != null) // 注销广播
                context.unregisterReceiver(receiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // 刷新界面
        removeMaskView()
        //todo
        viewModelStore.clear()
        super.dismiss()
    }

    /**
     * 显示在界面的底部
     */
    open fun showBottom() {
        showAtLocation(
            (context as Activity).window.decorView,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            0
        )
    }

    /**
     * 显示在界面的顶部
     */
    open fun showTop() {
        showAtLocation(
            (context as Activity).window.decorView,
            Gravity.TOP or Gravity.CENTER_HORIZONTAL,
            0,
            0
        )
    }

    /**
     * 显示在指定View的正上方
     *
     * @param view    指定的View
     * @param yOffset Y轴偏移量
     */
    open fun showViewTop(view: View, yOffset: Int) {
        maskHeight = getScreenH(context) - view.height - yOffset
        // 获取需要在其上方显示的控件的位置信息
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        // 获取自身的长宽高
        this.contentView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        val popupHeight = this.contentView.measuredHeight
        val popupWidth = this.contentView.measuredWidth
        //在控件上方显示
        maskGravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        showAtLocation(
            view,
            Gravity.NO_GRAVITY,
            location[0] + popupWidth / 2,
            location[1] - popupHeight - yOffset
        )
    }


    /**
     * 获取状态栏高度
     */
    private  fun getStatusHeight(activity: Activity): Int {
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        return frame.top
    }



    override fun getViewModelStore(): ViewModelStore = mViewModelStore

    protected open fun <T: ViewModel> getViewModel(clazz: Class<T>): T{
        return ViewModelProvider(this).get(clazz)
    }
}