package com.example.component_demo1.ui.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.component_demo1.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.demo1_activity_coroutines.*


class MvvmActivity : AppCompatActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo1_activity_coroutines)
        /*创建viewmodel*/
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        btn.setOnClickListener {
            /*请求数据*/
            weatherViewModel.getData(this)
//            weatherViewModel.getData2(this)
        }

        /*数据发生变化时更新ui*/
        weatherViewModel.loginState.observe(this, Observer {
            //            tv.text = it?.toString()
        })
        weatherViewModel.dataState.observe(this, Observer {
            it?.apply {
                tv.text = toString()
            }

        })
    }
}
