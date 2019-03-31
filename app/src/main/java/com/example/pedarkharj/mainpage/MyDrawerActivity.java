package com.example.pedarkharj.mainpage;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj.R;
import com.example.pedarkharj.profile.SharedPrefManager;
import com.example.pedarkharj.profile.User;

public class MyDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    String username, email;
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

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            User user = SharedPrefManager.getInstance(this).getUser();
            username = user.getName();
            email = user.getEmail();
            
            if (username != null)
                usernameTV.setText(username);
            if (email != null)
                emailTV.setText(email);
        }
        /**********          profile />           **********/


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
        /*****************          Drawer />          ******************/

    }

    /*****************          Drawer          ******************/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.frag1:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Frag1()).commit();
                break;
            case R.id.frag2:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Frag2()).commit();
                break;
            case R.id.frag3:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Frag3()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "send", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_exit:
//                Toast.makeText(this, "exit", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, usernameTV.getText(), Toast.LENGTH_SHORT).show();
                usernameTV.setText("text");
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
    }

    /*****************          Drawer />          ******************/

    // double back pressed
    boolean alreadyPressed = false;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START); //if drawable open, onBackPressed should close it
        else {
            if (alreadyPressed) {
                super.onBackPressed(); //else, close the activity as usual
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
