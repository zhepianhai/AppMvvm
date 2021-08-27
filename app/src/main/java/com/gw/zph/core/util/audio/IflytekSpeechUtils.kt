package com.gw.zph.core.util.audio

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.gw.zph.application.MyApplication
import com.iflytek.cloud.*

/**
 * 科大讯飞语音工具类
 * */
class IflytekSpeechUtils private constructor(context: Context) {
    companion object {
        val instance: IflytekSpeechUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            IflytekSpeechUtils(context=MyApplication.Instance.applicationContext) }
    }
    var mContext: Context? = null
    var mInitListener: InitListener? = null
    var mSynthesizer: SpeechSynthesizer? = null

    // 默认本地发音人
    private val voicerLocal = "aisjinger"

    init {
        this.mContext = context
        /**
         * 初始化监听器。
         */
        if (mInitListener == null) {
            mInitListener = InitListener { i ->
                if (i != ErrorCode.SUCCESS) {
                    Log.d("TAG", "初始化失败")
                    return@InitListener
                }
                mSynthesizer!!.setParameter(SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_MSC)
                mSynthesizer!!.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD)
                mSynthesizer!!.setParameter(SpeechConstant.VOICE_NAME, voicerLocal)
                mSynthesizer!!.setParameter(SpeechConstant.SPEED, "50") // 设置语速
                mSynthesizer!!.setParameter(SpeechConstant.VOLUME, "80") // 设置音量，范围 0~100
                mSynthesizer!!.setParameter(
                    SpeechConstant.ENGINE_TYPE,
                    SpeechConstant.TYPE_CLOUD
                ) //设置云端

            }
        }


    }
    /**
     * 文本转语音
     * @param content
     * */
    fun  doSpeak(content: String?) {
        if (mSynthesizer == null) {
            mSynthesizer = SpeechSynthesizer.createSynthesizer(
                mContext,
                mInitListener
            )
        }
        if (mSynthesizer!!.isSpeaking) {
            mSynthesizer!!.stopSpeaking()
        }
        mSynthesizer!!.startSpeaking(content, mSynthesizerListener)
    }
    fun stopSpeaking(){
        if(mSynthesizer!=null&&mSynthesizer!!.isSpeaking){
            mSynthesizer!!.stopSpeaking()
        }
    }

    @Suppress("ControlFlowWithEmptyBody")
    private val mSynthesizerListener: SynthesizerListener =
        object : SynthesizerListener {
            override fun onSpeakBegin() {
                //开始播放
            }

            override fun onBufferProgress(i: Int, i1: Int, i2: Int, s: String) {
                //合成进度
            }

            override fun onSpeakPaused() {
                //暂停播放
            }

            override fun onSpeakResumed() {
                //继续播放
            }

            override fun onSpeakProgress(i: Int, i1: Int, i2: Int) {
                //播放进度
            }

            @Suppress("SENSELESS_COMPARISON")
            override fun onCompleted(speechError: SpeechError) {
                if (speechError == null) { //播放完成
                } else {
                }
            }

            override fun onEvent(i: Int, i1: Int, i2: Int, bundle: Bundle) {
                //  ToastHelper.showToast(context, "错误码" + i + "第二个" + i1 + "第三个" + i2);
            }
        }
}