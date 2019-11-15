package com.example.pedarkharj_edit2.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.Contact;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Event;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.Routines;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    final public static int INTENT_CODE = 1;
    final public static String INTENT_MASSEGE = "NEW_NAME";
    RecyclerView recyclerView;
    ArrayList<Contact> mContacts;
    DatabaseHelper db;

    ParticipantAdapter adaptor;
    String newName;
    Context mContext = this;
    Activity mActivity = this;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        mContacts = new ArrayList<Contact>();
        db = new DatabaseHelper(mContext);

        //Floating Btn
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//            startActivityForResult(new Intent(mContext, AddContactActivity.class), INTENT_CODE);
            startActivity(new Intent(mContext, AddContactActivity.class));
            finish();
        });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 1){
//            newName = data.getStringExtra(INTENT_MASSEGE);
//            if (newName != null){
//                participants.add(new Participant(newName));
//                doRecyclerView();
//            }
//        }
//    }

    /********************************************       Methods     ****************************************************/

    private void doRecyclerView() {


        /**
         * todo I'm forming Contacts as Participants, so I won't need to create another adaptor or even edit that. change this shit later in order not to get fucked up!
         */
        List<Contact> mContacts0 = db.getAllContacts();
        List<Participant> participants = new ArrayList<>(mContacts0.size());

        mContacts.addAll(mContacts0);
        for (Contact c : mContacts0){
            Participant participant = new Participant();
            participant.setContact(c);
            participant.setId((int) c.getId());
            participant.setName(c.getName());
            participants.add(participant);
//
//            Log.d("Contact", c.getId()+ " : "+ c.getName());
//            Log.e("Contact", participant.getId()+ " : "+ participant.getName());
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adaptor = new ParticipantAdapter(mContext, R.layout.sample_conntacts_horizental, participants);
        recyclerView.setAdapter(adaptor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //recyclerView
        recyclerView = findViewById(R.id.contacts_recView);
        doRecyclerView();
    }
}
