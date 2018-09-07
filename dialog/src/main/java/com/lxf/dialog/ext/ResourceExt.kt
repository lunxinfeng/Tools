package com.lxf.dialog.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.view.View
import com.lxf.dialog.MaterialDialog


internal fun MaterialDialog.getStringArray(@ArrayRes res: Int?): Array<String>? {
    if (res == null) return emptyArray()
    return context.resources.getStringArray(res)
}

internal fun <T: View> T.dimenPx(@DimenRes res:Int) = resources.getDimensionPixelSize(res)

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
