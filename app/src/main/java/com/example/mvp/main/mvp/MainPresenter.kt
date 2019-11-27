package com.example.mvp.main.mvp

import android.util.Log
import javax.inject.Inject

class MainPresenter @Inject constructor() : MainContract.Presenter, MainContract.View {

    override fun getList(isLoading: Boolean) {
        Log.i("", "" + isLoading)
    }

    override fun showVideoList(data: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}