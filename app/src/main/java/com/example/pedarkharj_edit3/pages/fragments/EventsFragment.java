package com.example.pedarkharj_edit3.pages.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.example.pedarkharj_edit3.classes.Event;
import com.example.pedarkharj_edit3.classes.IOnBackPressed;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.web_db_pref.SharedPrefManager;
import com.example.pedarkharj_edit3.pages.AddEventParticesActivity;

import java.util.ArrayList;
import java.util.List;


public class EventsFragment extends Fragment implements IOnBackPressed {
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
    TextView counter_text_view, title;
    View mView;
    //Action mode
    List<Event> mEvents;
    List<Event> selectionList;
    String newName;
    
    int counter, selectedEventId;
    //    boolean is_select_one = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_event_mng, container, false);
        mContext = getContext();
        mActivity = getActivity();
        MainActivity.navPosition = Routines.EVENTS;

        db = new DatabaseHelper(mContext);

        toolbar =  mView.findViewById(R.id.m_toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);


        mEvents = new ArrayList<>();
        //
        db = new DatabaseHelper(mContext);
        setRecView(); //show Events
        //Action mode
        selectionList = new ArrayList<>();
        counter_text_view = mView.findViewById(R.id.tv_counter);
        title = mView.findViewById(R.id.textView);
        counter_text_view.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        counter = 0;


        //back imageView btn
//        ImageView backBtn = mView.findViewById(R.id.back_btn);
//        ImageView backBtn = mView.findViewById(R.id.back_btn);
        ImageView backBtn = mView.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(item -> Toast.makeText(mContext, "back", Toast.LENGTH_SHORT).show());
        setHasOptionsMenu(true); //for menu items in fragment (edit & delete)

        /*
         * recView onClick
         */
        Log.e("recOnClick", "onClick");
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (Routines.is_events_in_action_mode){
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
                if (!Routines.is_events_in_action_mode){
                    setActionModeOn();
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
    //-------------------------     RecyclerView    --------------------------//
    private void setRecView() {
        recyclerView = mView.findViewById(R.id.rv);

        mEvents = db.getAllEvents();
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
        int itemsInScreen = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, itemsInScreen, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);


        //
        adaptor = new MyAdapter(mContext);
        adaptor.setEvents(realEvents);
        adaptor.setItemsInScreen(3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adaptor);
    }



    //---------------    ActionMode (selection on longClick)    ----------------//
    //Edit & Delete
    private void setActionModeOn() {
        toolbar.getMenu().clear();//clear activity menu
        toolbar.inflateMenu(R.menu.menu_action_mode);//inflate action mode menu
        counter_text_view.setVisibility(View.VISIBLE); //make textView visible on it
//        updateCounter(counter); //show textView with nothing selected by def
        title.setVisibility(View.GONE);
        Routines.is_events_in_action_mode = true;
//        adaptor.notifyDataSetChanged();//notify adapter about this  change
    }

    //Delete only
    private void setActionMode2On() {
        toolbar.getMenu().clear();//clear activity menu
        toolbar.inflateMenu(R.menu.menu_action_mode_2);//inflate action mode menu
        counter_text_view.setVisibility(View.VISIBLE); //make textView visible on it
        title.setVisibility(View.GONE);
        Routines.is_events_in_action_mode = true;
        selectedEventId = 0;
//        is_select_one = false;
    }

    private void setActionModeOff() {
        toolbar.getMenu().clear();//clear activity menu
//        toolbar.inflateMenu(R.menu.menu_action_mode);
        counter_text_view.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        Routines.is_events_in_action_mode = false;//make checkbox visible
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
                intent.putExtra(Routines.SEND_EVENT_ID_INTENT, selectedEventId);
                startActivity(intent);
//                finish();
                break;
        }

        return true;
    }

    private void restartPage(short page) {
        MainActivity.navPosition = page;
        mActivity.finish();
        startActivity(mActivity.getIntent());
    }



//-------------------------    other stuff    --------------------------//

//    @Override
//    public void onBackPressed() {
//
//        //        super.onBackPressed();
//        if (Routines.is_events_in_action_mode){
//            selectionChangeColor(R.color.colorTransparent);
//            setActionModeOff();
//        }else {
//            mActivity.finish();
//            startActivity(mActivity.getIntent());
//        }
//
//    }

    @Override
    public boolean onMyBackPressed(Context mContext) {

        if (Routines.is_events_in_action_mode){
//            selectionChangeColor(R.color.colorTransparent);
            setActionModeOff();
            Toast.makeText(mContext, "in_action_mode", Toast.LENGTH_SHORT).show();
            return true;
        }else{
            Toast.makeText(mContext, "Fuck u looser!", Toast.LENGTH_SHORT).show();
//            restartPage(Routines.HOME);
            return false;
        }
    }
}
