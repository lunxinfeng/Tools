package com.lxf.tools.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager


fun getScreenDisplayMetrics(context: Context): DisplayMetrics {
    val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val metrics = DisplayMetrics()
    manager.defaultDisplay.getMetrics(metrics)
    return metrics
}

fun getScreenSize(context: Context): Array<Int> {
    val metrics = getScreenDisplayMetrics(context)
    return arrayOf(metrics.widthPixels,metrics.heightPixels)
}