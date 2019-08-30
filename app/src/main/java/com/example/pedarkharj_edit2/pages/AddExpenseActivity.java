package com.example.pedarkharj_edit2.pages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.Contact;
import com.example.pedarkharj_edit2.classes.ContactsAddAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Contact> contacts;
    ContactsAddAdapter adapter;
    RecyclerView recyclerView;
    Context mContext;
    Button removeBtn, dateBtn, particBtn;
    RelativeLayout buyerBtn;
    Date mDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        mContext = this;
        particBtn = findViewById(R.id.partic_btn); particBtn.setOnClickListener(this);
        buyerBtn = findViewById(R.id.buyer_btn); buyerBtn.setOnClickListener(this);
        dateBtn = findViewById(R.id.date_btn); dateBtn.setOnClickListener(this);




        recyclerView = findViewById(R.id.chooseBuyer_RecView);

        contacts = new ArrayList<Contact>();
        contacts.add(new Contact(drawableToBitmap(R.drawable.w), "hamed"));
        contacts.add(new Contact("reza dasdf dadas dasd"));
        contacts.add(new Contact(drawableToBitmap(R.drawable.r),"غلوم"));
        contacts.add(new Contact("حسین عباس پور"));
        contacts.add(new Contact("محمد صیدالی"));
        contacts.add(new Contact("پیمان"));
        contacts.add(new Contact("reza"));
        contacts.add(new Contact(drawableToBitmap(R.drawable.r),"مری"));
        contacts.add(new Contact("غلوم"));
        contacts.add(new Contact(drawableToBitmap(R.drawable.w),"حامد گنجعلی"));
        contacts.add(new Contact("حسین حسی حشسیح حظسز شسزبح پور"));
        contacts.add(new Contact("حامد گنجعلی"));
        contacts.add(new Contact("محمد صیدالی"));
        contacts.add(new Contact("مری"));
        contacts.add(new Contact("پیمان"));
        contacts.add(new Contact("reza"));
        contacts.add(new Contact("غلوم"));
        contacts.add(new Contact("حسین عباس پور"));
        contacts.add(new Contact("حامد گنجعلی"));
        contacts.add(new Contact("محمد صیدالی"));
        contacts.add(new Contact("مری"));
        contacts.add(new Contact("پیمان"));
        contacts.add(new Contact("reza"));
        contacts.add(new Contact("غلوم"));
        contacts.add(new Contact("حسین عباس پور"));
        contacts.add(new Contact("حامد گنجعلی"));
        contacts.add(new Contact("محمد صیدالی"));
        contacts.add(new Contact("مری"));
        contacts.add(new Contact("پیمان"));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.HORIZONTAL, false);
//        gridLayoutManager.setOrientation(gridLayoutManager.scrollHorizontallyBy(3));
        recyclerView.setLayoutManager(gridLayoutManager);
        //
        adapter = new ContactsAddAdapter(mContext, contacts);
        recyclerView.setAdapter(adapter);
    }



    /********************************************       Methods     ****************************************************/

    Bitmap drawableToBitmap(int drawable){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable);
        return bitmap;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

            case R.id.partic_btn:
                startActivity(new Intent(mContext,  ParticipantsActivity.class));
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
