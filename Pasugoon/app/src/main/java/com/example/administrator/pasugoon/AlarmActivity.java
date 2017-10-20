package com.example.administrator.pasugoon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.administrator.pasugoon.lib.MyLog;

import static java.lang.String.valueOf;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "checkFunction";
    private static final String INTENT_ACTION = "com.example.adminstrator.paugoon";
    TimePicker tp;
    int hourofDay, minute;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        context = getApplicationContext();
        tp = (TimePicker) findViewById(R.id.timePicker);
        Button setting_button = (Button) findViewById(R.id.time_setting_submit);
        Button delete_button = (Button) findViewById(R.id.delete_setting);
        setting_button.setOnClickListener(this);
        delete_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_setting_submit:
                MyLog.d(TAG, "submit_button click");
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                hourofDay = tp.getHour();
                minute = tp.getMinute();
                long second = hourofDay * 3600000 + minute * 60000;
                Intent receiverIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, receiverIntent, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, second, 86400000,pendingIntent);
                Toast.makeText(getApplicationContext(), "자동 실행이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete_setting:
                MyLog.d(TAG, "delete_button click");
                releaseAlarm(this);
                Toast.makeText(getApplicationContext(), "자동 실행이 꺼졌습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    // 알람 해제
    private void releaseAlarm(Context context) {
        MyLog.d(TAG, "releaseAlarm()");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent Intent = new Intent(INTENT_ACTION);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, 0);
        alarmManager.cancel(pIntent);

    }
}
