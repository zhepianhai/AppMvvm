package com.gw.safty.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.gw.safty.common.widget.LoadingDialog
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gw.safty.common.extensions.*
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * 抽象Fragment基类，通用Fragment基类。
 * 包含了 【处理Navigation返回时重新创建视图的解决方法】，【Databinding支持】
 *       【协程支持】，【各种网络状态的的监听】
 * 若不使用DataBinding，泛型传 [ViewDataBinding] 即可
 * @author rjc
 * 创建: 2020/4/20 11:51
 */
abstract class BaseFragment<Binding : ViewDataBinding> : Fragment(), CoroutineScope by MainScope() {
    /**
     * 标记Fragment的View是否已经创建过
     * 因为[Navigation]框架的机制，由Fragment A 导航到Fragment B，再由Fragment B返回A时，
     * A会重新走[onCreateView]、[onViewCreated]等函数，导致重新创建View，致使如[EditText]等
     * 已经输入的数据丢失，所以使用此字段标志
     */
    var viewCreateOnce: Boolean = false

    /**
     * 标记[onViewCreated] 是否已经执行过，作用同[viewCreateOnce]
     */
    var viewCreatedRunOnce: Boolean = false

    /**
     * 是否使用[Navigation] 默认的特性(参考[viewCreateOnce]的说明)
     * 如果在子类覆盖了此字段，则表明使用[Navigation] 默认的特性，即
     * 每次返回当前Fragment都可能重建
     */
    open var useNavigationDefaultFeature: Boolean = false

    /**
     * DataBinding绑定得到的绑定对象
     * 若修改/重写/覆盖了[layoutBinding]，则此字段必定为空
     */
    var binder: Binding? = null

    /**
     * 是否使用DataBinding，若不使用，则使用传统方式加载布局，但是[binder]必定为空
     */
    open var layoutBinding: Boolean = true

    /**
     * 布局id，直接赋值即可
     */
    abstract val layoutId: Int

    /**
     * 加载动画对话框
     */
//    private var loadingDialog: LoadingDialog? = null
    private var tipDialog: QMUITipDialog?=null
    private var mRootView: View? = null

    /**
     * 不要在此方法下进行控件或者数据初始化
     * 原因参考 [viewCreateOnce] 的说明
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (layoutBinding) {
            binder?.let { if (!useNavigationDefaultFeature) return it.root } //如果(默认)不是Navigation的特性，并且binder已经被创建，直接返回
            binder = DataBindingUtil.inflate<Binding>(layoutInflater, layoutId, container, false)
            viewCreateOnce = true
            binder?.lifecycleOwner = this
            binder?.let {
                onCreateViewRunOnce(it.root, container, savedInstanceState)
                return it.root
            }
        } else {
            mRootView?.let { if (!useNavigationDefaultFeature) return it }//如果(默认)不是Navigation的特性，并且binder已经被创建，直接返回
            mRootView = layoutInflater.inflate(layoutId, container, false)
            viewCreateOnce = true
            mRootView?.let {
                onCreateViewRunOnce(it, container, savedInstanceState)
                return it
            }
        }
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!viewCreatedRunOnce) {
            viewCreatedRunOnce = true
            onViewCreatedRunOnce(view, savedInstanceState)
        }
    }

    /**
     * ⚠️ 这个方法是在[onCreateView]返回view之前执行的，尽量不要重写此方法，
     *    因为onCreateView还没有返回view，这时候拿view是为空的，容易出错
     *    尽量使用/重写 [onViewCreatedRunOnce]
     * 此方法在 [Fragment] 首次创建时在 [onCreateView]中被调用
     * 但仅会执行一次,但若是修改/重写/覆盖了[useNavigationDefaultFeature]字段，则此方法于[onCreateView]相同
     * ps:不需要再创建[View] !!!!!!!!!!!!!!!!!!!!!!!
     * 为了解决[Navigation]特性导致的问题而做
     * 详细原因参照[viewCreateOnce]的注释说明
     * @param view 已经创建好但view
     */
    open fun onCreateViewRunOnce(
        createdView: View,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {

    }

    /**
     * 此方法等同于[onViewCreated]
     * 但仅会执行一次
     * 为了解决[Navigation]特性导致的问题而做
     * 详细原因参照[viewCreateOnce]的注释说明
     */
    open fun onViewCreatedRunOnce(view: View, savedInstanceState: Bundle?) {

    }

    /**
     * 调用此方法监听网络加载状态
     * ⚠️如果超过⚠️20⚠️秒网络请求没有成功，将自动隐藏对话框，如需自定义时间可自行修改
     */
    fun setupBaseViewModel(viewModel: BaseViewModel) {
        viewModel.uiState.observe(this, NonNullObserver { model ->
            if (model?.showProgress == true) {
                showLoading(900000)
            } else if (model?.showProgress == false) {
                hideLoading()
            }
            model.showError?.let {
                showError(message = it)
            }
            model.showErrorResId?.let {
                showError(messageId = it)
            }
            model.showAuthFail?.let {
                showAuthFail()
            }
        })
    }

    protected open fun showLoading(maxTimeMillion: Int){
        showLoading()
        launch {
            delay(maxTimeMillion.toLong())
            if (tipDialog?.isShowing == true){
                exShortToast("超时")
                hideLoading()
            }
        }
    }

    protected open fun showLoading() {
        tipDialog?.let { return }
        activity?.let {  tipDialog= QMUITipDialog.Builder(
            it
        )
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("正在加载")
            .create() }
    }

    protected open fun hideLoading() {
        if (tipDialog?.isShowing == true){
            tipDialog?.dismiss()
            tipDialog = null
        }
    }

    protected open fun showError(message: String? = null, messageId: Int? = null){
        message?.let { exShortToast(it) }
        messageId?.let { exShortToast(it) }
    }

    protected open fun showAuthFail(){
        AlertDialog.Builder(requireActivity())
            .setCancelable(false)
            .setTitle("提示")
            .setMessage("你当前身份已过期，请重新登录!")
            .setPositiveButton("重新登录") { d, _ ->
                d.dismiss()
                reLogin()
            }.setNegativeButton("退出") { d, _ ->
                d.dismiss()
                exitApp()
            }.show()
    }
    protected open fun reLogin() {

    }

    /**
     * 退出app的同时会清理掉用户的登录
     */
    protected open fun exitApp() {
//        activity?.apply {
//            getSharedPreferences(STATUS_PREF_KEY, Context.MODE_PRIVATE).edit(true) {
//                putString(USER_MODEL_KEY, "")
//                com.province.platform.network.CacheInterceptor.setToken("")
//            }
//            intentTo(ACTION_TO_EXIT)
//        }
    }

//    private fun intentTo(action: String) {
//        val intent = Intent(action)
//        requireActivity().startActivity(intent)
//        requireActivity().finish()
//    }

    override fun onDestroyView() {
        hideLoading()
        super.onDestroyView()
    }
    /**
     * 没有使用kotlin的lazy，不会缓存，频繁调用可能会频繁生成
     */
    fun <VM: ViewModel> createViewModel(clazz: Class<VM>): VM {
        return ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(clazz)
    }

    fun <VM: ViewModel> createActivityViewModel(clazz: Class<VM>): VM {
        return ViewModelProvider(requireActivity().viewModelStore, requireActivity().defaultViewModelProviderFactory).get(clazz)
    }
}