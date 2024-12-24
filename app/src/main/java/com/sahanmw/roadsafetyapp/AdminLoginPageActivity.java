package com.sahanmw.roadsafetyapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLoginPageActivity extends Activity {

    // Firebase Firestore instance
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_login);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // UI elements
        EditText adminUsername = findViewById(R.id.adminUsername);
        EditText adminPassword = findViewById(R.id.adminPassword);
        Button loginButton = findViewById(R.id.loginButton);

        // Login button logic
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = adminUsername.getText().toString();
                String password = adminPassword.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {
                    // Assuming loginAdmin(username, password) checks if credentials are correct
                    loginAdmin(username, password);  // Call the login function
                } else {
                    Toast.makeText(AdminLoginPageActivity.this, "Enter both username and password", Toast.LENGTH_SHORT).show();
                }
            }

            // Function for login logic
            private void loginAdmin(String username, String password) {
                db.collection("PoliceAdmins")
                        .whereEqualTo("username", username)
                        .whereEqualTo("password", password)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                // Login successful, show the first message
                                new AlertDialog.Builder(AdminLoginPageActivity.this)
                                        .setTitle("Message")
                                        .setMessage("Login Successfully ...!")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // After clicking "OK", go directly to the Admin Dashboard
                                                Intent intent = new Intent(AdminLoginPageActivity.this, AdminDashboardActivity.class);
                                                startActivity(intent);
                                                finish();  // End current activity
                                            }
                                        })
                                        .show();
                            } else {
                                // Login failed
                                Toast.makeText(AdminLoginPageActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });



        // Forgot password logic
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLoginPageActivity.this, AdminForgotPasswordActivity.class));
            }
        });
    }

    // Admin login logic
    private void loginAdmin(String username, String password) {
        db.collection("PoliceAdmins")
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Login successful, move to admin dashboard
                        startActivity(new Intent(AdminLoginPageActivity.this, AdminDashboardActivity.class));
                        finish(); // Close login activity
                    } else {
                        // Login failed
                        Toast.makeText(AdminLoginPageActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
