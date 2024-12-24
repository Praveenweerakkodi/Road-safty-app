package com.sahanmw.roadsafetyapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends Activity {

    private EditText newPasswordInput, confirmPasswordInput;
    private Button resetPasswordButton;
    private FirebaseFirestore db;
    private String username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Retrieve the username and email from the ForgotPasswordActivity
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");

        // Log the values to ensure they are not null
        Log.d("ResetPasswordActivity", "Username: " + username + ", Email: " + email);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        // Reset Password button functionality
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordInput.getText().toString().trim();
                String confirmPassword = confirmPasswordInput.getText().toString().trim();

                if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter both password fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update the password in Firestore for the matching user
                db.collection("users")
                        .whereEqualTo("username", username)
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    // User found
                                    String userId = task.getResult().getDocuments().get(0).getId();
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("password", newPassword);

                                    db.collection("users").document(userId).update(updates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ResetPasswordActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                                                        finish(); // Close activity
                                                    } else {
                                                        Toast.makeText(ResetPasswordActivity.this, "Failed to update password. Please try again.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // No user found
                                    Toast.makeText(ResetPasswordActivity.this, "User not found. Please check your username and email.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
