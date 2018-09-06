package com.lxf.dialog.anim

import android.animation.ObjectAnimator
import android.view.View
import com.lxf.dialog.BaseAnim


class FadeIn: BaseAnim() {
    override fun setupAnimation(view: View) {
        animatorSet.playTogether(
                ObjectAnimator
                        .ofFloat(view, "alpha", 0f, 1f)
                        .setDuration(duration)
        )
    }
}