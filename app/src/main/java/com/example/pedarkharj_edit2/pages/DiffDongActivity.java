package com.example.pedarkharj_edit2.pages;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Event;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit2.classes.Routines;

import java.util.ArrayList;
import java.util.List;

public class DiffDongActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    static boolean CASH_MODE = true;
    static boolean DONG_MODE = false;

    RecyclerView recyclerView;
    List<Participant> usersList;
    DatabaseHelper db;
    ParticipantAdapter adaptor;
    Context mContext = this;
    Activity mActivity = this;
    Event curEvent;

    int dongsNumber, eachDongAmount,expense;
    int[] usersIds;
    int userDong;
    boolean layoutMode;

    FloatingActionButton fab;
    Spinner spinner;
    // the rectangle above
    TextView tvR1, tvR2, tvC1, tvC2, tvL1, tvL2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_dong);
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        usersList = new ArrayList<Participant>();
        db = new DatabaseHelper(mContext);
        curEvent = db.getEventById( getIntent().getIntExtra(Routines.SEND_EVENT_ID_INTENT, 1) );
        layoutMode = DONG_MODE; //by default

        expense = getIntent().getIntExtra(Routines.SEND_EXPENSE_INT_INTENT, 0);
        usersIds = getIntent().getIntArrayExtra(Routines.SEND_USERS_INTENT);
        dongsNumber = usersIds.length;
        eachDongAmount = (int) (expense/dongsNumber);

        //the rectangle above
        tvL1 = findViewById(R.id. tv_title_my_expense);
        tvL2 = findViewById(R.id.tv_my_expense );
        tvC1 = findViewById(R.id. tv_title_my_dong);
        tvC2 = findViewById(R.id. tv_my_dong);
        tvR1 = findViewById(R.id. tv_title_my_result);
        tvR2 = findViewById(R.id. tv_my_result);


        //back imageView btn
        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(item -> onBackPressed());

        //recyclerView
        recyclerView = findViewById(R.id.diff_dong_recView);
//        doRecyclerView(DONG_MODE); //not needed

        /*
         * Spinner
         */
        spinner = findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("تعداد دنگ");
        list.add("مقداد دنگ");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        //
        spinner.setOnItemSelectedListener(this);

        /*
         * recView onClick
         */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Button plus = view.findViewById(R.id.plus_btn);
                Button minus = view.findViewById(R.id.minus_btn);
                EditText editText = view.findViewById(R.id.dong_Etxt2);
                userDong = Integer.valueOf(editText.getText().toString());
                Log.i("fuck013", userDong + "");

                plus.setOnClickListener(item -> {
                    editText.setText(String.valueOf(++userDong));
                    tvC2.setText(String.valueOf(++dongsNumber));
                    eachDongAmount = (int) (expense/dongsNumber);
                    tvR2.setText(String.valueOf(eachDongAmount));
                });
                minus.setOnClickListener(item -> {
                    tvC2.setText(String.valueOf(--dongsNumber));
                    editText.setText(String.valueOf(--userDong));
                    eachDongAmount = (int) (expense/dongsNumber);
                    tvR2.setText(String.valueOf(eachDongAmount));
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        /*
         * the rectangle above
         */
        if (layoutMode == DONG_MODE){
            tvL1.setText("مبلغ خرج");
            tvL2.setText(String.valueOf(expense));

            tvC1.setText("تعداد دنگ ها");
            tvC2.setText(String.valueOf(dongsNumber));

            tvR1.setText("قیمت هر دنگ");
            tvR2.setText(String.valueOf(eachDongAmount));

        }else {

        }


        /*
         * Floating Btn
         */
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            //todo: done btn
        });
    }




    /********************************************       Methods     ****************************************************/
    private void doRecyclerView(boolean cashMode) {
        usersList.clear();
        for (int i : usersIds){
            usersList.add(db.getParticeById(i));
        }
        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        if (!cashMode) {
            adaptor = new ParticipantAdapter(mActivity, R.layout.sample_diff_dong__dong_mode, usersList);
        }
        else {
            adaptor = new ParticipantAdapter(mActivity, R.layout.sample_diff_dong__cash_mode, usersList);
        }
        recyclerView.setAdapter(adaptor);
    }

    //spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedIten = parent.getItemAtPosition(position).toString();
        if (selectedIten.equals("تعداد دنگ")){
            doRecyclerView(DONG_MODE);
            layoutMode = DONG_MODE;
        }else{
            doRecyclerView(CASH_MODE);
            layoutMode = CASH_MODE;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
