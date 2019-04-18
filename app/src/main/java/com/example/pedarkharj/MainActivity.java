package com.example.pedarkharj;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pedarkharj.mainpage.MyDrawerActivity;
import com.example.pedarkharj.profile.PicProfile;
import com.example.pedarkharj.profile.ProfileActivity;
import com.example.pedarkharj.profile.SharedPrefManager;
import com.example.pedarkharj.profile.User;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onResume() {
//        getUserInfoFromServer(this);
        super.onResume();
    }

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
            getUserInfoFromServer(getApplicationContext());

//            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            return false;
        });

        MenuItem m2 = menu.add("Test Connection");
//        m2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m2.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(MainActivity.this, TestActivity.class));
            return false;
        });

        MenuItem m4 = menu.add("drawer");
//        m4.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m4.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(MainActivity.this, MyDrawerActivity.class));
            return false;
        });

            MenuItem m5 = menu.add("pic profile");
        m5.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m5.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(MainActivity.this, PicProfile.class));
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void getUserInfoFromServer(Context context) {
        //get user info from server
        if (SharedPrefManager.getInstance(context).isLoggedIn()) {
            User user = SharedPrefManager.getInstance(context).getUser();
            SharedPrefManager.getInstance(context).new mSyncUser().execute(user);

        }
    }
}
