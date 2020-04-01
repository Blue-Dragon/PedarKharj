package com.example.pedarkharj_edit3.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.models.Contact;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.Routines;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;

import java.util.ArrayList;
import java.util.List;

public class AddEventParticesActivity extends AppCompatActivity {
    List<Contact> contacts;
    List<Participant> allContactsTo_participants, selectedPartices;
    List<Integer> ids;
    Context mContext;
    Activity mActivity = this;
    MyAdapter adaptor, selectedAdaptor;
    DatabaseHelper db;
    Event existedEvent;

    int curEventId;
    boolean edit_mode;
    RecyclerView recyclerView, selected_recView;
    FloatingActionButton fab;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_partices);
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        inits();
        onClicks();
//        showSpotlightIntro();


        //-----------     RecView    -----------//

        setRecView(); //show contacts (allContactsTo_participants) and init selectedPartices
        setSelectedRecView(selectedPartices); //setting selectedPartice if we are in edit mode

         // onClick
        Log.e("recOnClick", "onClick");
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Participant participant = allContactsTo_participants.get(position);

                //checking if already selected
                if (checkIfAlreadySelected(participant)){
                    removePartice(view, participant);
                }else
                    addPartice(view, participant);


                Log.d("recOnClick", participant.getName());
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

         // onClick selected
        Log.e("Selected_RecOnClick", "onClick");
        selected_recView.addOnItemTouchListener(new RecyclerTouchListener(mContext, selected_recView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Participant participant = selectedPartices.get(position);
                //remove partice from event
                removePartice(view, participant);
//                setSelectedRecView(selectedPartices);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //------------     Floating Btn    ------------//
        fab.setOnClickListener(view -> {

            if (!edit_mode){
                //get saved partices in tempEvent
                Routines.addParticesToTempEvent(selectedPartices, db);
            }

            int[] ids = new int[selectedPartices.size()];
            int i = 0;
            for (Participant participant: selectedPartices){
                Log.d("Fuck06", "i: "+ i );
                ids[i++] = participant.getId();
            }

            if (selectedPartices.size() > 0){

                int eventId = selectedPartices.get(0).getEvent().getId();
//                Log.d("Fuck09", "eventId_ sent"+ eventId);
                Intent intent = new Intent(mContext, AddEventFinalActivity.class);
                if (edit_mode){
                    eventId = curEventId;
                    intent.putExtra(Routines.EDIT_MODE, Routines.EDIT_MODE_TRUE);
                }
                intent.putExtra(Routines.NEW_EVENT_PARTIC_IDS_INTENT, ids);
                intent.putExtra(Routines.NEW_EVENT_PARTIC_EVENT_ID_INTENT, eventId);
                startActivity(intent);
            }else {
                Toast.makeText(mContext, "لطفا اعضا را انتخاب کنید", Toast.LENGTH_SHORT).show();
            }
//
        });

        //
        db.closeDB();
    }



    /***********************************       METHODS     ***********************************/
    private void inits() {
        mContext = this;
//        edit_mode = false;

        backBtn = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.rv);
        selected_recView = findViewById(R.id.rv_01);
        fab = this.findViewById(R.id.fab);


        db = new DatabaseHelper(mContext);
        contacts = db.getAllContacts();
        selectedPartices = new ArrayList<>();
        allContactsTo_participants = Routines.contactToPartic(contacts);
        ids = new ArrayList<>();

        /*
         * init selectedPartice if we are in edit mode
         */
        curEventId = getIntent().getIntExtra(Routines.SEND_EVENT_ID_INTENT, 0);
        if (curEventId > 0){
            edit_mode = true;
            existedEvent = db.getEventById(curEventId);
            selectedPartices = db.getAllParticeUnderEvent(curEventId);
        }

    }
    private void onClicks() {
        backBtn.setOnClickListener(item -> onBackPressed());

    }

    //-------------------------     RecyclerView    --------------------------//
    private void setRecView() {
        /**
         *  All contacts
         */

        /**
         * todo: set Contact instead of Partice... :
         * I'm forming Contacts as Participants, so I won't need to create another
         * adaptor or even edit that. change this shit later in order not to get fucked up!
         */
//        contacts = db.getAllContacts();
//        allContactsTo_participants = Routines.contactToPartic(contacts);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //
        adaptor = new MyAdapter(mContext, R.layout.sample_conntacts_horizental, allContactsTo_participants);
        recyclerView.setAdapter(adaptor);
    }


    /**
     * setting selectedPartice if we are in edit mode
     */
    private void setSelectedRecView(List<Participant> participants){

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        selected_recView.setLayoutManager(gridLayoutManager);
        //
        selectedAdaptor = new MyAdapter(mContext, R.layout.sample_contact, participants);
//        recyclerView.smoothScrollToPosition( allContactsTo_participants.size() - 1 ); // focus on the End of the list
        selected_recView.setAdapter(selectedAdaptor);

    }


    private void removePartice(View view, Participant participant) {
        selectedPartices.remove(participant);
//        db.deletePartic(participant);
        selectedAdaptor.notifyDataSetChanged();
        //change color
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent_white));
    }

    private void addPartice(View view, Participant participant) {
        selectedPartices.add(participant);
        if (edit_mode) db.createParticipantUnderEvent(participant, existedEvent);
        selectedAdaptor.notifyDataSetChanged();
        //change color
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.selected_green));

    }

    private boolean checkIfAlreadySelected(Participant participant) {
        //ids of all selected partices
        ids.clear();
        for (int i=0; i<selectedPartices.size(); i++){
            ids.add(i, (int) selectedPartices.get(i).getContact().getId());
        }
        if (ids.size() == 0) return false;

        return ids.contains(participant.getId());
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    //----------------------    Spotlight       ------------------------//
//    private void showSpotlightIntro() {
//
//        int[] location = new int[2];
//        fab.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
//
//
//        SimpleTarget simpleTarget = new SimpleTarget.Builder(mActivity)
////                .setPoint(fab, fab) // position of the Target. setPoint(Point point), setPoint(View view) will work too.
//                .setPoint(x, y)
//                .setRadius(80f) // radius of the Target
//                .setTitle("the title") // title
//                .setDescription("the description") // description
//                .build();
//
//
//
//
//        fab.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override public void onGlobalLayout() {
//                fab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                Spotlight.with(mActivity)
//                        .setDuration(1) // duration of Spotlight emerging and disappearing in ms
//                        .setAnimation(new DecelerateInterpolator(2f)) // animation of Spotlight
//                        .setTargets(simpleTarget) // set targes. see below for more info
//                        // callback when Spotlight starts
//                        .setOnSpotlightStartedListener(() -> Toast.makeText(mContext, "spotlight is started", Toast.LENGTH_SHORT).show())
//                        // callback when Spotlight ends
//                        .setOnSpotlightEndedListener(() ->{
//                            Toast.makeText(mContext, "spotlight is ended", Toast.LENGTH_SHORT).show();
//                        })
//                        .start(); // start Spotlight
//            }
//        });
//
//    }


}
