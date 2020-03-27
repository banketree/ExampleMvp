package com.example.base_fun.http

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.base_fun.dialog.LoadingDialog
import com.thinkcore.kandroid.toast
import kotlinx.coroutines.*
import timber.log.Timber

//http UI执行体
abstract class HttpViewModel : ViewModel(), LifecycleObserver {

    private val tipsResultData: MutableLiveData<String> = MutableLiveData()
    private var loadingDialog: LoadingDialog? = null

    open fun init(appCompatActivity: AppCompatActivity?) {
        appCompatActivity?.apply {
            tipsResultData.observe(this, Observer {
                if (it.isEmpty()) return@Observer
                appCompatActivity.toast(it)
            })
        }
    }

    // OnLifecycleEvent()内的注解Lifecycle.Event.XXX 对应不同的生命周期方法，你可以根据需要监听不同的生命周期方法。
    // 方法名可以随意，这里为了方便理解定义为onResumeListener()。
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeListener() {
        Timber.d("onResume: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseListener() {
        Timber.d("onPause: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestoryListener() {
        Timber.d("onDestory: ")
        hideLoadingDialog()
    }

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

    fun showToast(content: String) {
        tipsResultData.postValue(content)
    }

    fun showLoadingDialog(activity: Activity) {
        if (isShowLoadingDialog()) return
        activity.let {
            loadingDialog = LoadingDialog(it)
            loadingDialog?.show()
        }
    }

    fun hideLoadingDialog() {
        Timber.i("hideLoadingDialog")
        loadingDialog?.hide()
        loadingDialog = null
    }

    fun isShowLoadingDialog(): Boolean = loadingDialog != null && loadingDialog!!.isShowing()
}