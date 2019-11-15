package com.example.pedarkharj_edit2.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    LinearLayout calcLT;
    DatabaseHelper db;

    RecyclerView recyclerView;
    Context mContext;
    Activity mActivity;
    Button removeBtn, dateBtn, particBtn;
    RelativeLayout buyerBtn;
    Date mDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        mContext = this;
        mActivity = this;
        mParticipants = new ArrayList<Participant>();
        db = new DatabaseHelper(mContext);

        particBtn = findViewById(R.id.custom_dong_btn);              particBtn.setOnClickListener(this);
        buyerBtn = findViewById(R.id.buyer_btn);                         buyerBtn.setOnClickListener(this);
        dateBtn = findViewById(R.id.date_btn);                             dateBtn.setOnClickListener(this);
        calcLT = findViewById(R.id.calculator);
        recyclerView = findViewById(R.id.participants_RecView);
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
                Toast.makeText(mContext, "Hi", Toast.LENGTH_SHORT).show();
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
