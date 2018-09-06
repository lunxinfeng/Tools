package com.lxf.dialog

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


fun <T:View> T.isVisible():Boolean{
    return if (this is Button)
        visibility == View.VISIBLE && text.trim().isNotEmpty()
    else
        visibility == View.VISIBLE
}

fun <T:View> T.dimenPx(@DimenRes res:Int) = resources.getDimensionPixelSize(res)

internal inline fun <T : View> T.waitForLayout(crossinline f: T.() -> Unit) =
        viewTreeObserver.apply {
            addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    removeOnGlobalLayoutListener(this)
                    this@waitForLayout.f()
                }
            })
        }

internal fun <T> MaterialDialog.inflate(
        @LayoutRes res: Int,
        root: ViewGroup? = null
): T {
    return LayoutInflater.from(context).inflate(res, root, false) as T
}

internal fun getDrawable(
        context: Context,
        @DrawableRes res: Int? = null,
        @AttrRes attr: Int? = null,
        fallback: Drawable? = null
): Drawable? {
    if (attr != null) {
        val a = context.obtainStyledAttributes(intArrayOf(attr))
        try {
            var d = a.getDrawable(0)
            if (d == null && fallback != null) {
                d = fallback
            }
            return d
        } finally {
            a.recycle()
        }
    }
    if (res == null) return fallback
    return ContextCompat.getDrawable(context, res)
}

internal fun MaterialDialog.getString(
        @StringRes res: Int
): CharSequence? {
    if (res == 0) return null
    return context.resources.getText(res)
}

internal fun MaterialDialog.populateIcon(
        imageView: ImageView,
        @DrawableRes iconRes: Int?,
        icon: Drawable?
) {
    val drawable = getDrawable(context, res = iconRes, fallback = icon)
    if (drawable != null) {
        (imageView.parent as View).visibility = View.VISIBLE
        imageView.visibility = View.VISIBLE
        imageView.setImageDrawable(drawable)
    } else {
        imageView.visibility = View.GONE
    }
}

internal fun MaterialDialog.populateText(
        textView: TextView,
        @StringRes textRes: Int? = null,
        text: CharSequence? = null,
        typeface: Typeface?
) {
    val value = text ?: getString(textRes?:0)
    if (value != null) {
        (textView.parent as View).visibility = View.VISIBLE
        textView.visibility = View.VISIBLE
        textView.text = value
        if (typeface != null) {
            textView.typeface = typeface
        }
    } else {
        textView.visibility = View.GONE
    }
}

internal fun MaterialDialog.addContentScrollView() {
    if (this.contentScrollView == null) {
        this.contentScrollView = inflate(R.layout.md_dialog_stub_scrollview, this.view)
        this.contentScrollView!!.rootView = this.view
        this.contentScrollViewFrame = this.contentScrollView!!.getChildAt(0) as LinearLayout
        this.view.addView(this.contentScrollView, 1)
    }
}

internal fun MaterialDialog.addContentMessageView(@StringRes res: Int?, text: CharSequence?,typeface: Typeface?) {
    if (this.textViewMessage == null) {
        this.textViewMessage = TextView(context)
        this.contentScrollViewFrame!!.addView(this.textViewMessage)
        if (typeface != null) {
            this.textViewMessage?.typeface = typeface
        }
    }
    atLeastOneIsNotNull("message", text, res)
    this.textViewMessage!!.text = text ?: getString(res!!)
}

internal fun MaterialDialog.getActionButton(which: WhichButton) =
        view.buttonsLayout.actionButtons[which.index] as AppCompatButton

internal fun MaterialDialog.onActionButtonClicked(which: WhichButton) {
    when (which) {
        WhichButton.POSITIVE -> {
            positiveListeners.invokeAll(this)
//            val adapter = getListAdapter() as? DialogAdapter<*, *>
//            adapter?.positiveButtonClicked()
        }
        WhichButton.NEGATIVE -> negativeListeners.invokeAll(this)
        WhichButton.NEUTRAL -> neutralListeners.invokeAll(this)
    }
    if (autoDismissEnabled) {
        dismiss()
    }
}

internal fun MaterialDialog.preShow() {
    this.preShowListeners.invokeAll(this)
//    this.view.apply {
//        if (titleLayout.shouldNotBeVisible()) {
//            // Reduce top and bottom padding if we have no title
//            contentView.updatePadding(
//                    top = frameMarginVerticalLess,
//                    bottom = frameMarginVerticalLess
//            )
//        }
//        if (getCheckBoxPrompt().isVisible()) {
//            // Zero out bottom content padding if we have a checkbox prompt
//            contentView.updatePadding(bottom = 0)
//        }
//    }
}

internal fun MutableList<DialogCallback>.invokeAll(dialog: MaterialDialog) {
    for (callback in this) {
        callback.invoke(dialog)
    }
}