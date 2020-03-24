package com.example.pedarkharj_edit3.pages;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class AddEventParticesActivity extends AppCompatActivity {
    Context mContext;
    RecyclerView recyclerView, selected_recView;
    List<Contact> contacts;
    List<Participant> allContactsTo_participants, selectedPartices;
    MyAdapter adaptor, selectedAdaptor;
    DatabaseHelper db;
    Event existedEvent;

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
            existedEvent = db.getEventById(curEventId);
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
//                selectedPartices.add(participant);
//                selectedAdaptor.notifyDataSetChanged(); //setSelectedRecView(selectedPartices);
                addPartice(participant);

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
                //remove partice from event
                removePartice(participant);
//                setSelectedRecView(selectedPartices);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //-------------------------     Floating Btn    --------------------------//
        fab = this.findViewById(R.id.fab);
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
        adaptor = new MyAdapter(mContext, R.layout.sample_conntacts_horizental, allContactsTo_participants);
        recyclerView.setAdapter(adaptor);
    }


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


    private void removePartice(Participant participant) {
        new AlertDialog.Builder(mContext)
                .setTitle("Are you sure?")
                .setMessage("All data will be lost")
                .setPositiveButton("Yep", (dialogInterface, i) -> {
                    selectedPartices.remove(participant);
                    db.deletePartic(participant);
                    selectedAdaptor.notifyDataSetChanged();
                })
                .setNegativeButton("No, wait!", (dialogInterface, i) -> {})
                .show();
    }

    private void addPartice(Participant participant) {
        selectedPartices.add(participant);
        if (edit_mode) db.createParticipantUnderEvent(participant, existedEvent);
        selectedAdaptor.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

}
