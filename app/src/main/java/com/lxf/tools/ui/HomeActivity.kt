package com.lxf.tools.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lxf.recyclerhelper.BaseQuickAdapter
import com.lxf.recyclerhelper.BaseViewHolder
import com.lxf.tools.R
import com.lxf.tools.net_hint.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {
    private val adapter = Adapter(R.layout.item_recycler_home)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = this@HomeActivity.adapter
        }

        adapter.setNewData(listOf(
                "RecyclerAdapterHelper",
                "Dialog",
                "Retrofit",
                "SlideView"
        ))
        adapter.setOnItemClickListener { _, _, position ->
            when(position){
                0 -> startActivity(Intent(this@HomeActivity,RecyclerActivity::class.java))
                1 -> startActivity(Intent(this@HomeActivity,DialogActivity::class.java))
                2 -> startActivity(Intent(this@HomeActivity,RetrofitActivity::class.java))
                3 -> startActivity(Intent(this@HomeActivity,SlideViewActivity::class.java))
            }
        }
    }

    inner class Adapter(layoutId:Int,data:List<String>? = null)
        : BaseQuickAdapter<String, BaseViewHolder>(layoutId,data) {
        override fun convert(holder: BaseViewHolder?, item: String?) {
            holder!!.setText(R.id.tv_content,item)
        }
    }
}
