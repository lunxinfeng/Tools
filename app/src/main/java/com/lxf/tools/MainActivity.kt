package com.lxf.tools

import android.os.Bundle
import com.lxf.tools.util.AppUtil
import kotlinx.android.synthetic.main.activity_main.*
import com.lxf.tools.net_hint.BaseActivity


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { AppUtil.killBackgroundApp(this,"com.eg.android.AlipayGphone") }
    }
}
