package com.slb.media_lib.helper

import android.app.Activity
import android.media.AudioRecord
import android.media.MediaRecorder
import com.gw.safty.common.extensions.exShortToast
import com.slb.media_lib.R
import com.slb.media_lib.helper.config.AudioConfig
import com.slb.media_lib.util.ZPHPcmToWavUtil
import com.slb.media_lib.weiget.WaveformView
import java.io.File
import java.io.FileOutputStream

/**
 * 录制音频的工具类
 *
 * */
open class AudioRecordHelper(private val activity: Activity, private val waveformView: WaveformView) {
    private var isRecording: Boolean = false
    private var audioRecord: AudioRecord?=null
    private var currentFilePathPcm = ""
    private var currentFilePathWav = ""
    private var audioPath:String=""

    open fun setAudioPath(path: String) {
        audioPath = path
    }
    private val minBufferSize: Int = AudioRecord.getMinBufferSize(
        AudioConfig.SAMPLE_RATE_INHZ,
        AudioConfig.CHANNEL_CONFIG,
        AudioConfig.AUDIO_FORMAT
    )



    open fun startRecordSound(onSuccess: (savedPath: String) -> Unit,
                              onFailed: (msg: String) -> Unit) {
        if(audioPath.isEmpty()){
            activity.exShortToast(activity.getString(R.string.file_error))
            return
        }
        val filename = "${System.currentTimeMillis()}.pcm"
        val file = File(audioPath, filename)
        if (file.exists()) {
            file.delete()
        }

        activity.exShortToast("音频采集开始")
        val data = ByteArray(minBufferSize)
        if(audioRecord==null||audioRecord!!.state==AudioRecord.STATE_UNINITIALIZED){
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC, AudioConfig.SAMPLE_RATE_INHZ, AudioConfig.CHANNEL_CONFIG,
                AudioConfig.AUDIO_FORMAT, minBufferSize
            )
        }
        audioRecord!!.startRecording()
        isRecording = true
        //pcm数据无法直接播放，保存为wav格式
        Thread(Runnable {
            var os: FileOutputStream? = null
            try {
                os = FileOutputStream(file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            while (isRecording) {
                val read: Int = audioRecord?.read(data, 0, minBufferSize)!!

                //如果读取音频数据没有出现错误，就将数据写到文件
                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os!!.write(data)
                        waveformView.addData(getShort(data))
                    } catch (e: Exception) {
                    }
                }
            }
            currentFilePathPcm = file.absolutePath
            val pcmToWavUtil = ZPHPcmToWavUtil(
                AudioConfig.SAMPLE_RATE_INHZ,
                AudioConfig.CHANNEL_CONFIG,
                AudioConfig.AUDIO_FORMAT
            )
            val pcmFile = File(currentFilePathPcm)
            val wavFile = File(audioPath, "${System.currentTimeMillis()}.wav")
            pcmToWavUtil.pcmToWav(pcmFile.absolutePath, wavFile.absolutePath)
            currentFilePathWav = wavFile.absolutePath
            onSuccess(currentFilePathWav)
            try {
                os!!.close()
            } catch (e: Exception) {
                onFailed(e.printStackTrace().toString())
            }
        }).start()
    }
    open fun stopRecord() {
        activity.exShortToast("音频采集结束")
        isRecording = false
        //释放资源
        audioRecord?.let {
            if (it.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                it.stop()
            }
            if (it.state == AudioRecord.STATE_INITIALIZED) {
                it.release()
            }
        }

    }
    private fun getShort(b: ByteArray): Short {
        return (((b[1].toInt() shl 8) or b[0].toInt() and 0xff).toShort())
    }
}