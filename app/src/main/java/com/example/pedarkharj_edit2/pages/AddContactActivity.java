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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pedarkharj_edit2.MainActivity;
import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.Contact;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Routines;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AddContactActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private Activity mActivity;
    String name, family;
    EditText nameEdt, familyEdt;
    RelativeLayout fromContactsBtn;
    ImageView cancelImg, doneImg;
    DatabaseHelper db;

    Bitmap bitmap;
    Bitmap resizedBitmap;
    CircleImageView profPic;
    boolean newImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        mActivity =this;
        mContext = this;
        newImg = false;
        db = new DatabaseHelper(mContext);

        fromContactsBtn = findViewById(R.id.addFromContacts_btn);    fromContactsBtn.setOnClickListener(this);
        profPic = findViewById(R.id.prof_pic);                                      profPic.setOnClickListener(this);
        cancelImg = findViewById(R.id.cancel_img);                               cancelImg.setOnClickListener(this);
        doneImg = findViewById(R.id.check_img);                                  doneImg.setOnClickListener(this);
        fromContactsBtn = findViewById(R.id.addFromContacts_btn);    fromContactsBtn.setOnClickListener(this);
        nameEdt = findViewById(R.id.name_edt);
        familyEdt = findViewById(R.id.family_edt);
//
        db.closeDB();
    }


    /********************************************       Methods     ****************************************************/

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.prof_pic:
                if (Build.VERSION.SDK_INT >= 23)
                    Routines.requestPermissions(mActivity, new String[]{CAMERA, READ_EXTERNAL_STORAGE}, Routines.PER_CODE_CAMERA_READexSTG);
                else
                    Routines.chooseCameraGallery(mActivity);
                break;

            case R.id.addFromContacts_btn:
                if (Build.VERSION.SDK_INT >= 23)
                    Routines.requestPermissions(mActivity, new String[]{READ_CONTACTS}, Routines.PER_CODE_READ_CONTACTS);
                else
                    Routines.chooseCameraGallery(mActivity);
                break;

            case R.id.check_img:
                addNewContact();
                break;

            case R.id.cancel_img:
                finish();
                break;
        }
    }


    //
    private void addNewContact() {
            final String fullName = nameEdt.getText().toString().trim() + familyEdt.getText().toString().trim();

            //first we will do the validations (name)
            if (TextUtils.isEmpty(fullName)) {
                nameEdt.setError("لطفا نام را وارد کنید");
                nameEdt.requestFocus();
                return;
            }

//        String imgString = Routines.encodeToBase64(mContext, participant);

        //creating a new Contact object
        Contact contact = new Contact();
        contact.setName(fullName);
        if (newImg) contact.setBitmapStr(Routines.encodeToBase64(resizedBitmap));  //see if we have any images

        //add to db
        db.createContact(contact);
        startActivity(new Intent(mContext, ContactsActivity.class));
        finish();
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

            case Routines.PER_CODE_READ_CONTACTS:
                boolean permissionContact = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (grantResults.length > 0 && permissionContact) {
                    Toast.makeText(mActivity, "مجوز دسترسی contacts داده شد", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "مجوز دسترسی contacts داده نشد", Toast.LENGTH_SHORT).show();
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
                    profPic.setImageBitmap(resizedBitmap);
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
                    profPic.setImageBitmap(resizedBitmap);
                    newImg = true;
                }
            }
        }
    }

    /**/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mContext, ContactsActivity.class));
        finish();
    }
}
