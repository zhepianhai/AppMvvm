package com.gw.safty.common.network

open class BaseResponse(
    val success: Boolean?,
    val code: Int?,
    val message: String?,
    val path: String?,
    val throwable: String?,
    val accessToken: String?
)