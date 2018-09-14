package com.lxf.rxretrofit.callback

import com.lxf.rxretrofit.util.writeFile
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File

typealias ProgressListener = (
        bytesReaded:Long,
        totalLength:Long,
        progress:Int,
        done:Boolean
) -> Unit

abstract class DownloadObserver<V : BaseView>(
        val filePath:String
): ProgressObserver<ResponseBody, V>() {

    override fun doOnNext(data: ResponseBody) {
        Observable.just(data)
                .subscribeOn(Schedulers.io())
                .subscribe(object : ProgressObserver<ResponseBody, V>() {
                    override fun doOnError(errorMessage: String) {
                        this@DownloadObserver.doOnError(errorMessage)
                    }

                    override fun doOnNext(data: ResponseBody) {
                        writeFile(data, File(filePath))
                    }
                })
    }
}