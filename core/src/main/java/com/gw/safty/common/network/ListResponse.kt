package com.gw.safty.common.network

class ListResponse<out T> (
    success: Boolean?,
    code: Int?,
    message: String?,
    path: String?,
    throwable: String?,
    accessToken: String?,
    val data: List<T>?): BaseResponse(success, code, message, path, throwable, accessToken) {
}