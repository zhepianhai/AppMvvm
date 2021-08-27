package com.gw.safty.common.network

class CommonResponse<out T>(
    success: Boolean?,
    code: Int?,
    message: String?,
    path: String?,
    throwable: String?,
    accessToken: String?,
    val data: T?): BaseResponse(success, code, message, path, throwable, accessToken){

}