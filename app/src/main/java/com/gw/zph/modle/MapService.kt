package com.gw.zph.modle

import com.gw.safty.common.network.BaseResponse
import com.gw.zph.base.db.dao.OffLineLatLngInfo
import com.gw.zph.core.URL_NAME
import com.gw.zph.core.URL_NAME_EMERGENCY
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MapService {

    //上传位置信息接口批次
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("pers/position/insertList")
    @JvmSuppressWildcards
    fun uploadPositionList(
        @Body bean: List<OffLineLatLngInfo>
    ): Call<BaseResponse>

}