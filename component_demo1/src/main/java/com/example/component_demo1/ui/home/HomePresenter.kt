package com.example.component_demo1.ui.home

import com.example.base_fun.mvp.IPresenter
import com.example.component_demo1.http.HttpCallback
import com.example.component_demo1.http.WeatherApi
import retrofit2.Response
import javax.inject.Inject

class HomePresenter @Inject constructor() : IPresenter {

    private var homeActivity: HomeActivity? = null

    fun init(homeActivity: HomeActivity) {
        this.homeActivity = homeActivity
    }

    fun getWeather() {
        WeatherApi().getCityCodeByIp(object : HttpCallback<Any>() {
            override fun getBean(response: Response<*>): Any? {
                homeActivity?.initData(response.body().toString())
                return super.getBean(response)
            }
        })
    }
}