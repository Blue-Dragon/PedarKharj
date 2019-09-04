package com.example.pedarkharj_edit2.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;


public class Routines {
    public static final int PER_CODE_CAMERA_READexSTG = 1;
    public static final int PER_CODE_READ_CONTACTS = 2;
    public static final int GALLERY_INTENT = 3;
    public static final int CAMERA_INTENT = 4;

//    Context mContext;
    Activity mActivity;


    public Routines(Activity mActivity){
        this.mActivity = mActivity;
    }

    /********************************************       Methods     ****************************************************/

    public static void requestPermissions(Activity mActivity, String[] strings, int permissionCode) {
        ActivityCompat.requestPermissions(mActivity, strings, permissionCode);
    }


    //get pic options
    public static void chooseCameraGallery(Activity mActivity) {
        AlertDialog.Builder al = new AlertDialog.Builder(mActivity);
        al.setTitle("Choosing photo")
                .setMessage("How do you attend to set your photo?")
                .setPositiveButton("Gallary", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    mActivity.startActivityForResult(intent, GALLERY_INTENT);
                })
                .setNegativeButton("Camera", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    mActivity.startActivityForResult(intent, CAMERA_INTENT);
                })
                .show();
    }


    public static Bitmap resizeBitmap(Bitmap bitmap) {

        while (bitmap.getHeight() * bitmap.getWidth() > 90000){
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*0.8), (int) (bitmap.getHeight()*0.8), true);
        }
        return bitmap;
    }


    public static Bitmap drawableToBitmap(Context mContext, int drawable){
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawable);
        return bitmap;
    }
}
