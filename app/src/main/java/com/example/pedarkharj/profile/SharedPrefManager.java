package com.example.pedarkharj.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.pedarkharj.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SharedPrefManager {

    /**
     * We create a class named as SharedPreferences.java. In this class, we use the SharedPreferences class
     * to store the user detail. The SharedPreferences class contains four methods with the following functionalities:
     *
     * userLogin(): This method is used to store the user information in SharedPreferences after log-in.
     * isLoggedIn(): This method checks whether the user is already log-in or not.
     * getUser(): This method get the user information if log-in.
     * logout(): This method clear the SharedPreferences data and makes user log-out.
     */

    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_BITMAP_STRING = "bitmapstring";
    private static final String KEY_PIC_UPDATE_NUM = "picupdatenum";
    private static final String KEY_ID = "keyid";
    private static SharedPrefManager mInstance;
    private static Context ctx;
    private static Class aClass;
    private String username, email, gender;
    private int  picUpdateNum;


    private SharedPrefManager(Context context) {
        ctx = context;
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
    public void userLogin(User user) {

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (user.getId() > -1) {
            editor.putInt(KEY_ID, user.getId());
            editor.putString(KEY_USERNAME, user.getName());
            editor.putString(KEY_EMAIL, user.getEmail());
            editor.putString(KEY_GENDER, user.getGender());
            //TODO: save pic bitmap here
            editor.putString(KEY_BITMAP_STRING, encodeToBase64(user));
            editor.putInt(KEY_PIC_UPDATE_NUM, user.getPicUpdateNum());
            editor.apply();
        }
    }

    //profPic Bitmap to string
    private String encodeToBase64(User user) {
        Bitmap bitmap = user.getBitmap();
        if (bitmap == null) bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.image);
        String profPicString;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] picBytes = byteArrayOutputStream.toByteArray();
        profPicString = Base64.encodeToString(picBytes, Base64.DEFAULT);
        return profPicString;
    }


    //this method will check whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give info of the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_GENDER, null),
                decodeBase64(sharedPreferences.getString(KEY_BITMAP_STRING, null)), //TODO
                sharedPreferences.getInt(KEY_PIC_UPDATE_NUM, 0)
        );
    }

    private Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(ctx, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }


    //get user info every time this method is called(ex. when they open the app)
    public void syncUserInfo(User user) {
        if (user.getId() > -1 && username!=null && email!=null && gender!=null && picUpdateNum>-1)   {
            //update User info
           user.setName(username);
           user.setEmail(email);
           user.setGender(gender);
           user.setPicUpdateNum(picUpdateNum);
           //update sharedPreferences
           userLogin(user);
        } else {
            Toast.makeText(ctx, "oops! Null info happened.", Toast.LENGTH_SHORT).show();
        }
    }



    public class mSyncUser extends AsyncTask <User, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(ctx, "onPreExecute", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(User... users) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            User user = users[0];
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_USER_INFO,
                    response -> {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                publishProgress(obj.getString("message"));

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //getting user params reom server
                                username =  userJson.getString("username");
                                email = userJson.getString("email");
                                gender = userJson.getString("gender");
                                picUpdateNum = userJson.getInt("picUpdateNum");
//                                getNsetProfPic();
//                                syncUserInfo(user); //if u use it, pic will not be updated

                                //TODO: do your after-result task here
                                if (aClass != null){
                                    Intent intent = (new Intent(ctx, aClass));
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //sdk <= 23 needs this
                                    ctx.startActivity(intent);
                                }

                            } else {
                                publishProgress(obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    },
                    error -> {
                        publishProgress(error.getMessage());
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(user.getId()));
                    return params;
                }
            };

            VolleySingleton.getInstance(ctx).addToRequestQueue(stringRequest);
//            publishProgress("Syncing...");

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            for (String s:values){
                Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public void getNsetProfPic() {
        final Bitmap[] bmp = new Bitmap[1];
        User user = getUser();
        ImageRequest imageRequest = new ImageRequest(URLs.URL_IMAGE_DIR+ user.getName()+ ".jpg",
                response -> {
                    if (response != null){
                        bmp[0] = response;

                    }else{
                        bmp[0] =BitmapFactory.decodeResource(ctx.getResources(), R.drawable.image);
                        Toast.makeText(ctx, "response is null", Toast.LENGTH_SHORT).show();
                    }
                    user.setBitmap(bmp[0]);
                    userLogin(user); //to store changes in shp
                    Toast.makeText(ctx, "new pic downloaded", Toast.LENGTH_SHORT).show();
                },
                0,0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.ARGB_8888,
                Throwable::printStackTrace
        );
        VolleySingleton.getInstance(ctx).addToRequestQueue(imageRequest);
    }

}