package com.gw.safty.common.extensions

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

//View相关的拓展函数
fun View.exZoomAnim(duration: Long = 450) {
    val view = this
    val set = AnimatorSet()
    set.apply {
        playTogether(
            listOf(
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.12f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.12f)
            )
        )
        setDuration(duration)
        start()
    }
}

fun View.exReZoomAnim(duration: Long = 450) {
    val view = this
    val set = AnimatorSet()
    set.apply {
        playTogether(
            listOf(
                ObjectAnimator.ofFloat(view, "scaleX", 1.12f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 1.12f, 1f)
            )
        )
        setDuration(duration)
        start()
    }
}

fun View.exTranslationXAnim(startX: Float, endX: Float, duration: Long = 450) {
        ObjectAnimator.ofFloat(this, "translationX", startX, endX).apply {
            setDuration(duration)
            start()
        }
}