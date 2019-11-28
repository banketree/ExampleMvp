package com.example.mvp.main.mvp

import android.util.Log
import com.example.baselib.mvp.presenter.IPresenter
import com.example.baselib.mvp.view.IView
import com.example.mvp.main.HomeActivity
import com.example.mvp.presenter.WordPresenter
import javax.inject.Inject

class HomePresenter @Inject constructor() : IPresenter, IView {

    @Inject
    lateinit var wordPresenter: WordPresenter

    lateinit var homeActivity: HomeActivity

    fun init(homeActivity: HomeActivity) {
        this.homeActivity = homeActivity
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}