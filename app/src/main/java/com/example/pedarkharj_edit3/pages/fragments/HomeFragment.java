package com.example.pedarkharj_edit3.pages.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.BuyerDialog;
import com.example.pedarkharj_edit3.classes.models.Contact;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Expense;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
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
    List<Contact> defContats;

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
    ImageView upgrade_btn;

    ImageView mailBtn;
    TextView mailTv;
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
        // fab hides on scroll
        Routines.hideFabOnScroll(fab, recyclerView);

        //Tutorial
        if ( SharedPrefManager.getInstance(mActivity).getRunTurn(Routines.KEY_TURN_TIME_HOME) == Routines.FIRST_RUN ){
            createDefEvent();
            new Handler().postDelayed(() -> showTabTargetsSequences2(view), 1000);   // Delay 1 sec
        }
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
                    float debt = db.getParticeDebt(expense.getExpenseId(), participant.getId()); // returns -1 of not participated
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

            Log.d("curEventID", "cur event id: "+ curEvent.getId());


            lastSeenEventId = curEvent.getId();
            SharedPrefManager.getInstance(mContext).saveLastSeenEventId(lastSeenEventId); //save  lastSeenEventId in SharedPref
            spinnerTv.setText(curEvent.getEventName());
        }
        else {
            curEvent = null;
//            createDefEvent();
//            setCurEvent();
        }
    }

    private void doInits(View view) {
        MainActivity.navPosition = Routines.HOME;
        mContext = getContext();
        mActivity = getActivity();
        MainActivity.navPosition = Routines.HOME;
        Toolbar toolbar = view.findViewById(R.id.m_toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);
        db = new DatabaseHelper(mContext);
        /*
            events (temps not mentioned
         */
        events = Routines.deleteTempEvents(mActivity, db.getAllEvents());
//        events = db.getAllEvents(); //for spinner && def partices
        lastSeenEventId = SharedPrefManager.getInstance(mContext).getDefEventId();
        cardView = view.findViewById(R.id.details_card_layout);
        recyclerView = view.findViewById(R.id.rv_partice_expenses);
        mySpinner = view.findViewById(R.id.my_spinner);
        spinnerTv  = view.findViewById(R.id.spinner_tv);
        fab = view.findViewById(R.id.fab);
//        upgrade_btn = view.findViewById(R.id.mail_btn);

        //the rectangle above
        tvL1 = view.findViewById(R.id.tv_title_my_expense);
        tvL2 = view.findViewById(R.id.tv_my_expense);
        tvC1 = view.findViewById(R.id.tv_title_my_dong);
        tvC2 = view.findViewById(R.id.tv_my_dong);
        tvR1 = view.findViewById(R.id.tv_title_my_result);
        tvR2 = view.findViewById(R.id.tv_my_result);

        //
        mailBtn =view.findViewById(R.id.mail_btn);
        mailTv = view.findViewById(R.id.mail_tv);


        tvR1.setTextColor(getResources().getColor(R.color.grayTextColor));
        tvR2.setTextColor(getResources().getColor(R.color.grayTextColor));
        tvL1.setTextColor(getResources().getColor(R.color.primaryTextColor));
        tvL2.setTextColor(getResources().getColor(R.color.primaryTextColor));
        tvR1.setText("خرج من");
        tvC1.setText("دونگ من");
        tvL1.setText("حساب من");


    }

    private void doOnClicks() {
        cardView.setOnClickListener(this);
        mySpinner.setOnClickListener(this);
//        upgrade_btn.setOnClickListener(this);
        mailBtn.setOnClickListener(this);
        mailTv.setOnClickListener(this);
    }

    private void showBuyerDialog(Event curEvent) {
        if (curEvent!= null){
            BuyerDialog buyerDialog = new BuyerDialog(mActivity, curEvent);
            Objects.requireNonNull(buyerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

            if ( SharedPrefManager.getInstance(mActivity).getRunTurn(Routines.KEY_TURN_TIME_HOME) == Routines.FIRST_RUN ){
                buyerDialog.setCancelable(false);
            }
            buyerDialog.show();
        }
    }

    private void showEventsDialog(Event curEvent) {
        if (curEvent != null){
            BuyerDialog buyerDialog = new BuyerDialog(mActivity, curEvent, R.layout.sample_event);
            Objects.requireNonNull(buyerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            buyerDialog.show();
        }
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

//            case R.id.upgrade_btn:
//                startActivity(new Intent(mActivity, InfoActivity.class));
//                break;

            case R.id.mail_tv:
            case R.id.mail_btn:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse("mailto:"));
                i.setType("message/rfc822");
//                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getString(R.string.my_email)});
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
                i.putExtra(Intent.EXTRA_TEXT   , getString(R.string.email_body));
                try {
                    startActivity(Intent.createChooser(i, "ارسال با ..."));
                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
                break;

            default:
                break;
//                Toast.makeText(mContext, "Wrong item clicked!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRecView(Event curEvent) {
        if (curEvent != null){

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
    }

    private void createDefEvent() {
        if (events.size() < 1) {
            /*
             * adding partices and an event
             */
            lastSeenEventId = (int) createEvent("رویداد پیش فرض");

            events = db.getAllEvents();
        }
    }

    private long createEvent(String eventName) {

        /*
         * adding partices and an event
         */
        List<Contact> contacts = db.getAllContacts();
        if ( (defContats == null || defContats.size() < 1) && contacts.size() <1) {
            creatingDefContacts();
        }
        return db.createNewEventWithContacts(new Event(eventName), defContats);

    }

    private void creatingDefContacts() {
        long contact_1 = db.createContact(new Contact("من"));
        long contact_2 = db.createContact(new Contact("مخاطب 1"));
        long contact_3 = db.createContact(new Contact("مخاطب 2"));
        long contact_4 = db.createContact(new Contact("مخاطب 3"));

        defContats = new ArrayList<>();
        defContats.add(db.getContactById(contact_1));
        defContats.add(db.getContactById(contact_2));
        defContats.add(db.getContactById(contact_3));
//        defContats.add(db.getContactById(contact_4));
    }

    private void initRectangleAbove(Event event) {
        if (curEvent != null){
            List<Participant> participants = db.getAllParticeUnderEvent(event);
            if (participants.size() > 0) {
                Participant participant = db.getParticeByContactId(event.getId(), 1);

                float myExpenses = db.getParticTotalExpensePriceByParticeId(participant.getId());
                float myDebt = db.getAllParticDebtsByParticeId(participant.getId());
//            int allEventExpenses = db.getEventTotalExpensesByEventId(event.getId());
                tvR2.setText( Routines.getRoundFloatString(myExpenses));
                tvC2.setText(Routines.getRoundFloatString(myDebt));

                //
                float result = myExpenses - myDebt;
                String resultFloatString = Routines.getRoundFloatString(result);

                if (result > 0) resultFloatString = "+"+resultFloatString;
                tvL2.setText(resultFloatString);
                Routines.setTextColor(mContext, tvL2, tvL2.getText().toString());

                String resultTxt = result>=0 ? "طلب من" : "بدهی من" ;
                tvL1.setText(resultTxt);
            }
        }

//        Typeface tf = Typeface.createFromAsset(mActivity.getAssets(), "b_koodak.ttf");
        Typeface tf = Routines.getTypeFaceKoodak(mContext);
        tvR1.setTypeface(tf);
        tvR2.setTypeface(tf);
        tvL1.setTypeface(tf);
        tvL2.setTypeface(tf);
        tvC1.setTypeface(tf);
        tvC2.setTypeface(tf);

        Typeface tf0 = Routines.getTypeFaceYakan(mContext);
        mailTv.setTypeface(tf0);

        Typeface tf1 = Routines.getTypeFaceYakanB(mContext);
        spinnerTv.setTypeface(tf1);

    }


    /**
     * showCase for one item
     */
    public void showTabTargetView(View view) {

        TapTargetView.showFor(mActivity,                 // `this` is an Activity
                TapTarget.forView(fab, "This is a target", "We have the best targets, believe me")
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimaryDark)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.white)   // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(12)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.bk1)  // Specify the color of the description text
//                        .textColor(R.color.blue)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
//                        .icon(getResources().getDrawable(R.drawable.logo))                     // Specify a custom drawable to draw as the target
                        .targetRadius(60), // Specify the target radius (in dp)

                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
//                        doSomething();
//                        Toast.makeText(mContext, "Do something", Toast.LENGTH_SHORT).show();
//                        showTabTargetsSequences1(view);

                    }
                });



    }

    /**
     * showCase for multiple  items (a sequence of items)
     */
    public void showTabTargetsSequences2(View view) {
// 1

        new TapTargetSequence(mActivity)
                // 2
                .targets(
                        TapTarget.forView(fab, getString(R.string.addExpenseFab_title), getString(R.string.addExpenseFab_description))
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
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(50)

//                        TapTarget.forView(spinnerTv, "a", " desc")
//                                .outerCircleColor(R.color.colorPrimaryDark).outerCircleAlpha(0.96f).targetCircleColor(R.color.white)
//                                .titleTextSize(20).titleTextColor(R.color.white).descriptionTextSize(12).descriptionTextColor(R.color.bk1)
//                                .textTypeface(Typeface.SANS_SERIF).dimColor(R.color.black).drawShadow(true).cancelable(false)
//                                .tintTarget(true).transparentTarget(true).targetRadius(60)
                )
//                .continueOnCancel(true)


                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
//                        mActivity.onBackPressed();
//                        mActivity.startActivity(new Intent(mActivity, AddExpenseActivity.class));
                        fab.performClick();
                        SharedPrefManager.getInstance(mActivity).setNextRunTurn(Routines.KEY_TURN_TIME_HOME, Routines.SECOND_RUN);

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


}