package com.gw.slbdc.ui.main.mine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gw.safty.common.base.BaseViewModel
import com.gw.zph.modle.AllRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeFragmentViewModel: BaseViewModel() {
    private val repository = AllRepository()

//    private val _personInfo = MutableLiveData<PersonInfo>()
//    val personInfo: LiveData<PersonInfo> = _personInfo
//
//    fun getPersonInfo(userId: String){
//        launch {
//            withContext(Dispatchers.IO){
//                repository.getPersonInfo(userId)
//            }.success {
//                _personInfo.value = it
//            }.failure {
//                showError(nMessage)
//            }
//        }
//    }
}