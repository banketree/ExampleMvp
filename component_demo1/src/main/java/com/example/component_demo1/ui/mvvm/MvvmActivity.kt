package com.example.component_demo1.ui.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.component_demo1.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.demo1_activity_coroutines.*


class MvvmActivity : AppCompatActivity() {
    private lateinit var netViewModel: NetViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo1_activity_coroutines)
        /*创建viewmodel*/
        netViewModel = ViewModelProviders.of(this).get(NetViewModel::class.java)

        btn.setOnClickListener {
            /*请求数据*/
            netViewModel.getWeather()
            netViewModel.getHtml()
        }

        /*数据发生变化时更新ui*/
        netViewModel.weatherData.observe(this, Observer {
            tv.text = Gson().toJson(it)
        })
        netViewModel.baiduData.observe(this, Observer {
            tv.text = it?.toString()
        })
    }
}
