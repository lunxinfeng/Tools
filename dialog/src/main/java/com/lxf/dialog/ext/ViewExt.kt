package com.lxf.dialog.ext

import android.content.Context
import android.support.annotation.LayoutRes
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.EditText
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

internal fun <T : View> T.updatePadding(
        left: Int = this.paddingLeft,
        top: Int = this.paddingTop,
        right: Int = this.paddingRight,
        bottom: Int = this.paddingBottom
) {
    if (left == this.paddingLeft &&
            top == this.paddingTop &&
            right == this.paddingRight &&
            bottom == this.paddingBottom
    ) {
        // no change needed, don't want to invalidate layout
        return
    }
    this.setPadding(left, top, right, bottom)
}

internal fun <T : View> T.topMargin(): Int {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    return layoutParams.topMargin
}

internal fun <T : View> T.updateMargin(
        left: Int = -1,
        top: Int = -1,
        right: Int = -1,
        bottom: Int = -1
) {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    if (left != -1) {
        layoutParams.leftMargin = left
    }
    if (top != -1) {
        layoutParams.topMargin = top
    }
    if (right != -1) {
        layoutParams.rightMargin = right
    }
    if (bottom != -1) {
        layoutParams.bottomMargin = bottom
    }
    this.layoutParams = layoutParams
}

internal fun EditText.textChanged(callback: (CharSequence) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) = Unit

        override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
        ) = Unit

        override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
        ) = callback.invoke(s)
    })
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
