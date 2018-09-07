package com.lxf.dialog.ext

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.TextView
import com.lxf.dialog.FontConfig


internal fun <T: View> T.isVisible():Boolean{
    return if (this is Button)
        visibility == View.VISIBLE && text.trim().isNotEmpty()
    else
        visibility == View.VISIBLE
}

internal fun <V : View> ViewGroup.inflate(
        ctxt: Context = context,
        @LayoutRes res: Int
): V {
    return LayoutInflater.from(ctxt).inflate(res, this, false) as V
}

internal fun TextView.config(fontConfig: FontConfig){
    fontConfig.config(this)
}

internal inline fun <T : View> T.waitForLayout(crossinline f: T.() -> Unit) =
        viewTreeObserver.apply {
            addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    removeOnGlobalLayoutListener(this)
                    this@waitForLayout.f()
                }
            })
        }
