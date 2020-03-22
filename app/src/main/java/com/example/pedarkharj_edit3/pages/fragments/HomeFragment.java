package com.example.pedarkharj_edit3.pages.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                    int debt = db.getParticeDebt(expense.getExpenseId(), participant.getId());
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
        //
        adaptor = new MyAdapter(mContext, R.layout.sample_participant, mParticipants);
        recyclerView.setAdapter(adaptor);
    }

    //todo: def event should be one and make sense
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

    //todo: complete it
    private void initRectangleAbove(Event event) {
//        tvL2.setText(String.valueOf(0));
        List<Participant> participants = db.getAllParticeUnderEvent(event);
        if (participants.size() > 0) {
            int myExpenses = db.getParticTotalExpensePriceByParticeId(participants.get(0).getId()); //it is me. 1st partice of all
            int myDebt = db.getAllParticDebtsByParticeId(participants.get(0).getId()); //it is me. 1st partice of all
            int allEventExpenses = db.getEventTotalExpensesByEventId(event.getId());
            tvL2.setText(String.valueOf(allEventExpenses));
            tvC2.setText(String.valueOf(myExpenses));
            tvR2.setText(String.valueOf(myExpenses - myDebt));
        }

    }


}