package com.example.pedarkharj;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.pedarkharj.mainpage.MyDrawerActivity;
import com.example.pedarkharj.profile.PicProfile;
import com.example.pedarkharj.profile.ProfileActivity;
import com.example.pedarkharj.profile.SharedPrefManager;
import com.example.pedarkharj.profile.URLs;
import com.example.pedarkharj.profile.User;
import com.example.pedarkharj.profile.VolleySingleton;
import com.example.pedarkharj.storageTasks.SaveImgActivity;
import com.example.pedarkharj.storageTasks.StarterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onResume() {
//        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
//            SharedPrefManager.getInstance(getApplicationContext()).getNsetProfPic();
//        }
//        updateUserPicIfNeededAndGoTo(this);
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
//        m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        m1.setOnMenuItemClickListener(item -> {
            updateUserPicIfNeededAndGoTo(getApplicationContext(), ProfileActivity.class);
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
            updateUserPicIfNeededAndGoTo(getApplicationContext(), PicProfile.class);
            return false;
        });

        MenuItem m6 = menu.add("Save img");
        m6.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        m6.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(getApplicationContext(),  SaveImgActivity.class));
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void updateUserPicIfNeededAndGoTo(Context context, Class mClass) {
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
