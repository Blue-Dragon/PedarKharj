package com.example.pedarkharj_edit3.pages.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.BuyerDialog;
import com.example.pedarkharj_edit3.classes.models.Contact;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Expense;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.web_db_pref.SharedPrefManager;
import com.example.pedarkharj_edit3.pages.EventDetailActivity;
import com.example.pedarkharj_edit3.pages.ParticeResultActivity;
import com.takusemba.spotlight.OnSpotlightEndedListener;
import com.takusemba.spotlight.OnSpotlightStartedListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment implements View.OnClickListener, IEditBar {
    public static List<Expense> newExpenseList;
    List<Participant> mParticipants;
    List<Event> events;
    Contact[] defContats;
    public static int lastSeenEventId;
    Context mContext ;
    Activity mActivity;
    Event curEvent;
    MyAdapter adaptor;
    DatabaseHelper db;
    //
    CardView cardView;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    RelativeLayout mySpinner;
    TextView spinnerTv;
    TextView tvR1, tvR2, tvC1, tvC2, tvL1, tvL2; //The rectangle above
    //
//    int sentEventId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // inits
        doInits(view);
        doOnClicks();
        setCurEvent(); //and Setting default event and partices IF NOT EXIST
        initRectangleAbove(curEvent);  //doInits Rectangle


//        showSpotlightIntro(view);




        // RecView
        setRecView(curEvent);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Participant participant = mParticipants.get(position);
                List< Expense> expenseList = db.getAllExpensesOfEvent(curEvent);

                // not all expenses in expenseList are the ones our partice has participated in.
                newExpenseList = new ArrayList<>();
                for (Expense expense : expenseList){
                    float debt = db.getParticeDebt(expense.getExpenseId(), participant.getId());
                    if (debt > -1)
                        newExpenseList.add(expense);
                }

                Intent i = new Intent(mContext, ParticeResultActivity.class);
                i.putExtra(Routines.SEND_PARTICIPANT_ID_INTENT, participant.getId());
                i.putExtra(Routines.SEND_EVENT_ID_INTENT, curEvent.getId());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
                Participant participant = mParticipants.get(position);
                Log.d("recOnClick", participant.getResult());
            }
        }));

        // Floating Btn
        fab.setOnClickListener(view0 ->showBuyerDialog(curEvent));

        //MySpinner
        mySpinner.setOnClickListener(x -> showEventsDialog(curEvent));

        /*
         * TODO: hide the fucking fab while scrolling
         */
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




        //drawerLayout
//        createDrawer();

        //Close db
        db.closeDB();
        return view;
    }


    /********************************************       Methods     ****************************************************/

    /**
     *  set the event to show as recView
     */
    private void setCurEvent() {
        if (events.size() > 0){
            if (lastSeenEventId > 0) {
                curEvent = db.getEventById(lastSeenEventId);
            }
            else curEvent = events.get(0);

            Log.d("mID", "cur event id: "+ curEvent.getId());


            lastSeenEventId = curEvent.getId();
            SharedPrefManager.getInstance(mContext).saveLastSeenEventId(lastSeenEventId); //save  lastSeenEventId in SharedPref
            spinnerTv.setText(curEvent.getEventName());
        }
        else {
            createDefEvent();
            setCurEvent();
        }
    }

    private void doOnClicks() {
        cardView.setOnClickListener(this);
        mySpinner.setOnClickListener(this);
    }

    private void doInits(View view) {
        mContext = getContext();
        mActivity = getActivity();
        MainActivity.navPosition = Routines.HOME;

        Toolbar toolbar = view.findViewById(R.id.m_toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);

        db = new DatabaseHelper(mContext);

        events = db.getAllEvents(); //for spinner && def partices
        lastSeenEventId = SharedPrefManager.getInstance(mContext).getDefEventId();
        cardView = view.findViewById(R.id.details_card_layout);
        recyclerView = view.findViewById(R.id.rv_partice_expenses);
        mySpinner = view.findViewById(R.id.my_spinner);
        spinnerTv  = view.findViewById(R.id.spinner_tv);

        fab = view.findViewById(R.id.fab);


        //the rectangle above
        tvL1 = view.findViewById(R.id.tv_title_my_expense);
        tvL2 = view.findViewById(R.id.tv_my_expense);
        tvC1 = view.findViewById(R.id.tv_title_my_dong);
        tvC2 = view.findViewById(R.id.tv_my_dong);
        tvR1 = view.findViewById(R.id.tv_title_my_result);
        tvR2 = view.findViewById(R.id.tv_my_result);

        tvR1.setTextColor(getResources().getColor(R.color.grayTextColor));
        tvR2.setTextColor(getResources().getColor(R.color.grayTextColor));
        tvL1.setTextColor(getResources().getColor(R.color.primaryTextColor));
        tvL2.setTextColor(getResources().getColor(R.color.primaryTextColor));
        tvR1.setText("خرج من");
        tvC1.setText("دونگ من");
        tvL1.setText("حساب من");
    }

    private void showBuyerDialog(Event curEvent) {
        BuyerDialog buyerDialog = new BuyerDialog(mActivity, curEvent);
        Objects.requireNonNull(buyerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        buyerDialog.show();
    }

    private void showEventsDialog(Event curEvent) {
        BuyerDialog buyerDialog = new BuyerDialog(mActivity, curEvent, R.layout.sample_event);
        Objects.requireNonNull(buyerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        buyerDialog.show();
    }


    //-------------------------     RecyclerView    --------------------------//
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.details_card_layout:
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra(Routines.SEND_EVENT_ID_INTENT, curEvent.getId());
                startActivity(intent);
                break;

            default:
                Toast.makeText(mContext, "Wrong item clicked!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRecView(Event curEvent) {

        //show partices of the Event
        mParticipants = db.getAllParticeUnderEvent(curEvent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //
        adaptor = new MyAdapter(mContext, R.layout.sample_participant, mParticipants);
        recyclerView.setAdapter(adaptor);
    }

    //todo: def event should be just one event and its name should make sense
    private void createDefEvent() {
        if (events.size() < 1) {
            /*
             * adding partices and an event
             */
            lastSeenEventId = (int) createEvent("سفر شمال");
            createEvent("سفر 2");
            createEvent("سفر 3");
            createEvent("سفر 4");
            createEvent("سفر 5");

            events = db.getAllEvents();
        }
    }

    private long createEvent(String eventName) {

        /*
         * adding partices and an event
         */
        if (defContats == null || defContats.length < 1) {
            creatingDefContacts();
        }
        return db.createNewEventWithPartices(new Event(eventName), defContats);

    }

    private void creatingDefContacts() {
        long contact_1 = db.createContact(new Contact("Hamed"));
        long contact_2 = db.createContact(new Contact("Reza"));
        long contact_3 = db.createContact(new Contact("Sadi"));
        long contact_4 = db.createContact(new Contact("Abbas"));

        defContats = new Contact[]{db.getContactById(contact_1), db.getContactById(contact_2), db.getContactById(contact_3), db.getContactById(contact_4)};
    }

    private void initRectangleAbove(Event event) {
        List<Participant> participants = db.getAllParticeUnderEvent(event);
        if (participants.size() > 0) {
            float myExpenses = db.getParticTotalExpensePriceByParticeId(participants.get(0).getId()); //it is me. 1st partice of all
            float myDebt = db.getAllParticDebtsByParticeId(participants.get(0).getId()); //it is me. 1st partice of all
//            int allEventExpenses = db.getEventTotalExpensesByEventId(event.getId());
            tvR2.setText(Routines.getRoundFloatString(myExpenses));
            tvC2.setText(Routines.getRoundFloatString(myDebt));
            tvL2.setText(Routines.getRoundFloatString(myExpenses - myDebt));
        }
    }

//----------------------    Spotlight       ------------------------//
//    private void showSpotlightIntro(View v) {
//
//        int[] location = new int[2];
//        fab.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
//
//
//        SimpleTarget simpleTarget = new SimpleTarget.Builder(mActivity)
////                .setPoint(fab, fab) // position of the Target. setPoint(Point point), setPoint(View view) will work too.
//                .setPoint(x, y)
//                .setRadius(80f) // radius of the Target
//                .setTitle("the title") // title
//                .setDescription("the description") // description
//                .build();
//
//
//
//
//        fab.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override public void onGlobalLayout() {
//                fab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                Spotlight.with(mActivity)
//                        .setDuration(1) // duration of Spotlight emerging and disappearing in ms
//                .setAnimation(new DecelerateInterpolator(2f)) // animation of Spotlight
//                        .setTargets(simpleTarget) // set targes. see below for more info
//                        // callback when Spotlight starts
//                .setOnSpotlightStartedListener(() -> Toast.makeText(mContext, "spotlight is started", Toast.LENGTH_SHORT).show())
//                        // callback when Spotlight ends
//                .setOnSpotlightEndedListener(() ->{
//                    Toast.makeText(mContext, "spotlight is ended", Toast.LENGTH_SHORT).show();
//                })
//                        .start(); // start Spotlight
//            }
//        });
//
//    }

}