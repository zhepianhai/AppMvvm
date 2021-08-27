package com.gw.zph.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

@Suppress("UseExpressionBody", "DEPRECATION")
object Device {
    var init: Boolean = false
    var isSimulator: Boolean = false
        private set

    val brand: String = Build.BRAND ?: ""
    val model: String = Build.MODEL ?: ""
    var imei: String = ""
        private set

    var screenWidth: Int = 0
        private set
    var screenHeight: Int = 0
        private set

    fun setup(context: Context) {
        if (init) return
        setupImei(context)
        checkSimulator(context)
        screenWidth = context.resources.displayMetrics.widthPixels
        screenHeight = context.resources.displayMetrics.heightPixels
        init = true
    }

    @SuppressLint("HardwareIds")
    fun setupImei(context: Context) {
        imei = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun checkSimulator(context: Context) {
        val url = "tel:" + "123456"
        val intent = Intent()
        intent.data = Uri.parse(url)
        intent.action = Intent.ACTION_DIAL
        // 是否可以处理跳转到拨号的 Intent
        val canCallPhone = intent.resolveActivity(context.packageManager) != null
        //Build.FINGERPRINT.toLowerCase() .contains("test-keys") ||  //华胜天成的手机需要去掉
        val isNetworkOperator =
            (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkOperatorName.toLowerCase(
                Locale.getDefault()
            )
        isSimulator = Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.toLowerCase(Locale.getDefault()).contains("vbox") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("MuMu") ||
                Build.MODEL.contains("virtual") ||
                Build.SERIAL.equals("android", ignoreCase = true) ||
                Build.MANUFACTURER.contains("Genymotion") ||
                (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
                ("google_sdk" == Build.PRODUCT) ||
                (isNetworkOperator == "android") ||
                !canCallPhone
    }

    fun isRoot(): Boolean {
        val binPath = "/system/bin/su"
        val xBinPath = "/system/xbin/su"
        if (File(binPath).exists() && exec(binPath)) return true
        return File(xBinPath).exists() && exec(xBinPath)
    }

    fun exec(command: String): Boolean {
        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec("ls -l $command")
            val iStream = BufferedReader(InputStreamReader(process.inputStream))
            val str = iStream.readLine()
            if (str?.length ?: 0 >= 4) {
                val flag = str[3]
                if (flag == 's' || flag == 'x') return true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            process?.destroy()
        }
        return false
    }
}