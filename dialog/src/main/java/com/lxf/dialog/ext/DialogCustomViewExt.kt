package com.lxf.dialog.ext

import android.support.annotation.LayoutRes
import android.view.View
import com.lxf.dialog.MaterialDialog
import com.lxf.dialog.R

fun MaterialDialog.customView(
        @LayoutRes viewRes: Int? = null,
        view: View? = null,
        scrollable: Boolean = false
): MaterialDialog {
    if (this.contentRecyclerView != null) {
        throw IllegalStateException(
                "This dialog has already been setup with another type " +
                        "(e.g. list, message, input, etc.)"
        )
    }
    atLeastOneIsNotNull("customView", view, viewRes)
    if (scrollable || this.contentScrollViewFrame != null) {
        addContentScrollView()
        this.contentCustomView = view ?: inflate(viewRes!!, this.contentScrollViewFrame!!)
        if (!scrollable) {
            // We didn't explicitly want this view to be scrollable but we already had existing
            // scroll content. So, add top margin to separate a bit.
            this.contentCustomView!!.apply {
                updateMargin(top = topMargin() + dimenPx(R.dimen.md_dialog_frame_margin_vertical_less))
                updatePadding(bottom = 0)
            }
        }
        this.contentScrollViewFrame!!.addView(this.contentCustomView)
    } else {
        this.contentCustomView = view ?: inflate(viewRes!!, this.view)
        this.view.addView(this.contentCustomView, 1)
    }
    return this
}
