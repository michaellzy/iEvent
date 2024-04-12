package com.example.ievent.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ievent.R;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private CheckBox checkBox;


    private static final String PREF = "LOGIN_PREF";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.editText_login_EmailAddress);
        passwordEditText = findViewById(R.id.editText_login_pwd);
        progressBar = findViewById(R.id.progress_login);
        checkBox = findViewById(R.id.checkBox_remember);
        Button loginButton = findViewById(R.id.button_login);
        Button signUpButton = findViewById(R.id.button_signup);


        loginButton.setOnClickListener(v -> loginUser());

        signUpButton.setOnClickListener(v -> {
            // jump to sign-up page
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
        getPrefData();
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences sp = getSharedPreferences(PREF, MODE_PRIVATE);
        if (checkBox.isChecked()) {
            boolean isChecked = checkBox.isChecked();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("email", email);
            editor.putString("pwd", password);
            editor.putBoolean("checked", isChecked);
            editor.apply();
        } else {
            sp.edit().clear().apply();
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    // login succeed, jump to main
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // login failed, show failed message
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Email and password are required.", Toast.LENGTH_SHORT).show();
        }
    }


    private void getPrefData() {
        SharedPreferences sp = getSharedPreferences(PREF, MODE_PRIVATE);
        if (sp.contains("email")) {
            String storedEmail = sp.getString("email", "email not found");
            emailEditText.setText(storedEmail);
        }
        if (sp.contains("pwd")) {
            String storedPwd = sp.getString("pwd", "pwd not found");
            passwordEditText.setText(storedPwd);
        }
        if (sp.contains("checked")) {
            boolean storedIsChecked = sp.getBoolean("checked", false);
            checkBox.setChecked(storedIsChecked);
        }
    }
}