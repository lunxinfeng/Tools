package com.lxf.dialog.ext

import android.animation.Animator
import android.animation.AnimatorSet


internal fun AnimatorSet.onAnimatorEnd(function:(Animator?) -> Unit){
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            function.invoke(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationStart(animation: Animator?) {

        }
    })
}