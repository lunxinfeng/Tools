package com.lxf.tools.util

import android.app.ActivityManager
import android.content.Context


fun isServiceRunning(context: Context,serviceName: String):Boolean{
    if (serviceName.isEmpty())
        return false

    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningServices = activityManager.getRunningServices(30)
    runningServices.forEach {
        if (it.service.className == serviceName)
            return true
    }
    return false
}