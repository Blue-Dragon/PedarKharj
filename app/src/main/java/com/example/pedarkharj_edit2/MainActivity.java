package com.example.pedarkharj_edit2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pedarkharj_edit2.classes.BuyerDialog;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.Routines;
import com.example.pedarkharj_edit2.pages.AddExpenseActivity;
import com.example.pedarkharj_edit2.pages.ContactsActivity;
import com.example.pedarkharj_edit2.pages.DiffDongActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    RecyclerView recyclerView;
    ArrayList<Participant> participants;
    ParticipantAdapter adaptor;
    //
    Context mContext = this;
    Activity mActivity = this;
    FloatingActionButton fab;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        //
       Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        //Floating Btn
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            startActivity(new Intent(mContext, AddExpenseActivity.class));
            showBuyerDialog();
        });

        //recyclerView
        recyclerView = findViewById(R.id.rv_partice_expenses);
        doRecyclerView();
        //TODO: hide the fucking fab while scrolling
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (fab.getVisibility() != View.VISIBLE) fab.show();
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                while (dy == 0 && fab.getVisibility() == View.VISIBLE) fab.hide();
//
//
//            }
//        });
        //

         //drawerLayout
        createDrawer();

    }


    /********************************************       Methods     ****************************************************/

    private void showBuyerDialog() {
        new BuyerDialog(this).show();
    }


    private void doRecyclerView() {
        participants = new ArrayList<Participant>();
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.q), "Ali", 1000, 2050));
        participants.add(new Participant( "Reza", 15000, 2050));
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.r), "Mamad", 1000, 500));
//        participants.add(new Participant( "Hami", 5000, 2050));
//        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.q), "sadi", 1000, 2500));
//        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.r), "dad", 0, 2000));
//        participants.add(new Participant( "mom", 0, 6000));
//        participants.add(new Participant( "Ali", 1000, 2050));
//        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.r), "Reza", 15000, 2050));
//        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.w), "Mamad", 1000, 500));
        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        adaptor = new ParticipantAdapter(mContext, participants);
        recyclerView.setAdapter(adaptor);
    }


    /*****************          Drawer           ******************/
    private void createDrawer() {
        drawerLayout = findViewById(R.id.m_drawer);
        menu = findViewById(R.id.menu); menu.setOnClickListener(this);

        //Nav View
        navigationView = findViewById(R.id.m_navigation_view); //already initiated
        navigationView.setNavigationItemSelectedListener(this); //onNavigationItemSelected() metod

        //toggle (it takes care of drawable methods)
        toggle = new ActionBarDrawerToggle(mActivity, drawerLayout, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState(); //rotating the icon
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_contacts:
                startActivity(new Intent(mContext, ContactsActivity.class));
                break;
            case R.id.nav_def_event:
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Frag2()).commit();
                break;
            case R.id.nav_addNewEvent:
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Frag3()).commit();
                break;

        }

        //now we close the drawer
        drawerLayout.closeDrawer(GravityCompat.START);

        return true; //so when each item is selected, mark it as selected
    }

    //open drawer onclick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
        }
    }


    //open drawer onclick
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    /*******************/

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
