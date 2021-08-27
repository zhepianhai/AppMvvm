package com.slb.media_lib.util

import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi
import java.lang.Long.signum
/**
 * @author zph
 * 尺寸Size 的排序
 * */

class ZPHCompareSizesByArea : Comparator<Size> {

    // We cast here to ensure the multiplications won't overflow
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun compare(lhs: Size, rhs: Size) =
        signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
}