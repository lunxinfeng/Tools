package com.lxf.tools

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lxf.recyclerhelper.BaseQuickAdapter
import com.lxf.recyclerhelper.BaseViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import com.lxf.tools.net_hint.BaseActivity


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = Adapter(R.layout.item_recycler_main,null)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = adapter.apply {
                showWithAnimation(true)
                setErrorView(R.layout.error_view_recycler_main,recyclerView)
                setEmptyView(R.layout.empty_view_recycler_main,recyclerView)
            }
        }
        btnData.setOnClickListener { adapter.setNewData(listOf("1","2","3")) }
        btnEmpty.setOnClickListener { adapter.setNewData(null) }
        btnError.setOnClickListener { adapter.loadDataFail() }
    }

    class Adapter(layoutId:Int,data:List<String>?): BaseQuickAdapter<String, BaseViewHolder>(layoutId,data) {
        override fun convert(helper: BaseViewHolder?, item: String?) {
            helper?.setText(R.id.tv_content,item?:"")
        }
    }
}
