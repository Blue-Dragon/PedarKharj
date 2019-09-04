package com.example.pedarkharj_edit2.pages;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.BuyerDialog;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.Routines;

import java.util.ArrayList;

public class DiffDongActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Participant> participants;
    ParticipantAdapter adaptor;

    Context mContext = this;
    Activity mActivity = this;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_dong);

        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        //Floating Btn
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            //todo: done btn
        });

        //recyclerView
        recyclerView = findViewById(R.id.diff_dong_recView);
        doRecyclerView();
    }




    /********************************************       Methods     ****************************************************/
    private void doRecyclerView() {
        participants = new ArrayList<Participant>();
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.q), "Ali"));
        participants.add(new Participant( "Reza"));
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.r), "Mamad"));
        participants.add(new Participant( "Hami"));
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.q), "sadi"));
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.r), "dad"));
        participants.add(new Participant( "mom"));
        participants.add(new Participant( "Ali"));
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.r), "Reza"));
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.w), "Mamad"));
        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        adaptor = new ParticipantAdapter(mContext, participants);
        recyclerView.setAdapter(adaptor);
    }

}
