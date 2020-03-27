package com.example.base_lib.listener

import android.view.View

interface ViewClickListener {
    /**
     * 普通点击
     */
    fun onClick(view: View?, o: Any? = null, position: Int = 0)
}