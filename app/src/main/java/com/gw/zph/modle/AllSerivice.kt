package com.gw.zph.modle

import com.gw.safty.common.network.CommonResponse
import com.gw.safty.common.network.PagerResponse
import com.gw.zph.base.db.dao.OffLineLatLngInfo
import com.gw.zph.core.URL_NAME
import com.gw.zph.core.URL_NAME_EMERGENCY
import com.gw.zph.model.login.bean.UserBean
import com.gw.zph.modle.bean.MsgBean
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AllSerivice {
    /**
     * User
     * */
    //登录
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("bis/user/login")
    suspend fun login(@Body params: UserBean): CommonResponse<UserBean>
    //注册
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("bis/user/register")
    suspend fun register(@Body params: UserBean): CommonResponse<UserBean>
    /**
     * 轨迹
     * */
    //根据手机号查询
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("bis/tracked/getBy/{phone}")
    suspend fun getTrackedByPhoneList(
        @Path("phone") id: String): CommonResponse<ArrayList<OffLineLatLngInfo>>
    //新增
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("bis/tracked/listInsert")
    suspend fun addTrackedList(
        @Body bean: List<OffLineLatLngInfo>
    ): CommonResponse<ArrayList<OffLineLatLngInfo>>

    /**
     * 留言板
     * */
    //根据手机号查询
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("bis/msg/getBy/{phone}")
    suspend fun getMsgByPhoneList(
        @Path("phone") id: String): CommonResponse<ArrayList<MsgBean>>

    //查询全部
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("bis/msg/findAll")
    suspend fun getMsgAll(
        ): CommonResponse<ArrayList<MsgBean>>

    //新增
    @Headers("$URL_NAME:$URL_NAME_EMERGENCY")
    @POST("bis/msg/insert")
    suspend fun addMsg(
        @Body bean: MsgBean
    ): CommonResponse<MsgBean>
}