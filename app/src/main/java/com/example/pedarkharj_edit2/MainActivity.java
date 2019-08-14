package com.example.pedarkharj_edit2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdaptor;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Participant> participants;
    ParticipantAdaptor adaptor;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //recyclerView
        recyclerView = findViewById(R.id.rv_partice_expenses);
        //
        participants = new ArrayList<Participant>();
        participants.add(new Participant(drawableToBitmap(R.drawable.q), "Ali", 1000, 2050));
        participants.add(new Participant(drawableToBitmap(R.drawable.w), "Reza", 15000, 2050));
        participants.add(new Participant(drawableToBitmap(R.drawable.r), "Mamad", 1000, 500));
        participants.add(new Participant(drawableToBitmap(R.drawable.w), "Hami", 5000, 2050));
        participants.add(new Participant(drawableToBitmap(R.drawable.q), "sadi", 1000, 2500));
        participants.add(new Participant(drawableToBitmap(R.drawable.r), "dad", 0, 2000));
        participants.add(new Participant(drawableToBitmap(R.drawable.q), "mom", 0, 6000));
        participants.add(new Participant(drawableToBitmap(R.drawable.w), "Ali", 1000, 2050));
        participants.add(new Participant(drawableToBitmap(R.drawable.r), "Reza", 15000, 2050));
        participants.add(new Participant(drawableToBitmap(R.drawable.w), "Mamad", 1000, 500));
        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        adaptor = new ParticipantAdaptor(mContext, participants);
        recyclerView.setAdapter(adaptor);

    }

    Bitmap drawableToBitmap(int drawable){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable);
        return bitmap;
    }

}
