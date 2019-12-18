package com.example.component_demo1.ui.demo

import com.example.base_fun.mvp.BasePresenter
import com.example.base_fun.mvp.IView
import javax.inject.Inject

class Demo1Presenter  @Inject constructor() : BasePresenter(), IView {
    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}