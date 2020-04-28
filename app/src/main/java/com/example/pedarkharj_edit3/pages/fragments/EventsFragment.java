package com.example.pedarkharj_edit3.pages.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
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

    List<Event> mEvents;
    List<Event> selectionList;
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
    View view;
    //Action mode
    TextView counter_text_view, title;
    //    boolean is_select_one = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_event_mng, container, false);
        MainActivity.navPosition = Routines.EVENTS;
        init();
        // fab hides on scroll
        Routines.hideFabOnScroll(fab, recyclerView);


        backBtn.setOnClickListener(item -> mActivity.onBackPressed());
        setHasOptionsMenu(true); //for menu items in fragment (edit & delete)

        //Tutorial - TabTargetView
        if ( SharedPrefManager.getInstance(mActivity).getRunTurn(Routines.KEY_TURN_TIME_EVENTS) == Routines.FIRST_RUN ){
            new Handler().postDelayed(this::showTabTargetsSequences1, 500);   // Delay 0.5 sec
        }


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
                    restartPage(mActivity, Routines.HOME);
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

        fab.setOnClickListener(view0 -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            mActivity.finish();
            startActivity(new Intent(mContext, AddEventParticesActivity.class));
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
        fab = mView.findViewById(R.id.fab);
        view  = mView.findViewById(R.id.view);
    }

    //-------------------------     RecyclerView    --------------------------//
    private void setRecView() {
//        mEvents = db.getAllEvents();
        mEvents = Routines.deleteTempEvents(mActivity, db.getAllEvents());
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
                recyclerView.setItemAnimator(new DefaultItemAnimator());

        */

        // Grid Layout Manager
        int itemsInScreen = 3
                ;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, itemsInScreen, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


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

//                Toast.makeText(mContext, "Del", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.AlertDialogDanger);
                dialog.setTitle("پاک کنم؟")
                        .setMessage("این اطلاعات از دم نیست و نابود میشن هااا !")
                        .setPositiveButton("پاک کن بره داداش", (dialogInterface, i1) -> {
                            for (Event event : selectionList){
                                db.deleteEvent(event, true);
//                                Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                                if (event.getId() == HomeFragment.lastSeenEventId){
                                    SharedPrefManager.getInstance(mContext).clearDerfEvent();
                                    Toast.makeText(mContext, "رویداد "+ event.getEventName() + " پاک شد", Toast.LENGTH_SHORT).show();
                                }
                            }
                            restartPage(mActivity, Routines.EVENTS);
                        })
                        .setNegativeButton("نه، بی خیال!", (dialogInterface, i1) -> {})
                        .show();
                        // -----------------------
                break;

            case R.id.item_edit:
                Intent intent = new Intent(mContext, AddEventParticesActivity.class);
                intent.putExtra(Routines.SEND_EVENT_ID_INTENT, Routines.selectedItemId);
                startActivity(intent);
                onMyBackPressed();
//                finish();
                break;
        }

        return true;
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

    /**
     * first time tutorial (tapTarget)
     */
    private void showTabTargetsSequences1() {
// 1

        new TapTargetSequence(mActivity)
                // 2
                .targets(

                        TapTarget.forView(fab, getString(R.string.addEventFab_title), getString(R.string.addEventFab_description))
                                .outerCircleColor(R.color.colorPrimaryDark)
                                .outerCircleAlpha(Routines.tapAlpha)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(Routines.tapTitleSize)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(Routines.tapDescSize)
                                .descriptionTextColor(R.color.bk1)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(true)
                                .tintTarget(false)
                                .transparentTarget(true)
                                .targetRadius(50),

                        TapTarget.forView(view, getString(R.string.eventHold_title),  getString(R.string.eventHold_description) )
                                .outerCircleColor(R.color.colorPrimaryDark)
                                .outerCircleAlpha(Routines.tapAlpha)
                                .targetCircleColor(R.color.black)
                                .titleTextSize(Routines.tapTitleSize)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(Routines.tapDescSize)
                                .descriptionTextColor(R.color.bk1)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(false)
                                .transparentTarget(true)
                                .targetRadius(120)


                )

                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        SharedPrefManager.getInstance(mActivity).setNextRunTurn(Routines.KEY_TURN_TIME_EVENTS, Routines.SECOND_RUN);

                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                })

                // 6
                .start();

    }


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
