package com.example.base_fun.ui

import android.content.res.Resources
import com.example.base_lib.ui.LibActivity
import me.jessyan.autosize.AutoSizeCompat

abstract class BaseActivity : LibActivity() {

    override fun getResources(): Resources {
//        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())//如果没有自定义需求用这个方法
//        //AutoSizeCompat.autoConvertDensity((super.getResources(), 667, false);//如果有自定义需求就用这个方法
        return super.getResources()
    }
}