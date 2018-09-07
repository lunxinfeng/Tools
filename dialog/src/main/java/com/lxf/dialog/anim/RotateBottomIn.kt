package com.lxf.dialog.anim

import android.animation.ObjectAnimator
import android.view.View
import com.lxf.dialog.BaseAnim


class RotateBottomIn: BaseAnim() {
    override fun setupAnimation(view: View) {
        animatorSet.playTogether(
                ObjectAnimator
                        .ofFloat(view, "rotationX", 90f, 0f)
                        .setDuration(duration),
                ObjectAnimator
                        .ofFloat(view, "translationY", 200f, 0f)
                        .setDuration(duration),
                ObjectAnimator
                        .ofFloat(view, "alpha", 0f, 1f)
                        .setDuration(duration)
        )
    }
}