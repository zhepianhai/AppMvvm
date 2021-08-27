package com.gw.zph.base

import android.accounts.AuthenticatorException
import com.gw.safty.common.network.CommonResponse
import com.gw.safty.common.network.NetResult
import com.gw.safty.common.network.PagerResponse
import com.gw.safty.common.network.PagerWrapper
import com.gw.zph.core.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException

open class BaseRepository {
    protected suspend fun <T : Any> safeApiCall(
        errorMessage: String = "未知错误",
        block: suspend () -> NetResult<T>
    ): NetResult<T> {
        return try {
            block()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            NetResult.Error(IllegalArgumentException("参数错误"), "400") //参数异常
        } catch (e: HttpException) {
            e.printStackTrace()
            NetResult.Error(IOException("$errorMessage: ${e.code()}"), e.code().toString())
        } catch (e: Exception) {
            e.printStackTrace()
            NetResult.Error(IOException(errorMessage, e), "400") //网络错误
        }
    }

    /**
     * 此方法处理返回结果时, 把没有数据当作请求错误处理
     * 若接口本身无数据返回,请调用[nonResponse]方法
     */
    protected suspend fun <T : Any> checkResponse(
        response: CommonResponse<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): NetResult<T> {
        return coroutineScope {
            if (response.code == 9995 || response.code == 9999 || response.code == 9991) {
                NetResult.Error(AuthenticatorException("登录过期"), response.code.toString())
            } else if (response.code == 0 && response.success == true) {
                successBlock?.let { it() }
                response.data?.let {
                    if (it is Collection<*> && it.isEmpty()){
                        NetResult.EmptyResponse(response.code?.toString() ?: "0", response.message ?: "null")
                    } else NetResult.Success(it)
                } ?: let {
                    NetResult.EmptyResponse(response.code?.toString() ?: "0", response.message ?: "null")
                }
            } else{
                errorBlock?.let { it() }
                NetResult.Error(IOException(response.message), response.code?.toString() ?: "400")
            }
        }
    }

    protected suspend fun nonResponse(
        response: CommonResponse<Any>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): NetResult<Any> {
        return coroutineScope {
            if (response.code == 0 || response.success == true) {
                successBlock?.let { it() }
                NetResult.Success(Any())
            } else {
                errorBlock?.let { it() }
                NetResult.Error(IOException(response.message), response.code?.toString() ?: "")
            }
        }
    }

    protected suspend fun <T : Any> pagerResponse(
        response: PagerResponse<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null

    ): NetResult<PagerWrapper<T>> {
        return coroutineScope {
            if (response.code == 9995 || response.code == 9999 || response.code == 9991) {
                NetResult.Error(AuthenticatorException("登录过期"), response.code.toString())
            } else if (response.code == 0 && response.success == true) {
                successBlock?.let { it() }
                //即 data和list 都不为空并且list.size 大于0 成立 才返回成功
                if ((response.data?.list?.size ?: 0) > 0){ //data list 为空 结果将为 (0 > 0)
                    NetResult.Success(response.data!!)
                }else{
                    NetResult.EmptyResponse(response.code?.toString() ?: "0", response.message ?: "null")
                }
            } else{
                errorBlock?.let { it() }
                NetResult.Error(IOException(response.message), response.code?.toString() ?: "400")
            }
        }
    }

    inline fun <reified T> getServer(): T {
        return RetrofitClient.createService(T::class.java)
    }
}