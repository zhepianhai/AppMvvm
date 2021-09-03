package com.gw.zph.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.ViewCompat.setFitsSystemWindows
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gw.safty.common.base.BaseActivity
import com.gw.safty.common.extensions.dp
import com.gw.safty.common.extensions.jsonToType
import com.gw.safty.common.utils.file.FilePath
import com.gw.zph.R
import com.gw.zph.application.MyApplication
import com.gw.zph.core.*
import com.gw.zph.model.login.bean.UserBean
import com.gw.zph.ui.login.LoginActivity
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.tencent.mmkv.MMKV
import timber.log.Timber

@SuppressLint("Registered")
open class BaseActivityImpl : BaseActivity() {
    protected var requestLogin: Boolean = false
    private var reLoginDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QMUIStatusBarHelper.translucent(this)
        QMUIStatusBarHelper.setStatusBarLightMode(this)
        Timber.e("${this::class.java.simpleName} onCreate")
        if (!StatusHolder.statusInit) {
            val token = getSavedUserToken()
            if (token == null) {
                reLogin()
            } else {
                StatusHolder.setLoginUserBean(token)
                getSavedOrg()?.let { StatusHolder.setOrgBean(it) }
                StatusHolder.setMode(getSavedAppMode())
                StatusHolder.statusInit = true
            }
        }
        Device.setup(this)
        FilePath.setup(this)
        supportFragmentManager.registerFragmentLifecycleCallbacks(FragmentLifecycleCallback(), true)
    }

//    override fun reLogin() {
//        if (InvalidTokenDialog.isShowing) return
//        requestLogin = true
//        val invalidTokenDialog = InvalidTokenDialog()
//        invalidTokenDialog.setConfirmCallback {
//            StatusHolder.loginUserBean?.accessToken = it
//            val newUser = StatusHolder.loginUserBean?.toJsonString()
//            if (TextUtils.isEmpty(newUser)){
//                exShortToast("用户信息解析失败")
//            }
//            val isSave = MMKV.defaultMMKV().encode(KEY_USER_TOKEN, newUser)
//            if (!isSave) {
//                exShortToast("用户信息保存失败")
//            } else {
//                requestLogin = false
//            }
//        }
//        invalidTokenDialog.show(supportFragmentManager, "InvalidTokenDialog")
//    }

    override fun onPause() {
        super.onPause()
        if (reLoginDialog?.isShowing == true) {
            reLoginDialog?.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        if (requestLogin) {
            requestLogin = false
            reLogin()
        }
    }

    override fun exitApp() {
        super.exitApp()
        clearCache(this)
        MyApplication.Instance.finishAllActivities()
    }

    protected fun getSavedUserToken(): UserBean? {
//        val token: String = getSharedPreferences(KEY_CACHE_FILE, Context.MODE_PRIVATE)
//            .getString(KEY_USER_TOKEN, "") ?: ""
        val token: String = MMKV.defaultMMKV().decodeString(KEY_USER_TOKEN, "")
        if (token.isEmpty()) {
            return null
        }
        return token.jsonToType(UserBean::class.java)
    }

    protected fun getSavedAppMode(): Int {
//        return getSharedPreferences(KEY_CACHE_FILE, Context.MODE_PRIVATE).getInt(KEY_LOGIN_MODE, 3)
        val decodeInt = MMKV.defaultMMKV().decodeInt(KEY_LOGIN_MODE, 3)
        return decodeInt
    }

    protected fun getSavedOrg(): UserBean.AllOrgBean? {
//        val orgJson = getSharedPreferences(KEY_CACHE_FILE, Context.MODE_PRIVATE)
//            .getString(KEY_SELECT_ORG, "") ?: ""
        val orgJson = MMKV.defaultMMKV().decodeString(KEY_SELECT_ORG, "") ?: ""
        if (orgJson.isEmpty()) return null
        return orgJson.jsonToType(UserBean.AllOrgBean::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_LOGIN && resultCode == Activity.RESULT_OK) {
            requestLogin = false
            onLoginResult(requestCode, resultCode, data)
        }
    }

    /**
     * 重新登录返回成功之后，可在此方法内进行数据重新请求
     */
    protected open fun onLoginResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    /**
     * 授权失效时
     */
    protected open fun createWarningView(): View {
        val imageView = ImageView(this)
        val layoutParams = FrameLayout.LayoutParams(90.dp, 90.dp)
        layoutParams.gravity = Gravity.CENTER
        imageView.layoutParams = layoutParams
        imageView.setBackgroundResource(R.drawable.ic_token_file)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        val frameLayout = FrameLayout(this)
        frameLayout.addView(imageView, layoutParams)
        return frameLayout
    }

    /**
     * 没有使用kotlin的lazy，不会缓存，频繁调用可能会频繁生成
     */
    fun <VM : ViewModel> createViewModel(clazz: Class<VM>): VM {
        return ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(clazz)
    }

    class FragmentLifecycleCallback: FragmentManager.FragmentLifecycleCallbacks(){
        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            super.onFragmentResumed(fm, f)
            Timber.tag("打开").e("${f.javaClass.canonicalName}")
        }
    }

    companion object {
        const val REQ_LOGIN = 0x1100
        @Suppress("UNUSED_PARAMETER")
        fun clearCache(activity: Activity) {
//            activity.getSharedPreferences(KEY_CACHE_FILE, Context.MODE_PRIVATE).edit(commit = true) {
//                putString(KEY_USER_TOKEN, "")
//                putString(KEY_SELECT_ORG, "")
//                putInt(KEY_LOGIN_MODE, 3)
//            }
            MMKV.defaultMMKV().encode(KEY_USER_TOKEN, "")
            MMKV.defaultMMKV().encode(KEY_SELECT_ORG, "")
            MMKV.defaultMMKV().encode(KEY_LOGIN_MODE, 3)
        }

        @Suppress("ConstantConditionIf")
        fun getLoginActivity(): Class<*> {
            return LoginActivity::class.java
        }
    }

    /**
     * 隐藏键盘
     */
    open fun hintKeyBoard() {
        //拿到InputMethodManager
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //如果window上view获取焦点 && view不为空
        if (imm.isActive) {
            //拿到view的token 不为空
            if (currentFocus?.windowToken != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(
                    currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }



    open fun returnResultActivity() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}