package com.gw.zph.base

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.gw.safty.common.base.BaseFragment
import com.gw.safty.common.extensions.dp
import com.gw.safty.common.extensions.showSnackBar
import com.gw.safty.common.extensions.toJsonString
import com.gw.zph.R
import com.gw.zph.application.MyApplication
import com.gw.zph.base.BaseActivityImpl.Companion.REQ_LOGIN
import com.gw.zph.core.KEY_USER_TOKEN
import com.gw.zph.core.StatusHolder
import com.gw.zph.ui.splash.SplashActivity
import com.tencent.mmkv.MMKV

abstract class BaseFragmentImpl<DB: ViewDataBinding>: BaseFragment<DB>() {
    protected var requestLogin: Boolean = false
    private var reLoginDialog: Dialog? = null

    override fun onResume() {
        super.onResume()
        if (requestLogin) {
            requestLogin = false
            reLogin()
        }
    }
    override fun onPause() {
        super.onPause()
        if (reLoginDialog?.isShowing == true) {
            reLoginDialog?.dismiss()
        }
    }
    override fun reLogin() {
        BaseActivityImpl.clearCache(requireActivity())
    }

    open fun logout(){
        BaseActivityImpl.clearCache(requireActivity())
        StatusHolder.reset(context!!)
        val intent = Intent(requireActivity(), SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityForResult(intent, REQ_LOGIN)
    }

//    override fun showAuthFail() {
//        if (InvalidTokenDialog.isShowing) return
//        requestLogin = true
//        val invalidTokenDialog = InvalidTokenDialog()
//        invalidTokenDialog.setConfirmCallback {
//            StatusHolder.loginUserBean?.accessToken = it
//            val newUser = StatusHolder.loginUserBean?.toJsonString()
//            if (TextUtils.isEmpty(newUser)){
//                showSnackBar(view, "用户信息解析失败")
//            }
//            val isSave = MMKV.defaultMMKV().encode(KEY_USER_TOKEN, newUser)
//            if (!isSave) {
//                showSnackBar(view, "用户信息保存失败")
//            } else {
//                requestLogin = false
//            }
//        }
//        invalidTokenDialog.show(childFragmentManager, "InvalidTokenDialog")
//    }

    /**
     * 授权失效时
     */
    protected open fun createWarningView(): View {
        val imageView = ImageView(requireActivity())
        val layoutParams = FrameLayout.LayoutParams(90.dp, 90.dp)
        layoutParams.gravity = Gravity.CENTER
        imageView.layoutParams = layoutParams
        imageView.setBackgroundResource(R.drawable.ic_token_file)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        val frameLayout = FrameLayout(requireActivity())
        frameLayout.addView(imageView, layoutParams)
        return frameLayout
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_LOGIN && resultCode == Activity.RESULT_OK){
            requestLogin = false
            onLoginResult(requestCode,resultCode, data)
        }
    }

    override fun exitApp() {
        BaseActivityImpl.clearCache(requireActivity())
        MyApplication.Instance.finishAllActivities()
    }
    /**
     * 重新登录返回成功之后，可在此方法内进行数据重新请求
     */
    protected open fun onLoginResult(requestCode: Int, resultCode: Int, data: Intent?){

    }
}