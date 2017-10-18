package com.example.administrator.pasugoon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.pasugoon.item.MemberItem;
import com.example.administrator.pasugoon.lib.EtcLib;
import com.example.administrator.pasugoon.lib.MyLog;
import com.example.administrator.pasugoon.lib.MyToast;
import com.example.administrator.pasugoon.lib.StringLib;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity{
    private final String TAG = "checkFunction";
    Context context;
    SharedPreferences pref;

    ImageView profileIconImage;
    ImageView profileIconChangeImage;
    EditText nameEdit;
    EditText phoneEdit;

    String PhoneNumber;
    String ProfileName;
    String currentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        MyLog.d(TAG, "onCreate");

        pref =  getSharedPreferences("pref", 0);
        context = this;

        PhoneNumber = pref.getString("phone", EtcLib.getInstance().getPhoneNumber(context));
        currentName = pref.getString("name", "User");

        setToolbar();
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setToolbar() {
        MyLog.d(TAG, "setToolbar");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.profile_setting);
        }
    }

    private void setView() {
        MyLog.d(TAG, "setView");
        nameEdit = (EditText) findViewById(R.id.profile_name);
        nameEdit.setText(currentName);

        phoneEdit = (EditText) findViewById(R.id.profile_phone);
        phoneEdit.setText(PhoneNumber);

        TextView phoneStateEdit = (TextView) findViewById(R.id.phone_state);
        if (PhoneNumber.startsWith("0")) {
            phoneStateEdit.setText("(" + getResources().getString(R.string.device_number) + ")");
        } else {
            phoneStateEdit.setText("(" + getResources().getString(R.string.phone_number) + ")");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MyLog.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MyLog.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                close();
                break;

            case R.id.action_submit:
                save();
                break;
        }

        return true;
    }

    private boolean isChanged(String name,String phone) {
        if (name.equals(currentName) && phone.equals(PhoneNumber)) {
            Log.d(TAG, "return " + false);
            return false;
        } else {
            return true;
        }
    }

    private boolean isNoName(String name) {
        return StringLib.getInstance().isBlank(name);
    }

    private String getPhoneNumber() {
        String PhoneNum = phoneEdit.getText().toString();

        return PhoneNum;
    }

    private String getProfileName() {
        ProfileName = new String();
        ProfileName = nameEdit.getText().toString();

        return ProfileName;
    }

    private void close() {
        String newProfileName = getProfileName();
        String newPhoneNumber = getPhoneNumber();

        if (!isChanged(newProfileName, newPhoneNumber) && !isNoName(newProfileName)) {
            finish();
        } else if (isNoName(newProfileName)) {
            MyToast.s(context, R.string.name_need);
            finish();
        } else {
            new AlertDialog.Builder(this).setTitle(R.string.change_save)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            save();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    private void save() {
        String newPhoneNumber = getPhoneNumber();
        String newProfileName = getProfileName();

        if (!isChanged(newProfileName, newPhoneNumber)) {
            MyToast.s(this, R.string.no_change);
            finish();
            return;
        }

        MyLog.d(TAG, "insertPhoneNumber " + newPhoneNumber);
        MyLog.d(TAG, "insertProfileName " + newProfileName);

        SharedPreferences.Editor  editor = pref.edit();   //값을 입력, 삭제하기 위해 사용
        editor.putString("name", newProfileName);
        editor.putString("phone", newPhoneNumber);
        editor.commit();      //값을 저장할때 호출

        finish();

    }
}
