package com.example.pedarkharj_edit2.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pedarkharj_edit2.R;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AddContactActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_CODE = 1;
    private static final int PERMISSION_CODE_FROM_CONTACTS = 2;
    private static final int GALLERY_INTENT = 10;
    private static final int CAMERA_INTENT = 11;
    Bitmap bitmap;
    Bitmap resizedBitmap;
    private Context mContext;
    private Activity mActivity;

    CircleImageView profPic;
    ImageView cancelImg, doneImg;
    RelativeLayout fromContactsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        mActivity =this;
        mContext = this;

        fromContactsBtn = findViewById(R.id.addFromContacts_btn); fromContactsBtn.setOnClickListener(this);
        profPic = findViewById(R.id.prof_pic);
        cancelImg = findViewById(R.id.cancel_img);
        doneImg = findViewById(R.id.check_img);
        fromContactsBtn = findViewById(R.id.addFromContacts_btn);

        //def pic
        if (bitmap == null )bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.w);
        resizedBitmap = resizeBitmap(bitmap);
        profPic.setImageBitmap(resizedBitmap);

        //change profile pic
        fromContactsBtn.setOnClickListener(item -> {
            Toast.makeText(mContext, checkpermission() + "", Toast.LENGTH_SHORT).show();
//            if (Build.VERSION.SDK_INT >= 23 && !checkpermission())
                requestpermissions();
//            else
//                chooseCameraGallery();
        });

    }



    /********************************************       Methods     ****************************************************/

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.prof_pic:
                //todo fuck u
                break;
        }
    }



    /*       permission stuff        */
//    private Boolean checkPermission(Activity mActivity, String... strings) {
//        boolean b;
//
//        //todo: wtf?
//        for (String string : strings) {
//            int permission = ContextCompat.checkSelfPermission(mActivity, string);
//            b = (permission == PackageManager.PERMISSION_GRANTED);
//            if (!b) return b;
//            break;
//        }
//
//        b = true;
//        return b;
//    }

    //change profile pic methods
    private Boolean checkpermission() {
        int chkcamera = ContextCompat.checkSelfPermission(mActivity, CAMERA);
        int chkgallery = ContextCompat.checkSelfPermission(mActivity, READ_EXTERNAL_STORAGE);
        return chkcamera == PackageManager.PERMISSION_GRANTED && chkgallery == PackageManager.PERMISSION_GRANTED;
    }

    private void requestpermissions() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA, READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
        Toast.makeText(mContext, "WTF?", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSION_CODE:
//                boolean permissioncamera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                boolean permissiongallery = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                if (grantResults.length > 0 && permissioncamera && permissiongallery) {
//                    Toast.makeText(mActivity, "مجوز دسترسی داده شد", Toast.LENGTH_SHORT).show();
//                    chooseCameraGallery();
//                } else {
//                    Toast.makeText(mActivity, "مجوز دسترسی داده نشد", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }

    //get pic options
    private void chooseCameraGallery() {
        AlertDialog.Builder al = new AlertDialog.Builder(mActivity);
        al.setTitle("Choosing photo")
                .setMessage("How do you attend to set your photo?")
                .setPositiveButton("Gallary", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_INTENT);
                })
                .setNegativeButton("Camera", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_INTENT);
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //gallery image
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                if (bitmap != null){
                    //resizing image
                    resizedBitmap = resizeBitmap(bitmap);
                    profPic.setImageBitmap(resizedBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //camera image
            if (data!=null && data.getExtras()!=null) {
                bitmap = (Bitmap) data.getExtras().get("data") ;
                if (bitmap != null) {
                    resizedBitmap = resizeBitmap(bitmap);
                    profPic.setImageBitmap(resizedBitmap);
                }
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap) {

        while (bitmap.getHeight() * bitmap.getWidth() > 90000){
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*0.8), (int) (bitmap.getHeight()*0.8), true);
        }
        return bitmap;
    }
    /**/



}
