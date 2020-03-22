package com.example.pedarkharj_edit3.pages.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.models.Contact;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.pages.AddContactActivity;

import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class ContactsFragment extends Fragment implements IContacts, View.OnClickListener {
    List<Contact> contactList;
    static DatabaseHelper db;
    MyAdapter adaptor;
    Context mContext;
    Activity mActivity;
    boolean isHeIntoIt;
    boolean firstTime;

    RecyclerView recyclerView;
    FloatingActionButton fab;
    Toolbar toolbar;
    View mView;
    ImageView backBtn;
    Button getBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_contacts, container, false);
        init();
        getBtn.setOnClickListener(this);

        /*
         * get system contacts
         */
//        checkIfHeLikesIt();


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

//    private void checkIfHeLikesIt() {
//        new AlertDialog.Builder(mContext)
//                .setTitle("مخاطبین گوشی رو اضافه کنم؟")
//                .setMessage("اینطوری کارت راحت تر میشه. البته  برای این کار به \"مجوز دسترسی به مخاطبین\" نیاز دارم.")
//                .setPositiveButton("افزودن مخاطبین", (dialogInterface, i1) -> {
//                    isHeIntoIt = true;
//
////                    int permissionContact = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
//
//                    if (Build.VERSION.SDK_INT >= 23 ){
//                        Routines.requestPermissions(mActivity, new String[]{ READ_CONTACTS}, Routines.PER_CODE_READ_CONTACTS);
//                        Toast.makeText(mContext, "0", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        readContacts(mContext);
//                        Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
//                    }
//                })
//
//                .setNegativeButton("نه، بی خیال!", (dialogInterface, i1) -> {
//                    isHeIntoIt = false;
//                })
//                .show();
//    }

    /********************************************       Methods     ****************************************************/
    private void init() {
        mContext = getContext();
        mActivity = getActivity();
        MainActivity.navPosition = Routines.CONTACTS;

        toolbar =  mView.findViewById(R.id.m_toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);

        db = new DatabaseHelper(mContext);

        backBtn = mView.findViewById(R.id.back_btn);
        recyclerView = mView.findViewById(R.id.recycler_view);
        getBtn = mView.findViewById(R.id.get);

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

//    private void restartPage(short page) {
//        MainActivity.navPosition = page;
//        mActivity.finish();
//        startActivity(mActivity.getIntent());
//    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.get:
                if (Build.VERSION.SDK_INT >= 23)
                    Routines.requestPermissions(mActivity, new String[]{READ_CONTACTS}, Routines.PER_CODE_READ_CONTACTS);
                else
                    Toast.makeText(mContext, "heY UI", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void readSystemContacts() {
        int count = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("Hey bitch!\n");

        ContentResolver cr = mContext.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                count++;

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
//                    System.out.println("name : " + name + ", ID : " + id);
                    //
                    builder.append("ID." + id).append(":\n" + name+"\n");

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        System.out.println("phone" + phone);
                        builder.append("phone: " + phone+ "\n");
                    }
                    pCur.close();


                    // get email and type
                    Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        // This would allow you get several email addresses
                        // if the email addresses were stored in an array
                        String email = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        String emailType = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                        System.out.println("Email " + email + " Email Type : " + emailType);
                        builder.append("Email " + email + " Email Type : " + emailType+ "\n");
                    }
                    emailCur.close();

                    // Get note.......
                    String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] noteWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                    Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                    if (noteCur.moveToFirst()) {
                        String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                        System.out.println("Note " + note);
                        builder.append("Note " + note+  "\n");

                    }
                    noteCur.close();

                }
            }
        }

        Log.d("contact_info", count + "") ;
        Log.d("contact_info", builder.toString()) ;
    }
}