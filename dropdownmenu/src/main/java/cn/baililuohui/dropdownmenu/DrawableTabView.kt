package cn.baililuohui.dropdownmenu

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

@Suppress("DEPRECATION")
class DrawableTabView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var tabClickCallback: OnTabClickCallback? = null

    var indicatorMode = INDICATOR_FIT // 指示器的绘制方式
    var indicatorHeight: Float = 2.5f //下划线指示器的高
    var indicatorColor: Int = Color.parseColor("#328cfd") //下划线指示器的颜色
    var drawableIndicatorId = R.drawable.drawable_indicator //右侧图片指示器的id, 不需要则设置为0即可
    var textColorListId: Int = R.color.tab_default_text_color //text文本的颜色，默认未选中黑色; 选中蓝色。 不需要则设置为0即可
    var indicatorFixSize: Int = -1 //如果需要直接指定下划线指示器的长，需设置模式和长度

    private var tabTextView: AppCompatTextView = AppCompatTextView(context) //默认显示的文本，可更改可重新赋值

    private var tabPosition = -1 //当前控件处于父控件的位置，需初始化时手动设置

    private var indicatorPaint = Paint() //绘制下划线的画笔

    init {
        setWillNotDraw(false)
        indicatorPaint.color = indicatorColor
        if (drawableIndicatorId > 0){
            val drawable = resources.getDrawable(drawableIndicatorId)
            tabTextView.compoundDrawablePadding = 8.dp
            drawable.setBounds(0, 5, 8.dp, 13.dp)
            drawable.level = 1
            tabTextView.setCompoundDrawables(null, null, drawable, null)
        }
        if (textColorListId > 0){
            tabTextView.setTextColor(ContextCompat.getColorStateList(context, textColorListId))
        }
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER
        addView(tabTextView, lp)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        if (isSelected){
            val textWidth = tabTextView.paint.measureText(tabTextView.text.toString())
            var startX: Float = paddingStart.toFloat()
            var endX: Float = (measuredWidth - paddingEnd).toFloat()
            if (indicatorMode == INDICATOR_FIT && textWidth < measuredWidth) {
                startX = (measuredWidth - textWidth) / 2
                endX = startX + textWidth
            }
            if (indicatorMode == INDICATOR_FIX && indicatorFixSize > 0){
                startX = (measuredWidth.toFloat() - indicatorFixSize) / 2
                endX = startX + indicatorFixSize
            }
            canvas.drawRect(
                startX,
                measuredHeight - indicatorHeight.dp,
                endX,
                measuredHeight.toFloat(),
                indicatorPaint
            )
            canvas.save()
        }
    }

    interface OnTabClickCallback{
        fun onTabClick(tab: DrawableTabView)
    }

    fun setOnTabClickCallback(callback: OnTabClickCallback){
        this.tabClickCallback = callback
        setOnClickListener {
            val select = isSelect()
            setSelect(!select)
            tabClickCallback?.onTabClick(this)
        }
    }

    fun getPosition(): Int = tabPosition

    fun setPosition(pos: Int){
        tabPosition = pos
    }

    fun getTextView(): AppCompatTextView = tabTextView
    fun setTextView(textView: AppCompatTextView){
        this.tabTextView = textView
    }

    fun isSelect() = tabTextView.isSelected

    fun setSelect(selected: Boolean){
        isSelected = selected
        tabTextView.isSelected = selected
        val drawable = tabTextView.compoundDrawables[2]
        drawable.level = 1 - drawable.level
//        postInvalidate()
    }

    companion object {
        const val TAG = "DrawableTabView"
        const val INDICATOR_FIT = 0 //根据文本长度自适应
        const val INDICATOR_FULL = 1 //绘制充满整个控件
        const val INDICATOR_FIX = 2

        fun defaultTab(context: Context, title: String, pos: Int): DrawableTabView{
            val tab = DrawableTabView(context).apply {
                setPosition(pos)
                indicatorMode = INDICATOR_FIX
                indicatorFixSize = 60.dp
            }
            tab.tabTextView.apply {
                text = title
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                maxLines = 1
                maxEms = 6
                ellipsize = TextUtils.TruncateAt.END
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params.weight = 1f
            params.gravity = Gravity.CENTER
            tab.layoutParams = params
            return tab
        }
    }
}