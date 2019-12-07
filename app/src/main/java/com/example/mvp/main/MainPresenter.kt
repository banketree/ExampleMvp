package com.example.mvp.main

import com.example.base_fun.mvp.IPresenter
import com.example.base_fun.mvp.IView
import javax.inject.Inject

class MainPresenter  @Inject constructor() : IPresenter, IView {
    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}