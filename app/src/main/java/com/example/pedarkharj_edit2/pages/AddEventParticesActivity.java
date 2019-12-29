package com.example.pedarkharj_edit2.pages;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pedarkharj_edit2.MainActivity;
import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.Contact;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Event;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit2.classes.Routines;

import java.util.ArrayList;
import java.util.List;

public class AddEventParticesActivity extends AppCompatActivity {
    Context mContext;
    RecyclerView recyclerView, selected_recView;
    List<Contact> contacts;
    List<Participant> allContactsTo_participants, selectedPartices;
    ParticipantAdapter adaptor, selectedAdaptor;
    DatabaseHelper db;

    FloatingActionButton fab;
    boolean edit_mode;
    int curEventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_partices);
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        mContext = this;
//        edit_mode = false;

        //back imageView btn
        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(item -> onBackPressed());


        /**
         * RecView & DB
         */
        db = new DatabaseHelper(mContext);
        allContactsTo_participants = new ArrayList<>();
        selectedPartices = new ArrayList<>();


        //-------------------------     RecView    --------------------------//
        recyclerView = findViewById(R.id.rv);
        selected_recView = findViewById(R.id.rv_01);
        setRecView(); //show contacts (allContactsTo_participants) and init selectedPartices
        /*
         * init selectedPartice if we are in edit mode
         */
        curEventId = getIntent().getIntExtra(Routines.SEND_EVENT_ID_INTENT, 0);
        if (curEventId > 0){
            edit_mode = true;
            selectedPartices = db.getAllParticeUnderEvent(curEventId);
        }
        setSelectedRecView(selectedPartices);

        /*
         * onClick
         */
        Log.e("recOnClick", "onClick");
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Participant participant = allContactsTo_participants.get(position);
                selectedPartices.add(participant);
                selectedAdaptor.notifyDataSetChanged(); //setSelectedRecView(selectedPartices);

                Log.d("recOnClick", participant.getName());
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        /*
         * onClick selected
         */
        Log.e("Selected_RecOnClick", "onClick");
        selected_recView.addOnItemTouchListener(new RecyclerTouchListener(mContext, selected_recView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Participant participant = selectedPartices.get(position);
                selectedPartices.remove(participant);
//                setSelectedRecView(selectedPartices);
                selectedAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //-------------------------     Floating Btn    --------------------------//
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            if (edit_mode){
                Event event = db.getEventById(curEventId);
                event.se
                db.updateEvent();
            }
            //get saved partices in tempEvent
            Routines.addParticesToTempEvent(selectedPartices, db);

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
                    intent.putExtra(Routines.EDIT_MODE, Routines.IS_EDIT_MODE);
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
        contacts = db.getAllContacts();
        allContactsTo_participants = Routines.contactToPartic(contacts);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        //
        adaptor = new ParticipantAdapter(mContext, R.layout.sample_conntacts_horizental, allContactsTo_participants);
        recyclerView.setAdapter(adaptor);
    }


    private void setSelectedRecView(List<Participant> participants){

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        selected_recView.setLayoutManager(gridLayoutManager);
        //
        selectedAdaptor = new ParticipantAdapter(mContext, R.layout.sample_contact, participants);
//        recyclerView.smoothScrollToPosition( allContactsTo_participants.size() - 1 ); // focus on the End of the list
        selected_recView.setAdapter(selectedAdaptor);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

}
