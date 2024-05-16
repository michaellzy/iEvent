package com.example.ievent.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ievent.database.IEventDatabase;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Base activity for all activities in the app
 * @author Tengkai Wang
 */
public class BaseActivity extends AppCompatActivity {
    protected FirebaseAuth mAuth;
    protected IEventDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        db = IEventDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }
}