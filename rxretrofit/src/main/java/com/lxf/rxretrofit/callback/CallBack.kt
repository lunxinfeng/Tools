package com.lxf.rxretrofit.callback

import io.reactivex.disposables.Disposable


interface CallBack<T> {
    fun doOnSubscribe(d: Disposable)
    fun doOnNext(data:T)
    fun doOnComplete()
    fun doOnError(e: Throwable,errorMessage:String)
}