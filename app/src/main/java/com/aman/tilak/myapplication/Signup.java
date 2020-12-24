package com.aman.tilak.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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


public class Signup extends AppCompatActivity implements View.OnClickListener, Dialog.dialogListener{

    EditText name,email,phno,password;
    Button reg;
    TextView gotosignin, forgotpassword;
    AccessCredential ac;
    String updatedUsername, updatedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ac= new AccessCredential(Signup.this);

        name= (EditText) findViewById(R.id.name_signup_id);
        email= (EditText) findViewById(R.id.email_signup_id);
        phno= (EditText) findViewById(R.id.phone_signup_id);
        password= (EditText) findViewById(R.id.password_signup_id);
        forgotpassword= (TextView) findViewById(R.id.forgot_password_signup_id);

        reg= (Button) findViewById(R.id.continue_signup_id);

        gotosignin= (TextView) findViewById(R.id.new_to_the_mission_signup__id);

        reg.setOnClickListener(this);
        gotosignin.setOnClickListener(this);
        forgotpassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case  R.id.continue_signup_id:
                String name1= name.getText().toString().trim(), email1= email.getText().toString().trim(), phno1= phno.getText().toString().trim(), password1= password.getText().toString().trim();
                SQLiteDatabase db = ac.getWritableDatabase();
                String query = "select * from user";
                Cursor c1 = db.rawQuery(query, null);
                if (!name1.isEmpty() || !email1.isEmpty() || !phno1.isEmpty() || !password1.isEmpty()) {
                    if (c1.getCount() >= 1) {
                        AlertDialog.Builder warning = new AlertDialog.Builder(Signup.this);
                        warning.setTitle("Warning");
                        warning.setCancelable(false);
                        warning.setIcon(R.drawable.ic_warning);
                        warning.setMessage("You are not allowed to attempt multiple signups!\n\nSignin to gain access\n\n");
                        warning.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog a1 = warning.create();
                        a1.show();
                    }
                    else if (c1.getCount() < 1) {
                        ac.insertData(email1, phno1, name1, password1);
                        Toast.makeText(this, "Data successfully inserted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Signup.this, Signin.class));
                        finish();
                    }
                }
                else{
                    Toast.makeText(this, " Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.new_to_the_mission_signup__id:
                startActivity(new Intent(Signup.this, Signin.class));
                finish();
                break;
            case R.id.forgot_password_signup_id:
                openDialog();
                break;
        }
    }

    private void openDialog() {
        Dialog d1= new Dialog();
        d1.show(getSupportFragmentManager(),"dialog");
    }

    @Override
    public void applyText(String email, String name, String password) {

        ac.update(email,name,password);

        AlertDialog.Builder resetData = new AlertDialog.Builder(Signup.this);

        resetData.setTitle("Display changes");
        resetData.setCancelable(false);
        resetData.setMessage("Email: "+ email + "\nNew username: " + name +"\nNew password: " + password);
        resetData.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog a1= resetData.create();
        a1.show();
    }
}