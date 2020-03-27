package com.example.base_fun.http

import androidx.activity.ComponentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.base_fun.dialog.LoadingDialog
import org.json.JSONException
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException

abstract class HttpCallback<T> {

    companion object {
        private val businessHashMap = hashMapOf<String, IHttpBusinessListener?>()

        fun addHttpBusinessIntercept(name: String, listener: IHttpBusinessListener?) {
            businessHashMap[name] = listener
        }

        fun removeHttpBusinessIntercept(name: String) {
            businessHashMap[name] = null
            businessHashMap.remove(name)
        }
    }

    private var serviceName: String = ""  //服务名称  具体业务
    private var dialogState: MutableLiveData<Boolean> = MutableLiveData() //对话框状态记录
    private var componentActivity: ComponentActivity? = null //依赖的界面

    constructor()

    constructor(serviceName: String) {
        this.serviceName = serviceName
    }

    constructor(componentActivity: ComponentActivity?) {
        this.componentActivity = componentActivity
        initState(componentActivity)
    }

    constructor(serviceName: String, componentActivity: ComponentActivity?) {
        this.componentActivity = componentActivity
        this.serviceName = serviceName
        initState(componentActivity)
    }

    suspend fun execute(call: suspend () -> Response<*>) {
        dialogState.postValue(true) //显示
        try {
            //网络请求
            val response = call()
            if (response.isSuccessful) {
                val body = getBean(response) ?: throw HttpError()
                if (body is RespBase<*>) {
                    onHttpBusiness(body)
                }
                onSucess(body)
                onFinish()
                dialogState.postValue(false) //隐藏
                return
            }

            Timber.e(" ${response.code()} ${response.message()}")
            onFaile(HttpError())//网络异常 表明服务器交流失败
        } catch (e: ConnectException) {
            onFaile(HttpError())//网络异常 表明服务器交流失败
        } catch (e: Exception) {
            onFaile(e)
        }

        onFinish()
        dialogState.postValue(false) //隐藏
    }

    open fun onSucess(response: T) {
        Timber.i("$serviceName onSucess")
    }

    open fun onFaile(ex: Exception) {
        Timber.e("$serviceName onFaile $ex")
    }

    open fun onFinish() {
        Timber.i("$serviceName onFinish")
    }

    @Throws(JSONException::class)
    open fun getBean(response: Response<*>): T? {
        return response.body() as? T
    }

    private fun initState(componentActivity: ComponentActivity?) {
        componentActivity ?: return
        dialogState.observe(componentActivity, Observer {
            if (it) showLoadingDialog()
            else hideLoadingDialog()
        })
    }

    private fun onHttpBusiness(respBase: RespBase<*>) {
        if (businessHashMap.isEmpty()) return
        for ((key, value) in businessHashMap) {
            if (value == null) continue
            (value as? IHttpBusinessListener)?.onBusiness(respBase)
        }
    }

    private var loadingDialog: LoadingDialog? = null
    private fun showLoadingDialog() {
        if (isShowLoadingDialog()) return
        componentActivity?.let {
            loadingDialog = LoadingDialog(it)
            loadingDialog?.show()
        }
    }

    private fun hideLoadingDialog() {
        Timber.i("hideLoadingDialog")
        loadingDialog?.hide()
        loadingDialog = null
    }

    fun isShowLoadingDialog(): Boolean = loadingDialog != null && loadingDialog!!.isShowing()
}