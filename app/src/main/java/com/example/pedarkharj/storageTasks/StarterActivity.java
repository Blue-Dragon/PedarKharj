package com.example.pedarkharj.storageTasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.pedarkharj.MainActivity;
import com.example.pedarkharj.R;
import com.example.pedarkharj.mainpage.MyDrawerActivity;
import com.example.pedarkharj.profile.LoginActivity;
import com.example.pedarkharj.profile.SharedPrefManager;
import com.example.pedarkharj.profile.User;

public class StarterActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            User user = SharedPrefManager.getInstance(this).getUser();
            SharedPrefManager.getInstance(this, MainActivity.class).new mSyncUser().execute(user);
//            progressBar.setVisibility(View.GONE);
//            finish();
        } else startActivity(new Intent(this, LoginActivity.class));

    }


}
