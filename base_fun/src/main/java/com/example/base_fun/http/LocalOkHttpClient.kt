package com.example.base_fun.http

import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import java.security.GeneralSecurityException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object LocalOkHttpClient {
    ///////////////////////////////////////okhttp3//////////////////////////////////////////////////////
    fun getOkHttpClient(
        handleOkHttpClient: (OkHttpClient.Builder).() -> Unit = {
            this.apply {
                connectTimeout(RetrofitFactory.TIME_OUT_SECOND, TimeUnit.SECONDS)
                readTimeout(RetrofitFactory.TIME_OUT_SECOND, TimeUnit.SECONDS)
                writeTimeout(RetrofitFactory.TIME_OUT_SECOND, TimeUnit.SECONDS)
//            addInterceptor(NetInterceptor())
                // 构建 OkHttpClient 时,将 OkHttpClient.Builder() 传入 with() 方法,进行初始化配置
                RetrofitUrlManager.getInstance().with(this)
            }
        }
    ): OkHttpClient {
        var okHttpClient: OkHttpClient
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
        return okHttpClient
    }
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