package com.lxf.tools.net_hint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lxf.tools.util.NetUtils;
import com.lxf.tools.util.RxBus;

/**
 * 注册
 * <receiver android:name=".net_hint.NetworkConnectChangedReceiver">
    <intent-filter>
    <action android:name="android.NET.conn.CONNECTIVITY_CHANGE" />
    <action android:name="android.Net.wifi.WIFI_STATE_CHANGED" />
    <action android:name="android.net.wifi.STATE_CHANGE" />
    </intent-filter>
   </receiver>
 */
public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkConnectChanged";
    @Override
    public void onReceive(Context context, Intent intent) {
        //**判断当前的网络连接状态是否可用*/
        boolean isConnected = NetUtils.isConnected(context);
        Log.d(TAG, "onReceive: 当前网络 " + isConnected);
        RxBus.getDefault().send(new NetworkChangeEvent(isConnected));
    }
}