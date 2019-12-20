package com.example.component_demo1.ui.mvvm

import androidx.activity.ComponentActivity
import androidx.lifecycle.MutableLiveData
import com.example.base_fun.http.HttpCallback
import com.example.base_fun.http.HttpViewModel
import com.example.component_demo1.http.WeatherApi
import com.google.gson.Gson
import retrofit2.Response

class WeatherViewModel : HttpViewModel() {

    companion object {
        const val LOGIN_STATE_SUCCESS = 0
        const val LOGIN_STATE_FAILURE = 1
    }

    // 登录状态
    val loginState: MutableLiveData<Int> = MutableLiveData()
    val dataState: MutableLiveData<String> = MutableLiveData()

    private var repository: WeatherApi? = null
    fun getData(componentActivity: ComponentActivity) {
        if (repository != null) return
        repository = WeatherApi()
        val httpCallback = object : HttpCallback<RespWeather>(componentActivity) { //传参componentActivity 控制是否显示对话框
            override fun onSucess(data: RespWeather) {
                super.onSucess(data)
                loginState.postValue(LOGIN_STATE_SUCCESS)
                dataState.postValue(data.toString())
            }

            override fun onFaile(ex: Exception) {
                super.onFaile(ex)
                loginState.postValue(LOGIN_STATE_FAILURE)
            }

            override fun onFinish() {
                super.onFinish()
                loginState.postValue(3)
                repository = null
            }

            override fun getBean(response: Response<*>): RespWeather? {
                val result = response.body() as String
                return Gson().fromJson(result, RespWeather::class.java)
            }
        }
        launchOnIO {
            httpCallback.execute { repository!!.getCityCodeByIpByString() }
        }
    }

    fun getData2(componentActivity: ComponentActivity) {
        if (repository != null) return
        repository = WeatherApi()
        val httpCallback = object : HttpCallback<RespWeather>(componentActivity) { //传参componentActivity 控制是否显示对话框
            override fun onSucess(data: RespWeather) {
                super.onSucess(data)
                loginState.postValue(LOGIN_STATE_SUCCESS)
                dataState.postValue(data.toString())
            }

            override fun onFaile(ex: Exception) {
                super.onFaile(ex)
                loginState.postValue(LOGIN_STATE_FAILURE)
            }

            override fun onFinish() {
                super.onFinish()
                loginState.postValue(3)
                repository = null
            }
        }
        launchOnIO {
            httpCallback.execute { repository!!.getCityCodeByIpByGson() }
        }
    }
}