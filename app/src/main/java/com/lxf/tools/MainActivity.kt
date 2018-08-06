package com.lxf.tools

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.lxf.tools.util.AppUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { AppUtil.killBackgroundApp(this,"com.eg.android.AlipayGphone") }
    }
}
