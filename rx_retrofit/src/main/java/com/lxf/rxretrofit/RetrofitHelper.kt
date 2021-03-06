package com.lxf.rxretrofit

import com.lxf.rxretrofit.config.OkHttpConfig
import com.lxf.rxretrofit.config.RetrofitConfig
import com.lxf.rxretrofit.manager.TaskManager
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import java.io.InputStream
import javax.net.ssl.HostnameVerifier


class RetrofitHelper private constructor() {
    private var retrofitConfig: RetrofitConfig = RetrofitConfig.newInstance()
    private var okHttpConfig: OkHttpConfig = OkHttpConfig.newInstance()
    private var maxRequests = 0
    private var maxRequestsPerHost = 0

    companion object {
        @Volatile
        private var instance: RetrofitHelper? = null

        /**
         * 用于常规网络请求
         */
        @JvmStatic
        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: RetrofitHelper().apply { instance = this }
                }

        /**
         * 用于文件的上传、下载
         */
        @JvmStatic
        fun newInstance() = RetrofitHelper()

        /**
         * 判断任务栈中有没有该任务
         */
        @JvmStatic
        fun hasTask(tag: String) = TaskManager.hasTask(tag)

        /**
         * 根据tag取消任务
         */
        @JvmStatic
        fun cancel(tag: String) {
            TaskManager.cancel(tag)
        }

        /**
         * 取消所有任务
         */
        @JvmStatic
        fun cancelAll() {
            TaskManager.cancelAll()
        }
    }

    /**
     * 是否开启日志
     */
    fun debug(debug: Boolean, level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): RetrofitHelper {
        okHttpConfig.debug(debug, level)
        return this
    }

    /**
     * 全局读取超时时间
     */
    fun readTimeOut(readTimeOut: Long): RetrofitHelper {
        okHttpConfig.readTimeOut(readTimeOut)
        return this
    }

    /**
     * 全局写入超时时间
     */
    fun writeTimeOut(writeTimeout: Long): RetrofitHelper {
        okHttpConfig.writeTimeOut(writeTimeout)
        return this
    }

    /**
     * 全局连接超时时间
     */
    fun connectTimeout(connectTimeout: Long): RetrofitHelper {
        okHttpConfig.connectTimeout(connectTimeout)
        return this
    }

    /**
     * 添加全局拦截器
     */
    fun addInterceptor(interceptor: Interceptor): RetrofitHelper {
        okHttpConfig.addInterceptor(interceptor)
        return this
    }

    /**
     * 添加全局网络拦截器
     */
    fun addNetworkInterceptor(interceptor: Interceptor): RetrofitHelper {
        okHttpConfig.addNetworkInterceptor(interceptor)
        return this
    }

    /**
     * https的全局访问规则
     */
    fun hostnameVerifier(hostnameVerifier: HostnameVerifier): RetrofitHelper {
        okHttpConfig.hostnameVerifier(hostnameVerifier)
        return this
    }

    /**
     * https双向认证证书
     */
    fun certificates(bksFile: InputStream? = null, password: String? = null, vararg certificates: InputStream): RetrofitHelper {
        okHttpConfig.sslSocketFactory(bksFile, password, *certificates)
        return this
    }

    fun addCallAdapterFactory(factory: CallAdapter.Factory): RetrofitHelper {
        retrofitConfig.addCallAdapterFactory(factory)
        return this
    }

    fun addConverterFactory(factory: Converter.Factory): RetrofitHelper {
        retrofitConfig.addConverterFactory(factory)
        return this
    }

    /**
     * 设置调度器
     */
    fun dispatcher(dispatcher: Dispatcher): RetrofitHelper {
        okHttpConfig.dispatcher(dispatcher)
        return this
    }

    /**
     * 是否允许重连，默认true
     */
    fun retryOnConnectionFailure(retry: Boolean): RetrofitHelper {
        okHttpConfig.retryOnConnectionFailure(retry)
        return this
    }

    /**
     * 设置自定义OkHttpClient
     */
    fun client(client: OkHttpClient): RetrofitHelper {
        retrofitConfig.client(client)
        return this
    }

    /**
     * 全局基础URL
     */
    fun baseUrl(url: String): RetrofitHelper {
        retrofitConfig.baseUrl(url)
        return this
    }

    fun <T> create(service: Class<T>): T {
        if (!retrofitConfig.isCustomClient) {
            val client = okHttpConfig.build()
            if (maxRequestsPerHost != 0)
                client.dispatcher().maxRequestsPerHost = maxRequestsPerHost
            if (maxRequests != 0)
                client.dispatcher().maxRequests = maxRequests
            client(client)
        }
        return retrofitConfig.build().create(service)
    }


    /**
     * 设置最大并发(单个域名)
     */
    fun setMaxRequestsPerHost(max: Int): RetrofitHelper {
        maxRequestsPerHost = max
        return this
    }

    /**
     * 设置最大并发
     */
    fun setMaxRequests(max: Int): RetrofitHelper {
        maxRequestsPerHost = max
        return this
    }
}