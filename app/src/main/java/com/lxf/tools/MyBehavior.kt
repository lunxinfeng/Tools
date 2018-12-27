package com.lxf.tools

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View


class MyBehavior(
        context:Context,
        attrs:AttributeSet
): CoordinatorLayout.Behavior<RecyclerView>(context, attrs){
    override fun layoutDependsOn(parent: CoordinatorLayout?, child: RecyclerView?, dependency: View?): Boolean {
        println("layoutDependsOn：${child?.id == R.id.recyclerView_bg}")
        return child?.id == R.id.recyclerView_bg
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: RecyclerView?, dependency: View?): Boolean {
        println("onDependentViewChanged")
        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: RecyclerView, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        println("onStartNestedScroll")
        return axes == ViewCompat.SCROLL_AXIS_HORIZONTAL
    }

    override fun onNestedScrollAccepted(coordinatorLayout: CoordinatorLayout, child: RecyclerView, directTargetChild: View, target: View, axes: Int, type: Int) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type)
        println("onNestedScrollAccepted")
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: RecyclerView, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        println("onNestedScroll")
        child.scrollBy(dxConsumed,dyConsumed)
    }
}