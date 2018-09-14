package com.lxf.rxretrofit.callback

import io.reactivex.disposables.Disposable


abstract class ProgressObserver<T,V : BaseView>(
        private val view:V? = null,
        private val showProgress:Boolean = true
):BaseObserver<T>() {
    override fun doOnSubscribe(d: Disposable) {
        view?.showProgress(showProgress)
    }

    override fun doOnComplete() {
        view?.showProgress(false)
    }

    override fun doOnError(e: Throwable,errorMessage: String) {
        view?.showProgress(false)
        doOnError(errorMessage)
    }

    abstract fun doOnError(errorMessage: String)

}