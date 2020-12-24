package com.aman.tilak.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

//import com.haoc.cameraxfullcodedemo.R;

public class Dashboard extends AppCompatActivity {

    ImageView domestic,wild;
    EditText edt;
    String mobileno="";
    TextView wildanimals, domesticanimals;

    public void requestPermission() {
        String permissions[]= {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(Dashboard.this,permissions,200);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        domestic= (ImageView) findViewById(R.id.image_view_domestic_dashboard_id);
        wild= (ImageView) findViewById(R.id.image_view_wild_dashboard_id);

        wildanimals= (TextView) findViewById(R.id.wild_animal_dashboard_id);
        domesticanimals= (TextView) findViewById(R.id.domestic_animal_dashboard_id);

        wildanimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, Wild_animal.class));
                finish();
            }
        });

        domesticanimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, Domestic_animal.class));
                finish();
            }
        });

        requestPermission();

        Glide.with(Dashboard.this)
                .load("https://kids.nationalgeographic.com/content/dam/kids/photos/articles/Other%20Explore%20Photos/A-G/125-animals-tiger.ngsversion.1431105584797.adapt.1900.1.jpg")
                .centerCrop()
                .into(wild);

        Glide.with(Dashboard.this)
                .load("https://mymodernmet.com/wp/wp-content/uploads/2017/01/animal-selfies-thumbnail.jpg")
                .centerCrop()
                .into(domestic);


     /*   domestic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, Domestic_animal.class));
                finish();
            }
        });

        wild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,Wild_animal.class));
                finish();
            }
        });    */

        boolean network=hasNetwork();
        if (!network) {
            AlertDialog.Builder noNetwork= new AlertDialog.Builder(Dashboard.this);
            noNetwork.setTitle("Network issue");
            noNetwork.setMessage("Your internet connection is disabled!\nEnable internet\n");
            noNetwork.setCancelable(false);
            noNetwork.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    requestPermission();
                }
            });
            AlertDialog a1= noNetwork.create();
            a1.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, Menu.FIRST, Menu.NONE,"logout");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                startActivity(new Intent(Dashboard.this, Signin.class));
              //  finish();
                finishAffinity();
                System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean hasNetwork() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }
}