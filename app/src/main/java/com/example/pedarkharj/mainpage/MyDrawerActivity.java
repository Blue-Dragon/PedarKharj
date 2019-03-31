package com.example.pedarkharj.mainpage;

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

import com.example.pedarkharj.R;

import java.util.Objects;

public class MyDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_drawer);

        //Toolbar
        mToolbar = findViewById(R.id.m_toolbar);
        setSupportActionBar(mToolbar);

        /*****************          Drawer          ******************/
        //drawerLayout
        drawerLayout = findViewById(R.id.m_drawer);

        //Nav View
        navigationView = findViewById(R.id.m_navigation_view);
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
        /*****************          Drawer />          ******************/

    }

    /*****************          Drawer          ******************/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
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


}
