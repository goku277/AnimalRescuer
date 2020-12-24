package com.aman.tilak.myapplication.Database;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AccessCredential extends SQLiteOpenHelper {
    Context ctx;
    public AccessCredential(@Nullable Context context) {
        super(context, "credentials", null, 1);
        this.ctx= context;
    }

    public void insertData(String email, String phno, String name, String pass) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("emailid",email);
        cv.put("phoneno",phno);
        cv.put("name",name);
        cv.put("password",pass);
        db.insert("user",null,cv);
        db.close();
    }


    public void update(String email, String name1, String password1) {
        ContentValues cv= new ContentValues();
        cv.put("emailid",email);
        cv.put("name",name1);
        cv.put("password",password1);
        SQLiteDatabase db= this.getWritableDatabase();
        String values[]= {email};
        db.update("user",cv,"emailid=?",values);
        db.close();
    }

    public void delete() {
        String query= "select * from user";
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor c1= db.rawQuery(query,null);
        if (c1.getCount() > 0) {
            db.delete("user", null, null);
        }
        else Toast.makeText(ctx, " sorry but no data to delete!", Toast.LENGTH_SHORT).show();
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "create table user(emailid text, phoneno text, name text, password text);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}