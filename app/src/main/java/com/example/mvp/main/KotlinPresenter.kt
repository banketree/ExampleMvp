package com.example.mvp.main

import com.example.base_fun.mvp.BasePresenter
import com.example.base_fun.mvp.IPresenter
import com.example.base_fun.mvp.IView
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select
import om.example.base_lib.kandroid.i
import timber.log.Timber
import java.util.concurrent.TimeUnit
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

                GlobalScope.launch(Dispatchers.Main) {
                    //GlobalScope.launch  开启了线程（不管是不是在子线程中 都开启新线程）
                    Timber.i("testLaunch launch协程 2  ++++ id${Thread.currentThread().id}")
                }
            }
        }).start()

        //总结
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
        job.start() //启动的时候 用它
    }

    fun testWithContext() { //    withContext -不创建新的协程，在指定协程上运行代码块
//        val userId = withContext() { getEmailMessage(4000L, userId) }
////            等同于
//        async(CommonPool) { getEmailMessage(4000L, userId) }.await()
    }

    fun testWithTimeoutOrNull() = runBlocking {
        Timber.i("testWithTimeoutOrNull 线程  ++++ id${Thread.currentThread().id}")
        val result = withTimeoutOrNull(1300) {
            // withTimeout(1300) {
            Timber.i("testWithTimeoutOrNull 协程  ++++ id${Thread.currentThread().id}")
            delay(500)
            Timber.i("testWithTimeoutOrNull 协程  ++++ id${Thread.currentThread().id}")
            delay(500)
            Timber.i("testWithTimeoutOrNull 协程  ++++ id${Thread.currentThread().id}")
            try {
                delay(500)
                Timber.i("testWithTimeoutOrNull 协程try  ++++ id${Thread.currentThread().id}")
            } catch (e: Exception) {
                Timber.i("testWithTimeoutOrNull 协程catch  ++++ id${Thread.currentThread().id} + $e")
            } finally {
                Timber.i("testWithTimeoutOrNull 协程finally  ++++ id${Thread.currentThread().id}")
            }
            Timber.i("testWithTimeoutOrNull 协程 结束语  ++++ id${Thread.currentThread().id}")
            "结束语"
        }
        Timber.i("testWithTimeoutOrNull 线程  ++++ id${Thread.currentThread().id} result=$result")

//        ● withTimeout
//        超时自动结束，但是不会看到抛出异常；
//        用try catch 不可以捕获到异常信息；
//        不会执行到 RESULT 代码块；
//        ● withTimeoutOrNull
//        超时自动结束，但是不会看到抛出异常；
//        用try catch 可以捕获到异常信息；
//        会执行到 RESULT 代码块，但是result为null；
    }

    fun testTicker() {
        //解释： 每间隔1秒，发一个事件，每个事件之间间隔0秒；
        Timber.i("testTicker 线程  ++++ id${Thread.currentThread().id}")
        GlobalScope.launch(Dispatchers.Main) {
            Timber.i("testTicker 协程  ++++ id${Thread.currentThread().id}")
            val tickerChannel = ticker(1000)
            for (channel in tickerChannel) {
                Timber.i("testTicker 协程  ++++ id${Thread.currentThread().id}")
            }
        }
    }

    //broadcast 广播
    fun testBroadcast() {
        Timber.i("testBroadcast 线程  ++++ id${Thread.currentThread().id}")
        GlobalScope.launch(Dispatchers.Main) {
            Timber.i("testBroadcast 协程  ++++ id${Thread.currentThread().id}")
            val broadcastChannel = broadcast<String> {
                delay(2000)
                send("A")
                delay(1000)
                send("B")
                delay(1500)
                send("C")
                delay(15000)
            }
            val receiveChannel = broadcastChannel.openSubscription()
            Timber.i("testBroadcast 协程  ++++ id${Thread.currentThread().id}")
            for (element in receiveChannel) {
                Timber.i("testBroadcast 协程  ++++ id${Thread.currentThread().id} element:$element")
            }
            Timber.i("testBroadcast 协程Over  ++++ id${Thread.currentThread().id}")
        }
    }

    fun testActor() {
        Timber.i("testActor 线程  ++++ id${Thread.currentThread().id}")
        val sendChannel = GlobalScope.actor<String>(Dispatchers.Main) {
            for (message in channel) {
                Timber.i("testActor 协程  ++++ id${Thread.currentThread().id} message:$message")
            }
        }
        GlobalScope.launch {
            delay(1000L)
            sendChannel.send("100")
            delay(1500L)
            sendChannel.send("102")
            delay(2000L)
            sendChannel.send("106")
        }
    }

    fun testChannel() {
        //通过 GlobeScope 启动的协程单独启动一个协程作用域，内部的子协程遵从默认的作用域规则。通过 GlobeScope 启动的协程“自成一派”。
        Timber.i("testChannel 线程  ++++ id${Thread.currentThread().id}")
        val channel = Channel<String>()
        GlobalScope.launch(Dispatchers.Main) {
            launch {
                channel.send("A")
                delay(2)
                channel.send("B")
                delay(1)
                channel.send("C")
                channel.close()
            }
            for (result in channel) {
                Timber.i("testChannel 协程  ++++ id${Thread.currentThread().id} result:$result")
            }
        }
    }

    fun testCoroutineScope() {
        //    coroutineScope //协程的作用域，可以管理其域内的所有协程。一个CoroutineScope可以有许多的子scope。
        //coroutineScope 是继承外部 Job 的上下文创建作用域，在其内部的取消操作是双向传播的，子协程未捕获的异常也会向上传递给父协程。
        // 它更适合一系列对等的协程并发的完成一项工作，任何一个子协程异常退出，那么整体都将退出，简单来说就是”一损俱损“。
        // 这也是协程内部再启动子协程的默认作用域。

        Timber.i("testCoroutineScope 线程  ++++ id${Thread.currentThread().id}")
        GlobalScope.launch(Dispatchers.Main) {
            Timber.i("testCoroutineScope 线程  ++++ id${Thread.currentThread().id}")
            // print输出的结果顺序将会是 1， 2， 3， 4
            coroutineScope {
                delay(1000)
                Timber.i("testCoroutineScope 线程  ++++ id${Thread.currentThread().id} 1")
                launch {
                    delay(6000)
                    Timber.i("testCoroutineScope 线程  ++++ id${Thread.currentThread().id} 3")
                }
                Timber.i("testCoroutineScope 线程  ++++ id${Thread.currentThread().id} 2")
                return@coroutineScope
            }
            Timber.i("testCoroutineScope 线程  ++++ id${Thread.currentThread().id} 4")
        }
    }

    fun testSupervisorScope() {
        //    supervisorScope //协程的作用域，可以管理其域内的所有协程。一个CoroutineScope可以有许多的子scope。
        //supervisorScope 同样继承外部作用域的上下文，但其内部的取消操作是单向传播的，父协程向子协程传播，反过来则不然，
        // 这意味着子协程出了异常并不会影响父协程以及其他兄弟协程。它更适合一些独立不相干的任务，任何一个任务出问题，
        // 并不会影响其他任务的工作，简单来说就是”自作自受“，例如 UI，我点击一个按钮出了异常，其实并不会影响手机状态栏的刷新。
        // 需要注意的是，supervisorScope 内部启动的子协程内部再启动子协程，如无明确指出，则遵守默认作用域规则，也即 supervisorScope 只作用域其直接子协程。

        Timber.i("testSupervisorScope 线程  ++++ id${Thread.currentThread().id}")
        GlobalScope.launch(Dispatchers.Main) {
            Timber.i("testSupervisorScope 线程  ++++ id${Thread.currentThread().id}")
            // print输出的结果顺序将会是 1， 2， 3， 4
            supervisorScope {
                delay(1000)
                Timber.i("testSupervisorScope 线程  ++++ id${Thread.currentThread().id} 1")
                launch {
                    supervisorScope {
                        throw Exception("test")
                        Timber.i("testSupervisorScope 线程  ++++ id${Thread.currentThread().id} 3")
                    }
                }
                Timber.i("testSupervisorScope 线程  ++++ id${Thread.currentThread().id} 2")
                return@supervisorScope
            }
            Timber.i("testSupervisorScope 线程  ++++ id${Thread.currentThread().id} 4")
        }
    }

    fun testDispatchers() {
        //    Dispatchers.Default  共享后台线程池里的线程
        //    Dispatchers.IO - 共享后台线程池里的线程
        //    Dispatchers.Main - 主线程 Android主线程
        //    Dispatchers.Unconfined - 没指定，就是在当前线程  不限制，使用父Coroutine的现场
        Timber.i("testDispatchers 线程  ++++ id${Thread.currentThread().id}")
        GlobalScope.launch(Dispatchers.Main) {
            Timber.i("testDispatchers 协程Main  ++++ id${Thread.currentThread().id}")
        }
        GlobalScope.launch(Dispatchers.IO) {
            //IO 独立的一个线程
            Timber.i("testDispatchers 协程IO  ++++ id${Thread.currentThread().id}")

            GlobalScope.launch(Dispatchers.Unconfined) {
                Timber.i("testDispatchers 协程IO Unconfined  ++++ id${Thread.currentThread().id}")
            }
        }
        GlobalScope.launch(Dispatchers.Default) {
            //Default 默认启动一个新线程
            Timber.i("testDispatchers 协程Default  ++++ id${Thread.currentThread().id}")
            GlobalScope.launch(Dispatchers.IO) {
                Timber.i("testDispatchers 协程 Default IO  ++++ id${Thread.currentThread().id}")
            }

            GlobalScope.launch(Dispatchers.Unconfined) {
                Timber.i("testDispatchers 协程Default Unconfined  ++++ id${Thread.currentThread().id}")
            }
        }
        GlobalScope.launch(Dispatchers.Unconfined) {
            // Unconfined 跟启动的线程绑定
            Timber.i("testDispatchers 协程 Unconfined  ++++ id${Thread.currentThread().id}")
        }

        GlobalScope.launch(Dispatchers.Main) {
            Timber.i("testDispatchers 协程Main  ++++ id${Thread.currentThread().id}")
            GlobalScope.launch(Dispatchers.Unconfined) {
                Timber.i("testDispatchers 协程Main Unconfined  ++++ id${Thread.currentThread().id}")
            }
        }

        Timber.i("testDispatchers 线程Over  ++++ id${Thread.currentThread().id}")
    }

    //Producer
    fun testProducer() {
        Timber.i("testProducer 线程  ++++ id${Thread.currentThread().id}")
        GlobalScope.launch(Dispatchers.Default) {
            // 创建一个生产者方法
            fun produceSquares() = produce<Int>() {
                Timber.i("testProducer 协程  ++++ id${Thread.currentThread().id}")
                for (x in 1..5) send(x * x)
            }

            runBlocking<Unit> {
                // 得到生产者
                val squares = produceSquares()
                // 对生产者生产的每一个结果进行消费
                squares.consumeEach {
                    Timber.i("testProducer 协程  ++++ id${Thread.currentThread().id} produce:$it")
                }
            }
        }
    }

    fun testPipeline() {
        GlobalScope.launch {
            // 创建一个选择Deferred的生产者
            fun switchMapDeferreds(input: ReceiveChannel<Deferred<String>>) = produce<String>() {
                var current = input.receive() // 从获取第一个Deferred开始
                while (isActive) { // 循环直到被关闭或者被取消
                    val next = select<Deferred<String>?> {
                        // 选择下一个Deferred<String>如果已经关闭便返回null
                        input.onReceiveOrNull { update ->
                            update // 如果input中有新的Deferred(这个案例中是通过async返回的Deferred)发送过来便更新为当前的Deferred
                        }
                        // 如果在Deferred已经执行完成还没有新的Deferred过来，便会进行下面的操作
                        current.onAwait { value ->
                            send(value) // 发送这个Deferred携带的值给当前channel
                            input.receiveOrNull() // 等待并且从input中接收下一个Deferred，作为返回值
                        }
                    }
                    if (next == null) {
                        println("Channel was closed")
                        break // 结束循环
                    } else {
                        current = next
                    }
                }
            }

            // 创建一个async的方法，其返回的是一个Deferred
            fun asyncString(str: String, time: Long) = async() {
                delay(time)
                str
            }

            runBlocking<Unit> {
                val chan = Channel<Deferred<String>>() // 创建一个传递Deferred<String>的channel
                launch(coroutineContext) {
                    // 启动一个coroutine用于输出每次的选择结果
                    for (s in switchMapDeferreds(chan))
                        println(s)
                }
                chan.send(asyncString("BEGIN", 100))
                delay(200) // 挂起200ms，让在switchMapDeferreds中有足够的时间让BEGIN这个Deferred完成挂起与异步操作
                chan.send(asyncString("Slow", 500))
                delay(100) // 挂起100ms，让在switchMapDeferreds中没有足够时间让Slow这个Defferred完成挂起与异步操作
                chan.send(asyncString("Replace", 100)) // 在上面挂起 100ms毫秒以后，立马发送这个Replace的
                delay(500) // 挂起500ms 让上面的async有足够时间
                chan.send(asyncString("END", 500))
                delay(1000) // 挂起500ms 让上面的async有足够时间
                chan.close() // 关闭channel
                delay(500) // 延缓500ms让switchMapDeferreds有足够的时间输出'Channel was closed'
            }
        }
    }
//
//    CoroutineContext --》Job, ContinuationInterceptor, CoroutineName 和CoroutineId
//    AbstractCoroutine --》CoroutineScope， Job， Continuation， JobSupport。

    //relay、yield 区别
    //relay 和 yield 方法是协程内部的操作，可以挂起协程，区别是
    // relay 是挂起协程并经过执行时间恢复协程，当线程空闲时就会运行协程；
    // yield 是挂起协程，让协程放弃本次 cpu 执行机会让给别的协程，当线程空闲时再次运行协程。


    //Kotlin声明点变型与Java中的使用点变型进行对比
    //2、如何使用Kotlin中的使用点变型
    //3、Kotlin泛型中的星投影
    //4、使用泛型型变实现可用于实际开发中的Boolean扩展
    //
    //https://juejin.im/post/5daacd23518825104d08d386
}