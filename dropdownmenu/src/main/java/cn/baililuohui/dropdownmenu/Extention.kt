package cn.baililuohui.dropdownmenu

import android.content.res.Resources
import android.util.TypedValue

/**
 * 将某个数值当作dp值,并将其转成当前分辨率的px值
 * e.g.
 *      val pxValue = 10.asDp2Px //将10dp转换成px
 */
inline val Float.asPx2dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
inline val Int.asPx2dp: Int
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

inline val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()
inline val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )