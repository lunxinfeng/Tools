package com.lxf.tools.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.lxf.tools.R
import kotlinx.android.synthetic.main.activity_slide_view.*

class SlideViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_view)

        button.setOnClickListener { println("SlideViewActivity button click") }
    }
}
