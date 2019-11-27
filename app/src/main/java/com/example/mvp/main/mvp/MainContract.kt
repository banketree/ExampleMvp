package com.example.mvp.main.mvp

import com.example.baselib.mvp.presenter.IPresenter
import com.example.baselib.mvp.view.IView

interface MainContract {

    interface View : IView {
        fun showList(data: Any)
    }

    interface Presenter : IPresenter {
        fun getList(isLoading: Boolean)
    }
}