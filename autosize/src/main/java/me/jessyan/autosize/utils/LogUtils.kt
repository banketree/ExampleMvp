package me.jessyan.autosize.utils


import android.util.Log

class LogUtils private constructor() {

    init {
        throw IllegalStateException("you can't instantiate me!")
    }

    companion object {
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
}
