package com.example.pedarkharj_edit2.pages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit2.MainActivity;
import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Event;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit2.classes.Routines;
import com.example.pedarkharj_edit2.classes.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventMngActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Event> mEvents;
    ParticipantAdapter adaptor;
    //
    Context mContext = this;
    FloatingActionButton fab;
    DatabaseHelper db;
    Toolbar toolbar;
    //Action mode
    boolean is_in_action_mode = false;
//    boolean is_select_one = false;
    TextView counter_text_view, title;
    int counter, selectedEventId;
    List<Event> selectionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_mng);
        toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        mEvents = new ArrayList<>();
        //
        db = new DatabaseHelper(mContext);
        setRecView(); //show Events
        //Action mode
        selectionList = new ArrayList<>();
        counter_text_view = findViewById(R.id.tv_counter);
        title = findViewById(R.id.textView);
        counter_text_view.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        counter = 0;


        //back imageView btn
        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(item -> onBackPressed());

        /**
         * recView onClick
         */
        Log.e("recOnClick", "onClick");
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (is_in_action_mode){
                    prepareSelection(view, position);

                } else {
                    //open MainActivity on this event
                    Event event = mEvents.get(position);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra(Routines.SEND_EVENT_ID_INTENT, event.getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (!is_in_action_mode){
                    setActionModeOn();
                }
                onClick(view, position);

            }
        }));

        //-------------------------     Floating Btn    --------------------------//
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            startActivity(new Intent(mContext, AddEventParticesActivity.class));
//            finish();
//            showBuyerDialog();
        });


        //
        db.closeDB();
    }



 /***************************************     Methods     ******************************************/
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
        recyclerView.setHasFixedSize(true); //doesn't work
        recyclerView.setLayoutManager(gridLayoutManager);
        //
        adaptor = new ParticipantAdapter(mContext);
        adaptor.setEvents(realEvents);
        recyclerView.setAdapter(adaptor);
    }



    //---------------    ActionMode (selection on longClick)    ----------------//
    private void setActionModeOn() {
        toolbar.getMenu().clear();//clear activity menu
        toolbar.inflateMenu(R.menu.menu_action_mode);//inflate action mode menu
        counter_text_view.setVisibility(View.VISIBLE); //make textView visible on it
//        updateCounter(counter); //show textView with nothing selected by def
        title.setVisibility(View.GONE);
        is_in_action_mode = true;
//        adaptor.notifyDataSetChanged();//notify adapter about this  change
    }

    private void setActionMode2On() {
        toolbar.getMenu().clear();//clear activity menu
        toolbar.inflateMenu(R.menu.menu_action_mode_2);//inflate action mode menu
        counter_text_view.setVisibility(View.VISIBLE); //make textView visible on it
        title.setVisibility(View.GONE);
        is_in_action_mode = true;
        selectedEventId = 0;
//        is_select_one = false;
    }

    private void setActionModeOff() {
        toolbar.getMenu().clear();//clear activity menu
//        toolbar.inflateMenu(R.menu.menu_action_mode);
        counter_text_view.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        is_in_action_mode = false;//make checkbox visible
        adaptor.notifyDataSetChanged();//notify adapter about this  change
        counter = 0;
        selectedEventId = 0;
        selectionList.clear();
    }

    /*
     * textview above
     */
    private void updateCounter(int counter) {
        counter_text_view.setText(counter + " رویداد انتخاب شده");
    }

    /*
     * on select/deselect methods
     */
    private void prepareSelection(View view, int position) {
        Event event = mEvents.get(position);
        if (!selectionList.contains(event)) {
            selectionList.add(event);
            view.setForeground( new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorSelected) ));
            updateCounter(++counter);

        }else {
            selectionList.remove(event);
            view.setForeground( new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorTransparent) ));
            if (selectionList.isEmpty()) {
                setActionModeOff();
            } else {
                updateCounter(--counter);
            }
        }

        //edit & delete option be shown only if just ONE item is selected
        if (selectionList.size() > 1){
            setActionMode2On();

        } else if (selectionList.size() == 1){
            setActionModeOn();
            selectedEventId = selectionList.get(0).getId();
        }
    }

    private void selectionChangeColor(int id) {
        adaptor.setForeground( new ColorDrawable(ContextCompat.getColor(mContext, id)));
    }

    int i =0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_delete:

                new AlertDialog.Builder(mContext)
                        .setTitle("پاک کنم؟")
                        .setMessage("این اطلاعات از دم نیست و نابود میشن هااا !")
                        .setPositiveButton("پاک کن بره داداچ", (dialogInterface, i1) -> {
                            for (Event event : selectionList){
                                db.deleteEvent(event, true);
//                                Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                                if (event.getId() == MainActivity.lastSeenEventId){
                                    SharedPrefManager.getInstance(mContext).clearShrdPref();
                                    Toast.makeText(mContext, "EventId : "+ event.getId() + "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                            startActivity(new Intent(mContext, getClass()));
                            finish();
                        })
                        .setNegativeButton("نه، بی خیال!", (dialogInterface, i1) -> {})
                        .show();
                break;

            case R.id.item_edit:
                Intent intent = new Intent(mContext, AddEventParticesActivity.class);
                intent.putExtra(Routines.SEND_EVENT_ID_INTENT, selectedEventId);
                startActivity(intent);
//                finish();
                break;
        }

        return true;
    }

//-------------------------    other stuff    --------------------------//

    @Override
    public void onBackPressed() {

        //        super.onBackPressed();
        if (is_in_action_mode){
            selectionChangeColor(R.color.colorTransparent);
            setActionModeOff();
        }else {
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
        }

    }
}
