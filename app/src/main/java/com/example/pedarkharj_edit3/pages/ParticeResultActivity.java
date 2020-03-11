package com.example.pedarkharj_edit3.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Expense;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.pages.fragments.HomeFragment;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class ParticeResultActivity extends AppCompatActivity {
    List<Expense> expenseList;
    Event curEvent;
    Participant selectedPartic;
    DatabaseHelper db;
    MyAdapter adaptor;
    Context mContext;
    Activity mActivity;

    RecyclerView recyclerView;
    TextView tvR1, tvR2, tvC1, tvC2, tvL1, tvL2; //The rectangle above


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partice_result);

        mContext = this;
        mActivity = this;

        //-------------------------    inits    -------------------------- //
        Toolbar toolbar = findViewById(R.id.m_toolbar);
        ((AppCompatActivity) mActivity).setSupportActionBar(toolbar);

        db = new DatabaseHelper(mContext);

        //the rectangle above
        tvL1 = findViewById(R.id.tv_title_my_expense);
        tvL2 = findViewById(R.id.tv_my_expense);
        tvC1 = findViewById(R.id.tv_title_my_dong);
        tvC2 = findViewById(R.id.tv_my_dong);
        tvR1 = findViewById(R.id.tv_title_my_result);
        tvR2 = findViewById(R.id.tv_my_result);

        Intent data = getIntent();
        int sentEventId = data.getIntExtra(Routines.SEND_EVENT_ID_INTENT, 0);
        int sentParticeId = data.getIntExtra(Routines.SEND_PARTICIPANT_ID_INTENT, 0);
        if (sentParticeId > 0)
            selectedPartic = db.getParticeById(sentParticeId);
        if (sentEventId > 0)
            curEvent = db.getEventById(sentEventId);
        if (curEvent != null){
//            expenseList = db.getAllExpensesOfEvent(curEvent);

            // not all expenses in expenseList are the ones our partice has participated in.
            expenseList = HomeFragment.newExpenseList;
        }

        recyclerView = findViewById(R.id.recycler_view);
        doRecyclerView();
        initRectangleAbove(curEvent);  //init Rectangle
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        db.closeDB();
    }

    /********************************************       Methods     ****************************************************/

    //-------------------------     RecyclerView    --------------------------//
    private void doRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        adaptor = new MyAdapter(mContext);
        adaptor.setLayout(R.layout.sample_each_expense_2);
//        adaptor.setExpenseList(expenseList);

        adaptor.setExpenseList(expenseList);
        adaptor.setSelectedPartic(selectedPartic);

        adaptor.setExpenseMode2(true);

        recyclerView.setAdapter(adaptor);
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