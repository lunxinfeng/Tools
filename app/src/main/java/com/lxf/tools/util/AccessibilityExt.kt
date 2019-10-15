package com.lxf.tools.util

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Path
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.text.TextUtils


/**
 * 辅助插件是否打开
 */
fun isAccessibilitySettingsOn(context: Context, clazz:Class<in AccessibilityService> ): Boolean {
    var accessibilityEnabled = 0
    val service = context.packageName + "/" + clazz.canonicalName
    try {
        accessibilityEnabled = Settings.Secure.getInt(context.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED)
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
    }

    val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
    if (accessibilityEnabled == 1) {
        val settingValue = Settings.Secure.getString(context.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        if (settingValue != null) {
            mStringColonSplitter.setString(settingValue)
            while (mStringColonSplitter.hasNext()) {
                val accessibilityService = mStringColonSplitter.next()
                if (accessibilityService.equals(service, ignoreCase = true)) {
                    return true
                }
            }
        }
    }
    return false
}

/**
 * 跳转到开启辅助服务的设置页面
 */
fun toAccessibiitySettingActivity(context: Context){
    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
}

/**
 * 通过AccessibilityService在屏幕上某个位置单击
 */
@RequiresApi(Build.VERSION_CODES.N)
fun AccessibilityService.clickOnScreen(
        x:Float,
        y:Float,
        callback:AccessibilityService.GestureResultCallback,
        handler: Handler? = null
){
    val p = Path()
    p.moveTo(x,y)
    gestureOnScreen(p,callback = callback,handler = handler)
}

/**
 * 通过AccessibilityService在屏幕上模拟手势
 * @param path 手势路径
 */
@RequiresApi(Build.VERSION_CODES.N)
fun AccessibilityService.gestureOnScreen(
        path: Path,
        startTime:Long = 0,
        duration:Long = 100,
        callback:AccessibilityService.GestureResultCallback,
        handler: Handler? = null
){
    val builder = GestureDescription.Builder()
    builder.addStroke(GestureDescription.StrokeDescription(path, startTime, duration))
    val gesture = builder.build()
    dispatchGesture(gesture, callback, handler)
}

/**
 * 默认开启无障碍服务，无需用户手动授权，需要app在uid在系统进程，并声明权限
 * <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
 * <uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS"/>
 *
 * @param name 无障碍服务的名字，比如"cn.izis.test/cn.izis.test.ClickAccessibilityService"
 */
fun startAccessibilityServiceAuto(context: ContextWrapper, name:String){
    Settings.Secure.putString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
            name
    )
    Settings.Secure.putInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 1)
}