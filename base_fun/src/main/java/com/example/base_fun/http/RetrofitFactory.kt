package com.example.base_fun.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.GeneralSecurityException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

abstract class RetrofitFactory {

    private var okHttpClient: OkHttpClient? = null //请求端

    abstract fun getUrl(): String //请求域名

    //gson
    @Synchronized
    protected fun <T> gsonService(service: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build().create(service)
    }

    //String
    @Synchronized
    protected fun <T> stringService(service: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(getUrl())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(getOkHttpClient())
            .build().create(service)
    }

    ///////////////////////////////////////okhttp3//////////////////////////////////////////////////////
    private fun getOkHttpClient(): OkHttpClient {
        if (okHttpClient != null) return okHttpClient!!

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

        okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)
            .hostnameVerifier(UnSafeHostnameVerifier())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


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