package com.example.pedarkharj;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pedarkharj.profile.ProfileActivity;
import com.example.pedarkharj.profile.SharedPrefManager;
import com.example.pedarkharj.profile.URLs;
import com.example.pedarkharj.profile.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity{

    TextView tvTest;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvTest = findViewById(R.id.tvTest);
        tvTest.setMovementMethod(new ScrollingMovementMethod());
//        tvTest.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        tvTest.setText("Hi");
        getPageContent();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuItem m1 =  menu.add("login");
       m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
       m1.setOnMenuItemClickListener(item -> {
           loginTest();

           return false;
       });


        MenuItem m2 =  menu.add("sing up");
        m2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        m2.setOnMenuItemClickListener(item -> {
            String url = "http://192.168.43.54:8080/pedarKharj/registrationapi.php?apicall=signup";
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    url,
                    response -> {
                        tvTest.append("\nResponse is: "+ response+ "\n");
                    },
                    error -> {
                        tvTest.setText(error.getMessage());
//                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                    });
            requestQueue.add(stringRequest);
            return false;
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void getPageContent() {
//        // Instantiate the cache
//        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
//
//        // Set up the network to use HttpURLConnection as the HTTP client.
//        Network network = new BasicNetwork(new HurlStack());
//
//        // Instantiate the RequestQueue with the cache and network.
//        requestQueue = new RequestQueue(cache, network);

//        String url = "http://192.168.43.54:8080/pedarKharj/connection.php";
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.GET,
//                url,
//                 response -> {
//                     tvTest.append("\nResponse is: "+ response+ "\n");
//                 },
//                error -> {
//            tvTest.setText(error.getMessage());
////                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
//                });
//        requestQueue.add(stringRequest);
        //
        String url2 = "http://192.168.43.54:8080/pedarKharj/registrationapi.php?WRONGapicall=wrongAPI";
        StringRequest stringRequest2 = new StringRequest(
                Request.Method.GET,
                url2,
                response -> {
                    tvTest.append("\nResponse is: "+ response+ "\n");
                },
                error -> {
                    tvTest.setText(error.getMessage());
                });
        requestQueue.add(stringRequest2);
    }





    private void loginTest() {
        String url = "http://192.168.43.54:8080/pedarKharj/registrationapi.php?apicall=login";
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.POST,
//                url,
//                response -> {
//                    tvTest.append("\nResponse is: "+ response+ "\n");
//                },
//                error -> {
//                    tvTest.setText(error.getMessage());
//                });

        ///
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
        try {
            //converting response to json object
            JSONObject obj = new JSONObject(response);

            //if no error in response (if error=false in php code)
            if (!obj.getBoolean("error")) {

//                if (obj.getBoolean("message")) tvTest.append(obj.getString("message")); //getBoolean("message") can't be converted into boolean
                if (!obj.getString("message").isEmpty()) tvTest.append(obj.getString("message"));

//                //getting the user from the response
//                JSONObject userJson = obj.getJSONObject("user");
//
//                //creating a new user object
//                User user = new User(
//                        userJson.getInt("id"),
//                        userJson.getString("username"),
//                        userJson.getString("email"),
//                        userJson.getString("gender")
//                );
//
//                //storing the user in shared preferences
//                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//                //starting the profile activity
//                finish();
//                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            } else {
                //if error = true, give us the message
                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            tvTest.append("\n\n"+e+ "\n");
            e.printStackTrace();
        }
    },
    //errorListener
    error -> {
        if (error.networkResponse == null) {
            if (error.getClass().equals(TimeoutError.class)) {
                // Show timeout error message
                Toast.makeText(getApplicationContext(),
                        "Oops. Timeout error!",
                        Toast.LENGTH_LONG).show();
            }
        }

        Log.i("ERRROR_volleyConnect: ", "\n"+error+ "");
        Toast.makeText(getApplicationContext(), "ERROR:\n"+ error, Toast.LENGTH_SHORT).show();
    })
    {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("username", "Blue-Dragon");
        params.put("password", "123321");
        return params;
    }
    };
        ///


        requestQueue.add(stringRequest);
    }


}
