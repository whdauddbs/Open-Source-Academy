package com.example.administrator.pasugoon;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class alertdialogActivity extends Activity {

    private String notiMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bun = getIntent().getExtras();
        notiMessage = bun.getString("notiMessage");

        setContentView(R.layout.activity_alertdialog);

        TextView adMessage = findViewById(R.id.message);
        adMessage.setText(notiMessage);

        Button okButton = findViewById(R.id.submit);
        Button settingButton = findViewById(R.id.setting);
        okButton.setOnClickListener(new MyListener());
        settingButton.setOnClickListener(new MyListener());


    }
    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.submit:
                    finish();
                    break;

                case R.id.setting:
                    Intent intent = new Intent(alertdialogActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }

        }
    }
}
