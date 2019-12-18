package com.example.mvp.main

import com.example.base_fun.mvp.BasePresenter
import com.example.base_fun.mvp.IPresenter
import com.example.base_fun.mvp.IView
import kotlinx.coroutines.*
import om.example.base_lib.kandroid.i
import timber.log.Timber
import javax.inject.Inject

class KotlinPresenter @Inject constructor() : BasePresenter(), IView {
    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun init() {
        super.init()
    }

    override fun release() {
        super.release()
    }

    fun testCoroutine() {
        Timber.i("主线程 id${Thread.currentThread().id}")
        Thread(Runnable {
            try {
                testRepeat()
                testLaunch()
                testSuspend()
                testAsync()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }

    private fun testRepeat() = runBlocking {
        repeat(2) {
            Timber.i("协程$it  ++++ id${Thread.currentThread().id}")
            delay(1000)
        }
    }

    private fun testLaunch() {
        val job = GlobalScope.launch {
            delay(5000)
            Timber.i("协程结束")
        }

        Timber.i("${job.isActive} ${job.isCancelled} ${job.isCompleted}")
        job.cancel()
//        job.join()
    }

    private fun testSuspend() {
        GlobalScope.launch {
            val token = getToken()
            Timber.i(token)
        }

        repeat(8) {
        }
    }

    private suspend fun getToken(): String {
        delay(2000)
        return "token"
    }

    private fun testAsync() {
        GlobalScope.launch {
            val token = GlobalScope.async { getToken() }
            Timber.i("${token.await()}")
        }

        repeat(8) {
        }
    }
}