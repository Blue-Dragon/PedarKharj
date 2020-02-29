package com.example.pedarkharj_edit3;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.pedarkharj_edit3.classes.IOnBackPressed;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.pages.fragments.ContactsFragment;
import com.example.pedarkharj_edit3.pages.fragments.EventsFragment;
import com.example.pedarkharj_edit3.pages.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity  {

    Context mContext = this;
    Activity mActivity = this;
    public static DatabaseHelper db;
    public static short navPosition;
    Fragment selectedFragment;
    EventsFragment eventsFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------------------     Fragments   -------------------------- //
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //setting default fragment
        if (savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        }

        //setting should-be-shown fragment
        switch (navPosition) {
            case Routines.EVENTS:
                findViewById(R.id.nav_events).callOnClick();
                break;
            case Routines.CONTACTS:
                findViewById(R.id.nav_contacts).callOnClick();
                break;
            case Routines.HOME:
                findViewById(R.id.nav_home).callOnClick();
                break;

        }


    }


    /********************************************       Methods     ****************************************************/

    //-------------------------     Fragments    --------------------------//
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_events:
                        selectedFragment = new EventsFragment();
                        break;
                    case R.id.nav_contacts:
                        selectedFragment = new ContactsFragment();
                        break;
                }

                if (selectedFragment != null)
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,selectedFragment)
                            .addToBackStack(null) //so we can use `onBackPressed()` method in fragments
                            .commit();
                else
                    Toast.makeText(MainActivity.this, "Null Fragment called", Toast.LENGTH_SHORT).show();

                return true;
            };

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
//        eventsFragment =(EventsFragment) getFragmentManager().findFragmentById(R.id.activity_main);

//        if (count == 0){
//            super.onBackPressed();
//        }else {
//            Toast.makeText(mContext, "count is: "+ count, Toast.LENGTH_SHORT).show();
//            getSupportFragmentManager().popBackStack();
//        }

        //if in Home page
        if (navPosition == Routines.HOME ){
            finish();

        }else  if (navPosition == Routines.EVENTS && Routines.is_events_in_action_mode) {
            eventsFragment = (EventsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            eventsFragment.onMyBackPressed();

        }else{
            findViewById(R.id.nav_home).callOnClick();
        }
    }

}
