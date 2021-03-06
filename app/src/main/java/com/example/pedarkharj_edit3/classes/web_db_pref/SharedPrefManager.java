package com.example.pedarkharj_edit3.classes.web_db_pref;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    /**
     * We create a class named as SharedPreferences.java. In this class, we use the SharedPreferences class
     * to store the user detail. The SharedPreferences class contains four methods with the following functionalities:
     *
     * userLogin(): This method is used to store the user information in SharedPreferences after log-in.
     * isLoggedIn(): This method checks whether the user is already log-in or not.
     * getUser(): This method get the user information if logged-in.
     * logout(): This method clear the SharedPreferences data and makes user log-out.
     */

    private static final String SHARED_PREF_NAME = "defEvent";
    private static final String KEY_ID = "keyId";
    private static final String KEY_FIRST_TIME = "keyFirstTome";

    private static SharedPrefManager mInstance;
    private static Context mContext;
    private static Class aClass;
    private String username, email, gender;
    private SharedPreferences sharedPreferences;

    private SharedPrefManager(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }
    //creating an instance of our Constructor (not able to call constructor directly from out of this class)
    //-> this method calls the constructor if it is not called before.
    public static synchronized SharedPrefManager getInstance(Context context) { //its return type is SharedPrefManager, like String, int, void, ... Classes.
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    //------------------------   def event  --------------------------//
    /**
     * this method will store the default event id in shared preferences
     */
    public void saveLastSeenEventId(int eventId) {
        sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (eventId > -1) {
            editor.putInt(KEY_ID, eventId);
            editor.apply();
        }
    }

    /**
     *     this method will check whether user is already logged in or not
     */
    public int getDefEventId() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, 0) ;
    }

    /**
     *     this method will clear the defEvent
     */
    public void clearDerfEvent() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_ID);
        editor.apply();
    }

    //------------------------   first time run   --------------------------//




    /**
     * this method will store FirstTimeRun condition
     */
    public void isFirstTimeRun(boolean b) {
        sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_FIRST_TIME, b);
        editor.apply();
    }

    /**
     *     this method will check whether FirstTimeRun or not
     */
    public boolean getFirstTimeRun() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_FIRST_TIME, true) ;
    }

    //----------------------
    public void setNextRunTurn(String key_page, int timesRan) {
        sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key_page, timesRan);
            editor.apply();
    }


    public int getRunTurn(String key_page) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key_page, Routines.FIRST_RUN) ;
    }



    //----------------------


    /**
     *     this method will clear sharedPref
     */
    public void clearShrdPref() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    //--------------------------     previous app codes      ----------------------------//

    //this method will store the user data in shared preferences
//    public void userLogin(Participant participant) {
//        sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        if (participant.getId() > -1) {
//            editor.putInt(KEY_ID, participant.getId());
//            editor.putString(KEY_NAME, participant.getName());
//            //TODOthing: bellow code should not be deleted, but debugged!
////            String bitmapString = Routines.bitmapToString(mContext, participant);
////            if (!bitmapString.isEmpty())    editor.putString(KEY_BITMAP_STRING, bitmapString);
//            editor.apply();
//        }
//    }
//
//    //this method will check whether user is already logged in or not
//    public boolean isLoggedIn() {
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(KEY_NAME, null) != null;
//    }

    //this method will give info of the logged in user
//    public Participant getParticipant(){
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return new Participant(
//                sharedPreferences.getInt(KEY_ID, -1),
//                sharedPreferences.getString(KEY_NAME, null),
//                Routines.stringToBitmap(sharedPreferences.getString(KEY_BITMAP_STRING, null))
//        );
//    }


    //this method will logout the user
//    public void logout() {
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
////        Intent intent = new Intent(mContext, LoginActivity.class);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        mContext.startActivity(intent);
//    }

}
