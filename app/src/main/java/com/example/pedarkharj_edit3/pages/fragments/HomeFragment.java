package com.example.pedarkharj_edit3.pages.fragments;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    public static List<Expense> newExpenseList;
    List<Participant> mParticipants;
    List<Event> events;
    List<Integer> eventSpinerList;
    Map<Integer, Event> spinnerEventIdsMap;
    Contact[] defContats;

    public static int lastSeenEventId;
    Context mContext ;
    Activity mActivity;
    MainActivity mainActivity = new MainActivity();
    MyAdapter adaptor;
    Event curEvent;
    DatabaseHelper db;
    //
    CardView cardView;
    RecyclerView recyclerView;
    Spinner spinner;
    FloatingActionButton fab;
    CircleImageView drawerProfPic;
    ImageView menu, sync_iv;
    TextView tvR1, tvR2, tvC1, tvC2, tvL1, tvL2; //The rectangle above
    //
//    int sentEventId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getContext();
        mActivity = getActivity();
        MainActivity.navPosition = Routines.HOME;

        //-------------------------    inits    -------------------------- //
        Toolbar toolbar = view.findViewById(R.id.m_toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);

        db = new DatabaseHelper(mContext);

//        newExpenseList = new ArrayList<>();

//        mParticipants = new ArrayList<>();
        events = db.getAllEvents(); //for spinner && def partices
        eventSpinerList = new ArrayList<>();
        spinnerEventIdsMap = new HashMap<Integer, Event>(); //todo: use `new sparseArray<Event>` instead
        lastSeenEventId = SharedPrefManager.getInstance(mContext).getDefEventId();
        cardView = view.findViewById(R.id.details_card_layout);         cardView.setOnClickListener(this);

//        sentEventId = mActivity.getIntent().getIntExtra(Routines.SEND_EVENT_ID_INTENT, 0);
//        sync_iv = findViewById(R.id.sync);
//        sync_iv.setOnClickListener(this);

        //the rectangle above
        tvL1 = view.findViewById(R.id.tv_title_my_expense);
        tvL2 = view.findViewById(R.id.tv_my_expense);
        tvC1 = view.findViewById(R.id.tv_title_my_dong);
        tvC2 = view.findViewById(R.id.tv_my_dong);
        tvR1 = view.findViewById(R.id.tv_title_my_result);
        tvR2 = view.findViewById(R.id.tv_my_result);

        /*-------------------------     RecView   --------------------------*/

        /*
         * Setting default event and partices
         */
        createDefEvent();

        recyclerView = view.findViewById(R.id.rv_partice_expenses);

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



        /* -------------------------     Spinner    -------------------------- */
        spinner = view.findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();

        int j = 0;
        events = db.getAllEvents();
        for (Event event : events) {
            if (!event.getEventName().equals(Routines.EVENT_TEMP_NAME)) {
                list.add(event.getEventName());
                eventSpinerList.add(j);
                //save spinner events by order ids
                spinnerEventIdsMap.put(j++, event);
            }
        }
        if (events.size() == 0) createDefEvent();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        /*
         *  set the event to show as recView
         */
        // setting Cur Event
        if (lastSeenEventId > 0) {
            curEvent = db.getEventById(lastSeenEventId);
        } else curEvent = events.get(0);

        /*
         * setting spinner to show lastSeenEvent items
         */
        int selectedEventSpinnerId = 0;
        if (lastSeenEventId > 0) {
            for (int i = 0; i < events.size(); i++) {
                if (events.get(i).getId() == lastSeenEventId) {
                    selectedEventSpinnerId = i;
                    break;
                }
            }
        }


        SharedPrefManager.getInstance(mContext).saveLastSeenEventId(lastSeenEventId); //save  lastSeenEventId in SharedPref
//        Log.i("fuck011", "lastSeenEventId: " + lastSeenEventId + "");


        /*
         * setting spinner to show lastSeenEvent items:
         * Here, if we have a chosen event already (by Intent)
         */
//        if (sentEventId > 0 && sentEventId != lastSeenEventId) {
//
//            for (int i = 0; i < events.size(); i++) {
//                if (events.get(i).getId() == sentEventId) {
//                    selectedEventSpinnerId = i;
//                    break;
//                }
//            }
//        }
        spinner.setSelection(selectedEventSpinnerId); //def event

        spinner.setOnItemSelectedListener(this);
//        Log.i("fuck011", "saveLastSeenEventId: " + curEvent.getId() + "");



        /* -------------------------     recView onClick    -------------------------- */
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

        //-------------------------     Floating Btn    --------------------------//
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view0 -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            startActivity(new Intent(mContext, AddExpenseActivity.class));

            showBuyerDialog(curEvent);
        });


        //drawerLayout
//        createDrawer();

        //Close db
        db.closeDB();

        return view;
    }

    /********************************************       Methods     ****************************************************/


    private void showBuyerDialog(Event curEvent) {
        new BuyerDialog(mActivity, curEvent).show();
    }

    //-------------------------     Spinner    --------------------------//
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Event event = spinnerEventIdsMap.get(i);
        if (event == null)
            event = events.get(0); //todo: change it. if we remove the last event, there shouldn't be any. dough!

        for (Event event0 : events) {
            Log.i("fuck025", event0.getEventName());
        }

        curEvent = event;
        setRecParticesUnderEvent(curEvent); //show recyclerView
        SharedPrefManager.getInstance(mContext).saveLastSeenEventId(curEvent.getId()); //save curEvent (as defEvent for next time) to SharedPref
        initRectangleAbove(curEvent);  //init Rectangle


    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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


    private void setRecParticesUnderEvent(Event curEvent) {

        //show partices of the Event
        mParticipants = db.getAllParticeUnderEvent(curEvent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        adaptor = new MyAdapter(mContext, R.layout.sample_participant, mParticipants);
        recyclerView.setAdapter(adaptor);
    }

    private void createDefEvent() {
        if (events.size() < 1) {
            /*
             * adding partices and an event
             */
            createEvent("سفر شمال");
            createEvent("سفر 2");
            createEvent("سفر 3");
            createEvent("سفر 4");
            createEvent("سفر 5");

        }
    }

    private void createEvent(String eventName) {

        /*
         * adding partices and an event
         */
        if (defContats == null || defContats.length < 1) {
            creatingDefContacts();
        }
        db.createNewEventWithPartices(new Event(eventName), defContats);

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