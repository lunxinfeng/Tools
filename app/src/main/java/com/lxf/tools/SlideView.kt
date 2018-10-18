package com.lxf.tools

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Scroller


const val SLIDE_STATUS_OFF = 0
const val SLIDE_STATUS_START_SCROLL = 1
const val SLIDE_STATUS_ON = 2

class SlideView(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ):this(context, attrs, defStyleAttr, 0)

    constructor(
            context: Context,
            attrs: AttributeSet?
    ):this(context, attrs, 0, 0)

    constructor(
            context: Context
    ):this(context, null, 0, 0)

    private val TAG = javaClass.simpleName
    private val content:LinearLayout
    private val menu:LinearLayout
    private val scroller:Scroller
    private var menuWidth = 0 //菜单宽度
    private var velocityTracker:VelocityTracker? = null//测量速度
    private var onSlideListener:OnSlideListener? = null


    init {
        View.inflate(context,R.layout.slide_view,this)
        content = findViewById(R.id.content)
        menu = findViewById(R.id.menu)

        scroller = Scroller(context)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        menuWidth = menu.measuredWidth
        Log.d(TAG,"menuWidth=$menuWidth")
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var intercepted = false
        val x = event.x
        val y = event.y

        if (velocityTracker == null)
            velocityTracker = VelocityTracker.obtain()
        velocityTracker!!.addMovement(event)

        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                if (!scroller.isFinished)
                    scroller.abortAnimation()

                onSlideListener?.onSlide(content, SLIDE_STATUS_START_SCROLL)

                intercepted = true
            }
            MotionEvent.ACTION_UP -> {
                fastSlideTo()

                velocityTracker?.let {
                    it.recycle()
                    velocityTracker = null
                }

                intercepted = false
            }
        }

        return intercepted
    }

    private fun fastSlideTo(){
        velocityTracker!!.computeCurrentVelocity(100)
        val xVelocity = velocityTracker!!.xVelocity

        var newScrollX = 0
        if (scrollX > menuWidth * 0.75 || xVelocity < -100){
            newScrollX = menuWidth
        }else if (scrollX <= menuWidth * 0.75 || xVelocity > 100){
            newScrollX = 0
        }

        smoothScrollTo(newScrollX,0)

        onSlideListener?.onSlide(content,if (newScrollX == 0) SLIDE_STATUS_OFF else SLIDE_STATUS_ON)
    }

    private fun smoothScrollTo(destX:Int,destY:Int){
        val delta = destX - scrollX
        scroller.startScroll(scrollX,0,delta,0,Math.abs(delta) * 3)
        invalidate()
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.currX,scroller.currY)
            postInvalidate()
        }
    }


    interface OnSlideListener{
        fun onSlide(view:View,status:Int)
    }
}