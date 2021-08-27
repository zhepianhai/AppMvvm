package com.gw.zph.model.splash

import com.gw.safty.common.network.NetResult
import com.gw.zph.base.BaseRepository
import com.gw.zph.model.splash.bean.AppVersionInfoBean

class SplashRepository : BaseRepository() {
    private val service by lazy { getServer<SplashService>() }

    suspend fun loadVersionInfo(versionCode: String): NetResult<AppVersionInfoBean> {
        return safeApiCall { checkResponse(service.loadVersionInfo(versionCode)) }
    }
}