package com.gw.zph.application

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.facebook.stetho.Stetho
import com.getkeepsafe.relinker.ReLinker
import com.gw.safty.common.UncaughtExceptionCatcher
import com.gw.zph.BuildConfig
import com.gw.zph.base.db.DbHelper
import com.gw.zph.core.network.RetrofitClient
import com.gw.zph.utils.umeng.PushHelper
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.jeremyliao.liveeventbus.LiveEventBus
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import kiun.com.bvroutine.ActivityApplication
import kiun.com.bvroutine.handlers.ListHandler
import timber.log.Timber
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.properties.Delegates

@SuppressLint("Registered")
class MyApplication : ActivityApplication(), Application.ActivityLifecycleCallbacks  {
    private var mFinalCount: Int = 0
    private val activityList = LinkedList<Activity>()
    private var mamapl: AMapLocation? = null
    override fun onCreate() {
        super.onCreate()
        Instance = this
        registerActivityLifecycleCallbacks(this)
        Stetho.initializeWithDefaults(this)
        RetrofitClient.setup(this)
        setupTimber()
        setupMmkv()
        initSpeech()
        initDb()
        initUM()
        initEventBus()
        initQMUI()
        if (BuildConfig.DEBUG){
            Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionCatcher())
        }
    }

    private fun initEventBus() {
        ListHandler.configNormal("系统发生错误,请下拉重试", "未找到相应信息", "正在为您查找数据")
        //配置liveEventBus
        LiveEventBus.get()
            .config()
            .supportBroadcast(this)
            .lifecycleObserverAlwaysActive(true)
            .autoClear(false)
    }
    private fun initQMUI(){
        QMUISwipeBackActivityManager.init(this)
    }
    private fun initDb() {
        //复制assets目录下的数据库文件到应用数据库中
//        try {
//            copyDataBase("dbtest.db")
//            insertExternalDatabase(DbHelper.BASIC_PROBLEM_DB_NAME)
//        } catch (e: Exception) {
//        }
        DbHelper.getInstance().initAddr(this) //初始化外部数据库
        DbHelper.getInstance().init(this) //数据库初始化
    }
    private fun initUM(){
        //设置LOG开关，默认为false
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true)
//        PushHelper.init(this);
//        UMConfigure.preInit()
        UMConfigure.init(this, "613712695f3497702f2360ab", BuildConfig.BUILD_TYPE, UMConfigure.DEVICE_TYPE_PHONE, "")
//        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null)
        // 选用LEGACY_AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_AUTO)
    }
    companion object {
        var Instance: MyApplication by Delegates.notNull()
        const val LocationTest = true
    }

    private fun setupMmkv(){
        val dir = filesDir.absolutePath + "/mmkv"
        MMKV.initialize(dir) { ReLinker.loadLibrary(this@MyApplication, it) }
        MMKV.setLogLevel(MMKVLogLevel.LevelInfo)
    }

    private fun setupTimber(){
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }else {
            Timber.plant()
        }
    }
    /**
     * 科大讯飞语音初始化
     * */
    private fun initSpeech(){
        val param = StringBuffer()
        param.append("appid=5f0d13df")
        param.append(",")
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC)
        SpeechUtility.createUtility(this@MyApplication, param.toString())
    }
    fun finishAllActivities() {
        activityList.forEach {
            it.finish()
        }
        activityList.clear()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        savedInstanceState?.let {
        }
        activityList.add(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        mFinalCount++
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityList.remove(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        mFinalCount--
        if (mFinalCount <= 0) {
            Toast.makeText(activity, "应用已进入后台运行", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResumed(activity: Activity) {
        Timber.tag("打开").e("${activity.javaClass.canonicalName}")
    }
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    @Synchronized
    fun getMamapl(): AMapLocation? {
        return mamapl
    }

    @Synchronized
    fun setMamapl(mamapl: AMapLocation?) {
        this.mamapl = mamapl
    }



    @Throws(IOException::class)
    private fun copyDataBase(dbname: String) {
        // Open your local db as the input stream
        val myInput = this.assets.open(dbname)
        // Path to the just created empty db
        val outFileName = getDatabasePath(DbHelper.DB_NAME_ADD)
        if (!outFileName.exists()) {
            outFileName.parentFile?.mkdirs()

            // Open the empty db as the output stream
            val myOutput: OutputStream = FileOutputStream(outFileName)
            // transfer bytes from the inputfile to the outputfile
            val buffer = ByteArray(1024)
            var length: Int
            while (myInput.read(buffer).also { length = it } > 0) {
                myOutput.write(buffer, 0, length)
            }
            // Close the streams
            myOutput.flush()
            myOutput.close()
            myInput.close()
        }
    }

    private fun insertExternalDatabase(assetsFileName: String): Boolean {
        var success = true
        var `is`: InputStream? = null
        var fos: FileOutputStream? = null
        try {
            val destDbFile = getDatabasePath(assetsFileName)
            if (!destDbFile.exists()) {
                destDbFile.parentFile?.mkdirs()
                `is` = assets.open(assetsFileName)
                fos = FileOutputStream(destDbFile)
                val buffer = ByteArray(1024)
                var length: Int
                while (`is`.read(buffer).also { length = it } > 0) {
                    fos.write(buffer, 0, length)
                }
                success = true
            }
        } catch (e: java.lang.Exception) {
            success = false
            e.printStackTrace()
        } finally {
            try {
                `is`?.close()
                if (fos != null) {
                    fos.flush()
                    fos.close()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return success
    }
}