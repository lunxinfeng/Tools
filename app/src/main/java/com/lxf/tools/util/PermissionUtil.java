package com.lxf.tools.util;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Set;

public class PermissionUtil {

    /**
     * SYSTEM_ALERT_WINDOW权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isSystemAlertEnabled(Context context){
        return Settings.canDrawOverlays(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setSystemAlert(Activity activity, String packageName,int requestCode){
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + packageName));
        activity.startActivityForResult(intent,requestCode);
    }

    /**
     * 通知栏监听权限
     */
    public static boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        return packageNames.contains(context.getPackageName());
    }

    public static void setNotificationListener(Activity activity,int requestCode){
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        }else {
            intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        }
        activity.startActivityForResult(intent,requestCode);
    }

    public static void toggleNotificationListenerService(Context context, Class<? extends NotificationListenerService> cls) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, cls),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(context, cls),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
