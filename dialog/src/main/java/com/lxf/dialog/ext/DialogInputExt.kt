package com.lxf.dialog.ext

import android.content.Context
import android.support.annotation.CheckResult
import android.support.annotation.StringRes
import android.support.design.widget.TextInputLayout
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.lxf.dialog.MaterialDialog
import com.lxf.dialog.R
import com.lxf.dialog.WhichButton

typealias InputCallback = ((MaterialDialog, CharSequence) -> Unit)?

@CheckResult
fun MaterialDialog.getInputLayout(): TextInputLayout? {
    return this.contentCustomView as? TextInputLayout
}

@CheckResult
fun MaterialDialog.getInputField(): EditText? {
    return getInputLayout()?.editText
}

fun MaterialDialog.input(
        hint: String? = null,
        @StringRes hintRes: Int? = null,
        prefill: CharSequence? = null,
        @StringRes prefillRes: Int? = null,
        inputType: Int = android.text.InputType.TYPE_CLASS_TEXT,
        maxLength: Int? = null,
        waitForPositiveButton: Boolean = true,
        callback: InputCallback = null
): MaterialDialog {
    customView(R.layout.md_dialog_stub_input)
    onPreShow { showKeyboardIfApplicable() }
    if (!hasActionButtons()) {
        positiveButton(android.R.string.ok)
    }

    if (callback != null && waitForPositiveButton) {
        // Add an additional callback to invoke the input listener after the positive AB is pressed
        positiveButton { callback.invoke(this@input, getInputField()!!.text) }
    }

    if (maxLength != null) {
        getInputLayout()!!.apply {
            isCounterEnabled = true//显示计数器
            counterMaxLength = maxLength
        }
    }

    val editText = getInputField()!!

    editText.textChanged {
        if (maxLength != null)
            invalidateInputMaxLength()
        else
            getActionButton(WhichButton.POSITIVE).isEnabled = it.isNotEmpty()
        if (!waitForPositiveButton && callback != null) {
            callback.invoke(this, it)
        }
    }
    val resources = context.resources
    editText.setText(
            prefill ?: if (prefillRes != null) resources.getString(prefillRes) else null
    )
    editText.hint = hint ?: if (hintRes != null) resources.getString(hintRes) else null
    editText.inputType = inputType

    return this
}

internal fun MaterialDialog.showKeyboardIfApplicable() {
    val editText = getInputField() ?: return
    editText.post {
        editText.requestFocus()
        val imm =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }
}

internal fun MaterialDialog.invalidateInputMaxLength() {
    val maxLength = getInputLayout()!!.counterMaxLength
    val currentLength = getInputField()!!.text.length
    if (maxLength > 0) {
        getActionButton(WhichButton.POSITIVE).isEnabled = currentLength <= maxLength && currentLength != 0
    }
}
