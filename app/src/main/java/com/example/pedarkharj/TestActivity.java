package com.example.pedarkharj;

import android.app.VoiceInteractor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pedarkharj.profile.URLs;

public class TestActivity extends AppCompatActivity{

    TextView tvTest;
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
        tvTest.setText("Hi");
        getPageContent();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuItem m1 =  menu.add("Click");
       m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
       m1.setOnMenuItemClickListener(item -> {
           Toast.makeText(this, "toast", Toast.LENGTH_SHORT).show();
           getPageContent();
           return false;
       });
        return super.onCreateOptionsMenu(menu);
    }

    private void getPageContent() {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getApplicationContext());

//        // Instantiate the cache
//        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
//
//        // Set up the network to use HttpURLConnection as the HTTP client.
//        Network network = new BasicNetwork(new HurlStack());
//
//        // Instantiate the RequestQueue with the cache and network.
//        requestQueue = new RequestQueue(cache, network);

        String url = "http://192.168.43.54:8080/pedarKharj/connection.php";
        StringRequest stringRequest = new StringRequest(url,
                 response -> {
                     tvTest.append("\nResponse is: "+ response);
                 },
                error -> {
            tvTest.setText(error.getMessage());
//                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                });
        requestQueue.add(stringRequest);

    }
}
