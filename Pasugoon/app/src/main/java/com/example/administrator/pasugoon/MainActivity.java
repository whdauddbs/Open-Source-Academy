package com.example.administrator.pasugoon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.administrator.pasugoon.R;
import com.example.administrator.pasugoon.item.MemberItem;
import com.example.administrator.pasugoon.lib.MyLog;
import com.example.administrator.pasugoon.lib.StringLib;
import com.example.administrator.pasugoon.remote.RemoteService;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = "checkFunction";

    MemberItem memberItem;
    String ProfileName;
    DrawerLayout drawer;
    View headerLayout;
    //Boolean NetworkState = getIntent().getExtras().getBoolean("netstate");



    CircleImageView profileIconImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getSharedPreferences("pref", 0);   //값을 가져오기 위해 사용
        memberItem = ((MyApp)getApplication()).getMemberItem();
        ProfileName = pref.getString("name", "User");

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
        /*GoLib.getInstance()
                .goFragment(getSupportFragmentManager(), R.id.content_main,
                        BestFoodListFragment.newInstance());*/

    }

    /**
     * 프로필 정보는 별도 액티비티에서 변경될 수 있으므로
     * 변경을 바로 감지하기 위해 화면이 새로 보여질 대마다 setProfileView() 를 호출한다.
     */
    @Override
    protected void onResume() {
        super.onResume();

        setProfileView();
    }

    /**
     * 프로필 이미지와 프로필 이름을 설정한다.
     */
    private void setProfileView() {
        profileIconImage = (CircleImageView) headerLayout.findViewById(R.id.profile_icon);
        profileIconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        profileIconImage.setImageResource(R.drawable.ic_user);

        TextView nameText = (TextView) headerLayout.findViewById(R.id.name);
        nameText.setText(ProfileName);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
