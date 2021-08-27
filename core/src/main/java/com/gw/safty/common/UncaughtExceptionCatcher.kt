package com.gw.safty.common

class UncaughtExceptionCatcher: Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        e.printStackTrace()
    }
}