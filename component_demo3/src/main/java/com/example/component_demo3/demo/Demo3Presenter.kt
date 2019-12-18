package com.example.component_demo3.demo

import android.util.Log
import com.example.base_fun.mvp.BasePresenter
import com.example.base_fun.mvp.IPresenter
import com.example.base_fun.mvp.IView
import javax.inject.Inject

class Demo3Presenter  @Inject constructor() : BasePresenter(), IView {
    override fun showLoading() {
        Log.i("","")
    }

    override fun hideLoading() {
        Log.i("","")
    }
}