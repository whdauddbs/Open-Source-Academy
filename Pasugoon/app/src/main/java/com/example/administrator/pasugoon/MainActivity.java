package com.example.administrator.pasugoon;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.icu.text.SimpleDateFormat;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.pasugoon.item.MemberItem;
import com.example.administrator.pasugoon.lib.EtcLib;
import com.example.administrator.pasugoon.lib.MyLog;
import com.example.administrator.pasugoon.lib.MyToast;
import com.example.administrator.pasugoon.remote.RemoteService;
import com.example.administrator.pasugoon.remote.ServiceGenerator;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = "checkFunction";

    MemberItem memberItem;
    String ProfileName;
    DrawerLayout drawer;
    View headerLayout;

    Context context;
    ImageView imageView1, imageView2, imageView3;

    WifiManager wManager;
    NfcAdapter nfcAdapter;
    BluetoothAdapter mBluetoothAdapter;
    LocationManager locationManager;

    Boolean NetworkState;
    Boolean cameraWarning_Set;
    int wireless_State;
    String wireless_Off_Time;
    Intent isVaccine;

    MemberItem currentItem;
    SharedPreferences pref;

    CircleImageView profileIconImage;
    MyReceiver myReceiver = new MyReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        MyLog.d(TAG,"onCreate");
        currentItem = ((MyApp) getApplication()).getMemberItem();

        imageView1 = (ImageView) findViewById(R.id.wireless_toggle_button);
        imageView1.setOnClickListener(new MyListener());
        imageView2 = (ImageView) findViewById(R.id.install_vaccine_button);
        imageView2.setOnClickListener(new MyListener());
        imageView3 = (ImageView) findViewById(R.id.warning_toggle_button);
        imageView3.setOnClickListener(new MyListener());

        //통신 기능 상태 불러오기
        wManager = (WifiManager)context.getSystemService(WIFI_SERVICE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        //저장된 값 불러오기
        NetworkState = getIntent().getExtras().getBoolean("netstate");
        pref = getSharedPreferences("pref", 0);   //값을 가져오기 위해 사용
        memberItem = ((MyApp)getApplication()).getMemberItem();
        ProfileName = pref.getString("name", "User");
        cameraWarning_Set = pref.getBoolean("camera_set", false);
        wireless_State = BooleanToInt(check_Wireless());
        isVaccine = getPackageManager().getLaunchIntentForPackage("com.ahnlab.v3mobileplus");

        //버튼 색 결정
        toggle_Button(wireless_State, imageView1);
        if (isVaccine == null) { toggle_Button(0,imageView2);
        } else { toggle_Button(1,imageView2); }
        if (cameraWarning_Set == false) { toggle_Button(0,imageView3);
        } else { toggle_Button(1,imageView3); }

        //툴바 생성
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerLayout = navigationView.getHeaderView(0);


    }



    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            MyLog.d(TAG,"MyListener");
            switch (v.getId()) {
                case R.id.wireless_toggle_button:
                    MyLog.d(TAG,"wireless_toggle_button");
                    wireless_Off();
                    toggle_Button(wireless_State, imageView1);
                    Toast.makeText(getApplicationContext(),"스마트폰 USB포트에 연결 금지!", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.install_vaccine_button:
                    MyLog.d(TAG,"install_vaccine_button");
                    if(NetworkState == true) {
                        if (isVaccine == null) {
                            MyLog.d(TAG, "marketLaunch");
                            Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                            marketLaunch.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.ahnlab.v3mobileplus"));
                            startActivity(marketLaunch);
                        } else {
                            Toast.makeText(getApplicationContext(), "이미 백신이 설치되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else { Toast.makeText(getApplicationContext(),"네트워크 연결이 필요합니다.", Toast.LENGTH_SHORT).show(); }

                    break;

                case R.id.warning_toggle_button:
                    MyLog.d(TAG,"warning_toggle_button");
                    if (cameraWarning_Set == false) {
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction(Intent.ACTION_CAMERA_BUTTON);
                        intentFilter.addAction(Camera.ACTION_NEW_PICTURE);
                        intentFilter.addAction(Camera.ACTION_NEW_VIDEO);
                        registerReceiver(myReceiver, intentFilter);

                        cameraWarning_Set = true;
                        Toast.makeText(getApplicationContext(), "카메라를 사용할 떄마다 경고창이 뜹니다", Toast.LENGTH_SHORT).show();
                    } else {
                        unregisterReceiver(myReceiver);
                        cameraWarning_Set = false;
                        Toast.makeText(getApplicationContext(), "경고창을 해제합니다.", Toast.LENGTH_SHORT).show();
                    }
                    toggle_Button(BooleanToInt(cameraWarning_Set),imageView3);
                    break;
            }

        }
    }

    public  boolean check_Wireless() {
        MyLog.d(TAG,"check_Wireless");
        SimpleDateFormat date_Format= new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        boolean wifi, bluetooth, nfc, gps;

        if (!(wManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED ||
                wManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING )) {
            wifi = true;
        } else { wifi = false; }

        if(locationManager == null) { gps = true; } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { gps = true;
            } else { gps = false; }
        }

        if(mBluetoothAdapter == null) { bluetooth = true; } else {
            if (!mBluetoothAdapter.isEnabled()) { bluetooth = true;
            } else { bluetooth = false; }
        }

        if(nfcAdapter == null) { nfc = true; } else {
            if (!nfcAdapter.isEnabled()) { nfc = true;
            } else { nfc = false; }
        }

        if (wifi == true && bluetooth == true && nfc == true && gps ==true) {
            wireless_Off_Time = date_Format.format(today);
            return  true;
        }
        else {
            return false;
        }

    }

    public void wireless_Off() {

        MyLog.d(TAG,"wireless_off");
        if(wManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED ||
                wManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING )
        {
            wManager.setWifiEnabled(false);
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!(mBluetoothAdapter == null)) {
            if(mBluetoothAdapter.isEnabled()){
                mBluetoothAdapter.disable();
            }
        }
        if(!(nfcAdapter==null)) {
            if (nfcAdapter.isEnabled()) {
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle("NFC Connect");
                ad.setMessage("설정에서 NFC을 OFF 해주세요.");
                ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 4.2.2 (API 17) 부터 NFC 설정 환경이 변경됨.
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                            startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                        } else {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    }
                });
            }
        }
        MyLog.d("checkFunction","wireless_off_gps_before");
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            MyLog.d("checkFunction","wireless_off_gps");
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("GPS Connect");
            ad.setMessage("설정에서 GPS를 OFF 해주세요.");
            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //GPS 설정화면으로 이동
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            });
            ad.show();
        }
        save();
        toggle_Button(wireless_State, imageView1);
    }

    public void  save() {
        MyLog.d(TAG,"save");
        final MemberItem newItem = getMemberItem();

        if (!isChanged(newItem)) {
            MyToast.s(this, R.string.no_change);
            return;
        }

        RemoteService remoteService =
                ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertMember(newItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String seq = response.body();
                    try {
                        currentItem.seq = Integer.parseInt(seq);
                        if (currentItem.seq == 0) {
                            MyToast.s(context, R.string.member_insert_fail_message);
                            return;
                        }
                    } catch (Exception e) {
                        MyToast.s(context, R.string.member_insert_fail_message);
                        return;
                    }
                    currentItem.state = newItem.state;
                    currentItem.offTime = newItem.offTime;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }

        });
    }

    private boolean isChanged(MemberItem newItem) {
        if (newItem.seq == currentItem.seq
                && newItem.state==currentItem.state
                && newItem.offTime.equals(currentItem.offTime)) {
            return false;
        } else {
            return true;
        }
    }

    private MemberItem getMemberItem() {
        MemberItem item = new MemberItem();
        item.phone = EtcLib.getInstance().getPhoneNumber(context);
        item.state = wireless_State;
        item.offTime = wireless_Off_Time;
        return item;
    }

    public void toggle_Button(int bool,ImageView imageView) {

        if (bool == 1) {
            imageView.setImageResource(R.drawable.button_round_green);
        } else {
            imageView.setImageResource(R.drawable.button_round_red);
        }

    }

    /**
     * 화면이 새로 보여질 대마다 setProfileView() 를 호출하고, 기능 상태를 점검한다.
     */
    @Override
    protected void onResume() {
        super.onResume();

        wireless_State = BooleanToInt(check_Wireless());
        if (isVaccine == null) { toggle_Button(0,imageView2);
        } else { toggle_Button(1,imageView2); }
        toggle_Button(wireless_State,imageView1);
        setProfileView();
    }

    public int BooleanToInt(Boolean bool) {
        if (bool == true) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 프로필 이미지와 프로필 이름을 설정한다.
     */
    private void setProfileView() {
        profileIconImage = headerLayout.findViewById(R.id.profile_icon);
        profileIconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        profileIconImage.setImageResource(R.drawable.ic_user);

        TextView nameText = headerLayout.findViewById(R.id.name);
        nameText.setText(ProfileName);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            SharedPreferences.Editor  editor = pref.edit();   //값을 입력, 삭제하기 위해 사용
            editor.putBoolean("camera_set", cameraWarning_Set);
            editor.commit();      //값을 저장할때 호출
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            // Handle the camera action
        } else if (id == R.id.nav_alarm) {

        } else if (id == R.id.nav_check) {

        } else if (id == R.id.nav_day) {

        } else if (id == R.id.nav_profile) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
