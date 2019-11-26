package com.example.baselib.base

import androidx.annotation.NonNull
import com.example.baselib.di.component.AppComponent

/**
 * ================================================
 * 框架要求框架中的每个应用或组件都需要实现此类, 以满足规范
 * ================================================
 */
interface App {

    @NonNull
    fun getAppComponent(): AppComponent

}