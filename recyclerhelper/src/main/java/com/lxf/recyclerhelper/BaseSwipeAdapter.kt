package com.lxf.recyclerhelper

import android.support.annotation.LayoutRes
import java.util.*

private interface ItemTouchHelperListener{
    fun onMove(fromPosition: Int,toPosition: Int)
    fun onSwipe(position: Int,direction: Int)
}


abstract class BaseSwipeAdapter<T, VH : BaseViewHolder>(
        @LayoutRes layoutId:Int,
        data:MutableList<T>?
): BaseQuickAdapter<T, VH>(layoutId,data),ItemTouchHelperListener{
    fun isItemView(position: Int):Boolean{
        val type = getItemViewType(position)
        return type != HEADER_VIEW && type != FOOTER_VIEW && type != EMPTY_VIEW && type != ERROR_VIEW
    }
}



abstract class SimpleSwipeAdapter<T, VH : BaseViewHolder>(
        @LayoutRes layoutId:Int,
        data:MutableList<T>?
):BaseSwipeAdapter<T, VH>(layoutId,data) {
    override fun onMove(fromPosition: Int, toPosition: Int) {
        if (isItemView(fromPosition) && isItemView(toPosition)){
            Collections.swap(data,fromPosition,toPosition)
            notifyItemMoved(fromPosition,toPosition)
        }
    }

    override fun onSwipe(position: Int,direction: Int) {
        if (isItemView(position)){
            data?.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}