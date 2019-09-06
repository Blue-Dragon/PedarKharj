package com.example.pedarkharj_edit2.pages;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.BuyerDialog;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.Routines;

import java.util.ArrayList;
import java.util.List;

public class DiffDongActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView recyclerView;
    ArrayList<Participant> participants;
    ParticipantAdapter adaptor;

    Context mContext = this;
    Activity mActivity = this;
    FloatingActionButton fab;
    Spinner spinner;


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
        doRecyclerView(false);

        spinner = findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("تعداد دنگ");
        list.add("مقداد دنگ");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(this);
    }




    /********************************************       Methods     ****************************************************/
    private void doRecyclerView(boolean mode_02) {
        boolean b = mode_02;

        participants = new ArrayList<Participant>();
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.q), "Ali", 0));
        participants.add(new Participant( "Reza",2));
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.r), "Mamad",0));
        participants.add(new Participant( "Hami",1));
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.q), "sadi", 2));
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.r), "dad",1));
        participants.add(new Participant( "mom",2));
        participants.add(new Participant( "Ali",4));
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.r), "Reza",3));
        participants.add(new Participant(Routines.drawableToBitmap(mContext, R.drawable.w), "Mamad",22));
        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        if (!b) adaptor = new ParticipantAdapter(mActivity, R.layout.sample_diff_dong, participants);
        else adaptor = new ParticipantAdapter(mActivity, R.layout.sample_diff_dong_mode2, participants);
        recyclerView.setAdapter(adaptor);
    }

    //spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedIten = parent.getItemAtPosition(position).toString();
        if (selectedIten.equals("تعداد دنگ")){
            doRecyclerView(false);
        }else{
            doRecyclerView(true);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
