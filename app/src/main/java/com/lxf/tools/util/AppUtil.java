package com.lxf.tools.util;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AppUtil {
    /**
     * 启动app
     * @param context 上下文
     * @param packageName 包名
     */
    public static void launchAPP(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动浏览器
     * @param context 上下文
     * @param url 要启动的地址
     */
    public static void startBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * 杀死后台应用
     * @param context 上下文
     * @param packageName 包名
     */
    public static void killBackgroundApp(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null)
            am.killBackgroundProcesses(packageName);
    }

    public static void killApp(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (am != null)
//            am.killBackgroundProcesses(packageName);

        Method method = null;
        try {
            method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            method.invoke(am, packageName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
//        am.forceStopPackage(packageName);
    }

    /**
     * 跳转到设置页面
     * @param context 上下文
     */
    public static void toSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 跳转到应用详情页面
     * @param context 上下文
     */
    public static void toAppDetailActivity(Context context) {
        context.startActivity(
                new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.fromParts("package", context.getPackageName(), null)));
    }

    /**
     * 获取所有app信息
     *
     * 获取图标：resolveInfo.activityInfo.loadIcon(packageManager)
     * 获取包名：resolveInfo.activityInfo.packageName
     * 获取程序入口activity：resolveInfo.activityInfo.name
     * @param context 上下文
     * @return app信息列表
     */
    public static List<ResolveInfo> getAllApps(Context context){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = context.getPackageManager();

        return pm.queryIntentActivities(intent,0);
    }
}
