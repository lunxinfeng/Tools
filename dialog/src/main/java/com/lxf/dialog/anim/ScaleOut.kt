package com.lxf.dialog.anim

import android.animation.ObjectAnimator
import android.view.View
import com.lxf.dialog.BaseAnim


class ScaleOut: BaseAnim() {
    override fun setupAnimation(view: View) {
        animatorSet.playTogether(
                ObjectAnimator
                        .ofFloat(view, "ScaleX", 1f, 0f)
                        .setDuration(duration),
                ObjectAnimator
                        .ofFloat(view, "ScaleY", 1f, 0f)
                        .setDuration(duration)
        )
    }
}