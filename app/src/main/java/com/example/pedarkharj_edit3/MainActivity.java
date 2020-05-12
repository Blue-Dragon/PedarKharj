package com.example.pedarkharj_edit3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;

import com.example.pedarkharj_edit3.classes.MyCallBack;
import com.example.pedarkharj_edit3.classes.web_db_pref.SharedPrefManager;
import com.example.pedarkharj_edit3.pages.IntroActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.pages.fragments.ContactsFragment;
import com.example.pedarkharj_edit3.pages.fragments.EventsFragment;
import com.example.pedarkharj_edit3.pages.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity  {
    public static MyCallBack myCallBack;

    Context mContext = this;
    Activity mActivity = this;
    public static DatabaseHelper db;
    public static short navPosition;
    Fragment selectedFragment;
    EventsFragment eventsFragment;
    ContactsFragment contactsFragment;
    boolean isFirstTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isFirstTime = SharedPrefManager.getInstance(mContext).getFirstTimeRun();
        if (isFirstTime){
            startActivity(new Intent(mContext, IntroActivity.class));
            SharedPrefManager.getInstance(mContext).isFirstTimeRun(false);
            finish();
        }

        //------------------------z-     Fragments   -------------------------- //
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

        myCallBack = (pageId) -> Routines.restartPage(mActivity, pageId); //restart and update activity from other pages
    }


    /********************************************       Methods     ****************************************************/

    //-------------------------      permission stuff      --------------------------//
    // Routines.requestPermissions...
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionContact = grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (grantResults.length > 0 && permissionContact) {
//            if (navPosition == Routines.CONTACTS ) {
                contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                contactsFragment.readSystemContacts();
//            }
        } else {
//            Toast.makeText(mActivity, "مجوز دسترسی contacts داده نشد", Toast.LENGTH_SHORT).show();
            Toast.makeText(mActivity, "برای اتقال مخاطبین گوشی به اینجا به ابن  مجوز دسترسی نیاز داریم!", Toast.LENGTH_SHORT).show();
        }

    }

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
        //if in Home page
        if (navPosition == Routines.HOME ){
            //Close app
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
//            finish();
        }

        else  if (navPosition == Routines.EVENTS && Routines.is_in_action_mode) {
            eventsFragment = (EventsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            eventsFragment.onMyBackPressed();
        }
//        else  if (navPosition == Routines.CONTACTS && Routines.is_in_action_mode) {
//            contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//            contactsFragment.onMyBackPressed();
//        }

        else
            findViewById(R.id.nav_home).callOnClick();

    }

}
