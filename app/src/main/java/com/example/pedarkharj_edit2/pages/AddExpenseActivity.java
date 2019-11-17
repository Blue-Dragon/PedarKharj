package com.example.pedarkharj_edit2.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Event;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.Routines;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Participant> mParticipants;
    ParticipantAdapter adapter;
    LinearLayout calculator;

    RecyclerView recyclerView;
    Context mContext;
    Activity mActivity;
    Button removeBtn, dateBtn, particBtn,
    bp, b0, b1, b2, b3, b4, b5, b6, b7, b8, b9;
    boolean pbCanUse;
    EditText dongEText;
    RelativeLayout buyerBtn;
    Date mDate;
    TextView priceTv;

    int particId;
    Participant buyer;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        mContext = this;
        mActivity = this;
        pbCanUse = true;
        mParticipants = new ArrayList<Participant>();
        db = new DatabaseHelper(mContext);
        particId = getIntent().getIntExtra(Routines.PARTICIPANT_INFO, 0);
        buyer = db.getParticeById(particId);



        particBtn = findViewById(R.id.custom_dong_btn);              particBtn.setOnClickListener(this);
        buyerBtn = findViewById(R.id.buyer_btn);                         buyerBtn.setOnClickListener(this);
        dateBtn = findViewById(R.id.date_btn);                             dateBtn.setOnClickListener(this);
        dongEText = findViewById(R.id.dong_Etxt);
        priceTv = findViewById(R.id.price_txt);
        recyclerView = findViewById(R.id.participants_RecView);
        //
        calculator = findViewById(R.id.calculator);                          calculator.setOnClickListener(this);
//        bp = findViewById(R.id.bp);
//        b0 = findViewById(R.id.b0);
//        b1 = findViewById(R.id.b1);
//        b2 = findViewById(R.id.b2);
//        b3 = findViewById(R.id.b3);
//        b4 = findViewById(R.id.b4);
//        b5 = findViewById(R.id.b5);
//        b6 = findViewById(R.id.b6);
//        b7 = findViewById(R.id.b7);
//        b8 = findViewById(R.id.b8);
//        b9 = findViewById(R.id.b9);


        //
        if (particId != 0)  dongEText.setHint("خب... " +buyer.getName() + " چی خریده؟");
        //
        doRecyclerView();

    }



    /********************************************       Methods     ****************************************************/


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

            case R.id.custom_dong_btn:
                startActivity(new Intent(mContext,  DiffDongActivity.class));
                break;

            case R.id.buyer_btn:
                Toast.makeText(mContext, "buyer_btn", Toast.LENGTH_SHORT).show();
                break;

            case R.id.date_btn:
                DatePicker.Builder builder = new DatePicker
                        .Builder()
                        .theme(R.style.DialogTheme);
                mDate = new Date();
                builder.date(mDate.getDay(), mDate.getMonth(), mDate.getYear());
                builder.build((id1, calendar, day, month, year) -> {
                    mDate.setDate(day, month, year);
                    //dateBtn
                    dateBtn.setText(mDate.getDate());
                }).show(getSupportFragmentManager(), "");
                break;
        }
    }

    //
    public void onCalcClick(View view) {
        Button b = mActivity.findViewById(view.getId() ) ;

        StringBuilder builder = new StringBuilder();
        if (Float.valueOf(priceTv.getText().toString()) > 0f ) builder.append(priceTv.getText());

        // Not letting user to use '.' twice
        if (b.getId() == R.id.bp ){
            if (pbCanUse) {
                pbCanUse = false;
                builder.append(b.getText());
            }
        }
            //delete
        else if (b.getId() == R.id.bkSpace){
            char[] chars = priceTv.getText().toString().toCharArray();
            int i = chars.length;

            if (chars.length > 0 ){
                builder.delete(i-1, i);

                chars = builder.toString().toCharArray();
                if ( chars.length < 1 ) builder.append(0);
            }
        }
        // add number to priceTv
        else {
            builder.append(b.getText());
        }


        priceTv.setText(builder);
    }

    private void doRecyclerView() {

        //show partices of the Event todo: update -> get event
        Event event = db.getEventById(1);
        List<Participant> participants0 = db.getAllParticeUnderEvent(1);
        mParticipants.addAll(participants0);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.HORIZONTAL, false);
//        gridLayoutManager.setOrientation(gridLayoutManager.scrollHorizontallyBy(3));
        recyclerView.setLayoutManager(gridLayoutManager);
        //
        adapter = new ParticipantAdapter(mContext, R.layout.sample_contact, mParticipants);
        recyclerView.setAdapter(adapter);
    }




    //Persian Calender
    class Date extends DateItem {
        String getDate() {
            Calendar calendar = getCalendar();
            return String.format(Locale.US,
                    "%d/%d/%d",
                    getYear(), getMonth(), getDay(),
                    calendar.get(Calendar.YEAR),
                    +calendar.get(Calendar.MONTH) + 1,
                    +calendar.get(Calendar.DAY_OF_MONTH));
        }}

}
