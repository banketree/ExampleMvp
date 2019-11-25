package com.example.baselib.mvp


import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent


/**
 * ================================================
 * 基类 Model
 */
class BaseModel : IModel, LifecycleObserver {

    /**
     * 在框架中 [@BasePresenter.onDestroy] 时会默认调用 [@IModel.onDestroy]
     */
    override fun onDestroy() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }
}
