package com.lxf.rxretrofit.callback

import io.reactivex.disposables.Disposable


abstract class ProgressObserver<T,V : BaseView>(
        private val view:V,
        private val showProgress:Boolean
):BaseObserver<T>() {
    override fun doOnSubscribe(d: Disposable) {
        view.showProgress(showProgress)
    }

    override fun doOnComplete() {
        view.showProgress(false)
    }

    override fun doOnError(errorMessage: String) {
        view.showProgress(false)
    }

}