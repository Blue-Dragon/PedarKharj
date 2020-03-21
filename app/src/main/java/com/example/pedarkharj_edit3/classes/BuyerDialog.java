package com.example.pedarkharj_edit3.classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.web_db_pref.SharedPrefManager;
import com.example.pedarkharj_edit3.pages.AddExpenseActivity;

import java.util.List;

public class BuyerDialog extends Dialog {
    private Activity mActivity;
    public Dialog d;
    private Button yes, no;
    List<Participant> mParticipants;
    List<Event> eventList;
    MyAdapter adapter;
    DatabaseHelper db;
    Event event;
    int layoutId = -1;
    RecyclerView recyclerView;


    public BuyerDialog(Activity mActivity, Event event) {
        super(mActivity);
        this.mActivity = mActivity;
        this.event =event;
    }

    public BuyerDialog(Activity mActivity, Event event, int layoutId) {
        super(mActivity);
        this.mActivity = mActivity;
        this.event =event;
        this.layoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_buyer);

        db = new DatabaseHelper(mActivity);
        recyclerView = findViewById(R.id.chooseBuyer_RecView);
        doRecyclerView();

        /**
         * recView onClick
         */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mActivity, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (layoutId > 0 ){
                    //Event Mode
                    Event event = eventList.get(position);
                    goToSelectedEvent(event);

                }else {
                    Participant participant = mParticipants.get(position);
                    Intent intent =  new Intent(mActivity, AddExpenseActivity.class);
                    intent.putExtra(Routines.PARTICIPANT_INFO, participant.getId());
                    mActivity.startActivity(intent);
//                mActivity.finish();
                }

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        //
        db.closeDB();
    }


    private void doRecyclerView() {
        int itemsInScreen = 4;

        if (layoutId > 0){
            eventList = db.getAllEvents();
//            itemsInScreen = 3;
            adapter = new MyAdapter(mActivity);
            adapter.setLayout(layoutId);
            adapter.setEvents(eventList);
            adapter.setItemsInScreen(itemsInScreen);

        }else {
            //show partices of the Event
            mParticipants = db.getAllParticeUnderEvent(event);
//            itemsInScreen = 4;
            adapter = new MyAdapter(mActivity, R.layout.sample_contact, mParticipants);
            adapter.setItemsInScreen(itemsInScreen);
        }
        // Grid Layout Manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, itemsInScreen, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);

    }

    private void goToSelectedEvent(Event event) {
        SharedPrefManager.getInstance(mActivity).saveLastSeenEventId(event.getId());
        MainActivity.navPosition = Routines.HOME;
        mActivity.finish();
        mActivity.startActivity(mActivity.getIntent());
    }

}
