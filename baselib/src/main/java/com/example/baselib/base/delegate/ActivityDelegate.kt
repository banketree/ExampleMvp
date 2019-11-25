package com.example.baselib.base.delegate


import android.app.Activity
import android.os.Bundle

/**
 * ================================================
 * [Activity] 代理类,用于框架内部在每个
 * [Activity] 的对应生命周期中插入需要的逻辑
 * ================================================
 */
interface ActivityDelegate {

    fun onCreate(savedInstanceState: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onSaveInstanceState(outState: Bundle)

    fun onDestroy()

    companion object {
        val LAYOUT_LINEARLAYOUT = "LinearLayout"
        val LAYOUT_FRAMELAYOUT = "FrameLayout"
        val LAYOUT_RELATIVELAYOUT = "RelativeLayout"
        val ACTIVITY_DELEGATE = "ACTIVITY_DELEGATE"
    }
}