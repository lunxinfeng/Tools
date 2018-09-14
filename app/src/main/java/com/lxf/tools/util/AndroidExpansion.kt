package com.lxf.tools.util

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.lxf.rxretrofit.transformer.Transformer
import com.lxf.tools.bean.ResponseModel
import com.lxf.tools.rx.AndroidSchedulers
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(applicationContext, msg, duration).show()

fun String?.isEmpty() = TextUtils.isEmpty(this)

fun EditText.onChange(onChange: (s: CharSequence?) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onChange.invoke(s)
        }
    })
}

fun <D> Transformer.io_main_izis(cls: Class<D>) = ObservableTransformer<ResponseModel<String>, D> { it ->
    it
            .map {
                val data = it.data
                val result = GsonUtil.GsonToBean(data, cls)
                result
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

/**
 * @param num 共点多少次
 * @param des 行为描述
 * @param listener 逻辑操作
 */
fun View.clickN(num: Int, des: String, listener: (v: View) -> Unit) {

    val compositeDisposable = CompositeDisposable()
    var clickNum = 0

    fun click(listener: (v: View) -> Unit) {
        if (++clickNum == num) {
            listener.invoke(this)
            return
        } else {
            compositeDisposable.clear()
            if (clickNum in 3..(num - 1))
                Toast.makeText(context, "再点${num - clickNum}次$des", Toast.LENGTH_SHORT).show()
        }
        val d = Single.timer(1000, TimeUnit.MILLISECONDS)
                .subscribe { _ -> clickNum = 0 }
        compositeDisposable.add(d)
    }

    setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN)
            click(listener)
        false
    }
}