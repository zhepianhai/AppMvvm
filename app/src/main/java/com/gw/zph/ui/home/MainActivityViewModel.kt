package com.gw.slbdc.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gw.safty.common.base.BaseViewModel
import com.gw.zph.model.login.bean.UserBean

class MainActivityViewModel: BaseViewModel() {
    private val _changeOrgBean = MutableLiveData<UserBean.AllOrgBean>()
    val changeOrgBean: LiveData<UserBean.AllOrgBean> = _changeOrgBean

    fun setOrgChange(org: UserBean.AllOrgBean){
        _changeOrgBean.value = org
    }
}