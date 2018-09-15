package com.lxf.rxpermissions

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import io.reactivex.subjects.PublishSubject
import java.util.HashMap


internal class PermissionFragment: Fragment() {

    /**
     * 保存所有的权限请求，一旦有请求结果就将改请求移除，不论是允许还是拒绝
     */
    private val mSubjects = HashMap<String, PublishSubject<Permission>>()
    companion object {
        const val PERMISSION_REQUEST_CODE = 1001
    }

    internal fun requestPermissions(vararg permissions: String){
        requestPermissions(permissions, PERMISSION_REQUEST_CODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //activity重建时保留fragment实例
        retainInstance = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode!= PERMISSION_REQUEST_CODE) return

        val shouldShowRequestPermissionRationale = mutableListOf<Boolean>()
        for ( i in permissions.indices){
            shouldShowRequestPermissionRationale.add(shouldShowRequestPermissionRationale(permissions[i]))
        }

        onPermissionsResult(
                permissions,
                grantResults,
                shouldShowRequestPermissionRationale.toBooleanArray()
        )
    }

    fun onPermissionsResult(
            permissions: Array<out String>,
            grantResults: IntArray,
            shouldShowRequestPermissionRationale:BooleanArray
    ){
        for ((index,permission) in permissions.withIndex()){
            val subject = mSubjects[permission]
            subject?.let{
                mSubjects.remove(permission)
                val granted = grantResults[index] == PackageManager.PERMISSION_GRANTED
                it.onNext(Permission(
                        permission,
                        granted,
                        shouldShowRequestPermissionRationale[index]
                ))
                it.onComplete()
            }
        }
    }

    fun getSubject(permission: String) = mSubjects[permission]

    fun addSubjectForPermission(permission: String){
        mSubjects[permission] = PublishSubject.create()
    }
}