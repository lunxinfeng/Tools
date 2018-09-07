package com.lxf.tools.ui

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.BounceInterpolator
import com.lxf.dialog.FontConfig
import com.lxf.dialog.MaterialDialog
import com.lxf.dialog.anim.*
import com.lxf.dialog.ext.input
import com.lxf.dialog.ext.listItems
import com.lxf.dialog.ext.listItemsMultiChoice
import com.lxf.dialog.ext.listItemsSingleChoice
import com.lxf.tools.R
import com.lxf.tools.util.toast
import kotlinx.android.synthetic.main.activity_dialog.*

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        btnBaseDialog.setOnClickListener { baseDialog() }
        btnAnimDialog.setOnClickListener { animDialog() }
        btnListBase.setOnClickListener { listDialog() }
        btnSingleChoice.setOnClickListener { singleChoiceDialog() }
        btnMultiChoice.setOnClickListener { multiChoiceDialog() }
        btnInput.setOnClickListener { inputDialog() }
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
//                .animOnShow(ScaleIn(),interpolator = BounceInterpolator())
//                .animOnDismiss(ScaleOut(),duration = 300L)
                .animOnShow(ShakeIn())
                .animOnDismiss(RotateBottomOut())
//                .animOnShow(SlideTopIn())
//                .animOnDismiss(SlideBottomOut())
                .show()
    }

    private fun listDialog(){
        MaterialDialog(this)
                .title(text = "ListDialog")
                .listItems(
                        items = listOf("条目1","条目2","条目3","条目4","条目5"),
                        fontConfig = FontConfig(textColor = Color.RED)
                ){_, _, text ->
                    toast(text)
                }
                .positiveButton(text = "确定")
                .negativeButton(text = "取消")
                .animOnShow(ScaleIn(),interpolator = BounceInterpolator())
                .show()
    }

    private fun singleChoiceDialog(){
        MaterialDialog(this)
                .title(text = "SingleChoiceDialog")
                .listItemsSingleChoice(
                        items = listOf("条目1","条目2","条目3","条目4","条目5"),
                        fontConfig = FontConfig(textColor = Color.RED)
                ){_, _, text ->
                    toast(text)
                }
                .positiveButton(text = "确定")
                .negativeButton(text = "取消")
                .animOnShow(ScaleIn(),interpolator = BounceInterpolator())
                .show()
    }

    private fun multiChoiceDialog(){
        MaterialDialog(this)
                .title(text = "SingleChoiceDialog")
                .listItemsMultiChoice(
                        items = listOf("条目1","条目2","条目3","条目4","条目5","条目6","条目7","条目8","条目9","条目10","条目11","条目12","条目13","条目14"),
                        fontConfig = FontConfig(textColor = Color.RED)
                ){_, _, items ->
                    toast(items.toString())
                }
                .positiveButton(text = "确定")
                .negativeButton(text = "取消")
                .animOnShow(ScaleIn(),interpolator = BounceInterpolator())
                .show()
    }

    private fun inputDialog(){
        MaterialDialog(this)
                .title(text = "SingleChoiceDialog")
                .input(
                        hint = "请输入姓名",
                        maxLength = 12
                ){_, charSequence ->
                    toast(charSequence.toString())
                }
                .positiveButton(text = "确定")
                .negativeButton(text = "取消")
                .animOnShow(SlideTopIn(),interpolator = BounceInterpolator())
                .show()
    }
}
