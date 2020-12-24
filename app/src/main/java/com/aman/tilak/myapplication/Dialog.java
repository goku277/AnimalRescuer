package com.aman.tilak.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {

    EditText email, name,password;
    private dialogListener listener;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.reset_credential, null);

        builder.setView(view)
        .setCancelable(false)
        .setTitle("Reset Password")
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        })
        .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email1= email.getText().toString().trim();
                String name1= name.getText().toString().trim();
                String password1= password.getText().toString().trim();

                listener.applyText(email1,name1,password1);
            }
        });
        email= view.findViewById(R.id.email_reset_id);
        name= view.findViewById(R.id.new_name_reset_id);
        password= view.findViewById(R.id.new_password_reset_id);
        return builder.create();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (dialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(e.toString() + " must implement dialogListener");
        }
    }
    public interface dialogListener {
        public void applyText(String email, String name, String password);
    }
}