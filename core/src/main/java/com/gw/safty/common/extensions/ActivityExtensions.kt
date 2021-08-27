package com.gw.safty.common.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * 跳转[Activity]的方法
 * @param cls 需要打开的[Activity]
 * @param bundle 携带的数据
 */
fun Activity.exOpenActivity(cls: Class<out Activity>, bundle: Bundle? = null, finishCurrent: Boolean = false){
    val intent =  Intent(this, cls)
    bundle?.let { intent.putExtra("bundle", bundle) }
    startActivity(intent)
    if (finishCurrent) {
        finish()
    }
}

/**
 * 跳转[Activity]的方法
 * @param cls 需要打开的[Activity]
 * @param bundle 携带的数据
 */
inline fun<reified T: Activity> Activity.exOpenActivityForResult(resultCode: Int, bundle: Bundle?){
    val intent =  Intent(this, T::class.java)
    bundle?.let { intent.putExtra(ACTIVITY_BUNDLE_NAME, bundle) }
    startActivityForResult(intent, resultCode)
}


/**
 * 跳转[Activity]的方法
 * @param cls 需要打开的[Activity]
 * @param bundle 携带的数据
 */
inline fun<reified T: Activity> Fragment.exOpenActivityForResult(resultCode: Int, bundle: Bundle?){
    val intent =  Intent(requireActivity(), T::class.java)
    bundle?.let { intent.putExtra(ACTIVITY_BUNDLE_NAME, bundle) }
    this.startActivityForResult(intent, resultCode)
}

fun Activity.exHideInput(): Boolean{
    currentFocus?.windowToken?.let {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            if (isActive) {
                hideSoftInputFromWindow(it, InputMethodManager.HIDE_NOT_ALWAYS)
                return true
            }
        }
    }
    return false
}

fun Context.exShortToast(content: String){
    Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
}

fun Context.exLongToast(content: String){
    Toast.makeText(this, content, Toast.LENGTH_LONG).show()
}

fun Context.exShortToast(@StringRes id: Int){
    Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
}

fun Context.exLongToast(@StringRes id: Int){
    Toast.makeText(this, id, Toast.LENGTH_LONG).show()
}