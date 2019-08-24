package com.example.pedarkharj_edit2.pages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.Contact;
import com.example.pedarkharj_edit2.classes.ContactsAddAdapter;

import java.util.ArrayList;

public class AddExpenseActivity extends AppCompatActivity {
    ArrayList<Contact> contacts;
    ContactsAddAdapter adapter;
    RecyclerView recyclerView;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        mContext = this;
        recyclerView = findViewById(R.id.chooseContact_RecView);

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



    Bitmap drawableToBitmap(int drawable){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable);
        return bitmap;
    }
}
