package com.lxf.rxretrofit

import com.lxf.rxretrofit.api.ApiService
import com.lxf.rxretrofit.callback.BaseView
import com.lxf.rxretrofit.callback.DownloadObserver


fun RetrofitHelper.download(url:String,filePath:String){
    RetrofitHelper.getInstance()
            .create(ApiService::class.java)
            .downloadFile(url)
            .subscribe(object : DownloadObserver<BaseView>(filePath = filePath){
                override fun doOnError(errorMessage: String) {

                }
            })
}