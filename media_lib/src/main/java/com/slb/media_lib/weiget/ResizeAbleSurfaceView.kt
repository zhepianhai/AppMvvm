package com.slb.media_lib.weiget

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView

/**
 * @author zph
 * 解决SurfaceView 在Camera2中预览尺寸的问题
 * 有全屏和自动选择最接近预览分辨率的
 *
 * */


class ResizeAbleSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {
    private var previewWidth = -1
    private var previewHeight = -1

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        var width: Int = MeasureSpec.getSize(widthMeasureSpec)
        val height: Int = MeasureSpec.getSize(heightMeasureSpec)
        if (previewWidth === 0 || previewHeight === 0) {
            return
        }
        val bufferRatio: Float =( previewHeight / previewWidth).toFloat() //1080/1902=0.9

        if (previewWidth * previewHeight < width * height) {
            width = (height * bufferRatio).toInt()
            setMeasuredDimension(width, height)
        }
    }

    fun resize(width: Int, height: Int) {
        previewWidth = width
        previewHeight = height
        holder.setFixedSize(width, height)
        requestLayout()
        invalidate()
    }
}