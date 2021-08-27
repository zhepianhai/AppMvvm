package com.slb.media_lib.weiget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class WaveformView : View {

    private val datas: ArrayList<Short> = ArrayList()
    private var max: Short = 100
    private var mWidth = 0f
    private var mHeight = 0f
    private var space = 1f
    private var mWavePaint: Paint? = null
    private var baseLinePaint: Paint? = null
    private var mWaveColor: Int = Color.WHITE
    private var mBaseLineColor: Int = Color.WHITE
    private var waveStrokeWidth = 2f
    private var invalidateTime = 1000 / 100
    private var drawTime: Long = 0
    private var isMaxConstant = false

    constructor(context: Context?) : super(context) {initView()}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {initView()}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    fun initView() {
        mWaveColor = Color.parseColor("#f5f8f3")
        mBaseLineColor = Color.parseColor("#f5c830")

        waveStrokeWidth = 4f

//        max = 100
//        invalidateTime = 10

        space =12f

        initPainters()
    }

    private fun initPainters() {
        mWavePaint = Paint()
        mWavePaint?.color = mWaveColor // 画笔为color
        mWavePaint?.strokeWidth = waveStrokeWidth // 设置画笔粗细
        mWavePaint?.isAntiAlias = true
        mWavePaint?.isFilterBitmap = true
        mWavePaint?.strokeCap = Paint.Cap.ROUND
        mWavePaint?.style = Paint.Style.FILL
        baseLinePaint = Paint()
        baseLinePaint?.color = mBaseLineColor // 画笔为color
        baseLinePaint?.strokeWidth = 1f // 设置画笔粗细
        baseLinePaint?.isAntiAlias = true
        baseLinePaint?.isFilterBitmap = true
        baseLinePaint?.style = Paint.Style.FILL
    }

    /**
     * 如果改变相应配置  需要刷新相应的paint设置
     */
    fun invalidateNow() {
        initPainters()
        invalidate()
    }

    fun addData(data: Short) {
        var data = data

        if (data < 0) {
            data = (-data).toShort()
        }
        if (data > max && !isMaxConstant) {
            max = data
        }
        if (datas.size > mWidth / space) {
            synchronized(this) {
                datas.removeAt(0)
                datas.add(data)
            }
        } else {
            datas.add(data)
        }
        if (System.currentTimeMillis() - drawTime > invalidateTime) {
            invalidate()
            drawTime = System.currentTimeMillis()
        }
    }

    fun clear() {
        datas.clear()
        invalidateNow()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.translate(0F, mHeight / 2)
        drawBaseLine(canvas)
        drawWave(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

    private fun drawWave(mCanvas: Canvas) {
        for (i in datas.indices) {
            val x = i * space
            val y = datas[i].toFloat() / max * mHeight / 2
            mWavePaint?.let {
                mCanvas.drawLine(x, -y, x, y, it)
            }
        }
    }

    private fun drawBaseLine(mCanvas: Canvas) {
        baseLinePaint?.let { mCanvas.drawLine(0F, 0F, mWidth, 0F, it) }
    }


    fun getMax(): Short {
        return max
    }

    fun setMax(max: Short) {
        this.max = max
    }

    fun getSpace(): Float {
        return space
    }

    fun setSpace(space: Float) {
        this.space = space
    }

    fun getmWaveColor(): Int {
        return mWaveColor
    }

    fun setmWaveColor(mWaveColor: Int) {
        this.mWaveColor = mWaveColor
        invalidateNow()
    }

    fun getmBaseLineColor(): Int {
        return mBaseLineColor
    }

    fun setmBaseLineColor(mBaseLineColor: Int) {
        this.mBaseLineColor = mBaseLineColor
        invalidateNow()
    }

    fun getWaveStrokeWidth(): Float {
        return waveStrokeWidth
    }

    fun setWaveStrokeWidth(waveStrokeWidth: Float) {
        this.waveStrokeWidth = waveStrokeWidth
        invalidateNow()
    }

    fun getInvalidateTime(): Int {
        return invalidateTime
    }

    fun setInvalidateTime(invalidateTime: Int) {
        this.invalidateTime = invalidateTime
    }

    fun isMaxConstant(): Boolean {
        return isMaxConstant
    }

    fun setMaxConstant(maxConstant: Boolean) {
        isMaxConstant = maxConstant
    }
}