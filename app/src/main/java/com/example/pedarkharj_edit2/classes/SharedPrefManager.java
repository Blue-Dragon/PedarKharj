package com.example.pedarkharj_edit2.classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

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

    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_ID = "keyid";
    private static final String KEY_NAME = "keyname";;
    private static final String KEY_BITMAP_STRING = "bitmapstring";
    private static SharedPrefManager mInstance;
    private static Context mContext;
    private static Class aClass;
    private String username, email, gender;

    private SharedPrefManager(Context context) {
        mContext = context;
    }
    //creating an instance of our Constructor (not able to call constructor directly from out of this class)
    //-> this method calls the constructor if it is not called before.
    public static synchronized SharedPrefManager getInstance(Context context) { //its return type is SharedPrefManager, like String, int, void, ... Classes.
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public static synchronized SharedPrefManager getInstance(Context context, Class mClass) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        if (mClass != null){
            aClass = mClass;
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    public void userLogin(Participant participant) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (participant.getId() > -1) {
            editor.putInt(KEY_ID, participant.getId());
            editor.putString(KEY_NAME, participant.getName());
            //TODO: save pic bitmap here
            editor.putString(KEY_BITMAP_STRING, Routines.encodeToBase64(mContext, participant));
            editor.apply();
        }
    }

    //this method will check whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, null) != null;
    }

    //this method will give info of the logged in user
    public Participant getParticipant() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Participant(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAME, null),
                Routines.decodeBase64(sharedPreferences.getString(KEY_BITMAP_STRING, null)) //TODO
        );
    }


    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
//        Intent intent = new Intent(mContext, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
    }

}
