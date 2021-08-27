package com.gw.safty.common.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import com.airbnb.lottie.LottieDrawable
import com.gw.safty.common.R
import kotlinx.android.synthetic.main.dialog_loading.*

class LoadingDialog(context: Context): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window?.setGravity(Gravity.CENTER)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        val windowManager = ownerActivity?.windowManager
        val width = windowManager?.defaultDisplay?.width
        val height = windowManager?.defaultDisplay?.height
        val lp = window?.attributes
        width?.let { w ->
            lp?.width = (w * 0.8).toInt()
            window?.attributes = lp
        }
        height?.let { h ->
            lp?.height = (h * 0.8).toInt()
        }
        animationView.repeatCount = LottieDrawable.INFINITE
    }

    companion object {
        fun show(context: Context): LoadingDialog{
            val loadingDialog = LoadingDialog(context)
            loadingDialog.show()
            return loadingDialog
        }
    }
}