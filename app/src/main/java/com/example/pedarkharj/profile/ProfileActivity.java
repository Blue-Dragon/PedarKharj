package com.example.pedarkharj.profile;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pedarkharj.MainActivity;
import com.example.pedarkharj.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * in the ProfileActivity.java class, we will display the user information if the user is log-in otherwise,
     * it redirects to LoginActivity.java class. The onClick() method is used to log-out the user when
     * clicking on the button.
     */

    TextView id,userName,userEmail,gender;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){

            User user = SharedPrefManager.getInstance(this).getUser();
            //sync user info
            if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                SharedPrefManager.getInstance(getApplicationContext()).syncUserInfo(user);
            }

            id = findViewById(R.id.textViewId);
            userName = findViewById(R.id.textViewUsername);
            userEmail = findViewById(R.id.textViewEmail);
            gender = findViewById(R.id.textViewGender);
            btnLogout = findViewById(R.id.buttonLogout);

            id.setText(String.valueOf(user.getId()));
            userEmail.setText(user.getEmail());
            gender.setText(user.getGender());
            userName.setText(user.getName());

            btnLogout.setOnClickListener(this);
        }
        else{
            Intent  intent = new Intent(ProfileActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void onClick(View view){
        if(view.equals(btnLogout)){
            SharedPrefManager.getInstance(getApplicationContext()).logout();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}