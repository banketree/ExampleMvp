package com.example.component_demo1.mvvmdemo.demo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.example.component_demo1.R
import com.example.component_demo1.mvvmdemo.base.BaseViewModelActivity
import com.example.component_demo1.mvvmdemo.databean.Data

class ScrollingActivity : BaseViewModelActivity<ScrollingViewModel>() {

    override fun getLayoutAny() = R.layout.demo1_activity_main

    override fun initPlug() {
    }

    override fun providerVMClass(): Class<ScrollingViewModel>? = ScrollingViewModel::class.java

    private val TAG = this.javaClass.simpleName

    private val datas = mutableListOf<Data>()

    override fun initView() {
    }

    override fun initData() {
        viewModel.getActicle().observe(this, Observer {
            //获取到数据
            it?.run {
                datas.addAll(it)
            }
        })
    }
}
