package com.sahanmw.roadsafetyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgotPasswordActivity extends Activity {

    private EditText usernameInput, emailInput;
    private Button changePasswordButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        TextView backToLogin = findViewById(R.id.backToLogin);
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        // Back to Login Functionality
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish(); // Close current activity
            }
        });

        // Change Password button functionality
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter both username and email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Fetch user details from Firestore based on username and email
                db.collection("users")
                        .whereEqualTo("username", username)
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    // User exists, proceed to Reset Password page
                                    Toast.makeText(ForgotPasswordActivity.this, "User verified. Proceed to reset password.", Toast.LENGTH_SHORT).show();

                                    // Pass username and email to ResetPasswordActivity
                                    Intent resetIntent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                                    resetIntent.putExtra("username", username); // Pass username
                                    resetIntent.putExtra("email", email); // Pass email
                                    startActivity(resetIntent);
                                    finish(); // Close current activity
                                } else {
                                    // User not found or invalid username/email combination
                                    Toast.makeText(ForgotPasswordActivity.this, "Invalid username or email.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
