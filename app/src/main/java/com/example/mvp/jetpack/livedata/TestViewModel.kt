package com.example.mvp.jetpack.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class TestViewModel(val key: String) : ViewModel() {
    val nameEvent = MutableLiveData<String>()

    class Factory(private val mKey: String) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TestViewModel(mKey) as T
        }
    }
}