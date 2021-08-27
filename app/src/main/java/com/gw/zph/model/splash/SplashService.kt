package com.gw.zph.model.splash

import com.gw.safty.common.network.CommonResponse
import com.gw.zph.core.URL_NAME
import com.gw.zph.core.URL_NAME_EMERGENCY
import com.gw.zph.model.splash.bean.AppVersionInfoBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface SplashService {
    // 获取最新app版本信息
    @FormUrlEncoded
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("version/getLastest")
    suspend fun loadVersionInfo(@Field("version") versionCode: String): CommonResponse<AppVersionInfoBean>
}