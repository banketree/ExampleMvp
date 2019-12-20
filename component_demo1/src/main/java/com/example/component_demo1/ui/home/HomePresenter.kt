package com.example.component_demo1.ui.home

import com.example.base_fun.mvp.BasePresenter
import javax.inject.Inject

class HomePresenter @Inject constructor() : BasePresenter() {

    private var homeActivity: HomeActivity? = null

    fun init(homeActivity: HomeActivity) {
        this.homeActivity = homeActivity
    }

    fun getWeather() {
    }
}