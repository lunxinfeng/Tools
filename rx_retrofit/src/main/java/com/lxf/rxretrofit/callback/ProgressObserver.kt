package com.lxf.rxretrofit.callback

import io.reactivex.disposables.Disposable


abstract class ProgressObserver<T,V : BaseView>(
        tag: String? = null,
        private val view:V? = null
):BaseObserver<T>(tag) {
    override fun doOnSubscribe(d: Disposable) {
        view?.showProgress()
    }

    override fun doOnComplete() {
        view?.hideProgress()
    }

    override fun doOnError(e: Throwable,errorMessage: String) {
        view?.hideProgress()
        doOnError(errorMessage)
    }

    abstract fun doOnError(errorMessage: String)

}