package com.lxf.dialog.anim

import android.animation.ObjectAnimator
import android.view.View
import com.lxf.dialog.BaseAnim


class ShakeIn: BaseAnim() {
    override fun setupAnimation(view: View) {
        animatorSet.playTogether(
                ObjectAnimator
                        .ofFloat(view, "translationX", 0f, .10f, -25f, .26f, 25f, .42f, -25f, .58f, 25f, .74f, -25f, .90f, 1f, 0f)
                        .setDuration(duration)
        )
    }
}