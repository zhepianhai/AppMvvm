package com.gw.zph.model.login

import com.gw.safty.common.network.CommonResponse
import com.gw.zph.core.URL_NAME
import com.gw.zph.core.URL_NAME_EMERGENCY
import com.gw.zph.model.login.bean.CaptureImageBean
import com.gw.zph.model.login.bean.UserBean
import retrofit2.http.*

interface LoginService {
    /**
     * 请求发送短信验证码
     */
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("bis/insp/sendMessage")
    suspend fun requestSmsCode(
        @Query("phone") phone: String,
        @Query("imgCodeId") imgCodeId: String,
        @Query("code") code: String
    ): CommonResponse<Any>

    // 请求更新验证码
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @GET("/bis/insp/getCaptchaImage")
    suspend fun refreshImageCode(): CommonResponse<CaptureImageBean>

    // 获取用户信息接口
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("bis/insp/loginByCode")
    suspend fun loginByCode(
        @Query("phone") phone: String,
        @Query("code") code: String
    ): CommonResponse<UserBean>

    @FormUrlEncoded
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Field("persId")        persId: String,
        @Field("accessToken")   accessToken: String
    ): CommonResponse<Any>
}