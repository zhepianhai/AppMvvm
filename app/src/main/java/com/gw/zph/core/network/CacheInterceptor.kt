package com.gw.zph.core.network

import com.gw.zph.BuildConfig
import com.gw.zph.core.*
import com.gw.zph.core.StatusHolder.loginUserBean
import com.gw.zph.core.StatusHolder.orgBean
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class CacheInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String = loginUserBean?.accessToken ?: ""
        val persId: String = loginUserBean?.guid ?: ""
        val orgId: String = orgBean?.orgId ?: ""
        val accessToken: String = loginUserBean?.accessToken ?: ""

        val newRequest = chain.request()
            .newBuilder()
            .addHeader("version", API_VERSION)
            .addHeader("client", API_CLIENT)
            .addHeader("source", "${Device.brand},${Device.model}")
            .addHeader("uid", Device.imei)
            .addHeader("token", token)
            .addHeader("persId", persId)
            .addHeader("accessToken", accessToken)
            .addHeader("orgId", orgId)
            .build()

        //通过给定的键url_name,从request中获取headers
        val headers: List<String>? = newRequest.headers(URL_NAME)
        if (!headers.isNullOrEmpty()) {
            //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
            //builder.removeHeader(HttpConfig.HEADER_KEY);
            val oldUrl = newRequest.url() //从request中获取原有的HttpUrl实例oldHttpUrl
            val headerBit = headers[0]
            val newBaseUrl = when (headerBit) { //匹配获得新的BaseUrl
                URL_NAME_DEFAULT -> HttpUrl.parse(API_BASE_URL)
                URL_NAME_EMERGENCY -> HttpUrl.parse(BuildConfig.BASE_URL)
                URL_NAME_EMERGENCY_RAIN -> HttpUrl.parse(API_APP_BASE_URL_RAIN)
                URL_NAME_EMERGENCY_YYJZ -> HttpUrl.parse(API_APP_YYJZ_DSX_URL)
                URL_NAME_EMERGENCY_GLOBEVIEW -> HttpUrl.parse(API_APP_GLOBEVIEW_URL)
                else -> oldUrl
            }

            val oldSegments = oldUrl.pathSegments()
            newBaseUrl?.apply {
                //重建新的HttpUrl，修改需要修改的url部分
                val newUrlBuilder = oldUrl.newBuilder().scheme(scheme()).host(host()).port(port())
                if (BuildConfig.appendApi && URL_NAME_EMERGENCY == headerBit){
                    //下面方法是用于 IP地址带端口号，且有后缀目录的
                    //线上需要打开，本地不需要打开
                    repeat(oldSegments.size) { newUrlBuilder.removePathSegment(0) }
                    newUrlBuilder.addPathSegment("api")
                    oldSegments.forEach { newUrlBuilder.addPathSegment(it) }
                }
                val finalRequest = newRequest.newBuilder().url(newUrlBuilder.build()).build()
                return chain.proceed(finalRequest)
            }
        }
        return chain.proceed(newRequest)
    }

    companion object {
        const val API_VERSION = "v2"
        const val API_CLIENT = "001"
    }
}