package com.example.administrator.pasugoon;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.pasugoon.lib.MyLog;
import com.example.administrator.pasugoon.lib.MyToast;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends AppCompatActivity {
    private static final int PERMISSION_MULTI_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

 /*       화면을 구성하고 SDK 버전과 권한에 따른 처리를 한다.
        @param savedInstanceState 액티비티가 새로 생성되었을 경우, 이전 상태 값을 가지는 객체*/

        if(Build.VERSION.SDK_INT < 23) {
            goIndexActivity();
        } else {
            if (checkAndRequestPermissions()) {
                goIndexActivity();
            }
        }
    }
    /*
    권한을 확인하고 권한이 부여되어 있지 않다면 권한 요청
            @return 필요한 권한이 모두 부여되었다면 true, 그렇지 않다면 false
    */
    private boolean checkAndRequestPermissions(){
        String [] permissions = new String[]{               //권한 추가할때 여기다가 추가
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.NFC,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.RECEIVE_BOOT_COMPLETED
        };
        List<String> listPermissionsNeeded = new ArrayList<>();

        for ( String permission:permissions) {
            Log.v("Permission", "Permission");
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED ) {
                listPermissionsNeeded.add(permission);
            }
        }

        if(!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_MULTI_CODE);
            return  false;
        }
        return true;
    }
    /**
     * 권한 요청 결과를 받는 메소드
     * @param requestCode 요청 코드
     * @param permissions 권한 종류
     * @param grantResults 권한 결과
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length == 0) return;

        switch (requestCode) {
            case PERMISSION_MULTI_CODE:
                checkPermissionResult(permissions, grantResults);

                break;
        }
    }

    /**
     * 권한 처리 결과를 보고 인덱스 액티비티를 실행할 지,
     * 권한 설정 요청 다이얼로그를 보여줄 지를 결정한다.
     * 모든 권한이 승인되었을 경우에는 goIndexActivity() 메소드를 호출한다.
     * @param permissions 권한 종류
     * @param grantResults 권한 부여 결과
     */
    private void checkPermissionResult(String[] permissions, int[] grantResults) {
        boolean isAllGranted = true;

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false;
            }
        }

        //권한이 부여되었다면
        if (isAllGranted) {
            goIndexActivity();

            //권한이 부여되어 있지 않다면
        } else {
            showPermissionDialog();
        }
    }

    /**
     * 인덱스 액티비티를 실행하고 현재 액티비티를 종료한다.
     */
    private void goIndexActivity() {
        Intent intent = new Intent(this, IndexActivity.class);
        MyLog.d("checkFunction", "PermissionActivity");
        startActivity(intent);

        finish();
    }

    /**
     * 권한 설정 화면으로 이동할 지를 선택하는 다이얼로그를 보여준다.
     */
    private void showPermissionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.permission_setting_title);
        dialog.setMessage(R.string.permission_setting_message);
        dialog.setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                MyToast.s(PermissionActivity.this, R.string.permission_setting_restart);
                PermissionActivity.this.finish();

                goAppSettingActivity();
            }
        });
        dialog.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                PermissionActivity.this.finish();
            }
        });
        dialog.show();
    }

    /**
     * 권한을 설정할 수 있는 설정 액티비티를 실행한다.
     */
    private void goAppSettingActivity() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
