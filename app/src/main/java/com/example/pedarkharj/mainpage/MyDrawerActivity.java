package com.example.pedarkharj.mainpage;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.pedarkharj.R;
import com.example.pedarkharj.TestActivity;
import com.example.pedarkharj.profile.PicProfile;
import com.example.pedarkharj.profile.ProfileActivity;
import com.example.pedarkharj.profile.SharedPrefManager;
import com.example.pedarkharj.profile.URLs;
import com.example.pedarkharj.profile.User;
import com.example.pedarkharj.profile.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    CircleImageView profPic;

    String username, email, profilePicName;
    TextView usernameTV, emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_drawer);

        //Toolbar
        mToolbar = findViewById(R.id.m_toolbar);
        setSupportActionBar(mToolbar);

        /**********          profile            **********/
        //view
//        final View view =  getLayoutInflater().inflate(R.layout.drawer_header, null);
        navigationView = findViewById(R.id.m_navigation_view); //Nav view needs to be initiated here
        final View view = navigationView.getHeaderView(0);
        //init
        emailTV = view.findViewById(R.id.user_email);
        usernameTV = view.findViewById(R.id.username_txt);
        profPic = view.findViewById(R.id.nav_profile_pic);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            User user = SharedPrefManager.getInstance(this).getUser();
            username = user.getName();
            email = user.getEmail();
            profPic.setImageBitmap(user.getBitmap());


            if (username != null)
                usernameTV.setText(username);
            if (email != null)
                emailTV.setText(email);
        }else {

        }



        /*****************          Drawer          ******************/
        //drawerLayout
        drawerLayout = findViewById(R.id.m_drawer);

        //Nav View
//        navigationView = findViewById(R.id.m_navigation_view); //already initiated
        navigationView.setNavigationItemSelectedListener(this); //onNavigationItemSelected() metod

        //showing nav button
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        } //now set the action in onOptionsItemSelected method

        //toggle (it takes care of drawable methods)
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState(); //rotating the icon


        //choose a default frag

        /**
         * create this fragment once: when we rotate the device,  the activity is called again.
         * so we want the onCreate method to be executed once, even in this situation.
         * It will be handled in NavigationView, but here we should implement it manually.
         */
//        if (savedInstanceState == null){
//            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Frag1()).commit();
//            navigationView.setCheckedItem(R.id.frag1);
//        }


    }

    /*****************          Drawer          ******************/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_frag1:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Frag1()).commit();
                break;
            case R.id.nav_frag2:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Frag2()).commit();
                break;
            case R.id.nav_frag3:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Frag3()).commit();
                break;
            case R.id.nav_profile:
                updateUserInfoAndPicIfNeededAndGoTo(getApplicationContext(), ProfileActivity.class);
                break;
            case R.id.nav_pic_profile:
                updateUserInfoAndPicIfNeededAndGoTo(getApplicationContext(), PicProfile.class);
                break;
            case R.id.nav_logout:
                if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                }
                break;
            case R.id.nav_test_connection:
                startActivity(new Intent(getApplicationContext(), TestActivity.class));
                break;
        }

        //now we close the drawer
        drawerLayout.closeDrawer(GravityCompat.START);

        return true; //so when each item is selected, mark it as selected
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }/*****************          Drawer />          ******************/


    //sync user and pic if needed and go to the other page (if activity is set)
    public void updateUserInfoAndPicIfNeededAndGoTo(Context context, Class mClass) {
        //get user info from server
        if (SharedPrefManager.getInstance(context).isLoggedIn()) {
            User user = SharedPrefManager.getInstance(context).getUser();

            int exPicUpdateNum = user.getPicUpdateNum();
            final int[] curPicUpdateNum = {user.getPicUpdateNum()};

            Toast.makeText(context, "Before", Toast.LENGTH_SHORT).show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_USER_INFO,
                    response -> {
                        Toast.makeText(context, "After", Toast.LENGTH_SHORT).show();
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //getting user params reom server
                                int cur = curPicUpdateNum[0] = userJson.getInt("picUpdateNum");
                                profilePicName = userJson.getString("profilePicName");

                                Toast.makeText(context,
                                        "\nex: "+ exPicUpdateNum+
                                        "\ncurrant: "+ cur, Toast.LENGTH_LONG).show();

                                //update pic if needed
//                                if (exPicUpdateNum != cur)
                                    SharedPrefManager.getInstance(context).getNsetProfPic(profilePicName);
                                Toast.makeText(context, "After2", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context,
                                        "\nError: \n"+ obj.getString("message"), Toast.LENGTH_LONG).show();
                                Toast.makeText(context, "After3", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    },
                    error -> {
                        Toast.makeText(context, "error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
            )

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(user.getId()));
                    return params;
                }
            };
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
            Toast.makeText(context, "After4", Toast.LENGTH_SHORT).show();



//            startActivity(new Intent(context, mClass));
            SharedPrefManager.getInstance(context, mClass).new mSyncUser().execute(user);
        } else startActivity(new Intent(context, mClass));
    }

    // double back pressed
    boolean alreadyPressed = false;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START); //if drawable open, onBackPressed should close it
        else {
            if (alreadyPressed) {
//                super.onBackPressed(); //else, close the activity as usual
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }

            alreadyPressed = true;
            Toast.makeText(this, "press back again to exit!", Toast.LENGTH_SHORT).show();
            //give 2 seconds to press back again, or make the boolean false
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alreadyPressed=false;
                }
            }, 2000);
        }

    }
}
