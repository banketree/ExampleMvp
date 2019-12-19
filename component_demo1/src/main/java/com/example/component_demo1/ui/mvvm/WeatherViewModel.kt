package com.example.component_demo1.ui.mvvm

import androidx.lifecycle.MutableLiveData
import com.example.base_fun.http.HttpCallback
import com.example.base_fun.http.HttpViewModel
import com.example.base_fun.http.RespBase
import com.example.base_fun.http.convertHttpRes
import retrofit2.Response
import timber.log.Timber

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
//                response.run {
//                    // 进行响应处理
//                    handlingHttpResponse<RespBase>(
//                        convertHttpRes(),
//                        successBlock = {
//                            loginState.postValue(LOGIN_STATE_SUCCESS)
//                        },
//                        failureBlock = { ex ->
//                            loginState.postValue(LOGIN_STATE_FAILURE)
//                            handlingApiExceptions(ex)
//                        }
//                    )
//                }
            },
            catchBlock = { e ->
                // 请求异常处理
                handlingExceptions(e)
            }, finallyBlock = {
                repository = null
            }
        )
    }

    fun getData2() {
        if (repository != null) return
        repository = WeatherApi()

        launchOnIO(
            tryBlock = {
                val result = getResult<RespWeather>({ repository!!.getCityCodeByIpByGson() })
                if (result.status == Result.Status.SUCCESS) {
                    loginState.postValue(LOGIN_STATE_SUCCESS)
                } else {
                    loginState.postValue(LOGIN_STATE_FAILURE)
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


    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Result<T> {
        Timber.e(message)
        return Result.error("Network call has failed for a following reason: $message")
    }
}