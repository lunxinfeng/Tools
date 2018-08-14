package com.lxf.tools.net_hint;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.lxf.tools.R;
import com.lxf.tools.util.AppUtil;
import com.lxf.tools.util.RxBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {
    private View mTipView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private NetworkConnectChangedReceiver networkConnectChangedReceiver = new NetworkConnectChangedReceiver();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTipView();

        registerReceiver();
        subscribe();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(networkConnectChangedReceiver);
        super.onDestroy();
    }

    private void registerReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.NET.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.Net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(networkConnectChangedReceiver,intentFilter);
    }

    private void initTipView() {
        LayoutInflater inflater = getLayoutInflater();
        mTipView = inflater.inflate(R.layout.layout_network_tip, null); //提示View布局
        mTipView.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.toSettingActivity(BaseActivity.this);
            }
        });
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        //使用非CENTER时，可以通过设置XY的值来改变View的位置
        mLayoutParams.gravity = Gravity.TOP;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
    }

    private void subscribe() {
        RxBus.getDefault().toObservable(NetworkChangeEvent.class)
                .subscribe(new Observer<NetworkChangeEvent>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NetworkChangeEvent networkChangeEvent) {
                        if (networkChangeEvent.isConnected) {
                            if (mTipView != null && mTipView.getParent() != null) {
                                mWindowManager.removeView(mTipView);
                            }
                        } else {
                            if (mTipView.getParent() == null) {
                                mWindowManager.addView(mTipView, mLayoutParams);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
