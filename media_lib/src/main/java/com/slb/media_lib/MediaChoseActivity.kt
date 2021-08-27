package com.slb.media_lib

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View.*
import androidx.core.app.ActivityCompat
import com.google.android.material.tabs.TabLayout
import com.gw.safty.common.extensions.exShortToast
import com.gw.safty.common.utils.file.FilePath
import com.slb.media_lib.helper.AudioRecordHelper
import com.slb.media_lib.helper.Camera2SurfaceHelper
import com.slb.media_lib.weiget.TouchButton
import kotlinx.android.synthetic.main.activity_media_chose.*
import kotlinx.android.synthetic.main.layout_media_buttom.*
import java.io.File
import java.lang.Exception
import java.util.*


/**
 * 拍照
 * 录像 60秒 15秒
 * 录音
 * */

class MediaChoseActivity : BaseMediaChose(), TabLayout.OnTabSelectedListener,
    Camera2SurfaceHelper.Camera2HelpImp, TouchButton.OnRecordListener {

    private val permissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    private lateinit var imagePath: String
    private lateinit var videoPath: String
    private lateinit var audioPath: String

    companion object {
        fun openActivityForResult(activity: Activity, resultCode: Int) {
            val intent = Intent(activity, MediaChoseActivity::class.java)
            activity.startActivityForResult(intent, resultCode)
        }

        //设置默认进去选择的拍摄类型
        fun openActivityForResult(activity: Activity, defaultType: Int, resultCode: Int) {
            val intent = Intent(activity, MediaChoseActivity::class.java)
            intent.putExtra("DEFAULT_TYPE", defaultType)
            activity.startActivityForResult(intent, resultCode)
        }
    }

    private val REQUEST_CAMERA_CODE = 0X1932

    override fun getLayoutId(): Int {
        return R.layout.activity_media_chose
    }


    override fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_CAMERA_CODE)
            }
            return
        }
        checkFile()

    }

    private fun checkFile() {
        try {
            File(FilePath.fileBasePath).apply {
                if (!exists()) mkdirs()
            }
            File(FilePath.fileBasePath + FilePath.TAKE_MEDIA_FILE_PATH).apply {
                if (!exists()) mkdirs()
                imagePath = FilePath.fileBasePath + FilePath.TAKE_MEDIA_FILE_PATH
            }
            File(FilePath.fileBasePath + FilePath.TAKE_VIDEO_FILE_PATH).apply {
                if (!exists()) mkdirs()
                videoPath = FilePath.fileBasePath + FilePath.TAKE_VIDEO_FILE_PATH
            }
            File(FilePath.fileBasePath + FilePath.TAKE_Audio_FILE_PATH).apply {
                if (!exists()) mkdirs()
                audioPath = FilePath.fileBasePath + FilePath.TAKE_Audio_FILE_PATH
            }
            if(imagePath.isEmpty()||videoPath.isEmpty()||audioPath.isEmpty()){
                showFileErrorDialog()
            }else {
                initView()
            }
        } catch (e: Exception) {
            showFileErrorDialog()
        }

    }

    private fun initView() {
        initTitle()
        initSurfaceView()
        initClick()

    }


    private fun initTitle() {
    }

    private fun initSurfaceView() {
        camera2Helper = Camera2SurfaceHelper(this, surfaceView)
        camera2Helper.setImagPath(imagePath)
        camera2Helper.setMp4Path(videoPath)
        camera2Helper.setCamera2HelpImpl(this)

        audioRecordHelper = AudioRecordHelper(this, waveformView)
        audioRecordHelper.setAudioPath(audioPath)
        tl_tabs.addOnTabSelectedListener(this)
        tl_tabs.post {
            tl_tabs.getTabAt(defaultType)?.select()
        }
        touch_btn.setCanTouch(false)
        touch_btn.setColorType(TouchButton.TYPE_PIC)
    }

    private fun initClick() {
        touch_btn.setOnClickListener {
            camera2Helper.takePhoto()
        }
        img_del.setOnClickListener {
            showDelVideoDialog()
        }
        img_ok.setOnClickListener {
            saveMediaFile()
        }
        touch_btn.setOnRecordListener(this)
        img_picdir.setOnClickListener {
            camera2Helper.switchCamera()
        }
    }

    override fun onResume() {
        camera2Helper.onResume()
        super.onResume()
    }

    override fun onPause() {
        camera2Helper.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        camera2Helper.onDestroy()
        super.onDestroy()
    }


    //拍照回调
    override fun camera2HelperImageImpl(path: String) {
        mCurrentPicPath = path
        lay_meida_state.visibility = VISIBLE
        tl_tabs.visibility = INVISIBLE
        img_picdir.visibility = INVISIBLE
    }

    //视频拍摄回调
    override fun camera2HelperVideoImpl(path: String) {
        mCurrentVideoPath = path
    }

    //录制开始视频或音频
    override fun onRecordStar() {
        if (type == TYPE_RECORD_SOUND) {
            mCurrentAudioPath = ""
            audioRecordHelper.startRecordSound({
                runOnUiThread {
                    mCurrentAudioPath = it
                    lay_meida_state.visibility = VISIBLE
                }
            },
                {
                    runOnUiThread {
                        exShortToast("${getString(R.string.hind_error_record)}：$it")
                    }

                })
        } else {
            mCurrentVideoPath = ""
            startProgressBar()
            camera2Helper.takeVideo()
        }
        tl_tabs.visibility = INVISIBLE
        img_picdir.visibility = INVISIBLE

    }

    //录制结束（音频结束）
    override fun onRecordEnd() {
        if (type == TYPE_RECORD_SOUND) {
            audioRecordHelper.stopRecord()
        } else {
            timer.cancel()
            camera2Helper.takeVideo()
            progressBar.visibility = GONE
        }

        lay_meida_state.visibility = VISIBLE

    }

    //启动进度条
    private fun startProgressBar() {
        progressBar.visibility = VISIBLE
        progressBar.max = getCurrentPhotoTime()
        progressBar.progress = 0
        timer = Timer()
        timer.purge()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (progressBar.progress >= getCurrentPhotoTime()) { //指定时间取消
                    onRecordEnd()
                }
                progressBar.progress += 1
            }
        }, 0, 1000)

    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkFile()
            }
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let {
            when (it.text) {
                resources.getString(R.string.img_pic) -> {
                    type = TYPE_RECORD_PIC
                    img_picdir.visibility= VISIBLE
                    touch_btn.setCanTouch(false)
                    touch_btn.setColorType(TouchButton.TYPE_PIC)
                    if (waveformView.visibility == VISIBLE) {
                        waveformView.visibility = INVISIBLE
                    }
                    if (surfaceView.visibility == INVISIBLE) {
                        surfaceView.visibility = VISIBLE
                        camera2Helper.onResume()
                    }
                }
                resources.getString(R.string.img_recording_15) -> {
                    type = TYPE_RECORD_15
                    img_picdir.visibility= VISIBLE
                    touch_btn.setCanTouch(true)
                    touch_btn.setColorType(TouchButton.TYPE_VIDEO)
                    if (waveformView.visibility == VISIBLE) {
                        waveformView.visibility = INVISIBLE
                    }
                    if (surfaceView.visibility == INVISIBLE) {
                        surfaceView.visibility = VISIBLE
                        camera2Helper.onResume()
                    }
                }
                resources.getString(R.string.img_recording_60) -> {
                    type = TYPE_RECORD_60
                    img_picdir.visibility= VISIBLE
                    touch_btn.setCanTouch(true)
                    touch_btn.setColorType(TouchButton.TYPE_VIDEO)
                    if (waveformView.visibility == VISIBLE) {
                        waveformView.visibility = INVISIBLE
                    }
                    if (surfaceView.visibility == INVISIBLE) {
                        surfaceView.visibility = VISIBLE
                        camera2Helper.onResume()
                    }
                }
                resources.getString(R.string.img_recording_sound) -> {
                    type = TYPE_RECORD_SOUND
                    img_picdir.visibility= INVISIBLE
                    touch_btn.setCanTouch(true)
                    touch_btn.setColorType(TouchButton.TYPE_AUDIO)
                    waveformView.visibility = VISIBLE
                    surfaceView.visibility = INVISIBLE
                    camera2Helper.onPause()
                }
            }
        }

    }

}
