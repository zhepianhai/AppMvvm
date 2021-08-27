package com.gw.zph.core.network

import android.content.Context
import android.os.Environment
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.gw.safty.common.network.CusHttpLogInterceptor
import com.gw.safty.common.network.SecureNetworktConfig
import com.gw.zph.BuildConfig
import com.gw.zph.base.retrofitconvert.MyGsonConverterFactory
import okhttp3.Cache
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLSession

object RetrofitClient {
    private const val CONNECT_TIME_OUT = 30L
    private const val READ_WRITE_TIME_OUT = 20L

    private var retrofit: Retrofit? = null

    fun setup(context: Context) {
        val builder = OkHttpClient.Builder()
        // 连接超时时间
        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
        // 连接写入超时时间
        builder.writeTimeout(READ_WRITE_TIME_OUT, TimeUnit.SECONDS)
        // 链接读取超时时间
        builder.readTimeout(READ_WRITE_TIME_OUT, TimeUnit.SECONDS)
        // http缓存大小
        builder.cache(Cache(Environment.getDataDirectory(), 8 * 10 * 1000 * 1000))

        builder.addInterceptor(CacheInterceptor())
        if (BuildConfig.DEBUG_MODE) {
            // facebook的stetho拦截器
            builder.addNetworkInterceptor(StethoInterceptor()) // 记录http请求日志拦截器
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(httpLoggingInterceptor)
        }

        client = builder.build()
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MyGsonConverterFactory.create())
            .client(client!!)
            .build()
    }

    private var client: OkHttpClient? = null

    fun <T> createService(clazz: Class<T>): T {
        return retrofit?.create(clazz)
            ?: throw IllegalStateException("未初始化网络，请调用CommonRetrofitClient.setup(Context)初始化")
    }


}