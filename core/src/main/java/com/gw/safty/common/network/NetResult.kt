package com.gw.safty.common.network

sealed class NetResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : NetResult<T>()
    data class Error(val exception: Exception, val code: String) : NetResult<Nothing>()
    data class EmptyResponse(val code: String, val message: String): NetResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception] ----> code = $code"
            is EmptyResponse -> "EmptyResponse"
        }
    }
}