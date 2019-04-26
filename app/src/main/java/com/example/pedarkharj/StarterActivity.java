package com.example.pedarkharj;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.pedarkharj.R;
import com.example.pedarkharj.mainpage.MyDrawerActivity;
import com.example.pedarkharj.profile.LoginActivity;
import com.example.pedarkharj.profile.SharedPrefManager;
import com.example.pedarkharj.profile.URLs;
import com.example.pedarkharj.profile.User;
import com.example.pedarkharj.profile.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StarterActivity extends AppCompatActivity {

    ProgressBar progressBar;
    public static int formerPicNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            User user = SharedPrefManager.getInstance(this).getUser();
            formerPicNum = user.getPicUpdateNum();
            //sync user and pic if needed and go to the other page
            new delayTask().execute();
//            SharedPrefManager.getInstance(this, MyDrawerActivity.class).new mSyncUser().execute(user);
//            progressBar.setVisibility(View.GONE);
//            finish();
        } else startActivity(new Intent(this, LoginActivity.class));

    }



    private class delayTask extends AsyncTask <Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new MyDrawerActivity().updateUserInfoAndPicIfNeededAndGoTo(getApplicationContext(), MyDrawerActivity.class);
        }
    }

}
