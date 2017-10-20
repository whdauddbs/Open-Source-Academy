package com.example.administrator.pasugoon;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.IBinder;

import com.example.administrator.pasugoon.lib.MyLog;

public class MyService extends Service {
    private BroadcastReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.d("checkFunction","MyService_onCreate");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CAMERA_BUTTON);
        intentFilter.addAction(Camera.ACTION_NEW_PICTURE);
        intentFilter.addAction(Camera.ACTION_NEW_VIDEO);
        mReceiver = new MyReceiver();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.d("checkFunction","MyService_onStart");
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_CAMERA_BUTTON);
            intentFilter.addAction(Camera.ACTION_NEW_PICTURE);
            intentFilter.addAction(Camera.ACTION_NEW_VIDEO);
            mReceiver = new MyReceiver();
            registerReceiver(mReceiver, intentFilter);
        MyLog.d("checkFunction","MyService_START_STICKY");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        MyLog.d("checkFunction","onDestroy");
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
