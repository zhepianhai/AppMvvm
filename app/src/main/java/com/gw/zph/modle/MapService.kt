package com.gw.zph.modle

import com.gw.safty.common.network.BaseResponse
import com.gw.safty.common.network.CommonResponse
import com.gw.zph.base.db.dao.OffLineLatLngInfo
import com.gw.zph.core.URL_NAME
import com.gw.zph.core.URL_NAME_EMERGENCY
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.*

interface MapService {

    //上传位置信息接口批次
//    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
//    @POST("pers/position/insertList")
//    @JvmSuppressWildcards
//    fun uploadPositionList(
//        @Body bean: List<OffLineLatLngInfo>
//    ): Call<BaseResponse>

    //新增
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("bis/tracked/listInsert")
     fun addTrackedList(
        @Body bean: List<OffLineLatLngInfo>
    ): Call<BaseResponse>
}