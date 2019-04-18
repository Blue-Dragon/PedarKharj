package com.example.pedarkharj.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private static final String KEY_ID = "keyid";
    private static SharedPrefManager mInstance;
    private static Context ctx;

    private String username, email, gender;
    private SharedPrefManager(Context context) {
        ctx = context;
    }

    //creating an instance of our Constructor (not able to call constructor directly from out of this class)
    //-> this method calls the constructor if it is not called before
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
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
            editor.apply();
        }
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
                sharedPreferences.getString(KEY_GENDER, null)
        );
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
        if (user.getId() > -1 && username!=null && email!=null && gender!=null)   {
            //update User info
           user.setName(username);
           user.setEmail(email);
           user.setGender(gender);
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
            User user = users[0];
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_USER_INFO,
                    response -> {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                publishProgress(obj.getString("message"));
//                                Toast.makeText(ctx, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //getting user params reom server
                                username =  userJson.getString("username");
                                email = userJson.getString("email");
                                gender = userJson.getString("gender");

                                //TODO: do your after-result task here
                                ctx.startActivity(new Intent(ctx, ProfileActivity.class));
                                
                            } else {
                                publishProgress(obj.getString("message"));
//                                Toast.makeText(ctx, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    },
                    error -> {
                        publishProgress(error.getMessage());
//                        Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show()
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
//            Toast.makeText(ctx, "onPostExecute", Toast.LENGTH_SHORT).show();
        }
    }
}