package com.example.ievent.activity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ievent.R;
import com.google.firebase.auth.FirebaseAuth;
public class SignupActivity  extends AppCompatActivity{
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private FirebaseAuth mAuth;

    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.editText_signup_EmailAddress);
        passwordEditText = findViewById(R.id.editText_signup_pwd);
        confirmEditText = findViewById(R.id.editText_signup_confirm);
        progressBar = findViewById(R.id.progress_register);
        Button loginButton = findViewById(R.id.button_signup_login);
        Button signUpButton = findViewById(R.id.button_signup_signup);

        signUpButton.setOnClickListener(v -> createUser());
        loginButton.setOnClickListener(v -> {
            // jump to sign-up page
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void createUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmEditText.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)) {
            if (!password.equals(confirmPassword)) {
                // if pwd != confirm, show the fail message
                Toast.makeText(SignupActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            } else {
                // pwd correct
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                // Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            Toast.makeText(this, "Email and password fields cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }
}
