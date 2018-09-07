package com.lxf.dialog

import android.animation.TimeInterpolator
import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import com.lxf.dialog.ext.*
import com.lxf.dialog.layout.DialogLayout
import com.lxf.dialog.layout.DialogRecyclerView
import com.lxf.dialog.layout.DialogScrollView

typealias DialogCallback = (MaterialDialog) -> Unit

class MaterialDialog(context: Context) : Dialog(context) {
    var autoDismissEnabled: Boolean = true
        internal set
    var listAdapter:RecyclerView.Adapter<*>? = null
        get() = contentRecyclerView?.adapter
    private var animIn: BaseAnim? = null
    private var animOut: BaseAnim? = null

    internal val view: DialogLayout = inflate(R.layout.md_dialog_base)
    internal var textViewMessage: TextView? = null
    internal var contentScrollView: DialogScrollView? = null
    internal var contentScrollViewFrame: LinearLayout? = null
    internal var contentRecyclerView: DialogRecyclerView? = null
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
        setWindowConstraints()
//        window.apply {
//            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            //设置遮罩层
//            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//            attributes = attributes.apply {
//                dimAmount = 0.7f
//            }
//            attributes = attributes.apply {
//                width = ViewGroup.LayoutParams.MATCH_PARENT
//                height = ViewGroup.LayoutParams.MATCH_PARENT
//            }
//        }
        setOnShowListener { showListeners.invokeAll(this) }
        setOnDismissListener { dismissListeners.invokeAll(this) }
        setOnCancelListener { cancelListeners.invokeAll(this) }
    }

    fun atLeastOneIsNotNull(des: String, vararg elements: Any?) {
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
            fontConfig:FontConfig? = null
    ): MaterialDialog {
        atLeastOneIsNotNull("title", textRes, text)

        populateText(
                view.titleLayout.titleView,
                textRes,
                text,
                fontConfig
        )
        return this
    }

    fun message(
            @StringRes textRes: Int? = null,
            text: CharSequence? = null,
            fontConfig:FontConfig? = null
    ): MaterialDialog {
        if (this.contentCustomView != null) {
            throw IllegalStateException("message() should be used BEFORE customView().")
        }
        addContentScrollView()
        addContentMessageView(textRes, text, fontConfig)
        atLeastOneIsNotNull("title", textRes, text)
        return this
    }

    fun positiveButton(
            @StringRes res: Int? = null,
            text: CharSequence? = null,
            fontConfig:FontConfig? = null,
            click: DialogCallback? = null
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
                fontConfig = fontConfig
        )
        return this
    }

    fun negativeButton(
            @StringRes res: Int? = null,
            text: CharSequence? = null,
            fontConfig:FontConfig? = null,
            click: DialogCallback? = null
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
                fontConfig = fontConfig
        )
        return this
    }

    fun noAutoDismiss(): MaterialDialog {
        this.autoDismissEnabled = false
        return this
    }

    fun animOnShow(
            anim: BaseAnim,
            duration: Long = 500L,
            interpolator: TimeInterpolator = LinearInterpolator()
    ): MaterialDialog {
        this.animIn = anim.apply {
            this.duration = duration
            this.interpolator = interpolator
        }
        this.showListeners.add { startAnim(this.animIn!!) }
        return this
    }

    fun animOnDismiss(
            anim: BaseAnim,
            duration: Long = 500L,
            interpolator: TimeInterpolator = LinearInterpolator()
    ): MaterialDialog {
        this.animOut = anim.apply {
            this.duration = duration
            this.interpolator = interpolator
        }
        return this
    }

    fun onPreShow(callback: DialogCallback): MaterialDialog {
        this.preShowListeners.add(callback)
        return this
    }

    fun onShow(callback: DialogCallback): MaterialDialog {
        this.showListeners.add(callback)
        return this
    }

    fun onDismiss(callback: DialogCallback): MaterialDialog {
        this.dismissListeners.add(callback)
        return this
    }

    fun onCancel(callback: DialogCallback): MaterialDialog {
        this.cancelListeners.add(callback)
        return this
    }

    override fun show() {
        preShow()
        super.show()
    }

    override fun dismiss() {
        animOut?.let {
            it.animatorSet.onAnimatorEnd { super.dismiss() }
            startAnim(it)
        } ?: super.dismiss()
    }
}