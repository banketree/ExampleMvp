package com.example.base_fun.http

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.SocketTimeoutException


//http 请求体
abstract class HttpViewModel : ViewModel() {

//    /**
//     * 在IO线程中开启,修改为Dispatchers.IO
//     */
//    fun <T> launchOnIO(block: () -> Any, httpCallback: HttpCallback<T>) {
//        viewModelScope.launch(Dispatchers.IO) {
//            tryCatch(tryBlock = {
//                val response = block()
//                httpCallback?.apply {
//                    val result = getBean(response)
//                    if(result==null||result)
//                    onSucess(result!!)
//                }
//            }, catchBlock = { e ->
//                httpCallback?.onFaile(e)
//            }, finallyBlock = {
//                httpCallback?.onComplete()
//            })
//        }
//    }
    /**
     * 在主线程中开启
     * catchBlock、finallyBlock 并不是必须,不同的业务对于错误的处理也可能不同想要完全统一的处理是很牵强的
     */
    fun launchOnMain(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(e: Throwable) -> Unit = {},// 默认空实现，可根据具体情况变化
        finallyBlock: suspend CoroutineScope.() -> Unit = {}
    ) {
        viewModelScope.launch {
            tryCatch(tryBlock, catchBlock, finallyBlock)
        }
    }

    /**
     * 在IO线程中开启,修改为Dispatchers.IO
     */
    fun launchOnIO(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(e: Throwable) -> Unit = {},
        finallyBlock: suspend CoroutineScope.() -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            tryCatch(tryBlock, catchBlock, finallyBlock)
        }
    }

    /**
     * @param tryBlock 尝试执行的挂起代码块
     * @param catchBlock 捕获异常的代码块 "协程对Retrofit的实现在失败、异常时没有onFailure的回调而是直接已Throwable的形式抛出"
     * @param finallyBlock finally代码块
     */
    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(e: Exception) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Exception) {
                catchBlock(e)
            } finally {
                finallyBlock()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    /**
     * 处理请求层的错误,对可能的已知的错误进行处理
     */
    fun handlingExceptions(e: Throwable) {
        when (e) {
            is CancellationException -> {
            }
            is SocketTimeoutException -> {
            }
            is JsonParseException -> {
            }
            else -> {
            }
        }
    }

    /**
     * 处理响应层的错误
     */
    fun handlingApiExceptions(e: HttpError) {
        when (e) {
            HttpError.USER_EXIST -> {
            }
            HttpError.PARAMS_ERROR -> {
            }
            // .. more
        }
    }

    /**
     * 处理HttpResponse
     * @param res
     * @param successBlock 成功
     * @param failureBlock 失败
     */
    fun <T> handlingHttpResponse(
        res: HttpResponse,
        successBlock: (data: T) -> Unit,
        failureBlock: ((error: HttpError) -> Unit)? = null
    ) {
        when (res) {
            is Success<*> -> {
                successBlock.invoke(res.data as T)
            }
            is Failure -> {
                with(res) {
                    failureBlock?.invoke(error) ?: defaultErrorBlock.invoke(error)
                }
            }
        }
    }


    // 默认的处理方案
    val defaultErrorBlock: (error: HttpError) -> Unit = { error ->
        //    UiUtils.showToast(error.errorMsg ?: "${error.code}")            // 可以根据是否为debug进行拆分处理
    }

}