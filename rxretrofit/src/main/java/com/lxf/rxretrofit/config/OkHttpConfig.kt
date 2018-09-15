package com.lxf.rxretrofit.config

import com.lxf.rxretrofit.https.DefaultHostnameVerifier
import com.lxf.rxretrofit.https.HttpsUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.InputStream
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier


internal class OkHttpConfig private constructor(){

    init {
        okHttpClientBuilder = OkHttpClient.Builder()
                .hostnameVerifier(DefaultHostnameVerifier())
                .readTimeout(DEFAULT_MILLISECONDS,TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_MILLISECONDS,TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_MILLISECONDS,TimeUnit.MILLISECONDS)
    }

    companion object {
        private const val DEFAULT_MILLISECONDS = 60 * 1000L
        @Volatile
        private var instance: OkHttpConfig? = null
        private lateinit var okHttpClientBuilder: OkHttpClient.Builder

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance
                            ?: OkHttpConfig().apply { instance = this }
                }
        fun newInstance() = OkHttpConfig()
    }

    /**
     * 是否开启日志
     */
    internal fun debug(debug: Boolean,level:HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): OkHttpConfig {
        if (debug) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = level
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return this
    }

    /**
     * 全局读取超时时间
     */
    internal fun readTimeOut(readTimeOut: Long,timeUnit: TimeUnit = TimeUnit.MILLISECONDS): OkHttpConfig {
        okHttpClientBuilder.readTimeout(readTimeOut, timeUnit)
        return this
    }

    /**
     * 全局写入超时时间
     */
    internal fun writeTimeOut(writeTimeout: Long,timeUnit: TimeUnit = TimeUnit.MILLISECONDS): OkHttpConfig {
        okHttpClientBuilder.writeTimeout(writeTimeout, timeUnit)
        return this
    }

    /**
     * 全局连接超时时间
     */
    internal fun connectTimeout(connectTimeout: Long,timeUnit: TimeUnit = TimeUnit.MILLISECONDS): OkHttpConfig {
        okHttpClientBuilder.connectTimeout(connectTimeout, timeUnit)
        return this
    }

    /**
     * 添加全局拦截器
     */
    internal fun addInterceptor(interceptor: Interceptor): OkHttpConfig {
        okHttpClientBuilder.addInterceptor(interceptor)
        return this
    }

    /**
     * 添加全局网络拦截器
     */
    internal fun addNetworkInterceptor(interceptor: Interceptor): OkHttpConfig {
        okHttpClientBuilder.addNetworkInterceptor(interceptor)
        return this
    }

    /**
     * https的全局访问规则
     */
    internal fun hostnameVerifier(hostnameVerifier: HostnameVerifier): OkHttpConfig {
        okHttpClientBuilder.hostnameVerifier(hostnameVerifier)
        return this
    }

    /**
     * https双向认证证书
     */
    internal fun sslSocketFactory(bksFile: InputStream? = null, password: String? = null, vararg certificates: InputStream): OkHttpConfig {
        val sslParams = HttpsUtils.getSslSocketFactory(bksFile, password, certificates)
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
        return this
    }

    internal fun build() = okHttpClientBuilder.build()
}