package com.gw.zph.model.login

import android.text.TextUtils
import com.gw.safty.common.network.NetResult
import com.gw.zph.base.BaseRepository
import com.gw.zph.model.login.bean.CaptureImageBean
import com.gw.zph.model.login.bean.UserBean
import java.io.IOException

class LoginRepository : BaseRepository() {
    private val service by lazy { getServer<LoginService>() }

    suspend fun requestSmsCode(phone: String, imgCodeId: String, code: String): NetResult<Any> {
        return safeApiCall { nonResponse(service.requestSmsCode(phone, imgCodeId, code)) }
    }


    suspend fun refreshImageCode(): NetResult<CaptureImageBean> {
        return safeApiCall { checkResponse(service.refreshImageCode()) }
    }

    suspend fun loginByCode(phone: String, code: String): NetResult<UserBean> {
        return safeApiCall {
            val response = service.loginByCode(phone, code)
            val checkResponse = checkResponse(response)
            if (checkResponse is NetResult.Success) {
                checkResponse.data.accessToken = response.accessToken
            }
            checkResponse
        }
    }

    suspend fun refreshToken(persId: String, accessToken: String): NetResult<String>{
        return safeApiCall {
            val response = service.refreshToken(persId, accessToken)
            val newToken = response.accessToken
            if ((response.code == 0 || response.success == true) && newToken != null){
                NetResult.Success(newToken)
            } else {
                val msg = if (TextUtils.isEmpty(response.message)) "系统错误" else response.message
                NetResult.Error(IOException(msg), "${response.code}")
            }
        }
    }
}