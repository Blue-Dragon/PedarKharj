package com.example.pedarkharj_edit3.classes;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.classes.models.Contact;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Routines  {
    //Intent extras
    public static final String NEW_EVENT_PARTIC_IDS_INTENT = "NEW_EVENT_PARTIC_IDS_INTENT";
    public static final String NEW_EVENT_PARTIC_EVENT_ID_INTENT = "NEW_EVENT_PARTIC_EVENT_ID_INTENT";
    public static final String SEND_EVENT_ID_INTENT = "SEND_EVENT_ID_INTENT";
    public static final String SEND_CONTACT_ID_INTENT = "SEND_CONTACT_ID_INTENT";
    public static final String SEND_PARTICIPANT_ID_INTENT = "SEND_PARTICIPANT_ID_INTENT";
    public static final String SEND_EXPENSE_FLOAT_INTENT = "SEND_EXPENSE_FLOAT_INTENT";
    public static final String SEND_EXPENSE_ID_INTENT = "SEND_EXPENSE_ID_INTENT";
    public static final String SEND_USERS_INTENT = "SEND_USERS_INTENT";
    public static final String EDIT_MODE = "EDIT_MODE";
    public static final boolean EDIT_MODE_TRUE = true;
    //forResult
    public static final String RESULT = "RESULT";
    public static final int RESULT_OK = 1;
    public static final int RESULT_CANCEL = 0;
    //Permissions
    public static final int PER_CODE_CAMERA_READexSTG = 1;
    public static final int PER_CODE_READ_CONTACTS = 2;
    public static final int GALLERY_INTENT = 3;
    public static final int CAMERA_INTENT = 4;
    //adaptor
    public final static short UNSELECT_ALL = 0;
    public final static short SELECT_ALL = 1;
    public final static short NOT_SELECT_ALL = 2;
    //Navigation buttons
    public final static short HOME = 0;
    public final static short EVENTS = 1;
    public final static short CONTACTS = 2;
    //other stuff
    public static final String PARTICIPANT_INFO = "PARTICIPANT_INFO";
    public static final String EVENT_TEMP_NAME = "EVENT_TEMP";
    //Adaptor
    final static boolean AMOUNT_MODE = true;
    final static boolean DONG_MODE = false;
//    final static boolean EXPENSE_2 = true;
//    final static boolean EXPENSE_1 = false;

    //Not Final
    public static boolean is_in_action_mode;
    public static int counter, selectedItemId; //edit n' delete


    //    Context mContext;
    Activity mActivity;


    public Routines(Activity mActivity){
        this.mActivity = mActivity;
    }


    /********************************************       Methods     ****************************************************/

    // ----------------------    Photo Stuff   ------------------------- //
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

    //-------   Deprecated ! use `convertBitmapThumbnail1x1`, instead.
//    /**
//     *   Bitmap smaller
//     */
//    public static Bitmap resizeBitmap(Bitmap bitmap) {
//
//        while (bitmap.getHeight() * bitmap.getWidth() > 25000){
//            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*0.8), (int) (bitmap.getHeight()*0.8), true);
//        }
//        return bitmap;
//    }
//
//    public static Bitmap resizeBitmap(Context context, int bitmapId) {
//        return resizeBitmap( BitmapFactory.decodeResource(context.getResources(), bitmapId) );
//    }
    /**
     *   Bitmap to thumbnail
     */
    public static Bitmap convertBitmapThumbnail1x1(Bitmap bitmap) {
        Bitmap b = null;
        if (bitmap != null)
            b =ThumbnailUtils.extractThumbnail(bitmap, 150, 150);
        return b;
    }

    public static Bitmap convertBitmapThumbnail3x4(Bitmap bitmap) {
        Bitmap b = null;
        if (bitmap != null)
            b =ThumbnailUtils.extractThumbnail(bitmap, 150, 200);
        return b;
    }


    public static Bitmap drawableToBitmap(Context mContext, int drawable){
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawable);
        return bitmap;
    }

    /**
     * Bitmap  to String
     */
    public static String bitmapToString(Bitmap bitmap) {
        String profPicString = null;

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] picBytes = byteArrayOutputStream.toByteArray();
            profPicString = Base64.encodeToString(picBytes, Base64.DEFAULT);
        }
        return profPicString;
    }

//    /**
//     * byteArray to Bitmap
//     */
    public static Bitmap  byteArrayToBitmap(byte[] decodedByte) {
        return  BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    /**
     * String to Bitmap
     */
    public static Bitmap stringToBitmap(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    /**
     * shows Cropping photo screen in given x and y
     */
    public static void startCrop(Activity mActivity, Uri imageUri, int x, int y) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(false)
                .setAspectRatio(x, y)
                .start(mActivity);
    }

    //---------------------------------     Contacts       -----------------------------------//
    /**
     * Transfers all contacts of device to the app
     */
    public static void getContact(Context mContext) {
        int i = 0;
//        StringBuilder builder = new StringBuilder();

        DatabaseHelper db = new DatabaseHelper(mContext);
        ContentResolver cr = mContext.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur != null && cur.getCount() > 0) {


            while (cur.moveToNext() ) {
                Bitmap bitmap = null;
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                builder.append(name + "\n");

                ///---------   get photo   --------
                Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(id));
                Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                Cursor cursor = mContext.getContentResolver().query(photoUri,
                        new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    byte[] data = cursor.getBlob(0);
                    if (data != null) {
                        bitmap = byteArrayToBitmap(data);
//                        builder.append(bitmapToString(bitmap) + "\n\n");
//                    new ByteArrayInputStream(data);
                    }
                    cursor.close();
                }
                ///-----------------

                db.createContact(new Contact(name, bitmapToString(bitmap)));
                i++;
            }


//            Log.d("img_string", builder.toString());
            cur.close();
            db.closeDB();
        }
    }

    public static void deleteContect(Activity mActivity, Contact pressedContact) {
        DatabaseHelper db = new DatabaseHelper(mActivity);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("اخطار!")
                .setMessage("مطئنی میخوای پاک بشه؟")
                .setPositiveButton("اره", (dialog1, which) ->{
                    db.deleteContact(pressedContact);
//                    adaptor.notifyDataSetChanged();
                    Routines.restartPage(mActivity, Routines.CONTACTS);
                    Toast.makeText(mActivity, "مخاطب با موفقیت حذف شد.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("نه", (dialog1, which) ->{})
                .show();
        db.closeDB();
    }
    /**
     * Contacts to Prtices
     */
    public static List<Participant> contactToPartic(List<Contact> mContacts0){

        ArrayList<Contact> mContacts = new ArrayList<Contact>();
        List<Participant> participants = new ArrayList<>(mContacts0.size());

        mContacts.addAll(mContacts0);
        for (Contact c : mContacts0){
            Participant participant = new Participant();
            participant.setContact(c);
            if (c.getId() > 0) participant.setId((int) c.getId()); //todo: WHY the hell?
            participant.setName(c.getName());
            participants.add(participant);
//
//            Log.d("Contact", c.getId()+ " : "+ c.getName());
//            Log.e("Contact", participant.getId()+ " : "+ participant.getName());
        }
        return participants;
    }

    // ----------------------   others   ------------------------- //

    /**
     *  Permissions
     */
    public static void requestPermissions(Activity mActivity, String[] strings, int permissionCode) {
        ActivityCompat.requestPermissions(mActivity, strings, permissionCode);
    }

    // add to db
    public static List<Participant> addParticesToTempEvent (List<Participant> participants, DatabaseHelper db){
        //add partices to db
        //add an Event to these partices
        long id = db.createEvent(new Event(EVENT_TEMP_NAME));
        Event tempEvent = db.getEventById(id);
        Log.d("Fuck06", id +  "");
        db.createAllParticesUnderEvent(participants, tempEvent);

        return participants;
    }

    //
    public static void deleteTempEvent(Context mContext, int eventId) {
        DatabaseHelper db = new DatabaseHelper(mContext);
        Event tempEvent = db.getEventById(eventId);
        db.deleteEvent(tempEvent, true);
        db.closeDB();
    }

    public static void restartPage(Activity mActivity, short page) {
        MainActivity.navPosition = page;
//        mActivity.recreate();
        mActivity.finish();
        mActivity.startActivity(mActivity.getIntent());
        mActivity.overridePendingTransition(0, 0);
    }


    public static void showDialog(Context mContext) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("خطا!");
        dialog.setMessage("تا زمانی که نام این مخاطب در رویدادی ثبت شده باشد، امکان حذفش وجود ندارد.");
        dialog.setNeutralButton("باشه!", (dialog1, which) ->{});
        dialog.show();
    }


    public static float getRoundFloat(float f) {
        return f;
    }

    public static List<Float> getRoundFloatList(List<Float> floatList) {
        return floatList;
    }

    public static String getRoundFloatString(float f) {
        String s;

        DecimalFormat format  = new DecimalFormat("#.##");
        f =  Float.valueOf(format.format(f));
        s =  format.format(f);
        return s;
    }
}
