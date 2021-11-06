package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    /*myRef.setValue("Hello, World!");

    public void setMyRef(DatabaseReference myRef) {
        this.myRef = myRef;
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }*/
}
