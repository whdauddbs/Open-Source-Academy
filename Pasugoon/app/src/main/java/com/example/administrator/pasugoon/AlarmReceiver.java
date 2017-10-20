package com.example.administrator.pasugoon;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent Intent = new Intent(context, PermissionActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, PendingIntent.FLAG_ONE_SHOT);
        try {
                pIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
