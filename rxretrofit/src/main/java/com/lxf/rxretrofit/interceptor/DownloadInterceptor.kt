package com.lxf.rxretrofit.interceptor

import com.lxf.rxretrofit.callback.ProgressListener
import com.lxf.rxretrofit.responsebody.DownloadResponseBody
import okhttp3.Interceptor
import okhttp3.Response


class DownloadInterceptor(
        private val listener:ProgressListener? = null
):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        return response.newBuilder()
                .body(DownloadResponseBody( response.body()!!,progressListener = listener))
                .build()
    }
}