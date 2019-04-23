package com.example.pedarkharj;

import android.content.Context;
import android.content.Intent;
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
            updateUserInfoAndPicIfNeededAndGoTo(getApplicationContext(), MyDrawerActivity.class);
//            SharedPrefManager.getInstance(this, MyDrawerActivity.class).new mSyncUser().execute(user);
//            progressBar.setVisibility(View.GONE);
//            finish();
        } else startActivity(new Intent(this, LoginActivity.class));

    }


    public void updateUserInfoAndPicIfNeededAndGoTo(Context context, Class mClass) {
        //get user info from server
        if (SharedPrefManager.getInstance(context).isLoggedIn()) {
            User user = SharedPrefManager.getInstance(context).getUser();

            int exPicUpdateNum = user.getPicUpdateNum();
            final int[] curPicUpdateNum = {user.getPicUpdateNum()};

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_USER_INFO,
                    response -> {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //getting user params reom server
                                int cur = curPicUpdateNum[0] = userJson.getInt("picUpdateNum");

//                                Toast.makeText(context,
//                                        "\nex: "+ exPicUpdateNum+
//                                        "\ncurrant: "+ cur, Toast.LENGTH_LONG).show();

                                //update pic if needed
                                if (exPicUpdateNum != cur)
                                    SharedPrefManager.getInstance(getApplicationContext()).getNsetProfPic();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    },
                    Throwable::printStackTrace
            )
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(user.getId()));
                    return params;
                }
            };
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


//            startActivity(new Intent(context, mClass));
            SharedPrefManager.getInstance(context, mClass).new mSyncUser().execute(user);
        } else startActivity(new Intent(context, mClass));
    }


}
