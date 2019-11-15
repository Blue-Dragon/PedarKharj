package com.example.pedarkharj_edit2.pages;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.BuyerDialog;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Event;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.Routines;

import java.util.ArrayList;
import java.util.List;

public class DiffDongActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView recyclerView;
    ArrayList<Participant> mParticipants;
    DatabaseHelper db;
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

        mParticipants = new ArrayList<Participant>();
        db = new DatabaseHelper(mContext);

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
    private void doRecyclerView(boolean mode) {
        boolean b = mode;

        //show partices of the Event todo: update -> get event
        Event event = db.getEventById(1);
        List<Participant> participants0 = db.getAllParticeUnderEvent(1);
        mParticipants.addAll(participants0);

        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        if (!b) adaptor = new ParticipantAdapter(mActivity, R.layout.sample_diff_dong, mParticipants);
        else adaptor = new ParticipantAdapter(mActivity, R.layout.sample_diff_dong_mode2, mParticipants);
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
