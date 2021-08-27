package com.gw.safty.common.utils

import java.io.ByteArrayOutputStream
import java.io.InputStream

object ModelUtils {
    fun getString(input: InputStream): String {
        return input.bufferedReader().use { it.readText() }
    }
}