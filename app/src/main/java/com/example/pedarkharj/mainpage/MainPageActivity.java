package com.example.pedarkharj.mainpage;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pedarkharj.R;

public class MainPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        //drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.barcode) {
            Toast.makeText(this, ""+item.getItemId(), Toast.LENGTH_SHORT).show();

        } else if (id == R.id.game) {
            Toast.makeText(this, ""+item.getItemId(), Toast.LENGTH_SHORT).show();

        } else if (id == R.id.about_heading) {
            Toast.makeText(this, ""+item.getItemId(), Toast.LENGTH_SHORT).show();

        } else if (id == R.id.exit) {
            Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
