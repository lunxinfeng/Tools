package com.lxf.recyclerhelper

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

typealias OnMove = (viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder) -> Unit
typealias OnSwipe = (viewHolder: RecyclerView.ViewHolder, direction: Int) -> Unit

class ItemTouchHelperCallback(
        private val dragFlags:Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        private val swipeFlags:Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        private val onMove: OnMove? = null,
        private val onSwipe: OnSwipe? = null
): ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        return makeMovementFlags(dragFlags,swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        if (viewHolder!=null && target!=null)
            onMove?.invoke(viewHolder,target)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        viewHolder?.let { onSwipe?.invoke(viewHolder, direction) }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }
}