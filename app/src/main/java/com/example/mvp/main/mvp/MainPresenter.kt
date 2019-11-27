package com.example.mvp.main.mvp

import android.util.Log
import com.example.mvp.presenter.WordPresenter
import javax.inject.Inject

class MainPresenter @Inject constructor() : MainContract.Presenter, MainContract.View {

    @Inject
    lateinit var wordPresenter: WordPresenter

    override fun getList(isLoading: Boolean) {
        Log.i("", "" + isLoading)
    }

    override fun showList(data: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun testWord(){
        wordPresenter?.testHard()
        wordPresenter?.testSoft()
        wordPresenter?.testComm()
    }
}