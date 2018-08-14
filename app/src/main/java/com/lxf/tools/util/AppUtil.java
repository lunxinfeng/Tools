package com.lxf.tools.util;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

public class AppUtil {
    public static void launchAPP(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void killBackgroundApp(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null)
            am.killBackgroundProcesses(packageName);
    }

    public static void toSettingActivity(Context context){
        Intent intent =  new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        context.startActivity(intent);
    }
}
