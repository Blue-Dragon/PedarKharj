package com.example.pedarkharj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pedarkharj.profile.ProfileActivity;
import com.example.pedarkharj.profile.RegisterActivity;

public class MainActivity extends AppCompatActivity {

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

        return super.onCreateOptionsMenu(menu);
    }
}
