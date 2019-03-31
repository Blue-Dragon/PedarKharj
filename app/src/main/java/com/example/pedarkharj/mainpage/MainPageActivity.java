package com.example.pedarkharj.mainpage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pedarkharj.R;

import java.util.Objects;

public class MainPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //drawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        //Nav View
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //toggle (it takes care of drawable methods)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState(); //rotating the icon
        //
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);



        /**
         * create this fragment once: when we rotate the device,  the activity is called again.
         * so we want the onCreate method to be executed once, even in this situation.
         * It will be handled in NavigationView, but here we should implement it manually.
         */
//        if (savedInstanceState == null){
//            //default frag
//            getSupportFragmentManager().beginTransaction().replace(R.id.frag1, new Frag1()).commit();
//            navigationView.setCheckedItem(R.id.frag1);
//        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START); //if drawable open, onBackPressed should close it
        else
            super.onBackPressed(); //else, close the activity as usual
    }

    //nav buttons
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.frag1:
                try {
//                    getFragmentManager().beginTransaction().replace(R.id.frag1, new Frag1()).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag1, new Frag1()).commit();
                }catch (Exception e){
                    Log.i("Fuck", e.toString());
                }
                break;
            case R.id.frag2:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag2, new Frag2()).commit();
                break;
            case R.id.frag3:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag3, new Frag3()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "send", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_exit:
                Toast.makeText(this, "exit", Toast.LENGTH_SHORT).show();
                break;
        }

        //now we close the drawer
        drawerLayout.closeDrawer(GravityCompat.START);

        return true; //so when each item is selected, mark it as selected
    }
}
