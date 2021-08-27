package com.gw.safty.common.extensions

import android.text.TextUtils
import kotlin.contracts.contract

fun String.asDouble(defaultValue: Double = 0.0): Double {
    if (this.isEmpty()) return defaultValue
    return try {
        this.toDouble()
    } catch (e: Exception) {
        defaultValue
    }
}