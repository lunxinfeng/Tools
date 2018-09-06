package com.lxf.tools.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.lxf.dialog.MaterialDialog
import com.lxf.tools.R
import kotlinx.android.synthetic.main.activity_dialog.*

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        btnBaseDialog.setOnClickListener { baseDialog() }
    }

    private fun baseDialog(){
        MaterialDialog(this)
                .title(text = "BaseDialog")
                .message(text = "I am a BaseDialog!")
                .positiveButton(text = "确定")
                .negativeButton(text = "取消")
                .show()
    }
}
