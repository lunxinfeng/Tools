package com.lxf.dialog.listAdapter

interface DialogAdapter<DATA, SL> {

  fun replaceItems(
    items: List<DATA>,
    listener: SL
  )

  fun disableItems(indices: IntArray)

  fun positiveButtonClicked()
}