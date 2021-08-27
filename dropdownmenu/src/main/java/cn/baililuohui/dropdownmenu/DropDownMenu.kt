package cn.baililuohui.dropdownmenu

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout

/**
 * 自定义菜单控件
 * 控件结构为
 * LinearLayout
 *  --FixedTabContainer(LinearLayout)
 *  --FrameLayout
 *    --未打开菜单时默认显示的控件 (通过contentLayout 属性设置)
 *    --tab 对应的控件1
 *    --tab 对应的控件...
 *    --tab 对应的控件n
 */
@Suppress("DEPRECATION")
class DropDownMenu @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var contentLayoutId: Int = -1 // 显示主要内容的控件id

    private var menuDisplayAnim: Animation //显示动画
    private var menuHideAnimate: Animation //隐藏动画
    private var tabContainerHeight: Int // 默认tab的高

    private lateinit var tabLayout: FixedTabContainer //tab布局
    private lateinit var menuContentLayout: FrameLayout //显示下拉菜单详情
    private var adapter: MenuAdapter? = null //控制tab 和tab 对应显示内容的adapter

    //构造函数
    init {
        orientation = VERTICAL
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu)
        contentLayoutId = typedArray.getResourceId(R.styleable.DropDownMenu_contentLayout, -1)
        val menuDisplayAnimId =
            typedArray.getResourceId(R.styleable.DropDownMenu_menuShowAnim, R.anim.trans_show_anim)
        val menuHideAnimateId = typedArray.getResourceId(
            R.styleable.DropDownMenu_menuDismissAnim,
            R.anim.trans_dismiss_anim
        )
        tabContainerHeight =
            typedArray.getDimension(R.styleable.DropDownMenu_menuBarHeight, 50.dp.toFloat()).toInt()

        typedArray.recycle()

        menuDisplayAnim = AnimationUtils.loadAnimation(context, menuDisplayAnimId)
        menuDisplayAnim.fillAfter = false
        menuHideAnimate = AnimationUtils.loadAnimation(context, menuHideAnimateId)
        menuHideAnimate.fillAfter = false
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        val mWidthWHeight = LayoutParams(LayoutParams.MATCH_PARENT, tabContainerHeight)
        tabLayout = FixedTabContainer(context) //菜单
        addView(tabLayout, mWidthWHeight)

        val matchWidthHeight = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        menuContentLayout = FrameLayout(context) //点击菜单显示的内容(对应的容器)
        addView(menuContentLayout, matchWidthHeight)

        if (contentLayoutId > View.NO_ID) { //根据布局中设置的主要内容布局(contentLayout) 拿到对应控件
            val contentLayout = findViewById<View>(contentLayoutId) ?: throw NoSuchElementException(
                "在 DropDownMenu 中没有找到 contentLayout = $contentLayoutId 的控件。contentLayout 必须是包含在 DropDownMenu 中的布局或控件"
            )
            removeViewInLayout(contentLayout) //将'主要内容布局'控件在DropDownMenu中移除
            menuContentLayout.addView(contentLayout) //再将'主要内容布局'放到和菜单统一级别的控件中
        }
        tabLayout.setOnTabSelectionChangeListener(object :
            FixedTabContainer.OnTabSelectionChangeListener { //菜单的点击事件。菜单本身的状态和显示在FixedTabContainer 和 DrawableTabView中控制
            override fun onTabSelected(tab: DrawableTabView) { //某一个菜单选中时，可拿到选中菜单的位置
                menuContentLayout.getChildAt(tab.getPosition() + 1)?.let { t->//根据选中的菜单的位置，显示对应菜单的控件, 因为第一个是显示主要内容的，第二个开始才是显示下拉菜单的内容，所以+1
                    if (t.visibility == View.GONE) t.visibility = View.VISIBLE
                    else t.visibility = View.GONE
                    t.startAnimation(menuDisplayAnim)
                    adapter?.onViewShow(t, tab.getPosition(), tab)//并通知adapter当前某个菜单显示
                } ?: Log.d(TAG, "tab in ${tab.getPosition()} is null")
            }

            override fun onTabUnSelect(tab: DrawableTabView, animate: Boolean) {
                menuContentLayout.getChildAt(tab.getPosition() + 1)?.let { t->
                    if (t.visibility == View.VISIBLE) t.visibility = View.GONE
                    else t.visibility = View.VISIBLE
                    if (animate) t.startAnimation(menuHideAnimate)
                    adapter?.onViewDismiss(t, tab.getPosition(), tab)
                } ?: Log.d(TAG, "tab in ${tab.getPosition()} is null")
            }
        })
        menuContentLayout.setOnClickListener { tabLayout.resetSelection() }
    }

    /**
     * 设置adapter
     * 根据adapter 设置tab 和 tab对应显示的控件
     */
    fun <T : MenuAdapter> setAdapter(adapter: T) {
        val menuCount = adapter.getMenuCount()
        val containerLp = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        val contentLp = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        for (x in 0 until menuCount) {
            val tab = tabLayout.newTab()
            adapter.onMenuTabCreate(tab, x)
            tabLayout.addTab(tab, false)
            val menuContent = adapter.onCreateView(this, x)
//            menuContent.visibility = View.GONE //不需要黑色使用背景时，需要调用此方法
//            menuContentLayout.addView(menuContent, layoutParams)

            val menuContentWrapper = FrameLayout(context) //为了实现黑色背景，多嵌套了一层，若能直接使用白色背景，可去掉
            menuContentWrapper.setBackgroundColor(resources.getColor(R.color.black_p50))
            menuContentWrapper.visibility = View.GONE
            menuContentWrapper.addView(menuContent,contentLp)
            menuContentWrapper.setOnClickListener { close() }
            menuContentLayout.addView(menuContentWrapper, containerLp)
        }
    }

    fun close(){
        tabLayout.resetSelection()
    }

    fun setMenuVisible(visibility: Boolean){
        tabLayout.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    fun setBottomLineVisible(visible: Boolean){
        this.tabLayout.setBottomLineVisible(visible)
    }

    fun setTopLineVisible(visible: Boolean){
        this.tabLayout.setTopLineVisible(visible)
    }

    companion object{
        const val TAG: String = "DropDownMenu"
    }
}