/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.lxf.dialog.listAdapter

import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import com.lxf.dialog.FontConfig
import com.lxf.dialog.MaterialDialog
import com.lxf.dialog.R
import com.lxf.dialog.WhichButton
import com.lxf.dialog.ext.*

internal class SingleChoiceViewHolder(
  itemView: View,
  private val adapter: SingleChoiceDialogAdapter
) : RecyclerView.ViewHolder(itemView), OnClickListener {

  init {
    itemView.setOnClickListener(this)
  }

  val controlView: AppCompatRadioButton = itemView.findViewById(R.id.md_control)
  val titleView: TextView = itemView.findViewById(R.id.md_title)

  var isEnabled: Boolean
    get() = itemView.isEnabled
    set(value) {
      itemView.isEnabled = value
      controlView.isEnabled = value
      titleView.isEnabled = value
    }

  override fun onClick(view: View) = adapter.itemClicked(adapterPosition)
}

/**
 * The default list adapter for single choice (radio button) list dialogs.
 *
 * @author Aidan Follestad (afollestad)
 */
internal class SingleChoiceDialogAdapter(
        private var dialog: MaterialDialog,
        internal var items: List<String>,
        disabledItems: IntArray?,
        initialSelection: Int,
        private val fontConfig: FontConfig?,
        private val waitForActionButton: Boolean,
        internal var selection: SingleChoiceListener
) : RecyclerView.Adapter<SingleChoiceViewHolder>(), DialogAdapter<String, SingleChoiceListener> {

  private var currentSelection: Int = initialSelection
    set(value) {
      val previousSelection = field
      field = value
      notifyItemChanged(previousSelection)
      notifyItemChanged(value)
    }
  private var disabledIndices: IntArray = disabledItems ?: IntArray(0)

  internal fun itemClicked(index: Int) {
    this.currentSelection = index
    if (waitForActionButton && dialog.hasActionButtons()) {
      dialog.getActionButton(WhichButton.POSITIVE).isEnabled = true
    } else {
      this.selection?.invoke(dialog, index, this.items[index])
      if (dialog.autoDismissEnabled && !dialog.hasActionButtons()) {
        dialog.dismiss()
      }
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): SingleChoiceViewHolder {
    val listItemView: View = parent.inflate(dialog.context, R.layout.md_listitem_singlechoice)
    return SingleChoiceViewHolder(
        itemView = listItemView,
        adapter = this
    )
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(
    holder: SingleChoiceViewHolder,
    position: Int
  ) {
    holder.isEnabled = !disabledIndices.contains(position)

    holder.controlView.isChecked = currentSelection == position
    holder.titleView.text = items[position]
    holder.itemView.background = getDrawable(dialog.context,res = R.drawable.md_item_selector)

    fontConfig?.let { holder.titleView.config(it) }
  }

  override fun positiveButtonClicked() {
    if (currentSelection > -1) {
      selection?.invoke(dialog, currentSelection, items[currentSelection])
    }
  }

  override fun replaceItems(
    items: List<String>,
    listener: SingleChoiceListener
  ) {
    this.items = items
    this.selection = listener
    this.notifyDataSetChanged()
  }

  override fun disableItems(indices: IntArray) {
    this.disabledIndices = indices
    notifyDataSetChanged()
  }
}