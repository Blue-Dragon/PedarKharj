package com.example.pedarkharj_edit3.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.BuyerDialog;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Expense;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.web_db_pref.SharedPrefManager;

import java.util.List;
import java.util.Objects;

public class EventDetailActivity extends AppCompatActivity  {
    List<Expense> expenseList;
    List<Event> events;

    public static int lastSeenEventId;
    Context mContext ;
    Activity mActivity;
    MyAdapter adaptor;
    Event curEvent;
    DatabaseHelper db;
    //
    RelativeLayout mySpinner;
    TextView spinnerTv;
    RecyclerView recyclerView;
    TextView tvR1, tvR2, tvC1, tvC2, tvL1, tvL2; //The rectangle above


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // inits
        doInits();
        setCurEvent();
        initRectangleAbove(curEvent);  //doInits Rectangle


        // RecView
        setRecView(curEvent);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Expense expense = expenseList.get(position);

                Intent i = new Intent(mContext, EachExpenseDetailActivity.class);
                i.putExtra(Routines.SEND_EXPENSE_ID_INTENT, expense.getId());
                startActivity(i);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


        //MySpinner
        mySpinner.setOnClickListener(x -> showEventsDialog(curEvent));

        db.closeDB();
    }




    /********************************************       Methods     ****************************************************/
    /**
     *  set the event to show as recView
     */
    private void setCurEvent() {
        if (lastSeenEventId > 0) {
            curEvent = db.getEventById(lastSeenEventId);
        }
        else curEvent = events.get(0);

        lastSeenEventId = curEvent.getId();
        SharedPrefManager.getInstance(mContext).saveLastSeenEventId(lastSeenEventId); //save  lastSeenEventId in SharedPref
        spinnerTv.setText(curEvent.getEventName());
    }

    private void doInits() {
        mContext = this;
        mActivity = this;
        db = new DatabaseHelper(mContext);

        Toolbar toolbar = findViewById(R.id.m_toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);
        events = db.getAllEvents(); //for spinner && def partices
        lastSeenEventId = SharedPrefManager.getInstance(mContext).getDefEventId();

        recyclerView = findViewById(R.id.recycler_view);
        mySpinner = findViewById(R.id.my_spinner);
        spinnerTv  = findViewById(R.id.spinner_tv);

        //the rectangle above
        tvL1 = findViewById(R.id.tv_title_my_expense);
        tvL2 = findViewById(R.id.tv_my_expense);
        tvC1 = findViewById(R.id.tv_title_my_dong);
        tvC2 = findViewById(R.id.tv_my_dong);
        tvR1 = findViewById(R.id.tv_title_my_result);
        tvR2 = findViewById(R.id.tv_my_result);

        tvR1.setTextColor(getResources().getColor(R.color.grayTextColor));
        tvR2.setTextColor(getResources().getColor(R.color.grayTextColor));
        tvL1.setTextColor(getResources().getColor(R.color.primaryTextColor));
        tvL2.setTextColor(getResources().getColor(R.color.primaryTextColor));
        tvR1.setText("خرج های من");
        tvC1.setText("دونگ های من");
        tvL1.setText("حساب من");

    }

    private void showEventsDialog(Event curEvent) {
        BuyerDialog buyerDialog = new BuyerDialog(mActivity, curEvent, R.layout.sample_event);
        Objects.requireNonNull(buyerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        buyerDialog.show();
    }


    //-------------------------     RecyclerView    --------------------------//
    private void setRecView(Event curEvent) {
        expenseList = db.getAllExpensesOfEvent(curEvent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //
        adaptor = new MyAdapter(mContext);
        adaptor.setLayout(R.layout.sample_each_expense);
        adaptor.setExpenseList(expenseList);
        recyclerView.setAdapter(adaptor);
    }


    //todo: complete it
    private void initRectangleAbove(Event event) {
//        tvL2.setText(String.valueOf(0));
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


}
