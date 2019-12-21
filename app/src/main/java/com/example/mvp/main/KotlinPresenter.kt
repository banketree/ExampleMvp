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

    //runBlocking -->可以保证块内逻辑顺序、阻塞执行
    //默认的CommonPool线程数有限，如果底层方法使用runBlocking{...}执行阻塞逻辑、并且顶层方法大量启动并行任务调用这个方法，
    //此时，这些并行的阻塞任务、底层协程均被调度到CommonPool，协程本质上还是需要在线程下才能执行的，可此时线程资源已经全部被阻塞任务占用，
    //阻塞任务又在等待其内的协程返回结果，自此形成了死锁。
    fun testRepeat() = runBlocking {
        repeat(5) {
            Timber.i("协程$it  ++++ id${Thread.currentThread().id}")
            delay(1000)
        }

        Thread(Runnable {
            runBlocking {
                repeat(5) {
                    Timber.i("协程$it  ++++ id${Thread.currentThread().id}")
                    delay(1000)
                }
            }
        }).start()

        //测试runBlock死锁
//        launch {
//            var nextTime = System.currentTimeMillis()
//            while (true) {
//                val currentTime = System.currentTimeMillis()
//                if (currentTime > nextTime) {
//                    println("当前时间${System.currentTimeMillis()}")
//                    nextTime += 1000
//                }
//            }
//        }
//        delay(2000)
//        println("这里的代码无法得到执行")
//        println("这里的代码无法得到执行")
        //因为runBlocking中调用launch()会在当前线程中执行协程，
        //也就是说在runBlocking中不管开启多少个子协程它们都是使用runBlocking所使用的那一条线程来完成任务的，
        //所以就会出现上述的线程被霸占的情况。
        //如果在主线程中  则无响应
    }

    fun testLaunch() {
        Timber.i("testLaunch 协程  ++++ id${Thread.currentThread().id}")

        val job = GlobalScope.launch {
            //GlobalScope.launch  开启了线程
            delay(5000)
            Timber.i("testLaunch launch协程 1  ++++ id${Thread.currentThread().id}")
        }

        Timber.i("testLaunch run")
        Timber.i("${job.isActive} ${job.isCancelled} ${job.isCompleted}")
//        job.cancel()
//        job.join()

        Thread(Runnable {
            Timber.i("testLaunch Thread 线程  ++++ id${Thread.currentThread().id}")
            runBlocking {
                Timber.i("testLaunch runBlocking协程  ++++ id${Thread.currentThread().id}")

                GlobalScope.launch {
                    //GlobalScope.launch  开启了线程（不管是不是在子线程中 都开启新线程）
                    Timber.i("testLaunch launch协程 2  ++++ id${Thread.currentThread().id}")
                }
            }
        }).start()

        //runBlocking 跟启动线程挂钩
        //launch 永远开启新线程 不阻塞主线程
    }

    fun testSuspend() {
        Timber.i("testSuspend 线程  ++++ id${Thread.currentThread().id}")
        GlobalScope.launch {
            val token = getToken()
            Timber.i("testSuspend launch 线程  ++++ id${Thread.currentThread().id}")
        }
    }

    private suspend fun getToken(): String {
        delay(2000)
        return "token"
    }

    fun testAsync() {
        Timber.i("testAsync 线程  ++++ id${Thread.currentThread().id}")
        GlobalScope.launch {
            Timber.i("testAsync launch线程  ++++ id${Thread.currentThread().id}")
            val token = GlobalScope.async {
                //async 创建带返回值的协程，返回的是 Deferred 类
                Timber.i("testAsync async线程  ++++ id${Thread.currentThread().id}")
                getToken()
            }
            Timber.i("${token.await()}")
        }

        //launch、async 都是新开启一个线程 不过async 是有返回值的
    }

    fun testLaunchLAZY() {
        //LAZY - 懒加载模式，你需要它的时候，再调用启动，看这个例子
        var job: Job = GlobalScope.launch(start = CoroutineStart.LAZY) {
            Timber.d("协程开始运行，时间: " + System.currentTimeMillis())
        }

        Thread.sleep(1000L)
        // 手动启动协程
        job.start()
    }

//    withContext -不创建新的协程，在指定协程上运行代码块
//    coroutineScope
//    supervisorScope
//    CoroutineContext --》Job, ContinuationInterceptor, CoroutineName 和CoroutineId
//    AbstractCoroutine --》CoroutineScope， Job， Continuation， JobSupport。

//    Dispatchers.Default
//    Dispatchers.IO -
//    Dispatchers.Main - 主线程
//    Dispatchers.Unconfined - 没指定，就是在当前线程

    //relay、yield 区别

    //Kotlin声明点变型与Java中的使用点变型进行对比
    //2、如何使用Kotlin中的使用点变型
    //3、Kotlin泛型中的星投影
    //4、使用泛型型变实现可用于实际开发中的Boolean扩展
    //
    //https://juejin.im/post/5daacd23518825104d08d386
}