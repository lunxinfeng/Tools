package com.lxf.rxpermissions

import android.support.v4.app.FragmentActivity
import io.reactivex.Observable


class RxPermissions private constructor(
        activity: FragmentActivity
) {
    internal val fragment: PermissionFragment = getPermissionFragment(activity.supportFragmentManager)

    companion object {
        internal const val TAG_FRAGMENT = "RxPermissions_lxf"
        fun newInstance(activity: FragmentActivity) = RxPermissions(activity)
    }

    fun request(vararg permissions: String): Observable<Boolean> {
        return requestImplementation(*permissions)
                .buffer(permissions.size)
                .flatMap {
                    if (permissions.isEmpty())
                        return@flatMap Observable.empty<Boolean>()

                    return@flatMap Observable.just(it.none { !it.granted })
                }

    }

    fun requestEach(vararg permissions: String):Observable<Permission>{
        return requestImplementation(*permissions)
    }
}