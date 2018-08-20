package com.lxf.tools

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lxf.recyclerhelper.BaseQuickAdapter
import com.lxf.recyclerhelper.BaseViewHolder
import com.lxf.tools.util.AppUtil
import kotlinx.android.synthetic.main.activity_main.*
import com.lxf.tools.net_hint.BaseActivity


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        button.setOnClickListener { AppUtil.killBackgroundApp(this,"com.eg.android.AlipayGphone") }
        val adapter = Adapter(R.layout.item_recycler_main,null)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = adapter.apply {
                setEmptyView(R.layout.empty_view_recycler_main,recyclerView)
            }
        }
        button.setOnClickListener { adapter.setNewData(listOf("1","2","3")) }
    }

    class Adapter(layoutId:Int,data:List<String>?): BaseQuickAdapter<String, BaseViewHolder>(layoutId,data) {
        override fun convert(helper: BaseViewHolder?, item: String?) {
            helper?.setText(R.id.tv_content,item?:"")
        }
    }
}
