package com.aman.tilak.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.aman.tilak.myapplication.Database.AccessCredential;
//import com.haoc.cameraxfullcodedemo.R;

import org.w3c.dom.Text;

public class Signin extends AppCompatActivity implements View.OnClickListener {

    EditText name, password;
    Button calculate;
    TextView gotosignup, emergencyCall;
    AccessCredential ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        requestPermission();

        ac= new AccessCredential(Signin.this);

        name= (EditText) findViewById(R.id.username_signin_id);
        password= (EditText) findViewById(R.id.password_signin_id);

        calculate= (Button) findViewById(R.id.continue_signin_id);

        gotosignup= (TextView) findViewById(R.id.new_to_the_mission_id);

        emergencyCall= (TextView) findViewById(R.id.emergency_call_signin_id);

        calculate.setOnClickListener(this);
        gotosignup.setOnClickListener(this);
        emergencyCall.setOnClickListener(this);
    }

    public void requestPermission() {
        String permission[]={Manifest.permission.CALL_PHONE};
        ActivityCompat.requestPermissions(Signin.this,permission,200);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continue_signin_id:
                String getusername= name.getText().toString().trim(), getPassword= password.getText().toString().trim();
                SQLiteDatabase db = ac.getWritableDatabase();
                String query = "select * from user";
                Cursor c1 = db.rawQuery(query, null);
                if (c1.moveToFirst()) {
                    if (c1.getString(2).equals(getusername) && c1.getString(3).equals(getPassword)) {
                        startActivity(new Intent(Signin.this, Dashboard.class));
                        finish();
                        break;
                    } else
                        Toast.makeText(this, " Username  or Password donot match. please refer to signup username and password!", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.new_to_the_mission_id:
                startActivity(new Intent(Signin.this, Signup.class));
              //  finish();
                break;
            case R.id.emergency_call_signin_id:
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:+91 78969 07029"));
                startActivity(intent);
                break;
        }
    }
}