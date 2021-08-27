package com.gw.zph.core.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.File
import java.io.FileOutputStream

object BitmapExt {
    fun fromString(data: String): Bitmap?{
        try {
            val bitmapArray = Base64.decode(data, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    fun saveTo(btm: Bitmap, dest: String): File?{
        var fos: FileOutputStream? = null
        try {
            val file = File(dest)
            val parent = file.parentFile
            if (parent?.exists() != true){
                parent?.mkdirs()
            }
            if(!file.exists()){
                file.createNewFile()
            }
            fos = file.outputStream()
            btm.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.flush()
            return file
        }catch (e: Exception){
            e.printStackTrace()
        } finally {
            fos?.close()
        }
        return null
    }
}