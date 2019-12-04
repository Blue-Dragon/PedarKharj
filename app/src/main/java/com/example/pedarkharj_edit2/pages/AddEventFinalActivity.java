package com.example.pedarkharj_edit2.pages;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Event;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit2.classes.Routines;

import java.util.ArrayList;
import java.util.List;

public class AddEventFinalActivity extends AppCompatActivity {
    Context mContext;
    ArrayList<Participant> mParticipants;
    ParticipantAdapter adapter;
    DatabaseHelper db;

    Dialog d;
    Button yes, no;
    ImageView cancelImg, doneImg;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        mContext = this;
        db = new DatabaseHelper(mContext);

//        //back imageView btn
//        ImageView backBtn = findViewById(R.id.back_btn);
//        backBtn.setOnClickListener(item -> finish());


        //-------------------------     Floating Btn    --------------------------//
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            startActivity(new Intent(mContext, EventMngActivity.class));
            finish();
        });

        //-------------------------     RecView    --------------------------//
        recyclerView = findViewById(R.id.rv);
        doRecyclerView();

        //
        db.closeDB();
    }


    // ---------------------------  Methods  --------------------------- //

    private void doRecyclerView() {
        int[] ids = getIntent().getIntArrayExtra(Routines.NEW_EVENT_PARTIC_IDS_INTENT);

        mParticipants = new ArrayList<Participant>();
        for (int i : ids){
            mParticipants.add(db.getParticeById(i));
        }


        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        //
        adapter = new ParticipantAdapter(mContext, R.layout.sample_contact, mParticipants);
        recyclerView.setAdapter(adapter);
    }


    //todo: BUG
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int eventId = getIntent().getIntExtra(Routines.NEW_EVENT_PARTIC_EVENT_ID_INTENT, 0);
        try {
            Routines.deleteTempEvent(mContext, eventId);

        }catch (Exception e){
            Log.d("Fuck06", "eventId: "+ eventId + "\n"+ e + "");
        }
    }


}
