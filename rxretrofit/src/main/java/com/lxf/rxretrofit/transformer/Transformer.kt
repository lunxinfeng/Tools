package com.lxf.rxretrofit.transformer

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


object Transformer {
    fun <U> io_main() = ObservableTransformer<U,U>{
        it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}