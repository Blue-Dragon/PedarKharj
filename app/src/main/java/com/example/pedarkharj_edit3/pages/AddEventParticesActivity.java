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
    List<Contact> selectedContactsNew, existedContacts;
    List<Participant> allContactsTo_participants, selectedPartices;
    List<Integer> selectedContactsIdsNew;
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
        setRecView_horizental(true); //show contacts (allContactsTo_participants) and init selectedContactsNew
        setSelectedRecView(); //setting selectedContactsNew if we are in edit mode

         // onClick
        Log.e("recOnClick", "onClick");
        recyclerView_horizental.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView_horizental, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Participant participant = allContactsTo_participants.get(position);
                Contact contact = contacts.get(position);
                //reInit selectedContactsNew
                initSelectedContactIds();


                if (selectedContactsIdsNew.contains((int) contact.getId()) )
                    removePartice(view, contact);
                else
                    addPartice(view, contact);

//                Log.d("recOnClick", contact.getName());
//                getSelectecPartice();

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
//                Participant participant = selectedPartices.get(position);
                Contact contact = selectedContactsNew.get(position);
                //remove partice from event
                removePartice(view, contact);
                setRecView_horizental(true);

//                setSelectedRecView(selectedPartices);
                getSelectecPartice();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //------------     Floating Btn    ------------//
        fab.setOnClickListener(view -> {
//            if (edit_mode) db.createParticipantUnderEvent(participant, existedEvent);

            removeExistedParticeFromBefore();
            for (Contact contact: selectedContactsNew){
                Log.d("selectedContactsNew", ""+ contact.getName());
            }
            Log.d("selectedContactsNew", ".");

            //get saved partices in tempEvent
            if (!edit_mode){
                selectedPartices = Routines.addParticesToTempEvent(selectedContactsNew, db);

            }else if (existedEvent != null){
//                db.deleteAllParticeUnderEvent(existedEvent);

//                List<Participant> participants = db.getAllParticeUnderEvent(existedEvent);
//                for (Participant participant: participants){
//                    Log.d("eventParticipants", participant.getId()+ " / "+ participant.getContact().getId() + ": "+ participant.getName());
//                }
                selectedPartices = db.createEventNewParticipants(existedEvent, selectedContactsNew); //save new added ones
//                db.createAllParticesUnderEvent(selectedPartices, existedEvent);
//                db.createNewEventWithContacts(existedEvent, selectedContactsNew);
                selectedPartices = db.getAllParticeUnderEvent(existedEvent); // save all partices to pass to next slide
            }

            int[] myIds = new int[selectedPartices.size()];
            int i = 0;
            for (Participant participant: selectedPartices){
                Log.d("allParticesIds", "i: "+ i );
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

    private void removeExistedParticeFromBefore() {
        for (Contact contact : existedContacts){
            if (selectedContactsNew.contains(contact)){
                selectedContactsNew.remove(contact);
            }
        }
        initSelectedContactIds();
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
        selectedContactsNew = new ArrayList<>();
        allContactsTo_participants = Routines.contactToPartic(contacts);
        selectedContactsIdsNew = new ArrayList<>();
        existedContacts = new ArrayList<>();
        Routines.contactsSelectedIds = initSelectedContactIds(); //init both `selectedContactsIdsNew` and `Routines.contactsSelectedIds`

        /*
         * init selectedPartice if we are in edit mode
         */
        curEventId = getIntent().getIntExtra(Routines.SEND_EVENT_ID_INTENT, 0);
        if (curEventId > 0){
            edit_mode = true;
            existedEvent = db.getEventById(curEventId);
            selectedPartices = db.getAllParticeUnderEvent(curEventId);
            existedContacts = getContacts(selectedPartices);
            selectedContactsNew = getContacts(selectedPartices); //then we delete common contacts in both lists

        }

    }

    private List<Contact> getContacts(List<Participant> selectedPartices) {
        List<Contact> contactList = new ArrayList<>();
        for (Participant participant: selectedPartices){
            contactList.add(participant.getContact());
        }
        return contactList;
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

        adaptor = new MyAdapter(mContext);
        adaptor.setLayout(R.layout.sample_conntacts_horizental);
        adaptor.setContactList(contacts);
        //
        initSelectedContactIds();
        Log.d("Fuck0", selectedContactsIdsNew.size() + "  selectedContactsIdsNew");

        if (edit_mode || isAddEventParticeMode){
            adaptor.setIsAddEventParticeMode(true);
            Log.d("Fuck0", Routines.contactsSelectedIds.size() + "  screw u");
        }
        //
        recyclerView_horizental.setAdapter(adaptor);
    }


    /**
     * setting selectedPartice if we are in edit mode
     */
    private void setSelectedRecView(){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        selected_recView.setLayoutManager(gridLayoutManager);
        //

        selectedAdaptor = new MyAdapter(mContext);
        selectedAdaptor.setLayout(R.layout.sample_contact);
        selectedAdaptor.setContactList(selectedContactsNew);
//        recyclerView_horizental.smoothScrollToPosition( allContactsTo_participants.size() - 1 ); // focus on the End of the list
        selected_recView.setAdapter(selectedAdaptor);

    }


    private void removePartice(View view, Contact contact) {
//        db.deletePartic(participant);
        contact = findContactById(selectedContactsNew, contact.getId());
        selectedContactsNew.remove(contact);
        removeColorSelected(view); //change color
        initSelectedContactIds();
        selectedAdaptor.notifyDataSetChanged();
//        adaptor.notifyDataSetChanged();
    }

    private Contact findContactById(List<Contact> selectedContacts, long id) {
        Contact contact = null;
        for (Contact contact1 : selectedContacts){
            if (contact1.getId() == id){
                contact = contact1;
                break;
            }
        }
        return contact;
    }

    private void removeColorSelected(View view) {
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent_white));

    }

    private void addPartice(View view, Contact contact) {
        selectedContactsNew.add(contact);
//        if (edit_mode) db.createParticipantUnderEvent(participant, existedEvent);
        //change color
        setColorSelected(view);
        initSelectedContactIds();
        selectedAdaptor.notifyDataSetChanged();
    }

    private void setColorSelected(View view) {
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.selected_green));
    }

//    private boolean checkIfAlreadySelected(Participant participant) {
//        //selectedContactsIdsNew of all selected partices
//        selectedContactsIdsNew.clear();
//        Routines.contactsSelectedIds.clear();
//
//        for (int i=0; i<selectedPartices.size(); i++){
//            selectedContactsIdsNew.add(i, (int) selectedPartices.get(i).getContact().getId());
//            Routines.contactsSelectedIds.add(i, (int) selectedPartices.get(i).getContact().getId());
//        }
//        if (selectedContactsIdsNew.size() == 0) return false;
//
//        return selectedContactsIdsNew.contains((int) participant.getContact().getId());
//    }

    private List<Integer> initSelectedContactIds() {
        //selectedContactsIdsNew of all selected partices
        selectedContactsIdsNew.clear();
        Routines.contactsSelectedIds.clear();

        for (int i = 0; i< selectedContactsNew.size(); i++){
            selectedContactsIdsNew.add(i, (int) selectedContactsNew.get(i).getId());
        }

        return Routines.contactsSelectedIds = selectedContactsIdsNew;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    void getSelectecPartice(){
        StringBuilder builder = new StringBuilder();
        builder.append("selected partices:\n");
        for (Participant participant: selectedPartices){
            builder.append(participant.getId()+" : "+ participant.getContact().getId()+" -> "+ participant.getName()+"\n");
        }
        builder.append("\n");
        Log.d("selectedParticeIds", builder.toString());
    }



}
