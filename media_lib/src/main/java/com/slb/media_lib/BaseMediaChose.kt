package com.slb.media_lib

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gw.safty.common.base.BaseActivity
import com.gw.safty.common.extensions.exShortToast
import com.gw.safty.common.utils.StatusBarHelper
import com.slb.media_lib.helper.AudioRecordHelper
import com.slb.media_lib.helper.Camera2SurfaceHelper
import com.slb.media_lib.util.ZPHFileUtils
import kotlinx.android.synthetic.main.activity_media_chose.*
import kotlinx.android.synthetic.main.layout_media_buttom.*
import java.util.*


abstract class BaseMediaChose : BaseActivity(){

    abstract fun getLayoutId(): Int

    abstract fun checkPermission()

    companion object {
        val TYPE_RECORD_PIC = 0
        val TYPE_RECORD_60 = 1
        val TYPE_RECORD_15 = 2
        val TYPE_RECORD_SOUND = 3
    }
    val tag = "TAG"
    var type = TYPE_RECORD_PIC

    lateinit var timer: Timer

    lateinit var camera2Helper: Camera2SurfaceHelper

    lateinit var audioRecordHelper: AudioRecordHelper
    var mCurrentPicPath=""
    var mCurrentVideoPath=""
    var mCurrentAudioPath=""

    var defaultType=TYPE_RECORD_PIC
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        intent?.let {
            defaultType=it.getIntExtra("DEFAULT_TYPE",TYPE_RECORD_PIC)
        }
        StatusBarHelper.translucent(this)
        StatusBarHelper.setStatusBarLightMode(this)
        checkPermission()
    }


    protected fun getCurrentPhotoTime():Int{
        if (type == TYPE_RECORD_15) {
            return  15
        }
        return 60
    }
    /**
     * 恢复录制初始状态
     * */
    private fun initStarRecore(){
        if(type==TYPE_RECORD_SOUND){
            waveformView.clear()
        }else{
            progressBar.max =getCurrentPhotoTime()
            progressBar.progress=0
        }

        tl_tabs.visibility= View.VISIBLE
        lay_meida_state.visibility=View.INVISIBLE
        img_picdir.visibility=View.VISIBLE
    }
    protected fun showDelVideoDialog(){
        var title=getString(R.string.del_current_video)
        if(type==TYPE_RECORD_SOUND) title=getString(R.string.del_current_aduio)
        else if(type== TYPE_RECORD_PIC) title=getString(R.string.del_current_pic)
        AlertDialog.Builder(this)
            .setTitle(title)
            .setPositiveButton(getString(R.string.hind_ok)
            ) { la, _ ->
                la.dismiss()
                if(mCurrentVideoPath.isNotEmpty()){
                    ZPHFileUtils.deleteDirContainFile(mCurrentVideoPath)
                }
                initStarRecore()
            }
            .setNeutralButton(getString(R.string.hind_dis)
            ) { la, _ ->
                la.dismiss()
            }.create()
            .show()
    }
    //点击保存时候操作
    protected fun saveMediaFile(){
        exShortToast(getString(R.string.hind_success_record))
        initStarRecore()
        //2 回传值
        val intent = Intent()
        when (type) {
            TYPE_RECORD_SOUND -> {
                intent.putExtra("KEY_DATA", mCurrentAudioPath)
                intent.putExtra("KEY_TYPE", 2)
            }
            TYPE_RECORD_PIC -> {
                intent.putExtra("KEY_DATA", mCurrentPicPath)
                intent.putExtra("KEY_TYPE", 0)
            }
            else -> {
                intent.putExtra("KEY_DATA", mCurrentVideoPath)
                intent.putExtra("KEY_TYPE", 1)
            }
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun showFileErrorDialog(){
        var title=getString(R.string.file_error)
        AlertDialog.Builder(this)
            .setTitle(title)
            .setPositiveButton(getString(R.string.hind_ok)
            ) { la, _ ->
                la.dismiss()
               finish()
            }.setCancelable(false)
            .create()
            .show()
    }

}