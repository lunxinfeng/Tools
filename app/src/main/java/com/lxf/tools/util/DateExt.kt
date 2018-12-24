package com.lxf.tools.util

import java.util.*


/**
 * 当天0点时间
 */
fun getStartTimeCurrDay() = Calendar.getInstance().apply {
    set(Calendar.HOUR, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis