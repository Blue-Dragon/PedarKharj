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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.models.Contact;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.Routines;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AddContactActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private Activity mActivity;
    EditText nameEdt, familyEdt;
    ImageView cancelImg, doneImg, backBtn;
    DatabaseHelper db;

    RelativeLayout fromContactsBtn;
    Bitmap bitmap;
    Bitmap resizedBitmap;
    CircleImageView profPic;
    boolean newImg;
    boolean isEditMode;
    private Uri uri;
    int editContactId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);


        inits();
        onClicks();
//
        db.closeDB();
    }






    /********************************************       Methods     ****************************************************/
    private void inits() {
        mActivity =this;
        mContext = this;
        newImg = false;
        isEditMode = false;
        db = new DatabaseHelper(mContext);

        profPic = findViewById(R.id.prof_pic);
        cancelImg = findViewById(R.id.cancel_img);
        doneImg = findViewById(R.id.check_img);
        nameEdt = findViewById(R.id.name_edt);
        familyEdt = findViewById(R.id.family_edt);
        fromContactsBtn = findViewById(R.id.addFromContacts_btn);
        //Edit Mode
        isEditMode = getIntent().getBooleanExtra(Routines.EDIT_MODE, false);
        editContactId = getIntent().getIntExtra(Routines.SEND_CONTACT_ID_INTENT, 0);
        if (isEditMode && editContactId > 0){
            Contact contact = db.getContactById( editContactId );
            Bitmap bitmap;

            if (contact.getBitmapStr()!=null && contact.getBitmapStr().length() > 0){
                bitmap = Routines.stringToBitmap(contact.getBitmapStr());
                profPic.setImageBitmap(bitmap);
            }
            nameEdt.setText(contact.getName());
        }
    }

    private void onClicks() {
        doneImg.setOnClickListener(this);
        cancelImg.setOnClickListener(this);
        profPic.setOnClickListener(this);
        fromContactsBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.prof_pic:
                if (Build.VERSION.SDK_INT >= 23)
                    Routines.requestPermissions(mActivity, new String[]{CAMERA, READ_EXTERNAL_STORAGE}, Routines.PER_CODE_CAMERA_READexSTG);
                else
                    CropImage.startPickImageActivity(mActivity);
                break;

            case R.id.addFromContacts_btn:
                if (Build.VERSION.SDK_INT >= 23)
                    Routines.requestPermissions(mActivity, new String[]{READ_CONTACTS}, Routines.PER_CODE_READ_CONTACTS);
                else
//                    Toast.makeText(mContext, "heY UI", Toast.LENGTH_SHORT).show();
                break;

            case R.id.check_img:
                addOrEditContact();
                break;

            case R.id.cancel_img:
                finish();
                startActivity(new Intent(mContext, MainActivity.class));
                break;
        }
    }


    //
    private void addOrEditContact() {
        Contact contact;
        final String fullName = nameEdt.getText().toString().trim() + " " + familyEdt.getText().toString().trim();

            //first we will do the validations (name)
            if (TextUtils.isEmpty(fullName)) {
                nameEdt.setError("لطفا نام را وارد کنید");
                nameEdt.requestFocus();
                return;
            }
        //creating a new Contact object / Editing
        if (isEditMode && editContactId>0)
            contact = db.getContactById( editContactId );
        else
            contact = new Contact();


        contact.setName(fullName);
        if (newImg) contact.setBitmapStr(Routines.bitmapToString(resizedBitmap));  //see if we have any images


        if (isEditMode && editContactId>0)
            db.updateContact(contact);
        else
            db.createContact(contact);//add to db
        finish();
        startActivity(new Intent(mContext, MainActivity.class));
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
            } else Routines.startCrop(mActivity, imageUri, 1, 1);
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
//                            profPic.setImageBitmap(bitmap);
                            //resizing image
//                        resizedBitmap = Routines.resizeBitmap(bitmap);
                            resizedBitmap = Routines.convertBitmapThumbnail1x1(bitmap);
                            profPic.setImageBitmap(resizedBitmap);
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
                    Toast.makeText(mActivity, "مجوز دسترسی داده نشد", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }
}
