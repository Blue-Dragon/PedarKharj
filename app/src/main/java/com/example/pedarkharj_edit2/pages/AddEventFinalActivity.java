package com.example.pedarkharj_edit2.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Event;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.Routines;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AddEventFinalActivity extends AppCompatActivity {
    Context mContext;
    Activity mActivity;
    ArrayList<Participant> mParticipants;
    ParticipantAdapter adapter;
    DatabaseHelper db;

    Bitmap bitmap;
    Bitmap resizedBitmap;
    boolean newImg;
    CircleImageView eventPic;

    boolean suddenly_stop;
    int eventId;
    Event event;
    EditText ed;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        mContext = this;
        mActivity = this;
        db = new DatabaseHelper(mContext);
        newImg = false;

        //back imageView btn
        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(item -> onBackPressed());

        suddenly_stop = true;
        ed = findViewById(R.id.name_edt);
        eventId = getIntent().getIntExtra(Routines.NEW_EVENT_PARTIC_EVENT_ID_INTENT, 0);
        event = db.getEventById(eventId);
//        //back imageView btn
//        ImageView backBtn = findViewById(R.id.back_btn);
//        backBtn.setOnClickListener(item -> finish());
        eventPic = findViewById(R.id.prof_pic);
        eventPic.setOnClickListener(item ->{
            suddenly_stop = false;
            if (Build.VERSION.SDK_INT >= 23)
                Routines.requestPermissions(mActivity, new String[]{CAMERA, READ_EXTERNAL_STORAGE}, Routines.PER_CODE_CAMERA_READexSTG);
            else
                Routines.chooseCameraGallery(mActivity);
        });
        suddenly_stop = true;



        //-------------------------     Floating Btn    --------------------------//
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            updateEvent();
            suddenly_stop = false;
            startActivity(new Intent(mContext, EventMngActivity.class));
            finish();
        });

        //-------------------------     RecView    --------------------------//
        recyclerView = findViewById(R.id.rv);
        doRecyclerView();

        //
        db.closeDB();
    }

    // ********************************  Methods  ******************************** //

    private void updateEvent() {
        final String eventName = ed.getText().toString().trim();
        if (TextUtils.isEmpty(eventName)){
            ed.setText("لطفا نام رویداد را وارد کنید");
            ed.requestFocus();
            return;
        }

        event.setEventName(eventName);
        if (resizedBitmap != null) event.setBitmapStr( Routines.encodeToBase64(resizedBitmap) );
        db.updateEvent(event);

    }


    private void doRecyclerView() {
        int[] ids = getIntent().getIntArrayExtra(Routines.NEW_EVENT_PARTIC_IDS_INTENT);

        mParticipants = new ArrayList<Participant>();
        for (int i : ids){
            mParticipants.add(db.getParticeById(i));
        }


        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        //
        adapter = new ParticipantAdapter(mContext, R.layout.sample_contact, mParticipants);
        recyclerView.setAdapter(adapter);

    }


    /*       permission stuff        */
    // Routines.requestPermissions...
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case Routines.PER_CODE_CAMERA_READexSTG:
                boolean permissioncamera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean permissiongallery = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (grantResults.length > 0 && permissioncamera && permissiongallery) {
                    Toast.makeText(mActivity, "مجوز دسترسی داده شد", Toast.LENGTH_SHORT).show();
                    Routines.chooseCameraGallery(mActivity);
                } else {
                    Toast.makeText(mActivity, "مجوز دسترسی داده نشد", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    //get pic options (camera or gallery)
    //Routines.chooseCameraGallery...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //gallery image
        if (requestCode == Routines.GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                if (bitmap != null){
                    //resizing image
                    resizedBitmap = Routines.resizeBitmap(bitmap);
                    eventPic.setImageBitmap(resizedBitmap);
                    newImg = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //camera image
            if (data!=null && data.getExtras()!=null) {
                bitmap = (Bitmap) data.getExtras().get("data") ;
                if (bitmap != null) {
                    resizedBitmap = Routines.resizeBitmap(bitmap);
                    eventPic.setImageBitmap(resizedBitmap);
                    newImg = true;
                }
            }
        }
    }

    /**/


    @Override
    protected void onStop() {
        super.onStop();
        if (suddenly_stop) {
            Routines.deleteTempEvent(mContext, eventId);
            Log.d("Fuck07", "onStop");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (suddenly_stop) {
            Routines.deleteTempEvent(mContext, eventId);
            Log.d("Fuck07", "onStop");
        }
    }
}
