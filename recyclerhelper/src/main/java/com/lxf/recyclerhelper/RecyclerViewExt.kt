package com.lxf.recyclerhelper

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


fun RecyclerView.attachToItemTouchHelper(itemTouchHelperCallback: ItemTouchHelper.Callback){
    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
}