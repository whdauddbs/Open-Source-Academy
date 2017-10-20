package com.example.administrator.pasugoon;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.pasugoon.lib.MyLog;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String name = intent.getAction();

        MyLog.d("checkFunction","onReceive");
        Intent i = new Intent( context, alertdialogActivity.class );
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        try {
            pi.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
