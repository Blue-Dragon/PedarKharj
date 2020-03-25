package com.example.pedarkharj_edit3.pages.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.web_db_pref.SharedPrefManager;
import com.example.pedarkharj_edit3.pages.AddEventParticesActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EventsFragment extends Fragment implements IOnBackPressed, IEditBar {
    final public static int INTENT_CODE = 1;
    final public static String INTENT_MASSEGE = "NEW_NAME";

    Context mContext ;
    Activity mActivity ;
    MainActivity mainActivity = new MainActivity();
    MyAdapter adaptor;
    DatabaseHelper db;
    //
    RecyclerView recyclerView;
    FloatingActionButton fab;
    Toolbar toolbar;
    View mView;
    ImageView backBtn;
    //Action mode
    TextView counter_text_view, title;
    List<Event> mEvents;
    List<Event> selectionList;

    //    boolean is_select_one = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_event_mng, container, false);
        MainActivity.navPosition = Routines.EVENTS;
        init();

        backBtn.setOnClickListener(item -> Toast.makeText(mContext, "back", Toast.LENGTH_SHORT).show());
        setHasOptionsMenu(true); //for menu items in fragment (edit & delete)

        setRecView(); //show Events
        /*
         * recView onClick
         */
        Log.e("recOnClick", "onClick");
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (Routines.is_in_action_mode){
                    prepareSelection(view, position);

                } else {
                    //open MainActivity on this event
                    Event event = mEvents.get(position);

                    SharedPrefManager.getInstance(mContext).saveLastSeenEventId(event.getId());
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    intent.putExtra(Routines.SEND_EVENT_ID_INTENT, event.getId());
//                    MainActivity.navPosition = Routines.HOME;
//                    mActivity.finish();
//                    startActivity(intent);
                    restartPage(Routines.HOME);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (!Routines.is_in_action_mode){
                    setActionModeOn(toolbar, counter_text_view, title);
                }
                onClick(view, position);

            }
        }));

        //-------------------------     Floating Btn    --------------------------//
        fab = mView.findViewById(R.id.fab);
        fab.setOnClickListener(view0 -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            startActivity(new Intent(mContext, AddEventParticesActivity.class));
//            finish();
//            showBuyerDialog();
        });


        //
        db.closeDB();


        return mView;
    }




    /***************************************     Methods     ******************************************/
    private void init() {
        mContext = getContext();
        mActivity = getActivity();
        MainActivity.navPosition = Routines.EVENTS;

        toolbar =  mView.findViewById(R.id.m_toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);

        db = new DatabaseHelper(mContext);
        //Action mode
        selectionList = new ArrayList<>();
        counter_text_view = mView.findViewById(R.id.tv_counter);
        title = mView.findViewById(R.id.textView);
        initEditBar(counter_text_view, title);

        backBtn = mView.findViewById(R.id.back_btn);
        recyclerView = mView.findViewById(R.id.rv);
    }

    //-------------------------     RecyclerView    --------------------------//
    private void setRecView() {
        mEvents = db.getAllEvents();
        Collections.reverse(mEvents);

        // Not letting TempEvents to be shown
        List<Event> realEvents = new ArrayList<>();
        for (Event event : mEvents){
            if (!event.getEventName().equals(Routines.EVENT_TEMP_NAME)) realEvents.add(event);
        }

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        /*
         // Flexbox Layout Manager
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_BETWEEN);
        layoutManager.setAlignItems(AlignItems.CENTER);
        recyclerView.setLayoutManager(layoutManager);
        */

        // Grid Layout Manager
        int itemsInScreen = 3
                ;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, itemsInScreen, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);


        //
        adaptor = new MyAdapter(mContext);
        adaptor.setEvents(realEvents);
        adaptor.setItemsInScreen(itemsInScreen);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adaptor);
    }



    //---------------    ActionMode (selection on longClick)    ----------------//
    /*
     * on select/deselect methods
     */
    private void prepareSelection(View view, int position) {
        Event event = mEvents.get(position);
        if (!selectionList.contains(event)) {
            selectionList.add(event);
            view.setForeground( new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorSelected) ));
            updateCounter(++Routines.counter, counter_text_view);

        }else {
            selectionList.remove(event);
            view.setForeground( new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorTransparent) ));
            if (selectionList.isEmpty()) {
                setActionModeOff(toolbar, counter_text_view, title, adaptor);
                selectionList.clear();
            } else {
                updateCounter(--Routines.counter, counter_text_view);
            }
        }

        //edit & delete option be shown only if just ONE item is selected
        if (selectionList.size() > 1){
            setActionMode2On(toolbar, counter_text_view, title);

        } else if (selectionList.size() == 1){
            setActionModeOn(toolbar, counter_text_view, title);
            Routines.selectedItemId = selectionList.get(0).getId();
        }
    }


    int i =0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_delete:

                Toast.makeText(mContext, "Del", Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(mContext)
                        .setTitle("پاک کنم؟")
                        .setMessage("این اطلاعات از دم نیست و نابود میشن هااا !")
                        .setPositiveButton("پاک کن بره داداچ", (dialogInterface, i1) -> {
                            for (Event event : selectionList){
                                db.deleteEvent(event, true);
//                                Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                                if (event.getId() == HomeFragment.lastSeenEventId){
                                    SharedPrefManager.getInstance(mContext).clearShrdPref();
                                    Toast.makeText(mContext, "EventId : "+ event.getId() + "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }

                            restartPage(Routines.EVENTS);

                        })
                        .setNegativeButton("نه، بی خیال!", (dialogInterface, i1) -> {})
                        .show();
                break;

            case R.id.item_edit:
                Intent intent = new Intent(mContext, AddEventParticesActivity.class);
                intent.putExtra(Routines.SEND_EVENT_ID_INTENT, Routines.selectedItemId);
                startActivity(intent);
//                finish();
                break;
        }

        return true;
    }

    private void restartPage(short page) {
        MainActivity.navPosition = page;
//        mActivity.recreate();
        mActivity.finish();
        startActivity(mActivity.getIntent());
        mActivity.overridePendingTransition(0, 0);
    }



//-------------------------    other stuff    --------------------------//

//    @Override
//    public void onBackPressed() {
//
//        //        super.onBackPressed();
//        if (Routines.is_in_action_mode){
//            selectionChangeColor(R.color.colorTransparent);
//            setActionModeOff();
//        }else {
//            mActivity.finish();
//            startActivity(mActivity.getIntent());
//        }
//
//    }

    @Override
    public void onMyBackPressed() {
//        if (Routines.is_in_action_mode){
            selectionChangeColor(mContext, R.color.colorTransparent, adaptor);
            setActionModeOff(toolbar, counter_text_view, title, adaptor);
            selectionList.clear();
//        }else{
//            restartPage(Routines.HOME);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.navPosition = Routines.EVENTS;
    }
}
