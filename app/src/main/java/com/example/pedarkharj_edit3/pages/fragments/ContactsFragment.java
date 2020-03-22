package com.example.pedarkharj_edit3.pages.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.IEditBar;
import com.example.pedarkharj_edit3.classes.IOnBackPressed;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.models.Contact;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.pages.AddContactActivity;
import com.example.pedarkharj_edit3.pages.AddEventParticesActivity;

import java.util.ArrayList;
import java.util.List;


public class ContactsFragment extends Fragment implements IOnBackPressed, IEditBar {
    List<Contact> contactList;
    static DatabaseHelper db;
    MyAdapter adaptor;
    Context mContext;
    Activity mActivity;

    RecyclerView recyclerView;
    FloatingActionButton fab;
    Toolbar toolbar;
    View mView;
    ImageView backBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_contacts, container, false);
        init();

        backBtn.setOnClickListener(item -> Toast.makeText(mContext, "back", Toast.LENGTH_SHORT).show());
        setHasOptionsMenu(true); //for menu items in fragment (edit & delete)

        //Floating Btn
        fab = mView.findViewById(R.id.fab);
        fab.setOnClickListener(view0 -> {
//            startActivityForResult(new Intent(mContext, AddContactActivity.class), INTENT_CODE);
            startActivity(new Intent(getActivity(), AddContactActivity.class));
//            mActivity.finish();
        });

//        //recyclerView
        setRecView();
        /*
         * recView onClick
         */
        Log.e("recOnClick", "onClick");
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        return mView;
    }

    /********************************************       Methods     ****************************************************/
    private void init() {
        mContext = getContext();
        mActivity = getActivity();
        MainActivity.navPosition = Routines.EVENTS;

        toolbar =  mView.findViewById(R.id.m_toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);

        db = new DatabaseHelper(mContext);

        backBtn = mView.findViewById(R.id.back_btn);
        recyclerView = mView.findViewById(R.id.recycler_view);
    }


    private void setRecView() {
        contactList = db.getAllContacts();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adaptor = new MyAdapter(mContext);
        adaptor.setLayout(R.layout.sample_conntacts_horizental);
        adaptor.setContactList(contactList);
        recyclerView.setAdapter(adaptor);
    }

    //---------------    ActionMode (selection on longClick)    ----------------//

    private void restartPage(short page) {
        MainActivity.navPosition = page;
        mActivity.finish();
        startActivity(mActivity.getIntent());
    }

    @Override
    public void onMyBackPressed() {

    }


}