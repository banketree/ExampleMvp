package com.example.base_fun.http

import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.GeneralSecurityException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/*
* Retrofit 工厂
* */
abstract class RetrofitFactory {
    companion object {
        private var okHttpClient: OkHttpClient? = null //请求端
        private var gsonRetrofit: Retrofit? = null
        private var stringRetrofit: Retrofit? = null
    }

    abstract fun getUrl(): String //请求域名

    //gson
    @Synchronized
    protected fun <T> gsonService(service: Class<T>): T {
        if (gsonRetrofit == null) {
            synchronized(RetrofitFactory::class.java) {
                if (gsonRetrofit == null) {
                    gsonRetrofit = Retrofit.Builder()
                        .baseUrl(getUrl())
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(getOkHttpClient())
                        .build()
                }
            }
        }

        return gsonRetrofit!!.create(service)
    }

    //String
    @Synchronized
    protected fun <T> stringService(service: Class<T>): T {
        if (stringRetrofit == null) {
            synchronized(RetrofitFactory::class.java) {
                stringRetrofit = Retrofit.Builder()
                    .baseUrl(getUrl())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(getOkHttpClient())
                    .build()
            }
        }
        return stringRetrofit!!.create(service)
    }

    ///////////////////////////////////////okhttp3//////////////////////////////////////////////////////
    protected fun handleOkHttpClient(builder: OkHttpClient.Builder) {
        builder.apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            // 构建 OkHttpClient 时,将 OkHttpClient.Builder() 传入 with() 方法,进行初始化配置
            RetrofitUrlManager.getInstance().with(this)
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        if (okHttpClient == null) {
            synchronized(RetrofitFactory::class.java) {
                if (okHttpClient == null) {
                    val trustManager: X509TrustManager
                    val sslSocketFactory: SSLSocketFactory
                    try {
                        trustManager = UnSafeTrustManager()
                        val sslContext = SSLContext.getInstance("TLS")
                        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
                        sslSocketFactory = sslContext.socketFactory
                    } catch (e: GeneralSecurityException) {
                        throw RuntimeException(e)
                    }

                    var builder = OkHttpClient.Builder()
                        .sslSocketFactory(sslSocketFactory, trustManager)
                        .hostnameVerifier(UnSafeHostnameVerifier())
                    handleOkHttpClient(builder)
                    okHttpClient = builder.build()
                }
            }
        }
        return okHttpClient!!
    }

    class UnSafeTrustManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    class UnSafeHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }
}