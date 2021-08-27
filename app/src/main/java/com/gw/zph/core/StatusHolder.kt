package com.gw.zph.core

import com.gw.zph.model.login.bean.UserBean

object StatusHolder {
    var statusInit = false

    var appMode: Int = 3
        private set

    var loginUserBean: UserBean? = null
        private set

    var orgBean: UserBean.AllOrgBean? = null
        private set


    fun setLoginUserBean(user: UserBean) {
        loginUserBean = user
    }

    fun setOrgBean(org: UserBean.AllOrgBean) {
        orgBean = org
    }

    fun setMode(mode: Int) {
        appMode = mode
    }



    fun reset(){
        statusInit = false
        loginUserBean = null
        orgBean = null
    }
}