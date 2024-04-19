package com.example.ievent.activity;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ievent.R;
import com.example.ievent.database.data_manager.UserDataManager;
import com.example.ievent.entity.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity  extends BaseActivity{
    private TextInputEditText userNameText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmEditText;
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
        userNameText = findViewById(R.id.editText_signup_uname);
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
        String userName = userNameText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmEditText.getText().toString().trim();
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)) {
            if (!password.equals(confirmPassword)) {
                progressBar.setVisibility(View.GONE);
                // if pwd != confirm, show the fail message
                passwordEditText.setError("passwords do not match");
                confirmEditText.setError("passwords do not match");
                // Toast.makeText(SignupActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            } else {
                // pwd correct
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    String uid = mAuth.getCurrentUser().getUid();
                                    User user = new User(uid, email, userName);
                                    UserDataManager.getInstance().addNewUser(uid, user);
                                    Toast.makeText(SignupActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    // Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    intent.putExtra("UID", uid);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    // This will check for email collisions
                                    emailEditText.setError("This email is already registered");
                                } else {
                                    Toast.makeText(SignupActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "One of the field is empty.", Toast.LENGTH_SHORT).show();
        }

    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /***
     * when user touches empty space, the keyboard automatically hides
     * generated by chatGPT
     * @param event The touch screen event.
     *
     * @return whether the keyboard should hide
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof TextInputEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    hideKeyboard(v);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
