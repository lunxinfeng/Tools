package com.lxf.dialog.anim

import android.animation.ObjectAnimator
import android.view.View
import com.lxf.dialog.BaseAnim


class FadeOut: BaseAnim() {
    override fun setupAnimation(view: View) {
        animatorSet.playTogether(
                ObjectAnimator
                        .ofFloat(view, "alpha", 1f, 0f)
                        .setDuration(duration)
        )
    }
}