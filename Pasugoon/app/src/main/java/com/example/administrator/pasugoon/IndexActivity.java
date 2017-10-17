package com.example.administrator.pasugoon;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.administrator.pasugoon.lib.EtcLib;
import com.example.administrator.pasugoon.R;


/* 네트워크 연결이 되는지 확인한 후
전화번호를 받아와 서버와 대조한뒤 사용자 정보 전송,    인포값을 받아온다.
연결이 되지않았다면 메인으로 바로 이동
 */
public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        String phone = EtcLib.getInstance().getPhoneNumber(this); // 전화번호 받아오기
        Handler mhandler = new Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMain();
            }
        },1200);
    }

    public void startMain() {
        Intent intent = new Intent(IndexActivity.this, MainActivity.class);
        //intent.putExtra(netstate,NetworkState;)
        startActivity(intent);

        finish();
    }
}
