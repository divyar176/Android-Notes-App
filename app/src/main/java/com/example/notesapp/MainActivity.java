package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
    }

    public void redirectSignUp(View view) {
        startActivity(new Intent(MainActivity.this,
                SignupActivity.class));
    }
    //redirect user to home activity if task is successful
    private void redirect() {
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }
    //Click listener for login button
    public void btnLoginClick(View view) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (email.equals("")) {
            etEmail.setError("Enter Email");
        } else if (password.equals("")) {
            etPassword.setError("Enter Password");
        } else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                redirect();
                            } else {
                                Toast.makeText(MainActivity.this, "Login Failed :" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
    }

    public void forgotPwdClick(View view) {
        email = etEmail.getText().toString().trim();
        if (email.equals("")) {
            etEmail.setError("Enter Email");
        } else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Email sent to : " + email, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to send Email : " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    //Check if the user is already logged in
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser mUser = mAuth.getCurrentUser();
//
//        if (mUser != null) {
//            redirect();
//        }
//    }
}