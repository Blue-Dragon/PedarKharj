package com.example.pedarkharj.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pedarkharj.R;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;


public class PicProfile extends AppCompatActivity{

    private static final int PERMISSION_CODE = 1;
    private static final int GALLERY_INTENT = 10;
    private static final int CAMERA_INTENT = 11;

    CircleImageView profPicCircle;
    LinearLayout profilePicLT;
    EditText usernameEdt, emailEdt, passwordEdt;
    RadioGroup radioGroupGender;
    RadioButton maleRB, femaleRB;
    Button saveBtn;
    ProgressDialog pDialog;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_prifile);
        //init
        activity = this;
        profPicCircle = findViewById(R.id.user_profile_photo);
        profilePicLT = findViewById(R.id.change_pic_layout);
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

            //change profile pic
            profilePicLT.setOnClickListener(item -> {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!checkpermission())
                        requestpermissions();
                    else
                        choosecameragallery();
                }

            });




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

        /*
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


    //change profile pic methods
    private Boolean checkpermission() {
        int chkcamera = ContextCompat.checkSelfPermission(activity, CAMERA);
        int chkgallery = ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE);
        return chkcamera == PackageManager.PERMISSION_GRANTED && chkgallery == PackageManager.PERMISSION_GRANTED;
    }

    private void requestpermissions() {
        ActivityCompat.requestPermissions(activity, new String[]{CAMERA, READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                boolean permissioncamera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean permissiongallery = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (grantResults.length > 0 && permissioncamera && permissiongallery) {
                    Toast.makeText(activity, "مجوز دسترسی داده شد", Toast.LENGTH_SHORT).show();
                    choosecameragallery();
                } else {
                    Toast.makeText(activity, "مجوز دسترسی داده نشد", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void choosecameragallery() {
        AlertDialog.Builder al = new AlertDialog.Builder(activity);
        al.setTitle("انتخاب عکس")
                .setMessage("لطفا موقعیت عکس خود را مشخص کنید")
                .setPositiveButton("گالری", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_INTENT);
                })
                .setNegativeButton("دوربین", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_INTENT);
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profPicCircle.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Bitmap bitmap1 = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            profPicCircle.setImageBitmap(bitmap1);
        }
    }
}