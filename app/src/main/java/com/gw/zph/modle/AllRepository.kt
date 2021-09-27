package com.gw.zph.modle

import com.gw.safty.common.network.NetResult
import com.gw.zph.base.BaseRepository
import com.gw.zph.base.db.dao.OffLineLatLngInfo
import com.gw.zph.model.login.bean.UserBean
import com.gw.zph.modle.bean.MsgBean
import com.gw.zph.modle.bean.VipBean

class AllRepository  : BaseRepository() {
    private val service by lazy { getServer<AllSerivice>() }

    suspend fun login(user:UserBean): NetResult<UserBean> {
        return safeApiCall { checkResponse(service.login(user)) }
    }
    suspend fun register(user:UserBean): NetResult<UserBean> {
        return safeApiCall { checkResponse(service.register(user)) }
    }

    suspend fun getTrackedByPhoneList(persId:String): NetResult<ArrayList<OffLineLatLngInfo>> {
        return safeApiCall { checkResponse(service.getTrackedByPhoneList(persId)) }
    }

    suspend fun addTrackedList(persId:List<OffLineLatLngInfo>): NetResult<ArrayList<OffLineLatLngInfo>> {
        return safeApiCall { checkResponse(service.addTrackedList(persId)) }
    }

    suspend fun getMsgByPhoneList(persId:String): NetResult<ArrayList<MsgBean>> {
        return safeApiCall { checkResponse(service.getMsgByPhoneList(persId)) }
    }
    suspend fun getMsgAll(): NetResult<ArrayList<MsgBean>> {
        return safeApiCall { checkResponse(service.getMsgAll()) }
    }
    suspend fun addMsg(user:MsgBean): NetResult<MsgBean> {
        return safeApiCall { checkResponse(service.addMsg(user)) }
    }

    suspend fun addOrUpVip(user:VipBean): NetResult<VipBean> {
        return safeApiCall { checkResponse(service.addOrUpVip(user)) }
    }
    suspend fun getVipByPhone(persId:String): NetResult<VipBean> {
        return safeApiCall { checkResponse(service.getVipByPhone(persId)) }
    }
}