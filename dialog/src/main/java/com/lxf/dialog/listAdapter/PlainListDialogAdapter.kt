package com.lxf.dialog.listAdapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import com.lxf.dialog.FontConfig
import com.lxf.dialog.MaterialDialog
import com.lxf.dialog.R
import com.lxf.dialog.ext.*

internal class PlainListViewHolder(
  itemView: View,
  private val adapter: PlainListDialogAdapter
) : RecyclerView.ViewHolder(itemView), OnClickListener {
  init {
    itemView.setOnClickListener(this)
  }

  val titleView = (itemView as ViewGroup).getChildAt(0) as TextView

  override fun onClick(view: View) = adapter.itemClicked(adapterPosition)
}

internal class PlainListDialogAdapter(
  private var dialog: MaterialDialog,
  internal var items: List<String>,
  disabledItems: IntArray?,
  private val fontConfig:FontConfig?,
  private var waitForActionButton: Boolean,
  internal var selection: ItemListener
) : RecyclerView.Adapter<PlainListViewHolder>(), DialogAdapter<String, ItemListener> {

  private var disabledIndices: IntArray = disabledItems ?: IntArray(0)
  private var activatedIndex = -1

  var selectItem:String? = null
      get() = if (activatedIndex == -1) null else items[activatedIndex]

  fun itemClicked(index: Int) {
    if (waitForActionButton && dialog.hasActionButtons()) {
      // Wait for action button, mark clicked item as activated so that we can call the selection
      // listener when the positive action button is pressed.
      val lastActivated = activatedIndex
      activatedIndex = index
      if (lastActivated != -1) {
        notifyItemChanged(lastActivated)
      }
      notifyItemChanged(index)
    } else {
      // Don't wait for action button, call listener and dismiss if auto dismiss is applicable
      this.selection?.invoke(dialog, index, this.items[index])
      if (dialog.autoDismissEnabled && !dialog.hasActionButtons()) {
        dialog.dismiss()
      }
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): PlainListViewHolder {
    val listItemView: View = parent.inflate(dialog.context, R.layout.md_listitem)
    return PlainListViewHolder(
        itemView = listItemView,
        adapter = this
    )
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(
    holder: PlainListViewHolder,
    position: Int
  ) {
    holder.itemView.isEnabled = !disabledIndices.contains(position)

    val titleValue = items[position]
    holder.titleView.text = titleValue
    holder.itemView.background = getDrawable(dialog.context,res = R.drawable.md_item_selector)

    holder.itemView.isActivated = activatedIndex != -1 && activatedIndex == position

    if (fontConfig != null) {
      holder.titleView.config(fontConfig)
    }
  }

  override fun positiveButtonClicked() {
    if (activatedIndex != -1) {
      selection?.invoke(dialog, activatedIndex, items[activatedIndex])
      activatedIndex = -1
    }
  }

  override fun replaceItems(
    items: List<String>,
    listener: ItemListener
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