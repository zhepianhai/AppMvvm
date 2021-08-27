package com.gw.safty.common.base

import androidx.appcompat.app.AppCompatActivity
import com.gw.safty.common.extensions.*
import com.gw.safty.common.widget.LoadingDialog
import com.qmuiteam.qmui.arch.QMUIActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private var loadingDialog: LoadingDialog? = null

    fun setupBaseViewModel(viewModle: BaseViewModel) {
        viewModle.uiState.observe(this, NonNullObserver { model ->
            if (model?.showProgress == true)
                showLoading()
            else if (model?.showProgress == false)
                hideLoading()
            model.showError?.let {
                exShortToast(it)
            }
            model.showErrorResId?.let {
                exShortToast(it)
            }
            model.showAuthFail?.let {
                reLogin()
            }
        })
    }

    protected open fun reLogin() {

    }

    protected open fun exitApp() {

    }

    protected open fun showLoading(maxTimeMillion: Int) {
        showLoading()
        launch {
            delay(maxTimeMillion.toLong())
            hideLoading()
            exShortToast("超时")
        }
    }

    private fun showLoading() {
        loadingDialog?.let { return }
        loadingDialog = LoadingDialog.show(this)
    }

    protected fun hideLoading() {
        if (loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
        }
        loadingDialog = null
    }

    override fun onPause() {
        super.onPause()
        hideLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoading()
    }
}