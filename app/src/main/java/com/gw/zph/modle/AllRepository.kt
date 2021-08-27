package com.gw.zph.modle

import com.gw.safty.common.network.NetResult
import com.gw.zph.base.BaseRepository
import com.gw.zph.model.login.bean.UserBean

class AllRepository  : BaseRepository() {
    private val service by lazy { getServer<AllSerivice>() }

//    suspend fun getPersonInfo(userId: String): NetResult<UserBean> {
//        return safeApiCall { checkResponse(service.getPersonInfo(userId)) }
//    }
}