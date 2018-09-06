package com.lxf.dialog

import android.animation.AnimatorSet
import android.animation.TimeInterpolator
import android.view.View
import android.view.animation.LinearInterpolator


abstract class BaseAnim {
    var duration: Long = 500
    var interpolator: TimeInterpolator = LinearInterpolator()

    internal var animatorSet: AnimatorSet = AnimatorSet()

    protected abstract fun setupAnimation(view: View)

    fun start(view: View) {
        animatorSet.interpolator = interpolator
        reset(view)
        setupAnimation(view)
        animatorSet.start()
    }

    private fun reset(view: View) {
        view.pivotX = view.measuredWidth / 2.0f
        view.pivotY = view.measuredHeight / 2.0f
    }
}