package com.example.mvp.main.mvp

import android.util.Log
import com.example.mvp.presenter.WordPresenter
import javax.inject.Inject

class HomePresenter @Inject constructor() : HomeContract.Presenter, HomeContract.View {

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
}