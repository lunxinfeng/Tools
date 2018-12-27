package com.lxf.tools.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import com.lxf.recyclerhelper.BaseQuickAdapter
import com.lxf.recyclerhelper.BaseViewHolder
import com.lxf.tools.R
import kotlinx.android.synthetic.main.activity_game_menu.*

class GameMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_menu)

        val data = IntRange(1,500).toList()

        recyclerView_bg.apply {
            layoutManager = LinearLayoutManager(this@GameMenuActivity,LinearLayoutManager.HORIZONTAL,false)
            adapter = BgAdapter(R.layout.item_recycler_gamemenu_bg,data.toMutableList())
        }
        recyclerView_item.apply {
            layoutManager = LinearLayoutManager(this@GameMenuActivity,LinearLayoutManager.HORIZONTAL,false)
            adapter = ItemAdapter(R.layout.item_recycler_gamemenu_item,data.toMutableList())
        }

    }

    inner class ItemAdapter(
            layoutId: Int,
            data: MutableList<Int>? = null
    ): BaseQuickAdapter<Int, BaseViewHolder>(layoutId,data){
        override fun convert(holder: BaseViewHolder, item: Int) {
            holder.setText(R.id.tv_item,item.toString())
        }
    }

    inner class BgAdapter(
            layoutId: Int,
            data: MutableList<Int>? = null
    ): BaseQuickAdapter<Int, BaseViewHolder>(layoutId,data){
        override fun convert(holder: BaseViewHolder, item: Int) {

        }
    }
}
