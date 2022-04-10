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

public class SignupActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etConfirmPassword;
    private String email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
    }

    public void btnSignUpClick(View view) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();
        //Check if email is empty
        if (email.equals("")) {
            etEmail.setError("Enter Email");
        }
        //check if password is empty
        else if (password.equals("")) {
            etPassword.setError("Enter Password");
        }
        //check if confirm password is empty
        else if (confirmPassword.equals("")) {
            etConfirmPassword.setError("Confirm Password");
        }
        //check if password matches
        else if (!confirmPassword.equals(password)) {
            etConfirmPassword.setError("Password mismatch");
        } else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "Failed to create the User :" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}