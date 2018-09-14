package com.lxf.rxretrofit.callback

import com.lxf.rxretrofit.exception.ApiException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


abstract class BaseObserver<T> : Observer<T>,CallBack<T> {
    private var disposable:Disposable? = null

    override fun onSubscribe(d: Disposable) {
        disposable = d
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
        disposable?.dispose()
    }
}