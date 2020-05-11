package com.rjandaya.ordersystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rjandaya.ordersystem.Model.User;

public class Register extends AppCompatActivity {

    EditText editPhone, editName, editPassword, editPassword2;
    TextView textLogin;
    Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editPhone = (EditText)findViewById(R.id.editPhone);
        editName = (EditText)findViewById(R.id.editName);
        editPassword = (EditText)findViewById(R.id.editPassword);
        editPassword2 = (EditText)findViewById(R.id.editPassword2);

        buttonRegister = (Button)findViewById(R.id.buttonRegister);

//        Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(Register.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Check if field lef empty
                        if (editPhone.getText().toString().equals("") || editName.getText().toString().equals("") || editPassword.getText().toString().equals("") || editPassword2.getText().toString().equals("")) {
                            mDialog.dismiss();
                            Toast.makeText(Register.this, "Missing field", Toast.LENGTH_SHORT).show();
                        }

//                        Check if user already exists
                        if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(Register.this, "Phone number already registered", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            mDialog.dismiss();
                            User user = new User(editName.getText().toString(), editPassword.getText().toString());
                            table_user.child(editPhone.getText().toString()).setValue(user);
                            Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}
