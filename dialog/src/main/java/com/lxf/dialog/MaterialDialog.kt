package com.lxf.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.lxf.dialog.layout.DialogLayout
import com.lxf.dialog.layout.DialogScrollView

typealias DialogCallback = (MaterialDialog) -> Unit

class MaterialDialog(context: Context) : Dialog(context) {
    var autoDismissEnabled: Boolean = true
        internal set

    internal val view: DialogLayout = inflate(R.layout.md_dialog_base)
    internal var textViewMessage: TextView? = null
    internal var contentScrollView: DialogScrollView? = null
    internal var contentScrollViewFrame: LinearLayout? = null
//    internal var contentRecyclerView: DialogRecyclerView? = null
    internal var contentCustomView: View? = null

    internal val positiveListeners = mutableListOf<DialogCallback>()
    internal val negativeListeners = mutableListOf<DialogCallback>()
    internal val neutralListeners = mutableListOf<DialogCallback>()

    internal val preShowListeners = mutableListOf<DialogCallback>()
    internal val showListeners = mutableListOf<DialogCallback>()
    internal val dismissListeners = mutableListOf<DialogCallback>()
    internal val cancelListeners = mutableListOf<DialogCallback>()

    init {
        setContentView(view)
        this.view.dialog = this
    }

    fun atLeastOneIsNotNull(des:String, vararg elements:Any?){
        if (elements.filter { it == null }.size == elements.size)
            throw IllegalArgumentException("$des must be have at least one.")
    }

    fun icon(
            @DrawableRes res: Int? = null,
            drawable: Drawable? = null
    ): MaterialDialog {
        atLeastOneIsNotNull("icon", drawable, res)
        populateIcon(
                view.titleLayout.iconView,
                iconRes = res,
                icon = drawable
        )
        return this
    }

    fun title(
            @StringRes textRes: Int? = null,
            text: String? = null,
            typeface: Typeface? = null
    ): MaterialDialog {
        atLeastOneIsNotNull("title",textRes,text)

        populateText(
                view.titleLayout.titleView,
                textRes,
                text,
                typeface
        )
        return this
    }

    fun message(
            @StringRes textRes: Int? = null,
            text: CharSequence? = null,
            typeface: Typeface? = null

    ): MaterialDialog {
        if (this.contentCustomView != null) {
            throw IllegalStateException("message() should be used BEFORE customView().")
        }
        addContentScrollView()
        addContentMessageView(textRes, text,typeface)
        atLeastOneIsNotNull("title",textRes,text)
        return this
    }

    fun positiveButton(
            @StringRes res: Int? = null,
            text: CharSequence? = null,
            click: DialogCallback? = null,
            typeface: Typeface? = null
    ): MaterialDialog {
        if (click != null) {
            positiveListeners.add(click)
        }

        val btn = getActionButton(WhichButton.POSITIVE)
        if (res == null && text == null && btn.isVisible()) {
            // Didn't receive text and the button is already setup,
            // so just stop with the added listener.
            return this
        }

        populateText(
                btn,
                textRes = res,
                text = text,
                typeface = typeface
        )
        return this
    }

    fun negativeButton(
            @StringRes res: Int? = null,
            text: CharSequence? = null,
            click: DialogCallback? = null,
            typeface: Typeface? = null
    ): MaterialDialog {
        if (click != null) {
            negativeListeners.add(click)
        }

        val btn = getActionButton(WhichButton.NEGATIVE)
        if (res == null && text == null && btn.isVisible()) {
            // Didn't receive text and the button is already setup,
            // so just stop with the added listener.
            return this
        }

        populateText(
                btn,
                textRes = res,
                text = text,
                typeface = typeface
        )
        return this
    }

    fun noAutoDismiss(): MaterialDialog {
        this.autoDismissEnabled = false
        return this
    }


    fun onPreShow(callback: DialogCallback): MaterialDialog {
        this.preShowListeners.add(callback)
        return this
    }

    fun onShow(callback: DialogCallback): MaterialDialog {
        this.showListeners.add(callback)
        if (this.isShowing) {
            // Already showing, invoke now
            this.showListeners.invokeAll(this)
        }
        setOnShowListener { this.showListeners.invokeAll(this) }
        return this
    }

    fun onDismiss(callback: DialogCallback): MaterialDialog {
        this.dismissListeners.add(callback)
        setOnDismissListener { dismissListeners.invokeAll(this) }
        return this
    }

    fun onCancel(callback: DialogCallback): MaterialDialog {
        this.cancelListeners.add(callback)
        setOnCancelListener { cancelListeners.invokeAll(this) }
        return this
    }

    override fun show() {
        preShow()
        super.show()
    }
}