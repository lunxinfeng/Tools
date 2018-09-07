package com.lxf.dialog.ext

import android.support.annotation.ArrayRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.lxf.dialog.FontConfig
import com.lxf.dialog.MaterialDialog
import com.lxf.dialog.R
import com.lxf.dialog.WhichButton
import com.lxf.dialog.listAdapter.MultiChoiceDialogAdapter
import com.lxf.dialog.listAdapter.PlainListDialogAdapter
import com.lxf.dialog.listAdapter.SingleChoiceDialogAdapter

typealias ItemListener =
        ((dialog: MaterialDialog, index: Int, text: String) -> Unit)?
typealias SingleChoiceListener =
        ((dialog: MaterialDialog, index: Int, text: String) -> Unit)?
typealias MultiChoiceListener =
        ((dialog: MaterialDialog, indices: IntArray, items: List<String>) -> Unit)?

fun MaterialDialog.listItems(
        @ArrayRes res: Int? = null,
        items: List<String>? = null,
        fontConfig: FontConfig? = null,
        disabledIndices: IntArray? = null,
        waitForPositiveButton: Boolean = true,
        selection: ItemListener = null
): MaterialDialog {
    atLeastOneIsNotNull("listItems", items, res)
    val array = items ?: getStringArray(res)?.toList()
    val adapter = listAdapter

    if (adapter is PlainListDialogAdapter) {
        if (array != null) {
            adapter.replaceItems(array, selection)
        }
        if (disabledIndices != null) {
            adapter.disableItems(disabledIndices)
        }
        return this
    }

    return customListAdapter(
            PlainListDialogAdapter(
                    dialog = this,
                    items = array!!,
                    fontConfig = fontConfig,
                    disabledItems = disabledIndices,
                    waitForActionButton = waitForPositiveButton,
                    selection = selection
            )
    )
}

fun MaterialDialog.listItemsSingleChoice(
        @ArrayRes res: Int? = null,
        items: List<String>? = null,
        disabledIndices: IntArray? = null,
        initialSelection: Int = -1,
        fontConfig: FontConfig? = null,
        waitForPositiveButton: Boolean = true,
        selection: SingleChoiceListener = null
): MaterialDialog {
    val array = items ?: getStringArray(res)?.toList()
    val adapter = listAdapter

    if (adapter is SingleChoiceDialogAdapter) {
        if (array != null) {
            adapter.replaceItems(array, selection)
        }
        if (disabledIndices != null) {
            adapter.disableItems(disabledIndices)
        }
        return this
    }

    atLeastOneIsNotNull("listItemsSingleChoice", items, res)
    getActionButton(WhichButton.POSITIVE).isEnabled = initialSelection > -1
    return customListAdapter(
            SingleChoiceDialogAdapter(
                    dialog = this,
                    items = array!!,
                    disabledItems = disabledIndices,
                    initialSelection = initialSelection,
                    fontConfig = fontConfig,
                    waitForActionButton = waitForPositiveButton,
                    selection = selection
            )
    )
}

fun MaterialDialog.listItemsMultiChoice(
        @ArrayRes res: Int? = null,
        items: List<String>? = null,
        disabledIndices: IntArray? = null,
        initialSelection: IntArray = IntArray(0),
        fontConfig: FontConfig? = null,
        waitForPositiveButton: Boolean = true,
        selection: MultiChoiceListener = null
): MaterialDialog {
    val array = items ?: getStringArray(res)?.toList()
    val adapter = listAdapter

    if (adapter is MultiChoiceDialogAdapter) {
        if (array != null) {
            adapter.replaceItems(array, selection)
        }
        if (disabledIndices != null) {
            adapter.disableItems(disabledIndices)
        }
        return this
    }

    atLeastOneIsNotNull("listItemsMultiChoice", items, res)
    getActionButton(WhichButton.POSITIVE).isEnabled = initialSelection.isNotEmpty()
    return customListAdapter(
            MultiChoiceDialogAdapter(
                    dialog = this,
                    items = array!!,
                    disabledItems = disabledIndices,
                    initialSelection = initialSelection,
                    fontConfig = fontConfig,
                    waitForActionButton = waitForPositiveButton,
                    selection = selection
            )
    )
}

fun MaterialDialog.customListAdapter(
        adapter: RecyclerView.Adapter<*>
): MaterialDialog {
    addContentRecyclerView()
    if (this.contentRecyclerView!!.adapter != null)
        throw IllegalStateException("An adapter has already been set to this dialog.")
    this.contentRecyclerView!!.adapter = adapter
    return this
}

private fun MaterialDialog.addContentRecyclerView() {
    if (this.contentScrollView != null || this.contentCustomView != null) {
        throw IllegalStateException(
                "Your dialog has already been setup with a different type " +
                        "(e.g. with a message, input field, etc.)"
        )
    }
    contentRecyclerView?.let { return }

    this.contentRecyclerView = inflate(
            R.layout.md_dialog_stub_recyclerview,
            this.view
    )
//    this.contentRecyclerView!!.attach(this)
    this.contentRecyclerView!!.layoutManager = LinearLayoutManager(context)
    this.view.addView(this.contentRecyclerView, 1)
}

