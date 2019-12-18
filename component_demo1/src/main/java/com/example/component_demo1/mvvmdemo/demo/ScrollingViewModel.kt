package com.example.component_demo1.mvvmdemo.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.component_demo1.mvvmdemo.base.BaseViewModel
import com.example.component_demo1.mvvmdemo.databean.Data
import com.example.component_demo1.mvvmdemo.repository.ArticleRepository

class ScrollingViewModel : BaseViewModel() {

    private val TAG = ScrollingViewModel::class.java.simpleName

    private val datas: MutableLiveData<List<Data>> by lazy { MutableLiveData<List<Data>>().also { loadDatas() } }

    private val repository = ArticleRepository()

    fun getActicle(): LiveData<List<Data>> {
        return datas
    }

    private fun loadDatas() = launchUI {
        val result = repository.getDatas()
        datas.value = result.data
    }
}