package me.jessyan.autosize


import android.content.Context
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import me.jessyan.autosize.utils.AutoSizeUtils

/**
 * 通过声明 [ContentProvider] 自动完成初始化
 */
class InitProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        var application: Context? = context!!.applicationContext
        if (application == null) {
            application = AutoSizeUtils.Companion.applicationByReflect
        }
        AutoSizeConfig.instance?.let {
            it.setLog(true)
            it.init(application as Application)
            it.setUseDeviceSize(false)
        }

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}
