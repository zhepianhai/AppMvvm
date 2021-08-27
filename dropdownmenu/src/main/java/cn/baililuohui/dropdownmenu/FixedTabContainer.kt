package cn.baililuohui.dropdownmenu

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.children

@Suppress("DEPRECATION")
class FixedTabContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var onTabSelectionChangeListener: OnTabSelectionChangeListener ?= null
    private var showTabIndicator: Boolean = true

    /*
     * 分割线
     */
    private var mDividerPaint = Paint()
    private val mDividerColor = resources.getColor(R.color.default_divider_color) // 分割线颜色
    private var mDividerPadding = 18 // 分割线距离上下padding


    /*
     * 上下两条线
     */
    private var mLinePaint: Paint = Paint()
    private val mLineHeight = 1f
    private val mLineColor = Color.parseColor("#e5e5e5")
    private var drawBottomSeparatorLine = false
    private var drawTopSeparatorLine = false


    private val mTabTextSize = 15 // 指针文字的大小,sp

    private val mTabDefaultColor = -0xcccccd // 未选中默认颜色

    private val mTabSelectedColor = -0xcd7303 // 指针选中颜色

    private var drawableRight = 10

//    private var mTabCount = 0 // 设置的条目数量
    private var mSelectedTabPosition = -1 // 上一个指针选中条目

    init {
        orientation = HORIZONTAL
        setBackgroundColor(resources.getColor(R.color.white))
        setWillNotDraw(false)

        mDividerPaint.isAntiAlias = true
        mDividerPaint.color = mDividerColor
        mLinePaint.color = mLineColor
        mDividerPadding = mDividerPadding.dp
        drawableRight = drawableRight.dp
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until childCount - 1) { // 分割线的个数比tab的个数少一个
            val child = getChildAt(i)
            if (child == null || child.visibility == View.GONE) {
                continue
            }
            canvas.drawLine(
                child.right.toFloat(),
                mDividerPadding.toFloat(),
                child.right.toFloat(),
                measuredHeight - mDividerPadding.toFloat(),
                mDividerPaint
            )
        }

        if (drawTopSeparatorLine){
            //上边黑线
            canvas.drawRect(0f, 0f, measuredWidth.toFloat(), mLineHeight, mLinePaint)
        }

        if (drawBottomSeparatorLine){
            //下边黑线
            val top = measuredHeight - mLineHeight
            val right = measuredWidth.toFloat()
            val bottom = measuredHeight.toFloat()
            canvas.drawRect(0f, top, right, bottom, mLinePaint)
        }
    }

    /**
     * 添加相应的布局进此容器
     */
    fun setTitles(list: List<String>) {
        check(list.isNotEmpty()) { "条目数量位空" }
        removeAllViews()
        for (pos in list.indices) {
            val defaultTab = DrawableTabView.defaultTab(context, list[pos], pos)
            addTab(defaultTab)
        }
        postInvalidate()
    }

    fun newTab(title: String = "TAB", pos: Int = childCount) = DrawableTabView.defaultTab(context, title, pos)

    fun addTab(tab: DrawableTabView, isSelected: Boolean = false) {
        tab.setOnTabClickCallback(tabClickCallback)
        tab.setSelect(isSelected)
        addView(tab)
    }

    fun resetSelection(){
        children.forEach {
            if (it is DrawableTabView && it.isSelect()){
                it.setSelect(false)
                mSelectedTabPosition = -1
                onTabSelectionChangeListener?.onTabUnSelect(it,true)
            }
        }
    }

    private val tabClickCallback: DrawableTabView.OnTabClickCallback = object : DrawableTabView.OnTabClickCallback {
        override fun onTabClick(tab: DrawableTabView) {
            if (tab.isSelect()){
                if (mSelectedTabPosition in 0 until childCount){
                    getPositionTab(mSelectedTabPosition)?.let {
                        if (it.isSelected){
                            it.setSelect(false)
                            onTabSelectionChangeListener?.onTabUnSelect(it, false)
                        }
                    }
                }
                mSelectedTabPosition = tab.getPosition()
                onTabSelectionChangeListener?.onTabSelected(tab)
            } else {
                mSelectedTabPosition =  -1
                onTabSelectionChangeListener?.onTabUnSelect(tab, true)
            }
        }
    }

    /**
     * 获取当前pos内的TextView
     */
    fun getPositionTab(pos: Int): DrawableTabView? {
        val positionChild = getChildAt(pos)
        if (positionChild is DrawableTabView) {
            return positionChild
        }
        return null
    }

    fun setOnTabSelectionChangeListener(listener: OnTabSelectionChangeListener){
        this.onTabSelectionChangeListener = listener
    }

    fun setBottomLineVisible(visible: Boolean){
        this.drawBottomSeparatorLine = visible
    }

    fun setTopLineVisible(visible: Boolean){
        this.drawTopSeparatorLine = visible
    }

    /**
     * Tab 的选中和取消选中监听
     */
    interface OnTabSelectionChangeListener{
        /**
         * Tab 被选中时的回调
         * @param tab 被选中的tab
         */
        fun onTabSelected(tab: DrawableTabView)

        /**
         * Tab 被取消选中时的回调，从一个Tab切换到另一Tab时，此方法也会被调用
         * @param tab 当前取消选中的tab
         * @param animate 是否建议使用动画。当从一个tab切换到另一个Tab时，是否建议使用隐藏动画
         */
        fun onTabUnSelect(tab: DrawableTabView, animate: Boolean)
    }
}