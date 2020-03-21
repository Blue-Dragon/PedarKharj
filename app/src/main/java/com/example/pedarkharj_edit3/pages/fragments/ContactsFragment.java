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
    final public static int INTENT_CODE = 1;
    final public static String INTENT_MASSEGE = "NEW_NAME";
    RecyclerView recyclerView;
    static DatabaseHelper db;

    MyAdapter adaptor;
    Context mContext;
    Activity mActivity;
    MainActivity mainActivity = new MainActivity();
    FloatingActionButton fab;
    Toolbar toolbar;
    View mView;
    ImageView backBtn;

    //Action mode
    TextView counter_text_view, title;
    List<Contact> contactList;
    List<Contact> selectionList;

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
                if (Routines.is_in_action_mode){
                    prepareSelection(view, position);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (!Routines.is_in_action_mode){
                    setActionModeOn(toolbar, counter_text_view, title);
                }
                onClick(view, position);

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
        //Action mode
        selectionList = new ArrayList<>();
        counter_text_view = mView.findViewById(R.id.tv_counter);
        title = mView.findViewById(R.id.textView);
        initEditBar(counter_text_view, title);

        backBtn = mView.findViewById(R.id.back_btn);
        recyclerView = mView.findViewById(R.id.recycler_view);
    }


    private void setRecView() {
        contactList = db.getAllContacts();
//        List<Participant> participants = Routines.contactToPartic(mContacts0);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adaptor = new MyAdapter(mContext);
        adaptor.setLayout(R.layout.sample_conntacts_horizental);
        adaptor.setContactList(contactList);
        recyclerView.setAdapter(adaptor);
    }

    //---------------    ActionMode (selection on longClick)    ----------------//
    /*
     * on select/deselect methods
     */
    private void prepareSelection(View view, int position) {
//        Participant participant = participantList.get(position);
        Contact contact = contactList.get(position);

        if (!selectionList.contains(contact)) {
            selectionList.add(contact);
            view.setForeground( new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorSelected) ));
            updateCounter(++Routines.counter, counter_text_view);

        }else {
            selectionList.remove(contact);
            view.setForeground( new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorTransparent) ));
            if (selectionList.isEmpty()) {
                setActionModeOff(toolbar, counter_text_view, title, adaptor);
                selectionList.clear();
            } else {
                updateCounter(--Routines.counter, counter_text_view);
            }
        }

        //edit & delete option be shown only if just ONE item is selected
        if (selectionList.size() > 1){
            setActionMode2On(toolbar, counter_text_view, title);

        } else if (selectionList.size() == 1){
            setActionModeOn(toolbar, counter_text_view, title);
            Routines.selectedItemId = (int) selectionList.get(0).getId();
        }
    }


    int i =0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_delete:

                Toast.makeText(mContext, "Del", Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(mContext)
                        .setTitle("پاک کنم؟")
                        .setMessage("این اطلاعات از دم نیست و نابود میشن هااا !")
                        .setPositiveButton("پاک کن بره داداچ", (dialogInterface, i1) -> {
                            for (Contact contact : selectionList){
                                db.deleteContact(contact.getId());
                                Toast.makeText(mContext, "EventId : "+ contact.getId() + " Deleted", Toast.LENGTH_SHORT).show();
                            }

                            restartPage(Routines.CONTACTS);

                        })
                        .setNegativeButton("نه، بی خیال!", (dialogInterface, i1) -> {})
                        .show();
                break;

            case R.id.item_edit:
                Intent intent = new Intent(mContext, AddEventParticesActivity.class);
                intent.putExtra(Routines.SEND_EVENT_ID_INTENT, Routines.selectedItemId);
                startActivity(intent);
//                finish();
                break;
        }

        return true;
    }

    private void restartPage(short page) {
        MainActivity.navPosition = page;
        mActivity.finish();
        startActivity(mActivity.getIntent());
    }

    @Override
    public void onMyBackPressed() {

//        if (Routines.is_in_action_mode){
        selectionChangeColor(mContext, R.color.colorTransparent, adaptor);
        setActionModeOff(toolbar, counter_text_view, title, adaptor);
        selectionList.clear();
    }


}