package com.lxf.dialog.anim

import android.animation.ObjectAnimator
import android.view.View
import com.lxf.dialog.BaseAnim


class SlideTopOut: BaseAnim() {
    override fun setupAnimation(view: View) {
        animatorSet.playTogether(
                ObjectAnimator
                        .ofFloat(view, "translationY", 0f, -300f)
                        .setDuration(duration),
                ObjectAnimator
                        .ofFloat(view, "alpha", 1f, 0f)
                        .setDuration(duration * 3 / 2)
        )
    }
}