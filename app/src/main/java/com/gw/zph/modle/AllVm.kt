package com.gw.zph.modle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gw.safty.common.base.BaseViewModel
import com.gw.safty.common.extensions.nMessage
import com.gw.zph.base.db.dao.OffLineLatLngInfo
import com.gw.zph.model.login.bean.UserBean
import com.gw.zph.modle.bean.MsgBean
import com.gw.zph.modle.bean.VipBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AllVm : BaseViewModel() {
    private val repository: AllRepository = AllRepository()

    //登录
    private val _login = MutableLiveData<UserBean>()
    val loginModel: LiveData<UserBean> = _login
    fun login(userBean:UserBean){
        launch {
            withContext(Dispatchers.IO){
                repository.login(userBean)
            }.success {
                _login.value=it
            }.failure {
                showError(nMessage)
            }
        }
    }
    //注册
    private val _register = MutableLiveData<UserBean>()
    val registerModel: LiveData<UserBean> = _register
    fun register(userBean:UserBean){
        launch {
            withContext(Dispatchers.IO){
                repository.register(userBean)
            }.success {
                _register.value=it
            }.failure {
                showError(nMessage)
            }
        }
    }
    //获取轨迹
    private val _trackList = MutableLiveData<ArrayList<OffLineLatLngInfo>>()
    val trackListModel: LiveData<ArrayList<OffLineLatLngInfo>> = _trackList
    fun getTrackedByPhoneList(id:String){
        launch {
            withContext(Dispatchers.IO){
                repository.getTrackedByPhoneList(id)
            }.success {
                _trackList.value=it
            }.failure {
                showError(nMessage)
            }
        }
    }

    //批量添加轨迹
    private val _addtrackList = MutableLiveData<ArrayList<OffLineLatLngInfo>>()
    val addtrackListModel: LiveData<ArrayList<OffLineLatLngInfo>> = _addtrackList
    fun addTrackedList(list:ArrayList<OffLineLatLngInfo>){
        launch {
            withContext(Dispatchers.IO){
                repository.addTrackedList(list)
            }.success {
                _addtrackList.value=it
            }.failure {
                showError(nMessage)
            }
        }
    }

    //获取留言
    private val _msgList = MutableLiveData<ArrayList<MsgBean>>()
    val msgModel: LiveData<ArrayList<MsgBean>> = _msgList
    fun getMsgByPhoneList(id:String){
        launch {
            withContext(Dispatchers.IO){
                repository.getMsgByPhoneList(id)
            }.success {
                _msgList.value=it
            }.failure {
                showError(nMessage)
            }
        }
    }
    private val _msgAllList = MutableLiveData<ArrayList<MsgBean>>()
    val msgAllModel: LiveData<ArrayList<MsgBean>> = _msgAllList
    fun getMsgAll(){
        launch {
            withContext(Dispatchers.IO){
                repository.getMsgAll()
            }.success {
                _msgAllList.value=it
            }.failure {
                showError(nMessage)
            }
        }
    }
    //新增留言
    private val _addMsg = MutableLiveData<MsgBean>()
    val addMsgModel: LiveData<MsgBean> = _addMsg
    fun addMsg(msg:MsgBean){
        launch {
            withContext(Dispatchers.IO){
                repository.addMsg(msg)
            }.success {
                _addMsg.value=it
            }.failure {
                showError(nMessage)
            }
        }
    }

    //VIP
    private val _addVip = MutableLiveData<VipBean>()
    val addVipModel: LiveData<VipBean> = _addVip
    fun addVip(msg:VipBean){
        launch {
            withContext(Dispatchers.IO){
                repository.addOrUpVip(msg)
            }.success {
                _addVip.value=it
            }.failure {
                showError(nMessage)
            }
        }
    }
    private val _getvip = MutableLiveData<VipBean>()
    val getvipModel: LiveData<VipBean> = _getvip
    fun getVipByPhone(id:String){
        launch {
            withContext(Dispatchers.IO){
                repository.getVipByPhone(id)
            }.success {
                _getvip.value=it
            }.failure {
                showError(nMessage)
            }
        }
    }
}