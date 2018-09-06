package com.lxf.dialog.layout

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lxf.dialog.R
import com.lxf.dialog.dimenPx
import com.lxf.dialog.isVisible

/**
 * dialog顶部布局
 */
class DialogTitleLayout(
        context: Context,
        attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

    private val frameMarginVertical = dimenPx(R.dimen.md_dialog_frame_margin_vertical)
    private val titleMarginBottom = dimenPx(R.dimen.md_dialog_title_layout_margin_bottom)
    private val frameMarginHorizontal = dimenPx(R.dimen.md_dialog_frame_margin_horizontal)

    private val iconMargin = dimenPx(R.dimen.md_icon_margin)
    private val iconSize = dimenPx(R.dimen.md_icon_size)

    lateinit var iconView: ImageView
    lateinit var titleView: TextView

    /**
     * 是否所有元素不可见
     */
    val isAllElementGone:Boolean
        get() = !(iconView.isVisible() || titleView.isVisible())

    override fun onFinishInflate() {
        super.onFinishInflate()
        iconView = findViewById(R.id.md_icon_title)
        titleView = findViewById(R.id.md_text_title)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isAllElementGone){
            setMeasuredDimension(0,0)
            return
        }

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        var titleMaxWidth = parentWidth - (frameMarginHorizontal * 2)

        if (iconView.isVisible()) {
            iconView.measure(
                    MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY)
            )
            titleMaxWidth -= iconView.measuredWidth
        }

        titleView.measure(
                MeasureSpec.makeMeasureSpec(titleMaxWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val iconViewHeight = if (iconView.isVisible()) iconView.measuredHeight else 0
        val requiredHeight = Math.max(iconViewHeight, titleView.measuredHeight)
        val actualHeight = requiredHeight + frameMarginVertical + titleMarginBottom

        setMeasuredDimension(parentWidth,actualHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (isAllElementGone) return

        var titleLeft = frameMarginHorizontal
        val titleBottom = measuredHeight - titleMarginBottom
        val titleTop = titleBottom - titleView.measuredHeight
        val titleRight = titleLeft + titleView.measuredWidth


        if (iconView.isVisible()) {
            val titleHalfHeight = (titleBottom - titleTop) / 2
            val titleMidPoint = titleBottom - titleHalfHeight
            val iconHalfHeight = iconView.measuredHeight / 2

            val iconLeft: Int = titleLeft
            val iconTop = titleMidPoint - iconHalfHeight
            val iconRight = iconLeft + iconView.measuredWidth
            val iconBottom = iconTop + iconView.measuredHeight
            titleLeft = iconRight + iconMargin

            iconView.layout(iconLeft, iconTop, iconRight, iconBottom)
        }

        titleView.layout(titleLeft, titleTop, titleRight, titleBottom)
    }
}