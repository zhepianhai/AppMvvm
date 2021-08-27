package com.gw.safty.common.utils.file

import android.content.Context
import android.os.Environment
import timber.log.Timber
import java.io.File

object FilePath {
    var init: Boolean = false

    /**
     * 各种文件缓存的路径
     */
    private const val FILE_CACHE_PATH = "/files"
    private const val MEDIA_CACHE_PATH = "/media"
    private const val LUBAN_CACHE_PATH = "/luban"


     const val APP_HOME_PATH = "sldc"
     const val TAKE_MEDIA_FILE_PATH = "/Image"
     const val TAKE_MEDIA_FILE_PATH_SOURECE = "/ImageSource"
     const val TAKE_MEDIA_FILE_PATH_TEMP = "/Temp"
     const val OFF_LINE_MAP_FILE_PATH = "/OffLineMap"
     const val LOCATION_TEST_FILE_PATH = "/LocationTest"
     const val OFFLINE_DU_CHA_OBJ_PATH = "/OfflineDuChaObi"
     const val FILE_PATH_TEMP_OFFLINE_ALL = "/TempOfflineAll"
     const val TAKE_MEDIA_FILE_PATH_EDIT = "/ImageEdit" //图片编辑的文件夹
     const val TAKE_VIDEO_FILE_PATH = "/Video" //视频相关文件夹
     const val TAKE_VIDEO_FILE_PATH_EDIT = "/VideoEdit" //视频处理的文件夹
     const val TAKE_Audio_FILE_PATH = "/Audio"//音频相关


    var fileCachePath: String = "" //文件缓存
        private set
    var mediaCachePath: String = fileCachePath + MEDIA_CACHE_PATH  //多媒体缓存
        private set
    var lubanCachePath: String = fileCachePath + LUBAN_CACHE_PATH //鲁班压缩缓存
        private set

    val fileBasePath =
        Environment.getExternalStorageDirectory().path + File.separator + APP_HOME_PATH

    var externalPicturePath: String = "" //图片存储路径
    var externalVideoPath: String = "" //视频存储路径

    fun setup(context: Context) {
        if (init) return
        //初始化各类文件路径
        val extlCache = context.externalCacheDir?.path
        val extlFiles = context.cacheDir.path
        fileCachePath = (extlCache ?: extlFiles) + FILE_CACHE_PATH
        mediaCachePath = fileCachePath + MEDIA_CACHE_PATH
        lubanCachePath = fileCachePath + LUBAN_CACHE_PATH

        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let {
            if (!it.exists()) it.mkdirs()
            externalPicturePath = it.path
        }
        context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.let {
            if (!it.exists()) it.mkdirs()
            externalVideoPath = it.path
        }


        createAllPath()
        init = true
        Timber.d("缓存路径: $fileCachePath 外部图片路径: $externalPicturePath 外部视频路径: $externalPicturePath")
    }

    /**创建各类目录**/
    private fun createAllPath() {
        File(fileCachePath).apply {
            if (!exists()) {
                if (parentFile == null || parentFile?.exists() == false) {
                    parentFile!!.mkdir()
                }
                this.mkdirs()
            }
        }
        File(mediaCachePath).apply {
            if (!exists()) mkdirs()
        }
        File(lubanCachePath).apply {
            if (!exists()) mkdirs()
        }

        //外部文件夹存储创建

        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            File(fileBasePath).apply {
                if (!exists()) mkdirs()
            }
            File(fileBasePath + TAKE_MEDIA_FILE_PATH).apply {
                if (!exists()) mkdirs()
            }
            File(fileBasePath + TAKE_MEDIA_FILE_PATH_SOURECE).apply {
                if (!exists()) mkdirs()
            }
            File(fileBasePath + TAKE_MEDIA_FILE_PATH_TEMP).apply {
                if (!exists()) mkdirs()
            }
            File(fileBasePath + OFF_LINE_MAP_FILE_PATH).apply {
                if (!exists()) mkdirs()
            }
            File(fileBasePath + LOCATION_TEST_FILE_PATH).apply {
                if (!exists()) mkdirs()
            }
            File(fileBasePath + OFFLINE_DU_CHA_OBJ_PATH).apply {
                if (!exists()) mkdirs()
            }
            File(fileBasePath + FILE_PATH_TEMP_OFFLINE_ALL).apply {
                if (!exists()) mkdirs()
            }
            File(fileBasePath + TAKE_MEDIA_FILE_PATH_EDIT).apply {
                if (!exists()) mkdirs()
            }
            File(fileBasePath + TAKE_VIDEO_FILE_PATH).apply {
                if (!exists()) mkdirs()
            }
            File(fileBasePath + TAKE_VIDEO_FILE_PATH_EDIT).apply {
                if (!exists()) mkdirs()
            }
            File(fileBasePath + TAKE_Audio_FILE_PATH).apply {
                if (!exists()) mkdirs()
            }

        }

    }
}