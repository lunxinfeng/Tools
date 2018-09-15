package com.lxf.rxretrofit

import com.lxf.rxretrofit.api.ApiService
import com.lxf.rxretrofit.callback.BaseView
import com.lxf.rxretrofit.callback.DownloadObserver
import com.lxf.rxretrofit.callback.ProgressListener
import com.lxf.rxretrofit.interceptor.DownloadInterceptor
import com.lxf.rxretrofit.transformer.Transformer

typealias ErrorListener = (String) -> Unit
typealias CompleteListener = () -> Unit

fun RetrofitHelper.download(
        url:String,
        filePath:String,
        progressListener:ProgressListener? = null,
        completeListener:CompleteListener? = null,
        errorListener: ErrorListener? = null
){
    this
            .addInterceptor(DownloadInterceptor(progressListener))
            .baseUrl("https://www.baidu.com")
            .create(ApiService::class.java)
            .downloadFile(url)
            .compose(Transformer.io_main())
            .subscribe(object : DownloadObserver<BaseView>(filePath = filePath){
                override fun doOnComplete() {
                    super.doOnComplete()
                    completeListener?.invoke()
                }
                override fun doOnError(errorMessage: String) {
                    errorListener?.invoke(errorMessage)
                }
            })
}