package com.example.component_demo1.ui.mvvm

import androidx.lifecycle.MutableLiveData
import com.example.base_fun.http.HttpCallback
import com.example.base_fun.http.HttpViewModel
import com.example.base_fun.http.RespBase
import com.example.base_fun.http.convertHttpRes

class WeatherViewModel : HttpViewModel() {

    companion object {
        const val LOGIN_STATE_SUCCESS = 0
        const val LOGIN_STATE_FAILURE = 1
    }

    // 登录状态
    val loginState: MutableLiveData<Int> = MutableLiveData()

    private var repository: WeatherApi? = null
    fun getData() {
        if (repository != null) return
        repository = WeatherApi()

        launchOnIO(
            tryBlock = {
                val response = repository!!.getCityCodeByIpByGson()
                response.run {
                    // 进行响应处理
                    handlingHttpResponse<RespBase>(
                        convertHttpRes(),
                        successBlock = {
                            loginState.postValue(LOGIN_STATE_SUCCESS)
                        },
                        failureBlock = { ex ->
                            loginState.postValue(LOGIN_STATE_FAILURE)
                            handlingApiExceptions(ex)
                        }
                    )
                }
            },
            catchBlock = { e ->
                // 请求异常处理
                handlingExceptions(e)
            }, finallyBlock = {
                repository = null
            }
        )
    }
}