package com.lxf.dialog.ext

import android.support.annotation.ArrayRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.lxf.dialog.FontConfig
import com.lxf.dialog.MaterialDialog
import com.lxf.dialog.R
import com.lxf.dialog.listAdapter.PlainListDialogAdapter

typealias ItemListener =
        ((dialog: MaterialDialog, index: Int, text: String) -> Unit)?

fun MaterialDialog.getSelectedItem():Any?{
    return when(listAdapter){
        is PlainListDialogAdapter -> (listAdapter as PlainListDialogAdapter).selectItem
        else -> null
    }
}

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

