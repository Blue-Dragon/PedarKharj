package com.example.pedarkharj_edit2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit2.classes.BuyerDialog;
import com.example.pedarkharj_edit2.classes.Contact;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Event;
import com.example.pedarkharj_edit2.classes.Expense;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.PersianDate;
import com.example.pedarkharj_edit2.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit2.classes.Routines;
import com.example.pedarkharj_edit2.classes.SharedPrefManager;
import com.example.pedarkharj_edit2.pages.ContactsActivity;
import com.example.pedarkharj_edit2.pages.EventMngActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    List<Participant> mParticipants;
    List<Event> events ;
    Map spinnerEventIds;

    public static int defEventId;
    Context mContext = this;
    Activity mActivity = this;
    ParticipantAdapter adaptor;
    Event defEvent, curEvent;
    DatabaseHelper db;
    //
    RecyclerView recyclerView;
    FloatingActionButton fab;
    Spinner spinner;
    CircleImageView drawerProfPic;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageView menu;
    TextView tvR1, tvR2, tvC1, tvC2, tvL1, tvL2; //The rectangle above
    //
    int sentEventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

       Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        mParticipants = new ArrayList<>();
        db = new DatabaseHelper(mContext);
        events  = db.getAllEvents(); //for spinner && def partices
        spinnerEventIds =  new HashMap<Integer, Event>();
        defEventId = SharedPrefManager.getInstance(mContext).getDefEventId();


        sentEventId = getIntent().getIntExtra(Routines.SEND_EVENT_ID_INTENT, 0);

        //the rectangle above
        tvL1 = findViewById(R.id. tv_title_my_expense);
        tvL2 = findViewById(R.id.tv_my_expense );
        tvC1 = findViewById(R.id. tv_title_my_dong);
        tvC2 = findViewById(R.id. tv_my_dong);
        tvR1 = findViewById(R.id. tv_title_my_result);
        tvR2 = findViewById(R.id. tv_my_result);

        /*-------------------------     RecView   --------------------------*/

        /**
         * Setting default event and partices
         */
        createDefEvent();

        recyclerView = findViewById(R.id.rv_partice_expenses);

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




        /* -------------------------     Spinner    -------------------------- */
        spinner = findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();

        events = db.getAllEvents();
        for (Event event:events){
            if (!event.getEventName().equals(Routines.EVENT_TEMP_NAME)) {
                list.add(event.getEventName());
                //save events ids in spinner
                spinnerEventIds.put(event.getId(), event);
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        if (events.size() == 0 ) createDefEvent();

        /**
         *  set the event to show as recView
         */
        //def & cur Event
        if (defEventId > 0 ){
            Log.i("fuck016", defEventId + "");
            defEvent = db.getEventById(defEventId);
        }else  defEvent = events.get(0);

        curEvent = defEvent; // if we haven't chosen yet
        SharedPrefManager.getInstance(mContext).saveDefEvent(defEvent); //save  defEvent for next time to SharedPref
        Log.i("fuck011", "defEventId: " + defEventId+ "");

        //if we have a chosen event already
        if ( sentEventId > 0 ) {
            curEvent = db.getEventById(sentEventId);
        }
        spinner.setSelection( curEvent.getId() - 1); //def event

        spinner.setOnItemSelectedListener(this);
        Log.i("fuck011", "saveDefEvent: " + curEvent.getId()+ "");


        /**
         * recView onClick
         */
        List<Expense> expenseList = db.getAllExpensesOfEvent(curEvent);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                StringBuilder builder = new StringBuilder();
                Participant participant = mParticipants.get(position);
                Log.d("fuck023", "." );                Log.d("fuck023", "." );

                Log.d("fuck023", "partic name: " + participant.getName());


                int i =0;
                for (Expense expense : expenseList){

                    List<Participant> users = expense.getUserPartics();
                    for (Participant user: users){

                        if (user.getId() == participant.getId())
                            builder.append(expense.getCreated_at()).append("\n");
                            if (expense.getBuyer().getId() == participant.getId()) builder.append(expense.getExpensePrice()).append(" طلب و");
                            builder.append(expense.getExpenseDebts().get(0)).append(" بدهی از خرج: ").append(expense.getExpenseTitle()).append("\n\n");
                    }
                }

                new AlertDialog.Builder(mContext)
                        .setTitle("خلاصه خرج ها:")
                        .setMessage(builder.toString())
                        .show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Participant participant = mParticipants.get(position);
                Log.d("recOnClick", participant.getResult());
            }
        }));

        //-------------------------     Floating Btn    --------------------------//
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            startActivity(new Intent(mContext, AddExpenseActivity.class));

            showBuyerDialog(curEvent);
        });




        //drawerLayout
        createDrawer();

        //Close db
        db.closeDB();
    }



    /********************************************       Methods     ****************************************************/

    private void showBuyerDialog(Event curEvent) {
        new BuyerDialog(this, curEvent).show();
    }

    //-------------------------     Spinner    --------------------------//
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Event event = db.getEventById(i+1);

        if (event != null)    {
            curEvent = event;
            setRecParticesUnderEvent(curEvent);
            SharedPrefManager.getInstance(mContext).saveDefEvent(curEvent); //save curEvent (as defEvent for next time) to SharedPref
            initRectangleAbove();
            //init Rectangle
            int myExpenses = db.getAllParticExpensesByParticeId(1);
            int myDebt = db.getAllParticDebtsByParticeId(1);
            int allEventExpenses = db.getEventAllExpensesByEventId(event.getId());
            tvL2.setText(String.valueOf(allEventExpenses));
            tvC2.setText(String.valueOf(myExpenses));
            tvR2.setText(String.valueOf(myExpenses - myDebt));

        }
    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //-------------------------     RecyclerView    --------------------------//
    private void setRecParticesUnderEvent(Event curEvent) {

        //show partices of the Event
        mParticipants = db.getAllParticeUnderEvent(curEvent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        adaptor = new ParticipantAdapter(mContext, R.layout.sample_participant,  mParticipants);
        recyclerView.setAdapter(adaptor);
    }
    private void createDefEvent() {
        if (events.size() < 1 ){

            /*
             * adding partices and an event
             */
            long contact_1 =  db.createContact(new Contact("Hamed"));
            long contact_2 = db.createContact(new Contact("Reza"));
            long contact_3 =  db.createContact(new Contact("Sadi"));
            long contact_4 = db.createContact(new Contact("Abbas"));

            db.createNewEventWithPartices(new Event("سفر شمال")
                    , new Contact[]{db.getContactById(contact_1), db.getContactById(contact_2)
                            ,db.getContactById(contact_3), db.getContactById(contact_4), });

        }
    }


//-------------------------     Drawer    --------------------------//
    private void createDrawer() {
        menu = findViewById(R.id.menu); menu.setOnClickListener(this);
        drawerLayout = findViewById(R.id.m_drawer);

        //Nav View
        navigationView = findViewById(R.id.m_navigation_view); //already initiated
        navigationView.setNavigationItemSelectedListener(this); //onNavigationItemSelected() metod

        View mView = navigationView.getHeaderView(0);
        drawerProfPic = mView.findViewById(R.id.nav_profile_pic);
        drawerProfPic.setImageBitmap(Routines.resizeBitmap(mContext, R.drawable.profile) ); //set a def pic for nav_view

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
            case R.id.nav_events:
                startActivity(new Intent(mContext, EventMngActivity.class));
                break;

            case R.id.nav_our_number:
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Frag2()).commit();
                break;
            case R.id.nav_our_email:
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

        if (sentEventId != 0){
            // if we aren't  get back to EventMngActivity
            super.onBackPressed();
        } else {

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

    //todo: complete it
    private void initRectangleAbove() {
        tvL2.setText(String.valueOf(0));

    }


}
