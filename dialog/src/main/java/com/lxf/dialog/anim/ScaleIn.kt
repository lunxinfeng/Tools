package com.lxf.dialog.anim

import android.animation.ObjectAnimator
import android.view.View
import com.lxf.dialog.BaseAnim


class ScaleIn: BaseAnim() {
    override fun setupAnimation(view: View) {
        animatorSet.playTogether(
                ObjectAnimator
                        .ofFloat(view, "ScaleX", 0.1f, 1f)
                        .setDuration(duration),
                ObjectAnimator
                        .ofFloat(view, "ScaleY", 0.1f, 1f)
                        .setDuration(duration)
        )
    }
}