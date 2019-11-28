package com.example.mvp.main.mvp

import android.app.Activity
import android.util.Log
import com.example.baselib.mvp.presenter.IPresenter
import com.example.baselib.mvp.view.IView
import com.example.mvp.presenter.WordPresenter
import javax.inject.Inject

class MainPresenter @Inject constructor() : IPresenter, IView {

    @Inject
    lateinit var wordPresenter: WordPresenter

    fun getList(isLoading: Boolean) {
        Log.i("", "" + isLoading)
    }

    fun showList(data: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun testWord(activity: Activity){
        wordPresenter?.testHard()
        wordPresenter?.testSoft()
        wordPresenter?.testComm(activity)
    }
}