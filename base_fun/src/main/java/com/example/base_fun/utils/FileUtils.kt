@file:Suppress("DEPRECATION")

package com.example.base_fun.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.thinkcore.kandroid.saveBitmap
import com.thinkcore.kandroid.toTimeForYYMMDDHHMMSS
import com.thinkcore.storage.kandroid.getExternalImageDir
import com.thinkcore.storage.kandroid.getInterImageDir
import com.thinkcore.storage.kandroid.hasExternalStoragePermission
import java.io.File
import java.lang.Exception

object FileUtils {

    fun saveBitmapAndAlbum(context: Context, bitmap: Bitmap): Boolean {
        val filePath = if (context.hasExternalStoragePermission())
            context.getExternalImageDir() + "/" + System.currentTimeMillis().toTimeForYYMMDDHHMMSS() + ".jpg"
        else context.getInterImageDir() + "/" + System.currentTimeMillis().toTimeForYYMMDDHHMMSS() + ".jpg"
        return saveBitmapAndAlbum(
            context,
            bitmap,
            filePath
        )
    }

    fun saveBitmapAndAlbum(context: Context, bitmap: Bitmap, filePath: String): Boolean {
        if (!bitmap.saveBitmap(filePath)) {
            return false
        }

        val file = File(filePath)
        try {
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath, file.name, null
            )
        } catch (e: Exception) {
        }

        // 最后通知图库更新
        context.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse(file.absolutePath)
            )
        )
        return true
    }
}