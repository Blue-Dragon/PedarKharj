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
    RecyclerView recyclerView_horizental, selected_recView;
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
        setRecView_horizental(true); //show contacts (allContactsTo_participants) and init selectedPartices
        setSelectedRecView(selectedPartices); //setting selectedPartice if we are in edit mode

         // onClick
        Log.e("recOnClick", "onClick");
        recyclerView_horizental.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView_horizental, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Participant participant = allContactsTo_participants.get(position);
                //checking if already selected
                initSelectedParticeIds();

                if (ids.contains((int) participant.getContact().getId()) ){
                    Log.d("fuck00", "before: "+ selectedPartices.size());
                    removePartice(view, participant);
                    Log.d("fuck00", "after: "+ selectedPartices.size());
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
                setRecView_horizental(true);

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
            }else {

                db.deleteAllParticeUnderEvent(existedEvent);

                List<Participant> participants = db.getAllParticeUnderEvent(existedEvent);
                for (Participant participant: participants){
                    Log.d("eventParticipants", participant.getId()+ " / "+ participant.getContact().getId() + ": "+ participant.getName());
                }
                db.createAllParticesUnderEvent(selectedPartices, existedEvent);
            }

            int[] myIds = new int[selectedPartices.size()];
            int i = 0;
            for (Participant participant: selectedPartices){
                Log.d("Fuck06", "i: "+ i );
                myIds[i++] = participant.getId();
            }

            if (selectedPartices.size() > 0){

                int eventId = selectedPartices.get(0).getEvent().getId();
//                Log.d("Fuck09", "eventId_ sent"+ eventId);
                Intent intent = new Intent(mContext, AddEventFinalActivity.class);
                if (edit_mode){
                    eventId = curEventId;
                    intent.putExtra(Routines.EDIT_MODE, Routines.EDIT_MODE_TRUE);
                }
                intent.putExtra(Routines.NEW_EVENT_PARTIC_IDS_INTENT, myIds);
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
        recyclerView_horizental = findViewById(R.id.rv);
        selected_recView = findViewById(R.id.rv_01);
        fab = this.findViewById(R.id.fab);


        db = new DatabaseHelper(mContext);
        contacts = db.getAllContacts();
        selectedPartices = new ArrayList<>();
        allContactsTo_participants = Routines.contactToPartic(contacts);
        ids = new ArrayList<>();
        Routines.particSelectedIds = initSelectedParticeIds(); //init both `ids` and `Routines.particSelectedIds`

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
    private void setRecView_horizental(boolean isAddEventParticeMode) {
        /*
         *  All contacts
         */

        /*
         * todo: set Contact instead of Partice... :
         * I'm forming Contacts as Participants, so I won't need to create another
         * adaptor or even edit that. change this shit later in order not to get fucked up!
         */
//        contacts = db.getAllContacts();
//        allContactsTo_participants = Routines.contactToPartic(contacts);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false);
        recyclerView_horizental.setLayoutManager(gridLayoutManager);
        recyclerView_horizental.setItemAnimator(new DefaultItemAnimator());

        adaptor = new MyAdapter(mContext, R.layout.sample_conntacts_horizental,     allContactsTo_participants);
        //
        initSelectedParticeIds();
        Log.d("Fuck0", ids.size() + "  ids");

        if (edit_mode || isAddEventParticeMode){
            adaptor.setIsAddEventParticeMode(true);
            Log.d("Fuck0", Routines.particSelectedIds.size() + "  screw u");
        }
        //
        recyclerView_horizental.setAdapter(adaptor);
    }


    /**
     * setting selectedPartice if we are in edit mode
     */
    private void setSelectedRecView(List<Participant> participants){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        selected_recView.setLayoutManager(gridLayoutManager);
        //
        selectedAdaptor = new MyAdapter(mContext, R.layout.sample_contact, participants);

//        recyclerView_horizental.smoothScrollToPosition( allContactsTo_participants.size() - 1 ); // focus on the End of the list
        selected_recView.setAdapter(selectedAdaptor);

    }


    private void removePartice(View view, Participant participant) {
//        db.deletePartic(participant);
        selectedPartices.remove(participant);
        removeColorSelected(view); //change color
        initSelectedParticeIds();
        selectedAdaptor.notifyDataSetChanged();
//        adaptor.notifyDataSetChanged();
    }

    private void removeColorSelected(View view) {
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent_white));

    }

    private void addPartice(View view, Participant participant) {
        selectedPartices.add(participant);
        if (edit_mode) db.createParticipantUnderEvent(participant, existedEvent);
        //change color
        setColorSelected(view);
        initSelectedParticeIds();
        selectedAdaptor.notifyDataSetChanged();
    }

    private void setColorSelected(View view) {
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.selected_green));
    }

//    private boolean checkIfAlreadySelected(Participant participant) {
//        //ids of all selected partices
//        ids.clear();
//        Routines.particSelectedIds.clear();
//
//        for (int i=0; i<selectedPartices.size(); i++){
//            ids.add(i, (int) selectedPartices.get(i).getContact().getId());
//            Routines.particSelectedIds.add(i, (int) selectedPartices.get(i).getContact().getId());
//        }
//        if (ids.size() == 0) return false;
//
//        return ids.contains((int) participant.getContact().getId());
//    }

    private List<Integer> initSelectedParticeIds() {
        //ids of all selected partices
        ids.clear();
        Routines.particSelectedIds.clear();

        for (int i=0; i<selectedPartices.size(); i++){
            ids.add(i, (int) selectedPartices.get(i).getContact().getId());
        }


        return Routines.particSelectedIds = ids;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }




}
