package com.gw.zph.core

import android.content.Context
import com.gw.safty.common.utils.JSONUtil
import com.gw.zph.model.login.bean.UserBean
import com.gw.zph.utils.SharePreferencesUtils




object StatusHolder {
    var statusInit = false

    var appMode: Int = 3
        private set

    var userBean: UserBean? = null
        private set
    fun getCurUserBean(context:Context):UserBean?{
        if(userBean==null){
            var json=SharePreferencesUtils.getTokenData(context)
            userBean= JSONUtil.jsonToObj(json, UserBean::class.java)
        }
        return userBean
    }
    fun setLoginUserBean(user: UserBean,context: Context) {
        userBean = user
        var str=JSONUtil.objToJson(user);
        SharePreferencesUtils.setTokenData(context, str)
    }


    fun setMode(mode: Int) {
        appMode = mode
    }


    fun reset(context: Context) {
        userBean = null
        SharePreferencesUtils.clearAreaByAddCode(context)
    }
}