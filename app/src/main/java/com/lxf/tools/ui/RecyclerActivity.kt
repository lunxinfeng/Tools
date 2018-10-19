package com.lxf.tools.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lxf.recyclerhelper.*
import com.lxf.tools.R
import kotlinx.android.synthetic.main.activity_recycler.*
import com.lxf.tools.net_hint.BaseActivity


class RecyclerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        val adapter = Adapter(R.layout.item_recycler_recycler, null)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RecyclerActivity)
            this.adapter = adapter.apply {
                showWithAnimation(true)
                setErrorView(R.layout.error_view_recycler_main,recyclerView)
                setEmptyView(R.layout.empty_view_recycler_main,recyclerView)
            }
            attachToItemTouchHelper(ItemTouchHelperCallback(
                    onMove = { viewHolder,target ->
//                        adapter.onMove(viewHolder.adapterPosition,target.adapterPosition)
                    },
                    onSwipe = { viewHolder,direction ->
//                        adapter.onSwipe(viewHolder.adapterPosition,direction)
                    }
            ))
        }
        btnData.setOnClickListener { adapter.setNewData(mutableListOf("1","2","3")) }
        btnEmpty.setOnClickListener { adapter.setNewData(null) }
        btnError.setOnClickListener { adapter.loadDataFail() }
    }

    class Adapter(layoutId:Int,data:MutableList<String>?): SimpleSwipeAdapter<String, BaseViewHolder>(layoutId,data) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.tv_content,item)
        }
    }
}
