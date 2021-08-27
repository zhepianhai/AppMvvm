package com.gw.safty.common.extensions

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import timber.log.Timber
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception

fun Fragment.exShortToast(content: String) {
    Toast.makeText(requireActivity(), content, Toast.LENGTH_SHORT).show()
}
fun Fragment.showToast(content: String) {
    Toast.makeText(requireActivity(), content, Toast.LENGTH_SHORT).show()
}
fun Fragment.exLongToast(content: String) {
    Toast.makeText(requireActivity(), content, Toast.LENGTH_LONG).show()
}

fun Fragment.exShortToast(@StringRes id: Int) {
    Toast.makeText(requireActivity(), id, Toast.LENGTH_SHORT).show()
}

fun Fragment.exLongToast(@StringRes id: Int) {
    Toast.makeText(requireActivity(), id, Toast.LENGTH_LONG).show()
}

@SuppressLint("NewApi")
fun Fragment.openCameraForResult(
    action: String,
    path: String,
    requestCode: Int,
    body: (Pair<String?, Uri?>) -> Unit
) {
    val uri: Uri?
    var filePath: String? = null
    val intent = Intent(action)
    if (intent.resolveActivity(requireActivity().packageManager) == null) {
        exLongToast("此设备无相机")
        return
    }
//    if (isOverQ) {
//        val contentValues = ContentValues()
//        val insert = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        } else {
//            MediaStore.Images.Media.INTERNAL_CONTENT_URI
//        }
//        uri = requireActivity().contentResolver.insert(insert, contentValues)
//    } else {
        File(path).let {
            if (!it.exists()) {
                it.parentFile?.apply {
                    if (!exists()) this.mkdirs()
                }
            }
        }
        val realFile = File(path)
        filePath = realFile.path
        if (Environment.MEDIA_MOUNTED != Environment.getStorageState(realFile)) {
            exShortToast("无法保存图片")
            return
        }
        uri = if (isOverN) {
            FileProvider.getUriForFile(
                requireActivity(),
                "com.gw.safty.supervise_report.provider",
                realFile
            )
        } else {
            Uri.fromFile(realFile)
        }
//    }
    body(Pair(filePath, uri))
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    startActivityForResult(intent, requestCode)
}

fun Fragment.getRealPathQ(uri: Uri): String? {
    var cursor: Cursor? = null
    try {
        cursor = requireActivity().contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.Media.DATA),
            null,
            null,
            null,
            null
        )
        val columnIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DATA) ?: return null
        cursor.moveToFirst()
        val path = cursor.getString(columnIndex)
        val file = File(path)
        return Uri.fromFile(file).path
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    } finally {
        cursor?.close()
    }
}

fun Fragment.copyFileToApp(medias: List<String>, destPath: String): ArrayList<String>{
    val cr = requireActivity().contentResolver
    File(destPath).let {
        if (!it.exists()) it.mkdirs()
    }
    val savedFiles = arrayListOf<String>()
    medias.forEach {
        var ist: InputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            val fileName = it.substring(it.lastIndexOf("/") + 1)
            ist = cr.openInputStream(Uri.parse(it))
            val file = File(destPath, fileName)
            bos = BufferedOutputStream(FileOutputStream(file))
            val l = ist?.copyTo(bos) ?: 0L
            Timber.d("copyFileToApp = $it = $l")
            savedFiles.add(file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            ist?.close()
            bos?.close()
        }
    }
    return savedFiles
}