package com.example.base_fun.http

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

//http UI执行体
abstract class HttpViewModel : ViewModel() {

    /**
     * 在主线程中开启
     */
    fun launchOnMain(
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch {
            block()
        }
    }

    /**
     * 在IO线程中开启,修改为Dispatchers.IO
     */
    fun launchOnIO(
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            block()
        }
    }

    /**
     * 在线程中开启,修改为Dispatchers.Default
     */
    fun launchOnDefault(
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            block()
        }
    }

    /**
     * 在线程中开启,修改为Dispatchers.Unconfined
     */
    fun launchOnUnconfined(
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Unconfined) {
            block()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}