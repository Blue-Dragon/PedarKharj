package com.example.pedarkharj_edit3.classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.web_db_pref.SharedPrefManager;
import com.example.pedarkharj_edit3.pages.AddExpenseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuyerDialog extends Dialog {

    private static Activity activity;
    BuyerDialog buyerDialog = this;
    private Activity mActivity;
    List<Participant> mParticipants;
    List<Event> eventList;
    MyAdapter adapter;
    DatabaseHelper db;
    Event event;
    int layoutId = -1;

    RecyclerView recyclerView;
    TextView title;




    public BuyerDialog(Activity mActivity, Event event) {
        super(mActivity);
        this.mActivity = mActivity;
        this.event =event;
        setContext(mActivity);
    }

    public BuyerDialog(Activity mActivity, Event event, int layoutId) {
        super(mActivity);
        this.event =event;
        this.layoutId = layoutId;
        this.mActivity = mActivity;
        setContext(mActivity);
    }



    //Default Constructor
    public static void setContext(Activity activity1) {
        activity = activity1;
    }

    public BuyerDialog() {
        super(activity);
    }

    /******************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_buyer);

        db = new DatabaseHelper(mActivity);
        recyclerView = findViewById(R.id.chooseBuyer_RecView);

        title = findViewById(R.id.dialog_title);
        Typeface tf = Routines.getTypeFaceYakanB(mActivity);
        title.setTypeface(tf);

        doRecyclerView();

        if (layoutId > 0)
            title.setText("کدوم رویداد رو میخوای؟");


        /**
         * recView onClick
         */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mActivity, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (layoutId > 0 ){
                    //Event Mode
                    Event event = eventList.get(position);
                    buyerDialog.cancel(); // same as dismiss();
//                    dismiss();
                    goToSelectedEvent(event);

                }else {
                    Participant participant = mParticipants.get(position);
                    Intent intent =  new Intent(mActivity, AddExpenseActivity.class);
                    intent.putExtra(Routines.PARTICIPANT_INFO, participant.getId());

//                    mActivity.finish();
                    mActivity.startActivity(intent);
                    // remember to put it after startActivity, if you put it to above, animation will not working
                    // document say if we don't want animation we can put 0. However, if we put 0 instead of
                    // R.anim.no_animation, the exist activity will become black when animate
//                    mActivity.overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);

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
            eventList = Routines.deleteTempEvents(mActivity, db.getAllEvents());
            Collections.reverse(eventList);

            itemsInScreen = 3;
            adapter = new MyAdapter(mActivity);
            adapter.setLayout(layoutId);
            adapter.setEvents(eventList);
        }else {
            //show partices of the Event
            mParticipants = db.getAllParticeUnderEvent(event);
            adapter = new MyAdapter(mActivity, R.layout.sample_contact, mParticipants);
            adapter.setItemsInScreen(itemsInScreen);
        }

        adapter.setItemsInScreen(itemsInScreen);

        // Grid Layout Manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, itemsInScreen, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.setAdapter(adapter);

    }

    private void goToSelectedEvent(Event event) {
        SharedPrefManager.getInstance(mActivity).saveLastSeenEventId(event.getId());
        MainActivity.navPosition = Routines.HOME;
        mActivity.recreate();
//        mActivity.finish();
//        mActivity.startActivity(mActivity.getIntent());
//        mActivity.overridePendingTransition(0, 0);
    }

}
