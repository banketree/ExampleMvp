package me.jessyan.autosize.utils


import android.util.Log

object LogUtils {
    private val TAG = "AndroidAutoSize"
    var isDebug: Boolean = false

    fun d(message: String) {
        if (isDebug) {
            Log.d(TAG, message)
        }
    }

    fun w(message: String) {
        if (isDebug) {
            Log.w(TAG, message)
        }
    }

    fun e(message: String) {
        if (isDebug) {
            Log.e(TAG, message)
        }
    }
}
