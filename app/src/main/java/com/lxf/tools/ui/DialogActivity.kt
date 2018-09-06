package com.lxf.tools.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.BounceInterpolator
import com.lxf.dialog.MaterialDialog
import com.lxf.dialog.anim.*
import com.lxf.tools.R
import kotlinx.android.synthetic.main.activity_dialog.*

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        btnBaseDialog.setOnClickListener { baseDialog() }
        btnAnimDialog.setOnClickListener { animDialog() }
    }

    private fun baseDialog(){
        MaterialDialog(this)
                .title(text = "BaseDialog")
                .message(text = "I am a BaseDialog!")
                .positiveButton(text = "确定")
                .negativeButton(text = "取消")
                .show()
    }

    private fun animDialog(){
        MaterialDialog(this)
                .title(text = "AnimDialog")
                .message(text = "I am a AnimDialog!")
                .positiveButton(text = "确定")
                .negativeButton(text = "取消")
                .animOnShow(ScaleIn(),interpolator = BounceInterpolator())
                .animOnDismiss(ScaleOut(),duration = 300L)
                .show()
    }
}
