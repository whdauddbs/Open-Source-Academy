package com.example.administrator.pasugoon;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.pasugoon.item.MemberItem;
import com.example.administrator.pasugoon.lib.EtcLib;
import com.example.administrator.pasugoon.R;
import com.example.administrator.pasugoon.lib.MyLog;
import com.example.administrator.pasugoon.lib.RemoteLib;
import com.example.administrator.pasugoon.lib.StringLib;
import com.example.administrator.pasugoon.remote.RemoteService;
import com.example.administrator.pasugoon.remote.ServiceGenerator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/* 네트워크 연결이 되는지 확인한 후
전화번호를 받아와 서버와 대조한뒤 사용자 정보 전송, 인포값을 받아온다.
연결이 되지않았다면 메인으로 바로 이동
 */
public class IndexActivity extends AppCompatActivity {
    private  final String TAG = "checkFunction";
    boolean NetworkState = true;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        context = this;

        if (!RemoteLib.getInstance().isConnected(context)) {
            MyLog.d("checkFunction", String.valueOf(RemoteLib.getInstance().isConnected(context)));
            showNoService();
            return;
        }
    }
    /**
     * 일정 시간(1.2초) 이후에 startTask() 메소드를 호출해서
     * 서버에서 사용자 정보를 조회한다.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTask();
            }
        }, 1200);
    }
    // 현재 인터넷에 접속할 수 없기 때문에 일부 기능을 사용할 수 없다는 메시지를 띄운다.
    private void showNoService() {
        TextView messageText = (TextView) findViewById(R.id.message);
        messageText.setText(R.string.network_not_working);
        messageText.setVisibility(View.VISIBLE);
        MyLog.d("checkFunction", "ShowNoService");
        Button closeButton = (Button) findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkState = false;
                MyLog.d("checkFunction", "before_startMain");
                startMain();
            }
        });
        closeButton.setVisibility(View.VISIBLE);
    }

    /**
     * 현재 폰의 전화번호와 동일한 사용자 정보를 조회할 수 있도록
     * selectMemberInfo() 메소드를 호출한다.
     */
    public void startTask() {
        String phone = EtcLib.getInstance().getPhoneNumber(this);

        selectMember(phone);
    }

    /**
     * 리트로핏을 활용해서 서버로부터 사용자 정보를 조회한다.
     * 사용자 정보를 조회했다면 setMemberItem() 메소드를 호출하고
     * 그렇지 않다면 SaveAndStartMain(item) 메소드를 호출한다.
     *
     * @param phone 폰의 전화번호
     */
    public void selectMember(String phone) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<MemberItem> call = remoteService.selectMember(phone);
        call.enqueue(new Callback<MemberItem>() {
            @Override
            public void onResponse(Call<MemberItem> call, Response<MemberItem> response) {
                MemberItem item = response.body();
                if (response.isSuccessful() && !StringLib.getInstance().isBlank(item.phone)) {
                    MyLog.d(TAG, "success " + response.body().toString());
                    setMemberItem(item);
                } else {
                    MyLog.d(TAG, "not success");
                    SaveAndStartMain(item);
                }
            }

            @Override
            public void onFailure(Call<MemberItem> call, Throwable t) {
                notConnectServer();
                MyLog.d(TAG, "no internet connectivity1");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    /**
     * 전달받은 MemberItem을 Application 객체에 저장한다.
     * 그리고 startMain() 메소드를 호출한다.
     *
     * @param item 사용자 정보
     */
    private void setMemberItem(MemberItem item) {
        ((MyApp) getApplicationContext()).setMemberItem(item);
        startMain();
    }

    public void startMain() {
        MyLog.d("checkFunction", "startMain");
        Intent intent = new Intent(IndexActivity.this, MainActivity.class);
        intent.putExtra("netstate",NetworkState);
        startActivity(intent);

        finish();
    }

    /**
     * 사용자 정보를 조회하지 못했다면 insertMemberPhone() 메소드를 통해
     * 전화번호를 서버에 저장하고 MainActivity를 실행한다.
     * 그리고 현재 액티비티를 종료한다.
     *
     * @param item 사용자 정보
     */
    private void SaveAndStartMain(MemberItem item) {
        if (item == null || item.seq <= 0) {
            insertMemberPhone();
        }
        startMain();

    }

    /**
     * 폰의 전화번호를 서버에 저장한다.
     */
    private void insertMemberPhone() {
        String phone = EtcLib.getInstance().getPhoneNumber(context);
        RemoteService remoteService =
                ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertMemberPhone(phone);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    MyLog.d(TAG, "success insert id " + response.body().toString());
                } else {
                    int statusCode = response.code();

                    ResponseBody errorBody = response.errorBody();

                    MyLog.d(TAG, "fail " + statusCode + errorBody.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                notConnectServer();
                MyLog.d(TAG, "no internet connectivity2");
            }
        });
    }

    private void notConnectServer() {
        TextView messageText = (TextView) findViewById(R.id.message);
        messageText.setText(getBaseContext().getText(R.string.server_not_connecting));
        messageText.setVisibility(View.VISIBLE);

        Button closeButton = (Button) findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMain();
            }
        });
        closeButton.setVisibility(View.VISIBLE);
    }
}
