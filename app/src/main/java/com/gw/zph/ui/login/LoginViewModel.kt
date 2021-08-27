package com.gw.zph.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gw.safty.common.base.BaseViewModel
import com.gw.safty.common.extensions.nMessage
import com.gw.zph.model.login.LoginRepository
import com.gw.zph.model.login.bean.CaptureImageBean
import com.gw.zph.model.login.bean.ReqUserBean
import com.gw.zph.model.login.bean.UserBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel: BaseViewModel() {
    private val repository: LoginRepository = LoginRepository()

    fun requestSmsCode(params: ReqUserBean){
        launch {
            withContext(Dispatchers.IO){
                repository.requestSmsCode(params.account, params.imgCodeId, params.code)
            }.success {
                showError("短信验证码发送成功")
            }.failure {
                showError(nMessage)
            }
        }
    }

    private val _userModel = MutableLiveData<UserBean>()
    val userModel: LiveData<UserBean> = _userModel
    fun loginBySms(params: ReqUserBean) {
        launch {
            withContext(Dispatchers.IO){
                repository.loginByCode(params.account, params.code)
            }.success {
                _userModel.value = it
            }.failure {
                showError(nMessage)
            }
        }
    }

    private val _imageCode = MutableLiveData<CaptureImageBean>()
    val imageCode: LiveData<CaptureImageBean> = _imageCode
    fun refreshImageCode(){
        launch {
            withContext(Dispatchers.IO){
                repository.refreshImageCode()
            }.success {
                _imageCode.value = it
            }.failure {
                showError(nMessage)
            }
        }
    }


    private val _newToken = MutableLiveData<String>()
    val newToken: LiveData<String> = _newToken

    fun refreshToken(persId: String, accessToken: String){
        launch {
            withContext(Dispatchers.IO){
                repository.refreshToken(persId, accessToken)
            }.success {
                _newToken.value = it
            }.failure {
                showError(nMessage)
            }
        }
    }
}