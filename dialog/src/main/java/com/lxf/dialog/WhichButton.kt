package com.lxf.dialog

import com.lxf.dialog.layout.DialogActionButtonLayout.Companion.INDEX_NEGATIVE
import com.lxf.dialog.layout.DialogActionButtonLayout.Companion.INDEX_NEUTRAL
import com.lxf.dialog.layout.DialogActionButtonLayout.Companion.INDEX_POSITIVE


enum class WhichButton(val index: Int) {
    POSITIVE(INDEX_POSITIVE),
    NEGATIVE(INDEX_NEGATIVE),
    NEUTRAL(INDEX_NEUTRAL);

    companion object {
        fun get(index:Int) = when(index){
            INDEX_POSITIVE -> POSITIVE
            INDEX_NEGATIVE -> NEGATIVE
            INDEX_NEUTRAL -> NEUTRAL
            else -> throw IndexOutOfBoundsException("$index is not an action button index.")
        }
    }
}