package com.example.component_demo1.ui.mvvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.component_demo1.ui.mvvm.bean.Fiction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.HashMap
import kotlin.Exception as Exception1

class NetViewModel : ViewModel() {
    var fictions = MutableLiveData<List<Fiction>>()
    var weatherData = MutableLiveData<String>()
    var baiduData = MutableLiveData<String>()

    fun getFictions() {
        /*viewModelScope是一个绑定到当前viewModel的作用域  当ViewModel被清除时会自动取消该作用域，所以不用担心内存泄漏为问题*/
        viewModelScope.launch {
            try {
                /*withContext表示挂起块  配合Retrofit声明的suspend函数执行 该块会挂起直到里面的网络请求完成 最一行就是返回值*/
                val data = withContext(Dispatchers.IO) {

                    /*dataConvert扩展函数可以很方便的解析出我们想要的数据  接口很多的情况下下可以节省不少无用代码*/
                    RetrofitFactory.instance.getService(ApiService::class.java)
                        .getFictions().dataConvert()
                }

                /*给LiveData赋值  ui会自动更新*/
                fictions.value = data

            } catch (e: Exception) {
                /*请求异常的话在这里处理*/
                e.printStackTrace()

                Log.i("请求失败", "${e.message}")

            }


        }
    }

    fun getWeather() {
        /*viewModelScope是一个绑定到当前viewModel的作用域  当ViewModel被清除时会自动取消该作用域，所以不用担心内存泄漏为问题*/
        viewModelScope.launch {
            try {
                /*withContext表示挂起块  配合Retrofit声明的suspend函数执行 该块会挂起直到里面的网络请求完成 最一行就是返回值*/
                val data = withContext(Dispatchers.IO) {
                    val hashMap = HashMap<String, String>()
                    hashMap["output"] = "json"
                    hashMap["key"] = "4e34d358d9aa062b2c46afd627084f85"
                    /*dataConvert扩展函数可以很方便的解析出我们想要的数据  接口很多的情况下下可以节省不少无用代码*/
                    var data = RetrofitFactory.instance.getService(ApiService::class.java)
                        .getCityCodeByIp(hashMap)
                    Timber.i("$data")
                    data
                }

                /*给LiveData赋值  ui会自动更新*/
                weatherData.value = data?.toString()

            } catch (e: Exception) {
                /*请求异常的话在这里处理*/
                e.printStackTrace()

                Log.i("请求失败", "${e.message}")

            }


        }
    }

    fun getHtml() {
        /*viewModelScope是一个绑定到当前viewModel的作用域  当ViewModel被清除时会自动取消该作用域，所以不用担心内存泄漏为问题*/
        viewModelScope.launch {
            try {
                /*withContext表示挂起块  配合Retrofit声明的suspend函数执行 该块会挂起直到里面的网络请求完成 最一行就是返回值*/
                val data = withContext(Dispatchers.IO) {
                    val hashMap = HashMap<String, String>()
                    /*dataConvert扩展函数可以很方便的解析出我们想要的数据  接口很多的情况下下可以节省不少无用代码*/
                    var data = RetrofitFactory.instance.getService(ApiService::class.java)
                        .getHtml(hashMap)
                    Timber.i("$data")
                    data
                }

                /*给LiveData赋值  ui会自动更新*/
                baiduData.value = data?.toString()
            } catch (e: Exception) {
                /*请求异常的话在这里处理*/
                e.printStackTrace()
                Log.i("请求失败", "${e.message}")
                baiduData.value = e.message
            }
        }
    }

}