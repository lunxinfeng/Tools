package com.lxf.rxretrofit.callback

import com.lxf.rxretrofit.manager.TaskManager
import com.lxf.rxretrofit.exception.ApiException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


abstract class BaseObserver<T>(private val tag:String? = null) : Observer<T>,CallBack<T> {
    private var disposable:Disposable? = null

    override fun onSubscribe(d: Disposable) {
        if (tag == null)
            disposable = d
        else
            TaskManager.addTask(tag,d)
        doOnSubscribe(d)
    }

    override fun onNext(t: T) {
        doOnNext(t)
    }

    override fun onComplete() {
        doOnComplete()
        disposable()
    }

    override fun onError(e: Throwable) {
        doOnError(e,ApiException.handleException(e).errorInfo.toString())
        disposable()
    }

    private fun disposable(){
        if (tag == null)
            disposable?.dispose()
        else
            TaskManager.cancel(tag)
    }
}