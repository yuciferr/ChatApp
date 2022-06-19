package com.example.chatapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;

import com.example.chatapp.Models.Users;
import com.example.chatapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Please wait...");


        binding.btnSignUp.setOnClickListener(v -> {
            if (!binding.txtEmail.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty() && !binding.txtName.getText().toString().isEmpty()) {
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(), binding.txtPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Users user = new Users(binding.txtName.getText().toString(), binding.txtEmail.getText().toString(), binding.txtPassword.getText().toString());
                                    String id = task.getResult().getUser().getUid();
                                    database.getReference("Users").child(id).setValue(user);


                                    Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SignUpActivity.this, SignIn.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        });

            } else {
                Toast.makeText(SignUpActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        binding.acc.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignIn.class);
            startActivity(intent);
        });
    }
}