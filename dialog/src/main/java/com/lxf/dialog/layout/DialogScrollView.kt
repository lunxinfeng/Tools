package com.lxf.dialog.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import com.lxf.dialog.waitForLayout


class DialogScrollView(
        context: Context?,
        attrs: AttributeSet? = null
) : ScrollView(context, attrs) {

    var rootView: DialogLayout? = null

    private var isScrollable: Boolean = false
        get() = getChildAt(0).measuredHeight > height

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
//        waitForLayout {  }
    }


//    private fun invalidateDividers() {
//        if (childCount == 0 || measuredHeight == 0 || !isScrollable) {
//            rootView?.invalidateDividers(false, false)
//            return
//        }
//        val view = getChildAt(childCount - 1)
//        val diff = view.bottom - (measuredHeight + scrollY)
//        rootView?.invalidateDividers(
//                scrollY > 0,
//                diff > 0
//        )
//    }
}