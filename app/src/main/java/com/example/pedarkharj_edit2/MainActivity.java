package com.example.pedarkharj_edit2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pedarkharj_edit2.classes.BuyerDialog;
import com.example.pedarkharj_edit2.classes.Contact;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Event;
import com.example.pedarkharj_edit2.classes.Expense;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit2.pages.ContactsActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    RecyclerView recyclerView;
    List<Participant> mParticipants;
    ParticipantAdapter adaptor;
    //
    Context mContext = this;
    Activity mActivity = this;
    FloatingActionButton fab;
    DatabaseHelper db;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
       Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        mParticipants = new ArrayList<>();

        //Floating Btn
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            startActivity(new Intent(mContext, AddExpenseActivity.class));
            showBuyerDialog();
        });

        //-------------------------     SQLite    --------------------------//
        db = new DatabaseHelper(mContext);


        //todo: set default event and partices
        /*
         * adding partices and an event
         */
//        long contact_1 =  db.createContact(new Contact("Hamed"));
//        long contact_2 = db.createContact(new Contact("Reza"));
//        long contact_3 =  db.createContact(new Contact("Sadi"));
//        long contact_4 = db.createContact(new Contact("Abbas"));
//
//        db.createNewEventWithPartices(new Event("سفر")
//                , new Contact[]{db.getContactById(contact_1), db.getContactById(contact_2)
//                        ,db.getContactById(contact_3), db.getContactById(contact_4), });

        /*
         * adding a new expense with different debts
         */
        float price = 20000f;

        //update buyer expense
        Participant buyer = db.getParticeById(1);
        float old_expense = buyer.getExpense();
        buyer.setExpense(old_expense+price);
        db.updatePartice(buyer);
        Log.e("Fuck", "old: "+ old_expense+ "\nnew: "+ buyer.getExpense() );

        // A fake result of DiffDong Activity
        float[] newDebts = {2500f, 5000f, 1200f, 1800f};
        int[] userIds = new int[] {1,2,3,4};

        float debt;
        float ex_debts;
        float newDebt;
        int count = 0;
        for (int i : userIds){
            Participant user = db.getParticeById(i);
            Log.d("Fuck", "user "+ i +"  expense: "+ user.getExpense() );
            Log.d("Fuck", "user "+ i +"  debt "+ user.getDebt() );

            float mNewDebt = newDebts[count++];

            // update users debts in Partice Table
            ex_debts = user.getDebt();
            newDebt = ex_debts + mNewDebt;
            user.setDebt(newDebt);
            db.updatePartice(user);

            // set an expense in Expense Table
            debt = mNewDebt > 0 ? mNewDebt : price/userIds.length;
            Log.d("DEBT", String.valueOf(debt));
            Expense expense = new Expense(db.getEventById(1) ,db.getParticeById(1), new Participant[] {user}, "Fast food", price, debt);
            db.addExpense(expense);

            Log.e("Fuck", "user "+ i +" new expense: "+ user.getExpense() );
            Log.e("Fuck", "user "+ i +" new debt "+ user.getDebt() );
        }



        //recyclerView
        recyclerView = findViewById(R.id.rv_partice_expenses);
        setRecParticesUnderEvent(); //show partices of the Event

        /**
         * recView onClick
         */
        Log.e("recOnClick", "onClick");
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Participant participant = mParticipants.get(position);
                Log.d("recOnClick", participant.getName());
            }

            @Override
            public void onLongClick(View view, int position) {
                Participant participant = mParticipants.get(position);
                Log.d("recOnClick", participant.getResult());
            }
        }));


        /**
         * TODO: hide the fucking fab while scrolling
         */
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


        //Close db
        db.closeDB();
    }


    /********************************************       Methods     ****************************************************/

    private void showBuyerDialog() {
        new BuyerDialog(this).show();
    }


    //-------------------------     RecyclerView    --------------------------//
    private void setRecParticesUnderEvent() {

        //show partices of the Event todo: update -> get event
        Event event = db.getEventById(1);
        List<Participant> participants0 = db.getAllParticeUnderEvent(1);
        Log.d("Event", event.getEventName());
        mParticipants.addAll(participants0);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        adaptor = new ParticipantAdapter(mContext, participants0);
        recyclerView.setAdapter(adaptor);
    }


//-------------------------     Drawer    --------------------------//
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
