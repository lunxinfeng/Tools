package com.lxf.rxpermissions

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.FragmentManager
import io.reactivex.Observable


internal fun getPermissionFragment(fragmentManager: FragmentManager):PermissionFragment{
    var fragment = fragmentManager.findFragmentByTag(RxPermissions.TAG_FRAGMENT) as? PermissionFragment
    if (fragment == null){
        fragment = PermissionFragment()
        fragmentManager.beginTransaction()
                .add(fragment,RxPermissions.TAG_FRAGMENT)
                .commitNow()
    }
    return fragment
}

internal fun RxPermissions.requestImplementation(vararg permissions: String):Observable<Permission>{
    val list = mutableListOf<Observable<Permission>>()
    val unRequestPermissions = mutableListOf<String>()

    for (item in permissions){
        if (isGranted(item)){
            list.add(Observable.just(Permission(item,true,false)))
            continue
        }

        if (isRevoked(item)) {
            // Revoked by a policy, return a denied Permission object.
            list.add(Observable.just(Permission(item, false, false)))
            continue
        }

        if (fragment.getSubject(item) == null) {
            unRequestPermissions.add(item)
            fragment.addSubjectForPermission(item)
        }
        list.add(fragment.getSubject(item)!!)
    }

    if (unRequestPermissions.isNotEmpty()){
        fragment.requestPermissions(*unRequestPermissions.toTypedArray())
    }
    return Observable.concat(Observable.fromIterable(list))
}

@TargetApi(Build.VERSION_CODES.M)
internal fun RxPermissions.isGranted(permission:String) = !isMarshmallow()
        || fragment.activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

@TargetApi(Build.VERSION_CODES.M)
internal fun RxPermissions.isRevoked(permission: String) = isMarshmallow()
        && fragment.activity.let { it.packageManager.isPermissionRevokedByPolicy(permission,it.packageName) }

internal fun isMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M