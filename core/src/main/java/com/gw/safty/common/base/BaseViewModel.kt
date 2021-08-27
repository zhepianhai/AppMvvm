package com.gw.safty.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gw.safty.common.network.NetResult
import kotlinx.coroutines.*
import java.lang.Exception

open class BaseViewModel : ViewModel() {
    private val _uiState = MutableLiveData<UiModel>()
    val uiState: LiveData<UiModel>
        get() = _uiState

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.Main) { block() }
    }

    suspend fun <T> launchOnIO(block: suspend CoroutineScope.() -> T) {
        withContext(Dispatchers.IO) {
            block()
        }
    }

    fun launch(showLoading: Boolean = true, tryBlock: suspend CoroutineScope.() -> Unit): Job {
        return launchOnUI {
            if (showLoading) showLoading()
            tryBlock()
            if (showLoading) hideLoading()
        }
    }

    fun <T: Any> NetResult<T>.success(body: (T) ->Unit): NetResult<T>{
        when(this){
            is NetResult.Success -> body(data)
        }
        return this
    }

    fun <T: Any> NetResult<T>.failure(body: Exception.(NetResult.Error) ->Unit): NetResult<T>{
        when(this){
            is NetResult.Error -> {
                if (code == "9999" || code == "9991" || code == "9995"){
                    showAuthFail()
                }
                exception.body(this)
            }
        }
        return this
    }

    fun <T: Any> NetResult<T>.empty(body: (NetResult.EmptyResponse)->Unit): NetResult<T>{
        when(this){
            is NetResult.EmptyResponse -> body(this)
        }
        return this
    }

    protected fun showLoading() {
        emitUiState(showProgress = true)
    }
    protected fun hideLoading() {
        emitUiState(showProgress = false)
    }
    protected fun showError(str: String) {
        emitUiState(showError = str)
    }

    protected fun showAuthFail(){
        emitUiState(showAuthFail = true)
    }

    protected fun showErrorResString(resId: Int) {
        emitUiState(showErrorResId = resId)
    }
    private fun emitUiState(
        showProgress: Boolean? = null,
        showError: String? = null,
        showErrorResId: Int? = null,
        showAuthFail: Boolean? = null
    ) {
        val uiModel = UiModel(showProgress, showError, showErrorResId, showAuthFail)
        _uiState.value = uiModel
    }
}

data class UiModel(
    val showProgress: Boolean?,
    val showError: String?,
    val showErrorResId: Int?,
    val showAuthFail: Boolean?
)