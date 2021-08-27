package com.gw.zph.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gw.safty.common.base.BaseViewModel
import com.gw.safty.common.extensions.nMessage
import com.gw.zph.model.splash.bean.AppVersionInfoBean
import com.gw.zph.model.splash.SplashRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SplashActViewModel: BaseViewModel() {
    private val repository: SplashRepository by lazy { SplashRepository() }

    private val _versionInfo = MutableLiveData<AppVersionInfoBean?>()
    val versionInfo: LiveData<AppVersionInfoBean?> = _versionInfo

    fun loadVersionInfo(versionCode: String){
        launch {
            withContext(Dispatchers.IO){
                repository.loadVersionInfo(versionCode)
            }.success {
                _versionInfo.value = it
            }.empty {
                _versionInfo.value = null
            }.failure {
                showError(nMessage)
                _versionInfo.value = null
            }
        }
    }
}