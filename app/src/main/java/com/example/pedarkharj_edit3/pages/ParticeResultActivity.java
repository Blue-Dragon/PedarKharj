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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Expense;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.pages.fragments.HomeFragment;

import java.util.List;

/**
 * This activity is called when you click on each Expense card (at Home Fragment),
 * so you can see the details of your debts/ expenses.
 */
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
    TextView toolbarTitle;
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partice_result);
        Toolbar toolbar = findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);  //todo: bug
        inits();
        backBtn.setOnClickListener(item -> onBackPressed());

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

    private void inits() {
        mContext = this;
        mActivity = this;
        db = new DatabaseHelper(mContext);

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
            toolbarTitle = findViewById(R.id.textView);
            backBtn = findViewById(R.id.back_btn);

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

            toolbarTitle.setText("جزئیات حساب " + selectedPartic.getName());
            tvR1.setText("خرج ها" );
            tvC1.setText("دونگ ها");
            tvL1.setText("حساب نهایی");
        }

        recyclerView = findViewById(R.id.recycler_view);
    }

    /********************************************       Methods     ****************************************************/

    //-------------------------     RecyclerView    --------------------------//
    private void doRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //
        adaptor = new MyAdapter(mContext);
        adaptor.setLayout(R.layout.sample_each_expense_2);
//        adaptor.setExpenseList(expenseList);

        adaptor.setExpenseList(expenseList); //Only expenses that USER has participated in
        adaptor.setSelectedPartic(selectedPartic);

        adaptor.setExpenseMode2(true);

        recyclerView.setAdapter(adaptor);
    }

    //todo: complete it
    private void initRectangleAbove(Event event) {
//        tvL2.setText(String.valueOf(0));
        List<Participant> participants = db.getAllParticeUnderEvent(event);
        if (participants.size() > 0) {
            float myExpenses = db.getParticTotalExpensePriceByParticeId(selectedPartic.getId());
            float myDebt = db.getAllParticDebtsByParticeId(selectedPartic.getId());
            float allEventExpenses = db.getEventTotalExpensesByEventId(event.getId());
            tvL2.setText(Routines.getRoundFloatString(allEventExpenses));
            tvC2.setText(Routines.getRoundFloatString(myExpenses));
            tvR2.setText(Routines.getRoundFloatString(myExpenses - myDebt));
        }

    }
}