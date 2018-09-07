package com.lxf.dialog.listAdapter

import android.support.v7.widget.AppCompatCheckBox
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

internal class MultiChoiceViewHolder(
  itemView: View,
  private val adapter: MultiChoiceDialogAdapter
) : RecyclerView.ViewHolder(itemView), OnClickListener {
  init {
    itemView.setOnClickListener(this)
  }

  val controlView: AppCompatCheckBox = itemView.findViewById(R.id.md_control)
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

internal class MultiChoiceDialogAdapter(
        private var dialog: MaterialDialog,
        internal var items: List<String>,
        disabledItems: IntArray?,
        initialSelection: IntArray,
        private val fontConfig: FontConfig?,
        private val waitForActionButton: Boolean,
        internal var selection: MultiChoiceListener
) : RecyclerView.Adapter<MultiChoiceViewHolder>(), DialogAdapter<String, MultiChoiceListener> {

  private var currentSelection: IntArray = initialSelection
    set(value) {
      val previousSelection = field
      field = value
      for (previous in previousSelection) {
        if (!value.contains(previous)) {
          notifyItemChanged(previous)
        }
      }
      for (current in value) {
        if (!previousSelection.contains(current)) {
          notifyItemChanged(current)
        }
      }
    }
  private var disabledIndices: IntArray = disabledItems ?: IntArray(0)

  internal fun itemClicked(index: Int) {
    val newSelection = this.currentSelection.toMutableList()
    if (newSelection.contains(index)) {
      newSelection.remove(index)
    } else {
      newSelection.add(index)
    }
    this.currentSelection = newSelection.toIntArray()

    if (waitForActionButton && dialog.hasActionButtons()) {
      dialog.getActionButton(WhichButton.POSITIVE).isEnabled = true
    } else {
      val selectedItems = this.items.pullIndices(this.currentSelection)
      this.selection?.invoke(dialog, this.currentSelection, selectedItems)
      if (dialog.autoDismissEnabled && !dialog.hasActionButtons()) {
        dialog.dismiss()
      }
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): MultiChoiceViewHolder {
    val listItemView: View = parent.inflate(dialog.context, R.layout.md_listitem_multichoice)
    return MultiChoiceViewHolder(
        itemView = listItemView,
        adapter = this
    )
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(
    holder: MultiChoiceViewHolder,
    position: Int
  ) {
    holder.isEnabled = !disabledIndices.contains(position)

    holder.controlView.isChecked = currentSelection.contains(position)
    holder.titleView.text = items[position]
    holder.itemView.background = getDrawable(dialog.context,res = R.drawable.md_item_selector)

    fontConfig?.let { holder.titleView.config(it) }
  }

  override fun positiveButtonClicked() {
    if (currentSelection.isNotEmpty()) {
      val selectedItems = items.pullIndices(currentSelection)
      selection?.invoke(dialog, currentSelection, selectedItems)
    }
  }

  override fun replaceItems(
    items: List<String>,
    listener: MultiChoiceListener
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