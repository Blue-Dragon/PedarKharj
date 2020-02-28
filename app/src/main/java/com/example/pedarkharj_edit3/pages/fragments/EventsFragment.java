package com.example.pedarkharj_edit3.pages.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.pages.AddContactActivity;


public class EventsFragment extends Fragment {
    final public static int INTENT_CODE = 1;
    final public static String INTENT_MASSEGE = "NEW_NAME";
    RecyclerView recyclerView;
    DatabaseHelper db;

    MyAdapter adaptor;
    String newName;
    Context mContext = getContext();
    Activity mActivity = getActivity();
    FloatingActionButton fab;
    MainActivity mainActivity = new MainActivity();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_mng, container, false);
        mContext = getContext();
        mActivity = getActivity();

        db = new DatabaseHelper(mContext);
        MainActivity.navPosition = Routines.CONTACTS;

        Toolbar toolbar =  view.findViewById(R.id.m_toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);


        return view;
    }



}