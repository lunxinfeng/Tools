package com.lxf.dialog

import android.graphics.Typeface
import android.widget.TextView


class FontConfig(
        private val textColor:Int? = null,
        private val textSize:Float? = null,
        private val typeface: Typeface? = null
) {

    fun config(textView: TextView){
        textColor?.let { textView.setTextColor(it) }
        textSize?.let { textView.textSize = it }
        typeface?.let { textView.typeface = typeface }
    }
}