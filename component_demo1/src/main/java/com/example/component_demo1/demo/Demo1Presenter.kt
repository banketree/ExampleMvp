package com.example.component_demo1.demo

import com.example.base_fun.mvp.IPresenter
import com.example.base_fun.mvp.IView
import javax.inject.Inject

class Demo1Presenter  @Inject constructor() : IPresenter, IView {
    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}