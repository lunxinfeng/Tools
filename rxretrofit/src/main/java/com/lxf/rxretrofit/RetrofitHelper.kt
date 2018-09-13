package com.lxf.rxretrofit

import com.lxf.rxretrofit.config.OkHttpConfig
import com.lxf.rxretrofit.config.RetrofitConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.InputStream
import javax.net.ssl.HostnameVerifier


class RetrofitHelper private constructor(){

    companion object {
        @Volatile
        private var instance: RetrofitHelper? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: RetrofitHelper()
                }
    }

    /**
     * 是否开启日志
     */
    fun debug(debug: Boolean,level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): RetrofitHelper {
        OkHttpConfig.getInstance().debug(debug,level)
        return this
    }

    /**
     * 全局读取超时时间
     */
    fun readTimeOut(readTimeOut: Long): RetrofitHelper {
        OkHttpConfig.getInstance().readTimeOut(readTimeOut)
        return this
    }

    /**
     * 全局写入超时时间
     */
    fun writeTimeOut(writeTimeout: Long): RetrofitHelper {
        OkHttpConfig.getInstance().writeTimeOut(writeTimeout)
        return this
    }

    /**
     * 全局连接超时时间
     */
    fun connectTimeout(connectTimeout: Long): RetrofitHelper {
        OkHttpConfig.getInstance().connectTimeout(connectTimeout)
        return this
    }

    /**
     * 添加全局拦截器
     */
    fun addInterceptor(interceptor: Interceptor): RetrofitHelper {
        OkHttpConfig.getInstance().addInterceptor(interceptor)
        return this
    }

    /**
     * 添加全局网络拦截器
     */
    fun addNetworkInterceptor(interceptor: Interceptor): RetrofitHelper {
        OkHttpConfig.getInstance().addNetworkInterceptor(interceptor)
        return this
    }

    /**
     * https的全局访问规则
     */
    fun hostnameVerifier(hostnameVerifier: HostnameVerifier): RetrofitHelper {
        OkHttpConfig.getInstance().hostnameVerifier(hostnameVerifier)
        return this
    }

    /**
     * https双向认证证书
     */
    fun certificates(bksFile: InputStream? = null, password: String? = null, vararg certificates: InputStream): RetrofitHelper {
        OkHttpConfig.getInstance().sslSocketFactory(bksFile, password, *certificates)
        return this
    }

    /**
     * 设置自定义OkHttpClient
     */
    fun client(client: OkHttpClient):RetrofitHelper{
        RetrofitConfig.getInstance().client(client)
        return this
    }

    /**
     * 全局基础URL
     */
    fun baseUrl(url:String):RetrofitHelper{
        RetrofitConfig.getInstance().baseUrl(url)
        return this
    }

    fun <T> create(service:Class<T>): T {
        if (!RetrofitConfig.getInstance().isCustomClient){
            val client = OkHttpConfig.getInstance().build()
            client(client)
        }
        return RetrofitConfig.getInstance().build().create(service)
    }
}