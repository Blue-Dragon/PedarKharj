package com.example.pedarkharj_edit3.pages;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.Routines;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AddEventFinalActivity extends AppCompatActivity {
    Context mContext;
    Activity mActivity;
    ArrayList<Participant> mParticipants;
    Event event;
    MyAdapter adapter;
    DatabaseHelper db;
    boolean suddenly_stop;
    int eventId;
    private Uri uri;

    Bitmap bitmap;
    Bitmap resizedBitmap;
    boolean newImg, edit_mode;
    CircleImageView changePic_bkg;
    ImageView eventPic;
    EditText ed;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_final);
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        inits();
        onClicks();


        if (edit_mode)
            setEventInfos();

        //    RecView
        doRecyclerView();

        //  Floating Btn
        fab.setOnClickListener(view -> {
            updateEvent();
            suddenly_stop = false;
        });


        db.closeDB();
    }





    // ********************************  Methods  ******************************** //
    private void inits() {
        mContext = this;
        mActivity = this;
        newImg = false;
        edit_mode = false;
        suddenly_stop = true;
        db = new DatabaseHelper(mContext);


        backBtn = findViewById(R.id.back_btn);
        changePic_bkg = findViewById(R.id.change_pic_bkg);
        eventPic = findViewById(R.id.prof_pic);
        ed = findViewById(R.id.name_edt);
        fab = this.findViewById(R.id.fab);
        recyclerView = findViewById(R.id.rv);

        eventId = getIntent().getIntExtra(Routines.NEW_EVENT_PARTIC_EVENT_ID_INTENT, 0);
        Log.d("fuck026", "eventId_received: "+ eventId);
        try {
            event = db.getEventById(eventId);
        }catch (Exception e){
            Log.e("fuck026", " "+ e);
        }

        edit_mode = getIntent().getBooleanExtra(Routines.EDIT_MODE, false);

    }

    private void onClicks() {
        backBtn.setOnClickListener(item -> onBackPressed());
        eventPic.setOnClickListener(item -> changePic());
        changePic_bkg.setOnClickListener(item -> changePic());
    }

    private void setEventInfos() {
        ed.setText(event.getEventName());

        try {
            if ( event.getBitmapStr() != null ){
                Log.i("fuck026", "BitmapStr not null: \n"+ event.getBitmapStr() );

                bitmap = Routines.stringToBitmap(event.getBitmapStr());
//                resizedBitmap = Routines.resizeBitmap(bitmap);
                resizedBitmap = Routines.convertBitmapThumbnail1x1(bitmap);
                eventPic.setImageBitmap(resizedBitmap);
            }
        }catch (Exception e){
            Log.e("fuck026",  e.toString());

        }

    }

    private void changePic() {
        suddenly_stop = false;
        if (Build.VERSION.SDK_INT >= 23) {
            Routines.requestPermissions(mActivity, new String[]{CAMERA, READ_EXTERNAL_STORAGE}, Routines.PER_CODE_CAMERA_READexSTG);
        }
        else {
            Routines.chooseCameraGallery(mActivity);
        }
    }

    private void updateEvent() {
        final String eventName = ed.getText().toString().trim();
        if (TextUtils.isEmpty(eventName)){
            ed.setError("لطفا نام رویداد را وارد کنید");
            ed.requestFocus();
            return;
        }

        event.setEventName(eventName);
        if (resizedBitmap != null) event.setBitmapStr( Routines.bitmapToString(resizedBitmap) );
        db.updateEvent(event);

        MainActivity.navPosition = Routines.EVENTS;
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    private void doRecyclerView() {
        int[] ids = getIntent().getIntArrayExtra(Routines.NEW_EVENT_PARTIC_IDS_INTENT);

        mParticipants = new ArrayList<Participant>();
        for (int i : ids){
            mParticipants.add(db.getParticeById(i));
        }

        int itemsInScreen = 6;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, itemsInScreen, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //
        adapter = new MyAdapter(mContext, R.layout.sample_contact, mParticipants);
        adapter.setItemsInScreen(itemsInScreen);
        recyclerView.setAdapter(adapter);

    }




    //-------------------------      ACTIVITY RESULT       --------------------------//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri = CropImage.getPickImageResultUri(mContext, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE){
            if (CropImage.isReadExternalStoragePermissionsRequired(mContext, imageUri)){
                uri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},  0);
            } else Routines.startCrop(mActivity, imageUri, 3,4);
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                if (result != null) {
//                    profPic.setImageURI(result.getUri());
                    /* *********   setting bitmap   ***********/
                    try {
                        Uri imgUri = result.getUri();
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);

                        if (bitmap != null){
//                            eventPic.setImageBitmap(bitmap);
                            //resizing image
//                            resizedBitmap = Routines.resizeBitmap(bitmap);
//                            resizedBitmap = bitmap;
                            resizedBitmap = Routines.convertBitmapThumbnail3x4(bitmap);
                            eventPic.setImageBitmap(resizedBitmap);
//                        profPic.setImageBitmap(resizedBitmap);
                            newImg = true;
                        }

                    } catch (Exception e) {
                        Log.e("bitmapErr", e.toString());
                    }

                    /* **********************/
//                    Toast.makeText(mContext, "yeeeeees", Toast.LENGTH_SHORT).show();
                }else   Toast.makeText(mContext, "Null result", Toast.LENGTH_SHORT).show();

            }
        }

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
//                    Toast.makeText(mActivity, "مجوز دسترسی داده شد", Toast.LENGTH_SHORT).show();
//                    Routines.chooseCameraGallery(mActivity);
                    CropImage.startPickImageActivity(mActivity);

                } else {
                    Toast.makeText(mActivity, "برای وارد کردن عکس از دوربین یا گالری به این دسترسی نیاز داریم.", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    /**/


//    @Override
//    protected void onStop() {
//        super.onStop();
//        suddenlyStopedTask();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        suddenlyStopedTask();
//    }

//    private void suddenlyStopedTask() {
//        if (suddenly_stop) {
//            if (edit_mode) updateEvent();
//            else Routines.deleteTempEvent(mContext, eventId);
//            Log.d("Fuck07", "onStop");
//        }
//    }
}
