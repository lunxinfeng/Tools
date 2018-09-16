package com.lxf.dialog.ext

import android.graphics.Point
import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.support.v7.widget.AppCompatButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.lxf.dialog.*
import com.lxf.dialog.listAdapter.DialogAdapter

internal fun MaterialDialog.setWindowConstraints() {
    val wm = this.window!!.windowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    val windowWidth = size.x
    val windowHeight = size.y

    context.resources.apply {
        val windowVerticalPadding = getDimensionPixelSize(
                R.dimen.md_dialog_vertical_margin
        )
        val windowHorizontalPadding = getDimensionPixelSize(
                R.dimen.md_dialog_horizontal_margin
        )
        val maxWidth = getDimensionPixelSize(R.dimen.md_dialog_max_width)
        val calculatedWidth = windowWidth - windowHorizontalPadding * 2

        this@setWindowConstraints.view.maxHeight = windowHeight - windowVerticalPadding * 2
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(this@setWindowConstraints.window!!.attributes)
        lp.width = Math.min(maxWidth, calculatedWidth)
        this@setWindowConstraints.window!!.attributes = lp
    }
}

internal fun <T> MaterialDialog.inflate(
        @LayoutRes res: Int,
        root: ViewGroup? = null
): T {
    return LayoutInflater.from(context).inflate(res, root, false) as T
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
        fontConfig:FontConfig?
) {
    val value = text ?: getString(textRes?:0)
    if (value != null) {
        (textView.parent as View).visibility = View.VISIBLE
        textView.visibility = View.VISIBLE
        textView.text = value

        fontConfig?.let { textView.config(it) }
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

internal fun MaterialDialog.addContentMessageView(
        @StringRes res: Int?,
        text: CharSequence?,
        fontConfig:FontConfig?
) {
    if (this.textViewMessage == null) {
        this.textViewMessage = inflate(R.layout.md_dialog_stub_message,contentScrollViewFrame)
        this.contentScrollViewFrame!!.addView(this.textViewMessage)

        fontConfig?.let { this.textViewMessage?.config(it) }
    }
    atLeastOneIsNotNull("message", text, res)
    this.textViewMessage!!.text = text ?: getString(res!!)
}

internal fun MaterialDialog.hasActionButtons() = view.buttonsLayout.visibleButtons.isNotEmpty()

fun MaterialDialog.getActionButton(which: WhichButton) =
        view.buttonsLayout.actionButtons[which.index] as AppCompatButton

internal fun MaterialDialog.onActionButtonClicked(which: WhichButton) {
    when (which) {
        WhichButton.POSITIVE -> {
            positiveListeners.invokeAll(this)
            val adapter = listAdapter as? DialogAdapter<*, *>
            adapter?.positiveButtonClicked()
        }
        WhichButton.NEGATIVE -> negativeListeners.invokeAll(this)
        WhichButton.NEUTRAL -> neutralListeners.invokeAll(this)
    }
    if (autoDismissEnabled) {
        dismiss()
    }
}

internal fun MaterialDialog.startAnim(
        anim: BaseAnim
){
    anim.start(window.decorView)
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