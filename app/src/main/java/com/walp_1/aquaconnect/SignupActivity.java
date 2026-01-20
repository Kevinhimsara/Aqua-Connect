package com.walp_1.aquaconnect;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText full_name, email, mobile_number, password, confirm_password;
    Button signup_btn, login_btn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        full_name = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        mobile_number = findViewById(R.id.mobile_number);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);

        signup_btn = findViewById(R.id.signup_btn);
        login_btn = findViewById(R.id.login_btn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), CustomerDashboardActivity.class));
            finish();
        }

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Full_Name = full_name.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Mobile_Number = mobile_number.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Confirm_Password = confirm_password.getText().toString().trim();

                // Check for empty fields
                if (TextUtils.isEmpty(Full_Name)) {
                    full_name.setError("Full Name is required.");
                    return;
                }

                if (TextUtils.isEmpty(Email)) {
                    email.setError("Email is required.");
                    return;
                }

                if (TextUtils.isEmpty(Mobile_Number)) {
                    mobile_number.setError("Mobile Number is required.");
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    password.setError("Password is required.");
                    return;
                }

                if (TextUtils.isEmpty(Confirm_Password)) {
                    confirm_password.setError("Please confirm your password.");
                    return;
                }

                // Check password length
                if (Password.length() < 6) {
                    password.setError("Password must be at least 6 characters long.");
                    return;
                }

                // Check if passwords match
                if (!Password.equals(Confirm_Password)) {
                    confirm_password.setError("Passwords do not match.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //Register the user

                fAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Full Name", Full_Name);
                            user.put("Email", Email);
                            user.put("Mobile Number", Mobile_Number);

                            // Save the user data in Firestore
                            documentReference.set(user).addOnSuccessListener(unused -> {
                                Toast.makeText(SignupActivity.this, "User details saved successfully!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(getApplicationContext(), CustomerDashboardActivity.class));
                            }).addOnFailureListener(e -> {
                                Toast.makeText(SignupActivity.this, "Failed to save user details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            });
                        }
                        else{
                            Toast.makeText(SignupActivity.this, "Error !!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }
}