package com.example.pedarkharj_edit2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pedarkharj_edit2.classes.BuyerDialog;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.pages.AddExpenseActivity;
import com.example.pedarkharj_edit2.pages.ContactsActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Participant> participants;
    ParticipantAdapter adaptor;

    Context mContext = this;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        //Floating Btn
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            startActivity(new Intent(mContext, AddExpenseActivity.class));
//            showBuyerDialog();
            startActivity(new Intent(mContext, ContactsActivity.class));
        });


        //recyclerView
        recyclerView = findViewById(R.id.rv_partice_expenses);
        doRecyclerView();
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (fab.getVisibility() != View.VISIBLE) fab.show();
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                while (dy == 0 && fab.getVisibility() == View.VISIBLE) fab.hide();
//
//
//            }
//        });
        //

    }

    
    
    
    /********************************************       Methods     ****************************************************/

    private void showBuyerDialog() {
        new BuyerDialog(this).show();
    }

    Bitmap drawableToBitmap(int drawable){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable);
        return bitmap;
    }

    private void doRecyclerView() {
        participants = new ArrayList<Participant>();
        participants.add(new Participant(drawableToBitmap(R.drawable.q), "Ali", 1000, 2050));
        participants.add(new Participant( "Reza", 15000, 2050));
        participants.add(new Participant(drawableToBitmap(R.drawable.r), "Mamad", 1000, 500));
        participants.add(new Participant( "Hami", 5000, 2050));
        participants.add(new Participant(drawableToBitmap(R.drawable.q), "sadi", 1000, 2500));
        participants.add(new Participant(drawableToBitmap(R.drawable.r), "dad", 0, 2000));
        participants.add(new Participant( "mom", 0, 6000));
        participants.add(new Participant( "Ali", 1000, 2050));
        participants.add(new Participant(drawableToBitmap(R.drawable.r), "Reza", 15000, 2050));
        participants.add(new Participant(drawableToBitmap(R.drawable.w), "Mamad", 1000, 500));
        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        adaptor = new ParticipantAdapter(mContext, participants);
        recyclerView.setAdapter(adaptor);
    }
    
}
