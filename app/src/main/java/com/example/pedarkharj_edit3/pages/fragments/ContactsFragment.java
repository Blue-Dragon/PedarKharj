package com.example.pedarkharj_edit3.pages.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.Contact;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.Participant;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.pages.AddContactActivity;

import java.util.List;


public class ContactsFragment extends Fragment {
    final public static int INTENT_CODE = 1;
    final public static String INTENT_MASSEGE = "NEW_NAME";
    RecyclerView recyclerView;
    static DatabaseHelper db;

    MyAdapter adaptor;
    String newName;
    Context mContext = getContext();
    Activity mActivity = getActivity();
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        Toolbar toolbar =  view.findViewById(R.id.m_toolbar);
//        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar); //todo: not working. why?

//        db = new DatabaseHelper(mContext);
        //todo: i feel this is gonna face u a nice bug. LOL!
        new MainActivity();
        db = MainActivity.db;


        //back imageView btn
        ImageView backBtn = view.findViewById(R.id.back_btn);
//        backBtn.setOnClickListener(item -> onBackPressed());

        //Floating Btn
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view0 -> {
//            startActivityForResult(new Intent(mContext, AddContactActivity.class), INTENT_CODE);
            startActivity(new Intent(getActivity(), AddContactActivity.class));
//            mActivity.finish();
        });

        //recyclerView
        recyclerView = view.findViewById(R.id.contacts_recView);
        doRecyclerView();

        return view;
    }

    /********************************************       Methods     ****************************************************/

    private void doRecyclerView() {


        /*
         * todo: set Contact instead of Partice... :
         * I'm forming Contacts as Participants, so I won't need to create another
         * adaptor or even edit that. change this shit later in order not to get fucked up!
         */
        List<Contact> mContacts0 = db.getAllContacts();
        List<Participant> participants = Routines.contactToPartic(mContacts0);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adaptor = new MyAdapter(mContext, R.layout.sample_conntacts_horizental, participants);
        recyclerView.setAdapter(adaptor);
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        //recyclerView
//        recyclerView = findViewById(R.id.contacts_recView);
//        doRecyclerView();
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(mContext, MainActivity.class));
//        mActivity.finish();
//    }

}