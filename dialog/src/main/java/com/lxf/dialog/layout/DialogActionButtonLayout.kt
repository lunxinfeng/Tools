package com.lxf.dialog.layout

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.lxf.dialog.*

/**
 * dialog底部布局
 */
internal class DialogActionButtonLayout(
        context: Context,
        attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

    companion object {
        const val INDEX_POSITIVE = 0
        const val INDEX_NEGATIVE = 1
        const val INDEX_NEUTRAL = 2
    }

    private val buttonHeightDefault = dimenPx(R.dimen.md_action_button_height)
    private val buttonHeightStacked = dimenPx(R.dimen.md_stacked_action_button_height)
    private val buttonFramePadding = dimenPx(R.dimen.md_action_button_frame_padding)
    private val buttonFramePaddingNeutral = dimenPx(R.dimen.md_action_button_frame_padding_neutral)
    private val buttonSpacing = dimenPx(R.dimen.md_action_button_spacing)

    /**
     * 是否堆叠按钮
     */
    private var stackButtons: Boolean = false
    /**
     * 底部的操作按钮
     */
    lateinit var actionButtons: Array<DialogActionButton>
    /**
     * 三个操作按钮中可见的按钮集合
     */
    val visibleButtons:Array<DialogActionButton>
        get() = actionButtons.filter { it.isVisible() }.toTypedArray()
    /**
     * 是否所有元素不可见
     */
    val isAllElementGone:Boolean
        get() = visibleButtons.isEmpty()

    override fun onFinishInflate() {
        super.onFinishInflate()
        actionButtons = arrayOf(
                findViewById(R.id.md_button_positive),
                findViewById(R.id.md_button_negative),
                findViewById(R.id.md_button_neutral)
        )

        for ((i,btn) in actionButtons.withIndex()){
            val which = WhichButton.get(i)
            btn.setOnClickListener { (parent as DialogLayout).dialog.onActionButtonClicked(which) }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isAllElementGone){
            setMeasuredDimension(0,0)
            return
        }

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)

        //----测量子view----
        for (button in visibleButtons){
            if (stackButtons){
                button.measure(
                        MeasureSpec.makeMeasureSpec(parentWidth,MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(buttonHeightStacked,MeasureSpec.EXACTLY)
                )
            }else{
                button.measure(
                        MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(buttonHeightDefault,MeasureSpec.EXACTLY)
                )
            }
        }

        if (visibleButtons.isNotEmpty() && !stackButtons) {
            var totalWidth = 0
            for (button in visibleButtons) {
                totalWidth += button.measuredWidth + buttonSpacing
            }
            if (totalWidth >= parentWidth) {
                stackButtons = true
            }
        }

        val totalHeight =  when {
            visibleButtons.isEmpty() -> 0
            stackButtons -> (visibleButtons.size * buttonHeightStacked) + buttonFramePadding
            else -> buttonHeightDefault + (buttonFramePadding * 2)
        }

        setMeasuredDimension(parentWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (isAllElementGone) return

        if (stackButtons) {
            var topY = 0
            for (button in visibleButtons) {
                val bottomY = topY + buttonHeightStacked
                button.layout(0, topY, measuredWidth, bottomY)
                topY = bottomY
            }
        }else {
            val topY = buttonFramePadding
            val bottomY = measuredHeight - buttonFramePadding

            if (actionButtons[INDEX_NEUTRAL].isVisible()) {
                val btn = actionButtons[INDEX_NEUTRAL]
                val leftX = buttonFramePaddingNeutral
                btn.layout(leftX,topY,leftX + btn.measuredWidth,bottomY)
            }

            var rightX = measuredWidth - buttonFramePadding
            if (actionButtons[INDEX_POSITIVE].isVisible()) {
                val btn = actionButtons[INDEX_POSITIVE]
                val leftX = rightX - btn.measuredWidth
                btn.layout(leftX, topY, rightX, bottomY)
                rightX = leftX - buttonSpacing
            }
            if (actionButtons[INDEX_NEGATIVE].isVisible()) {
                val btn = actionButtons[INDEX_NEGATIVE]
                val leftX = rightX - btn.measuredWidth
                btn.layout(leftX, topY, rightX, bottomY)
            }
        }
    }

}