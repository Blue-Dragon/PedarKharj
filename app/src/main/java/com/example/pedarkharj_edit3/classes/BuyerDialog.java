package com.example.pedarkharj_edit3.classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.pages.AddExpenseActivity;

import java.util.ArrayList;
import java.util.List;

public class BuyerDialog extends Dialog {
    private Activity mActivity;
    public Dialog d;
    private Button yes, no;
    RecyclerView recyclerView;
    List<Participant> mParticipants;
    MyAdapter adapter;
    DatabaseHelper db;
    Event event;


    public BuyerDialog(Activity mActivity, Event event) {
        super(mActivity);
        this.mActivity = mActivity;
        this.event =event;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_buyer);

        db = new DatabaseHelper(mActivity);
//        yes = (Button) findViewById(R.id.btn_yes);     yes.setOnClickListener(this);
//        no = (Button) findViewById(R.id.btn_no);        no.setOnClickListener(this);
        recyclerView = findViewById(R.id.chooseBuyer_RecView);
        doRecyclerView();

        /**
         * recView onClick
         */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mActivity, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Participant participant = mParticipants.get(position);
                Intent intent =  new Intent(mActivity, AddExpenseActivity.class);
                intent.putExtra(Routines.PARTICIPANT_INFO, participant.getId());
                mActivity.startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        //
        db.closeDB();
    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.btn_yes:
//                mActivity.startActivity(new Intent(mActivity, AddExpenseActivity.class));
////                mActivity.finish();
//                break;
//
//            case R.id.btn_no:
//                mActivity.startActivity(new Intent(mActivity, ContactsActivity.class));
////                mActivity.finish();
//                break;
//
//            default:
//                break;
//        }
//        dismiss();
//    }


    private void doRecyclerView() {
        mParticipants = new ArrayList<Participant>();
        db.closeDB();
        //show partices of the Event

        mParticipants = db.getAllParticeUnderEvent(event);

        // Grid Layout Manager
        int itemsInScreen = 4;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, itemsInScreen, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);


        adapter = new MyAdapter(mActivity, R.layout.sample_contact, mParticipants);
        recyclerView.setAdapter(adapter);

    }

}
