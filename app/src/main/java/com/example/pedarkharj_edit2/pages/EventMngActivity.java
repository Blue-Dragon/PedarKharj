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
import android.widget.ImageView;

import com.example.pedarkharj_edit2.MainActivity;
import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Event;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.Routines;

import java.util.ArrayList;
import java.util.List;

public class EventMngActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Event> mEvents;
    ParticipantAdapter adaptor;
    //
    Context mContext = this;
    FloatingActionButton fab;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_mng);
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        mEvents = new ArrayList<>();

        //back imageView btn
        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(item -> onBackPressed());

        //-------------------------     Floating Btn    --------------------------//
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            startActivity(new Intent(mContext, AddEventParticesActivity.class));
            finish();
//            showBuyerDialog();
        });

        //
        db = new DatabaseHelper(mContext);
        setRecView(); //show Events

        //
        db.closeDB();
    }


    //-------------------------     RecyclerView    --------------------------//
    private void setRecView() {
        recyclerView = findViewById(R.id.rv);

       mEvents = db.getAllEvents();
       // Not letting TempEvents to be shown
        List<Event> realEvents = new ArrayList<>();
        for (Event event : mEvents){
            if (!event.getEventName().equals(Routines.EVENT_TEMP_NAME)) realEvents.add(event);
        }


//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        //
        adaptor = new ParticipantAdapter(mContext);     adaptor.setEvents(realEvents);
        recyclerView.setAdapter(adaptor);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }
}
