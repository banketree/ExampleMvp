package com.example.component_demo2.demo

import com.example.base_fun.mvp.IPresenter
import com.example.base_fun.mvp.IView
import javax.inject.Inject

class Demo2Presenter  @Inject constructor() : IPresenter, IView {
    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}