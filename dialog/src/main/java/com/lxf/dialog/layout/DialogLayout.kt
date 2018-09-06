package com.lxf.dialog.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.lxf.dialog.MaterialDialog
import com.lxf.dialog.R


class DialogLayout(
        context: Context,
        attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    var maxHeight: Int = 0
    internal lateinit var dialog: MaterialDialog
    internal lateinit var titleLayout: DialogTitleLayout
    internal val contentView: View
        get() = getChildAt(1)
    internal lateinit var buttonsLayout: DialogActionButtonLayout

    override fun onFinishInflate() {
        super.onFinishInflate()
        titleLayout = findViewById(R.id.md_title_layout)
        buttonsLayout = findViewById(R.id.md_button_layout)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidth = MeasureSpec.getSize(widthMeasureSpec)
        var specHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (specHeight > maxHeight) {
            specHeight = maxHeight
        }

        titleLayout.measure(
                MeasureSpec.makeMeasureSpec(specWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        if (!buttonsLayout.isAllElementGone) {
            buttonsLayout.measure(
                    MeasureSpec.makeMeasureSpec(specWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
        }

        val titleAndButtonsHeight =
                titleLayout.measuredHeight + buttonsLayout.measuredHeight
        val remainingHeight = specHeight - titleAndButtonsHeight
        contentView.measure(
                MeasureSpec.makeMeasureSpec(specWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(remainingHeight, MeasureSpec.AT_MOST)
        )

        val totalHeight = titleLayout.measuredHeight +
                contentView.measuredHeight +
                buttonsLayout.measuredHeight
        setMeasuredDimension(specWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val titleLeft = 0
        val titleTop = 0
        val titleRight = measuredWidth
        val titleBottom = titleLayout.measuredHeight
        titleLayout.layout(
                titleLeft,
                titleTop,
                titleRight,
                titleBottom
        )

        val buttonsTop =
                measuredHeight - buttonsLayout.measuredHeight
        if (!buttonsLayout.isAllElementGone) {
            val buttonsLeft = 0
            val buttonsRight = measuredWidth
            val buttonsBottom = measuredHeight
            buttonsLayout.layout(
                    buttonsLeft,
                    buttonsTop,
                    buttonsRight,
                    buttonsBottom
            )
        }

        val contentLeft = 0
        val contentRight = measuredWidth
        contentView.layout(
                contentLeft,
                titleBottom,
                contentRight,
                buttonsTop
        )
    }
}