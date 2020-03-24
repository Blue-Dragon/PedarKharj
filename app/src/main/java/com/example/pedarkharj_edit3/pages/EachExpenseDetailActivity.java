package com.example.pedarkharj_edit3.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.models.Expense;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;

import java.util.List;

public class EachExpenseDetailActivity extends AppCompatActivity {
    List<Participant> participantList;
    Context mContext ;
    Activity mActivity;
    MyAdapter adaptor;
    Expense theExpense;
    DatabaseHelper db;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_each_expense);
        mContext = this;
        mActivity = this;

        //-------------------------    inits    -------------------------- //
        Toolbar toolbar = findViewById(R.id.m_toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);

        db = new DatabaseHelper(mContext);
        recyclerView = findViewById(R.id.recycler_view);

        int expenseId;
        Intent i = getIntent();
        expenseId = i.getIntExtra(Routines.SEND_EXPENSE_ID_INTENT, 0);
        if (expenseId > 0)
            theExpense = db.getExpenseByExpenseId(expenseId);

        if (theExpense !=null ){
            participantList = theExpense.getUserPartics();
            setRecyclerView();
        }

        /* -------------------------     recView onClick    -------------------------- */
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
    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        adaptor = new MyAdapter(mContext);
        adaptor.setLayout(R.layout.sample_partice_dong);
        adaptor.setParticipants(participantList);
        adaptor.setExpense(theExpense);
        recyclerView.setAdapter(adaptor);
    }

}
