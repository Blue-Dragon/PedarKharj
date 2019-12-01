package com.example.pedarkharj_edit2.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;

import com.example.pedarkharj_edit2.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class Routines {
    public static final int PER_CODE_CAMERA_READexSTG = 1;
    public static final int PER_CODE_READ_CONTACTS = 2;
    public static final int GALLERY_INTENT = 3;
    public static final int CAMERA_INTENT = 4;
    public static final String PARTICIPANT_INFO = "PARTICIPANT_INFO";
    public static int usersId = 0;
    private static int contactsSQLID ;

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

        while (bitmap.getHeight() * bitmap.getWidth() > 25000){
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*0.8), (int) (bitmap.getHeight()*0.8), true);
        }
        return bitmap;
    }

    public static Bitmap resizeBitmap(Context context, int bitmapId) {
        return resizeBitmap( BitmapFactory.decodeResource(context.getResources(), bitmapId) );
    }

    public static Bitmap drawableToBitmap(Context mContext, int drawable){
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawable);
        return bitmap;
    }

    //Bitmap to string
    public static String encodeToBase64(Bitmap bitmap) {
        String profPicString = null;

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] picBytes = byteArrayOutputStream.toByteArray();
            profPicString = Base64.encodeToString(picBytes, Base64.DEFAULT);
        }
        return profPicString;
    }

    // String to Bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

//    public static int newContactId(Context mContext){
//        return  MydbHelper.getInstance(mContext).getRowsCount();
//    }

    public static List<Participant> contactToPartic(List<Contact> mContacts0){

        ArrayList<Contact> mContacts = new ArrayList<Contact>();
        List<Participant> participants = new ArrayList<>(mContacts0.size());

        mContacts.addAll(mContacts0);
        for (Contact c : mContacts0){
            Participant participant = new Participant();
            participant.setContact(c);
            if (c.getId() > 0) participant.setId((int) c.getId());
            participant.setName(c.getName());
            participants.add(participant);
//
//            Log.d("Contact", c.getId()+ " : "+ c.getName());
//            Log.e("Contact", participant.getId()+ " : "+ participant.getName());
        }
        return participants;
    }

}
