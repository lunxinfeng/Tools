package com.lxf.dialog.ext

import android.support.annotation.StringRes
import android.view.View
import android.widget.CheckBox
import com.lxf.dialog.MaterialDialog

typealias BooleanCallback = ((MaterialDialog,Boolean) -> Unit)?

fun MaterialDialog.getCheckBoxPrompt(): CheckBox {
  return view.buttonsLayout.checkBoxPrompt
}

fun MaterialDialog.isCheckPromptChecked() = getCheckBoxPrompt().isChecked

/**
 * @param res The string resource to display for the checkbox label.
 * @param text The literal string to display for the checkbox label.
 * @param isCheckedDefault Whether or not the checkbox is initially checked.
 * @param onToggle A listener invoked when the checkbox is checked or unchecked.
 */
fun MaterialDialog.checkBoxPrompt(
  @StringRes res: Int = 0,
  text: String? = null,
  isCheckedDefault: Boolean = false,
  onToggle: BooleanCallback
): MaterialDialog {
  atLeastOneIsNotNull("checkBoxPrompt", text, res)
  view.buttonsLayout.checkBoxPrompt.apply {
    this.visibility = View.VISIBLE
    this.text = text ?: getString(res)
    this.isChecked = isCheckedDefault
    this.setOnCheckedChangeListener { _, checked ->
      onToggle?.invoke(this@checkBoxPrompt,checked)
    }
  }
  return this
}