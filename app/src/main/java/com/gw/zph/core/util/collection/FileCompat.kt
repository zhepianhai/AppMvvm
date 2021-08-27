package com.gw.zph.core.util.collection

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.gw.safty.common.extensions.isOverN
import com.gw.zph.BuildConfig
import java.io.File

object FileCompat {
    const val fileProviderAuth: String = BuildConfig.APPLICATION_ID + ".provider"

    fun getUriFromFile(context: Context, file: File): Uri?{
        try {
            return if (isOverN){
                FileProvider.getUriForFile(context, fileProviderAuth, file)
            } else{
                Uri.fromFile(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null;
    }
}