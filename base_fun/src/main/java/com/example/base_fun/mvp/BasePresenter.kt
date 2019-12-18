package com.example.base_fun.mvp

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter : IPresenter {
    private var compositeDisposable: CompositeDisposable? = null //订阅类

    override fun init() {
    }

    override fun release() {
        rleaseDisposable()
    }

    //添加订阅
    fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)//将所有 Disposable 放入容器集中处理
    }

    //释放订阅
    fun rleaseDisposable() {
        compositeDisposable?.clear()//保证 Activity 结束时取消所有正在执行的订阅
        compositeDisposable = null
    }
}