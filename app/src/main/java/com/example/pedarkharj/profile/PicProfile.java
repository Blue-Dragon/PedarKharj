package com.example.pedarkharj.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.pedarkharj.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class PicProfile extends AppCompatActivity{

    CircleImageView profPicCircle;
    LinearLayout changePrifilePic;
    EditText usernameEdt, emailEdt, passwordEdt;
    RadioGroup radioGroupGender;
    RadioButton maleRB, femaleRB;
    Button saveBtn;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_prifile);
        //init
        profPicCircle = findViewById(R.id.user_profile_photo);
        changePrifilePic = findViewById(R.id.change_pic_layout);
        usernameEdt = findViewById(R.id.usrname_edit);
        passwordEdt = findViewById(R.id.password_edit);
        emailEdt = findViewById(R.id.email_edit);
        saveBtn = findViewById(R.id.btn_save);
        radioGroupGender = findViewById(R.id.radioGender_picProfile);
        maleRB = findViewById(R.id.radioButtonMale_picProfile);
        femaleRB = findViewById(R.id.radioButtonFemale_picProfile);
        Context context;
        pDialog = new ProgressDialog(this);

        //male by default
        maleRB.setChecked(true);

        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            //init
            User user = SharedPrefManager.getInstance(this).getUser();
            usernameEdt.setHint(user.getName());
            emailEdt.setHint(user.getEmail());
            passwordEdt.setText("123321");
            String gender = user.getGender();
            if (gender.equals("Female")){
                femaleRB.setChecked(true);
            }

            //save
            saveBtn.setOnClickListener(item ->{
                pDialog.setMessage("sending data to DB...");
                pDialog.show();

                //
//                updateProfile();

                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                pDialog.cancel();
            });

            //on profile click


        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

    } // onCreate />


    private void updateProfile() {
        //init
        final String username = usernameEdt.getText().toString().trim();
        final String email = emailEdt.getText().toString().trim();
        final String password = passwordEdt.getHint().toString().trim();
        final String gender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();

        //check the validations
        if (TextUtils.isEmpty(username)) {
            usernameEdt.setError("Please enter username");
            usernameEdt.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailEdt.setError("Please enter your email");
            emailEdt.requestFocus();
            return;
        }
        //check email validation
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdt.setError("Enter a valid email");
            emailEdt.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEdt.setError("Enter a password");
            passwordEdt.requestFocus();
            return;
        }

        /**
         *  you should create an updateProfile.php.file first
         */

//        //send to DB
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER, //calling the overriden getParams()  method
//                response -> {
//                    pDialog.dismiss();
//
//                    try {
//                        //converting response to json object
//                        JSONObject obj = new JSONObject(response);
//                        //if response is not an error
//                        if (!obj.getBoolean("error")) {
//                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//
//                            //getting the user from the response
//                            JSONObject userJson = obj.getJSONObject("user");
//
//                            //creating a new user object
//                            User user = new User(
//                                    userJson.getInt("id"),
//                                    userJson.getString("username"),
//                                    userJson.getString("email"),
//                                    userJson.getString("gender")
//                            );
//
//                            //storing the user in shared preferences
//                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//
//                            //starting the profile activity
//                            finish();
//                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//                        } else {
//                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                },
//                //ErrorListener
//                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show())
//
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("username", username);
//                params.put("email", email);
//                params.put("password", password);
//                params.put("gender", gender);
//                return params;
//            }
//        };
//
//        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}