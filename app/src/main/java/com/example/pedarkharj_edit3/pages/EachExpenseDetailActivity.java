package com.example.pedarkharj_edit3.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.MyCallBack;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.models.Expense;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Each Expense added in an Event has least one buyer.
 * Each Expense (buyer) has a card that shows the expense price. (list shown at `EventDetailActivity` activity)
 * -------------------
 * This activity is called when you click on each card, so you can see the details (users, debts etc.) of that expense.
 */
public class EachExpenseDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static MyCallBack myCallBack;
    List<Participant> participantList;
    Context mContext ;
    Activity mActivity;
    MyAdapter adaptor;
    Expense theExpense;
    DatabaseHelper db;
    TextView dateTv, priceTv;
    FloatingActionButton editFabBtn;
    ImageView deleteBtn;

    RecyclerView recyclerView;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_each_expense);
        Toolbar toolbar = findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        inits();
        onClicks();


        if (theExpense !=null ){
            initExpenseStuff(theExpense);

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
    private void inits() {
        mContext = this;
        mActivity = this;
        db = new DatabaseHelper(mContext);
        recyclerView = findViewById(R.id.recycler_view);
        backBtn = findViewById(R.id.back_btn);
        dateTv = findViewById(R.id.tv_date);
        priceTv = findViewById(R.id.tv_price);
        deleteBtn = findViewById(R.id.delete_btn);
        editFabBtn =  findViewById(R.id.fab);
        //--------------------
        int expenseId;
        Intent i = getIntent();
        expenseId = i.getIntExtra(Routines.SEND_EXPENSE_ExpenseID_INTENT, 0);
        Log.d("theExpense", "received: "+ expenseId );
        if (expenseId > 0){
            theExpense = db.getExpenseByExpenseId(expenseId);
            Log.d("theExpense", "expense.getId: "+ theExpense.getExpenseId() + "");
            Log.d("theExpense",  ".");
        }
        myCallBack = MainActivity.myCallBack;
    }


    private void onClicks() {
        backBtn.setOnClickListener(item -> onBackPressed());
        deleteBtn.setOnClickListener(this);
        editFabBtn.setOnClickListener(this);
    }




    private void initExpenseStuff(Expense theExpense) {
        participantList = theExpense.getUserPartics();
        String price = Routines.getRoundFloatString(theExpense.getExpensePrice());
        priceTv.setText(price + "  تومان");
        dateTv.setText(theExpense.getCreated_at());

    }
    //-------------------------     RecyclerView    --------------------------//
    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //
        adaptor = new MyAdapter(mContext);
        adaptor.setLayout(R.layout.sample_partice_dong);
        adaptor.setParticipants(participantList);
        adaptor.setExpense(theExpense);
        recyclerView.setAdapter(adaptor);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_btn:
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.AlertDialogDanger);
                dialog.setTitle("پاک کنم؟")
                        .setMessage("این اطلاعات از دم نیست و نابود میشن هااا !")
                        .setPositiveButton("پاک کن بره داداش", (dialogInterface, i1) -> {

                            //update partices' total debts
                            updatePartice();
                            db.deleteExpenseGroupByExpenseId(theExpense.getExpenseId());

                            onBackPressed();
                        })

                        .setNegativeButton("نه، بی خیال!", (dialogInterface, i1) -> {})
                        .show();


                break;

            case R.id.fab:
                //edit
                Intent i = new Intent(mActivity, AddExpenseActivity.class);
                i.putExtra(Routines.SEND_EXPENSE_ExpenseID_INTENT, theExpense.getExpenseId());
                startActivity(i);
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * update debt and expensePrice in partice table
     */
    private void updatePartice() {
        float debt;
        float expense;
        float newDebt;

        for (Participant participant : participantList){
            debt = db.getParticeDebt(theExpense.getExpenseId(), participant.getId());

            if (debt > -1){
                newDebt = Routines.getRoundFloat(participant.getDebt() - debt);
                participant.setDebt(newDebt);
            }

            if (participant.getContact().getId() == theExpense.getBuyer().getContact().getId()){
                expense = participant.getExpense() - theExpense.getExpensePrice();
                participant.setExpense(Routines.getRoundFloat(expense));
            }

            db.updatePartice(participant);
        }
        db.updateParticipants(participantList);
        myCallBack.refreshMainActivity(Routines.HOME); //reset and update Home

        Log.d("updateParticeDebt", "_END_");

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(mActivity, EventDetailActivity.class));
        finish();
    }

}
