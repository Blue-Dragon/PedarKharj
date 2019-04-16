package com.example.pedarkharj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pedarkharj.mainpage.MyDrawerActivity;
import com.example.pedarkharj.profile.PicProfile;
import com.example.pedarkharj.profile.ProfileActivity;

import com.example.pedarkharj.retrofitStuff.RetroMainActivity;


public class MainActivity extends AppCompatActivity {


//    @Override
//    protected void onResume() {
//        //get user info from server
//        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
//            User user = SharedPrefManager.getInstance(this).getUser();
//            SharedPrefManager.getInstance(getApplicationContext()).getOtherParamsOnline(user);
//        }
//        super.onResume();
//    }

    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.tv1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem m1 = menu.add("go to profile");
        m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        m1.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            return false;
        });

        MenuItem m2 = menu.add("Test Connection");
//        m2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m2.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(MainActivity.this, TestActivity.class));
            return false;
        });

//        MenuItem m3 = menu.add("Register");
//        m3.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        m3.setOnMenuItemClickListener(item -> {
//            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
//            return false;
//        });

        MenuItem m4 = menu.add("drawer");
//        m4.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m4.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(MainActivity.this, MyDrawerActivity.class));
            return false;
        });

            MenuItem m5 = menu.add("pic profile");
//        m5.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m5.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(MainActivity.this, PicProfile.class));
            return false;
        });

        MenuItem m6 = menu.add("retro");
        m6.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m6.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(MainActivity.this, RetroMainActivity.class));
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }
}
